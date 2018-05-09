import java.sql.Timestamp

import core.BaseEntity
import dao.Tables

object Test3 extends App {

  import shapeless._
  import lens._
  import record._
  import syntax.singleton._

  val now = new Timestamp(System.currentTimeMillis())
  val u1 = Tables.User(1, "tom", "111", "222", Some("1331111000"), null, null)

  implicit val lgenBaseEntity = new LabelledGeneric[BaseEntity] {

    //  override type Repr = Record.`'id -> Int, 'createTime -> Timestamp, 'updateTime -> Timestamp`.T
    override type Repr = Record.`'id -> Int, 'createTime -> Timestamp, 'updateTime -> Timestamp`.T

    override def to(t: BaseEntity): Repr = ('id ->> t.id) :: ('createTime ->> t.createTime) :: ('updateTime ->> t.updateTime) :: HNil

    override def from(r: Repr): BaseEntity = new BaseEntity {
      override def id: Int = r('id)

      override def createTime: Timestamp = r('createTime)

      override def updateTime: Timestamp = r('updateTime)
    }
  }

  println(u1)
  val idLen = lens[BaseEntity] >> 'id
  println(idLen.get(u1))

  val lenCreateTime = lens[BaseEntity] >> 'createTime
  println(lenCreateTime.get(u1))
  println(lenCreateTime.set(u1)(now))
}
