import java.sql.Timestamp

import core.BaseEntity
import dao.Tables.User

/**
  * MyTest5
  *
  * @author 01372461
  */
object MyTest5 extends App {

  import shapeless._

  val witCreateTime = Witness('createTime)
  type tpeCreateTime = witCreateTime.T

  val now = new Timestamp(System.currentTimeMillis())
  def updateCreateTime[A <: BaseEntity](e: A, time: Timestamp)(implicit mkLens: MkFieldLens.Aux[A, tpeCreateTime, Timestamp]): A = {
    val lenCreateTime = mkLens()
    lenCreateTime.set(e)(time)
  }

  val u1 = User(1, "Tom", "111", "222", Some("18100001111"), null, null)
  println(updateCreateTime(u1, now))


}
