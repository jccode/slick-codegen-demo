package demo

import java.sql.Timestamp

import shapeless._

trait BaseEntity {
  def id: Int
  def createTime: Timestamp
  def updateTime: Timestamp
}

case class User(id: Int, name: String, age: Int, createTime: Timestamp, updateTime: Timestamp) extends BaseEntity

object Util {
  val witCreateTime = Witness('createTime)
  type tpeCreateTime = witCreateTime.T

  def updateCreateTime[A](a: A, time: Timestamp)(implicit mkLens: MkFieldLens.Aux[A, tpeCreateTime, Timestamp]): A = {
    val lenCreateTime = mkLens()
    lenCreateTime.set(a)(time)
  }
}

/**
  * LensTest
  *
  * @author 01372461
  */
object LensTest extends App {
  val user = User(1, "Tom", 18, null, null)
  val now = new Timestamp(System.currentTimeMillis())

  def normal_method_call_is_ok(): Unit = {
    println(Util.updateCreateTime(user, now))
  }
  normal_method_call_is_ok()

  // However, in the below `BaseRepo` definition,
  // Compiler gives an compile error:
  //
  //     could not find implicit value for parameter mkLens: shapeless.MkFieldLens.Aux[E,demo.Util.tpeCreateTime,java.sql.Timestamp]
  //
  //
  // Why? And how to fixed it?
}

/*
class BaseRepo[E <: BaseEntity] {
  def beforeUpdate(e: E): E = {
    val now = new Timestamp(System.currentTimeMillis())
    Util.updateCreateTime(e, now)
  }
}
*/
