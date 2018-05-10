package demo

import java.sql.Timestamp

import shapeless._

trait BaseEntity {
  def id: Int
  def createTime: Timestamp
  def updateTime: Timestamp


  val createTimeWitness = Witness('createTime)
  val updateTimeWitness = Witness('updateTime)
  type TypeCreateTime = createTimeWitness.T
  type TypeUpdateTime = updateTimeWitness.T
}

object BaseEntity {
  def copyWithUpdateTime[T <: BaseEntity](t: T, time: Timestamp)(implicit mkLens: MkFieldLens.Aux[T, T#TypeCreateTime, Timestamp]): T = {
    val len = mkLens()
    len.set(t)(time)
  }

  def copyWithCreateTime[T <: BaseEntity](t: T, time: Timestamp)(implicit mkLens: MkFieldLens.Aux[T, T#TypeUpdateTime, Timestamp]): T = {
    val len = mkLens()
    len.set(t)(time)
  }

  implicit class WithCreateTime[T <: BaseEntity](t: T) {
    def withCreateTime(time: Timestamp)(implicit mkLens: MkFieldLens.Aux[T, T#TypeCreateTime, Timestamp]): T = mkLens().set(t)(time)
  }

  implicit class WithUpdateTime[T <: BaseEntity](t: T) {
    def withUpdateTime(time: Timestamp)(implicit mkLens: MkFieldLens.Aux[T, T#TypeUpdateTime, Timestamp]): T = mkLens().set(t)(time)
  }
}

case class User(id: Int, name: String, age: Int, createTime: Timestamp, updateTime: Timestamp) extends BaseEntity

object Util {

  def updateCreateTime[A <: BaseEntity](a: A, time: Timestamp)(implicit mkLens: MkFieldLens.Aux[A, A#TypeCreateTime, Timestamp]): A = {
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
//    println(Util.updateCreateTime(user, now))
    println(BaseEntity.copyWithCreateTime(user, now))
    println(BaseEntity.copyWithUpdateTime(user, now))
    println(BaseEntity.copyWithUpdateTime(BaseEntity.copyWithCreateTime(user, now), now))
    println(user.withCreateTime(now).withUpdateTime(now))
  }
  normal_method_call_is_ok()

  // However, in the below `BaseRepo` definition,
  // Compiler gives an compile error:
  //
  //     could not find implicit value for parameter mkLens: shapeless.MkFieldLens.Aux[E,demo.Util.tpeCreateTime,java.sql.Timestamp]
  //
  //
  // Why? And how to fixed it?

  def class_param_call() = {
    val repo = new BaseRepo[User]
    println("--------------")
    println(repo.beforeUpdate(user))
  }
  class_param_call()
}


class BaseRepo[E <: BaseEntity] {
  def beforeUpdate(e: E)(implicit createTimeLens: MkFieldLens.Aux[E, E#TypeCreateTime, Timestamp], updateTimeLens: MkFieldLens.Aux[E, E#TypeUpdateTime, Timestamp]): E = {
    val now = new Timestamp(System.currentTimeMillis())
    e.withCreateTime(now).withUpdateTime(now)
  }
}

