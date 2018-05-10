import java.sql.Timestamp

import core.BaseEntity
import dao.Tables.User


/**
  * MyTest6
  *
  * @author 01372461
  */
object MyTest6 extends App {

  object UpdateUtil {
    import shapeless._
    import java.sql.Timestamp
    import core.BaseEntity

    val witCreateTime = Witness('createTime)
    type tpeCreateTime = witCreateTime.T

    val now = new Timestamp(System.currentTimeMillis())
    def updateCreateTime[A <: BaseEntity](e: A, time: Timestamp)(implicit mkLens: MkFieldLens.Aux[A, tpeCreateTime, Timestamp]): A = {
      val lenCreateTime = mkLens()
      lenCreateTime.set(e)(time)
    }
  }

  def t1(): Unit = {
    val u1 = User(1, "Tom", "111", "222", Some("18100001111"), null, null)
    val now = new Timestamp(System.currentTimeMillis())
    println(UpdateUtil.updateCreateTime(u1, now))
  }

//  class TestRepo[A <: BaseEntity] {
//
//    def beforeUpdate(a: A): A = {
//      val now = new Timestamp(System.currentTimeMillis())
//      UpdateUtil.updateCreateTime(a, now)
//    }
//  }
//
//  def t2(): Unit = {
//    val repo = new TestRepo[User]
//    val u1 = User(1, "Tom", "111", "222", Some("18100001111"), null, null)
//    println(repo.beforeUpdate(u1))
//  }
//
//  t2()
}
