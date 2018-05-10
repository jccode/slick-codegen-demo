import shapeless._
import java.sql.Timestamp

import demo.BaseEntity



trait BaseEntity {
  def id: Int
  def createTime: java.sql.Timestamp
  def updateTime: java.sql.Timestamp
}

case class User(id: Int, name: String, password: String, salt: String, mobile: Option[String], createTime: java.sql.Timestamp, updateTime: java.sql.Timestamp) extends BaseEntity

val u1 = User(1, "tom", "111", "222", Some("1331111000"), null, null)
val now = new Timestamp(System.currentTimeMillis())

val lUser = lens[demo.User]
val lenCreatetime = lUser.createTime
val lenUpdatetime = lUser.updateTime
val lenCreateUpdateTime = lenCreatetime ~ lenUpdatetime

lenCreatetime.get(u1)
lenCreatetime.set(u1)(now)
lenCreateUpdateTime.set(u1)(now, now)



/*
import lens._
import record._
import syntax.singleton._

implicit val lgenBaseEntity = new LabelledGeneric[BaseEntity] {

  override type Repr = Record.`'id -> Int, 'createTime -> Timestamp, 'updateTime -> Timestamp`.T

  override def to(t: BaseEntity): Repr = ('id ->> t.id) :: ('createTime ->> t.createTime) :: ('updateTime ->> t.updateTime) :: HNil

  override def from(r: Repr): BaseEntity = new BaseEntity {
    override def id: Int = r('id)

    override def createTime: Timestamp = r('createTime)

    override def updateTime: Timestamp = r('updateTime)
  }
}

val idLen = lens[BaseEntity] >> 'id
idLen.get(u1)
idLen.set(u1)(3)
*/


//def setTime[T <: BaseEntity](t: T): T = {
//  val lEntity = lens[T]
//  val lenCreatetime = lEntity.createTime
//  val lenUpdatetime = lEntity.updateTime
//  val lenCreateUpdateTime = lenCreatetime ~ lenUpdatetime
//  val now = new Timestamp(System.currentTimeMillis())
//  lenCreateUpdateTime.set(t)(now, now)
//}
//
//setTime(u1)


val (createTimeWitness, updateTimeWitness) = (Witness('createTime), Witness('updateTime))
type createTimeTpe = createTimeWitness.T
type updateTimeTpe = updateTimeWitness.T

def setCreateTime[T](t: T)(implicit mkLens: MkFieldLens.Aux[T, createTimeTpe, Timestamp]) = {
  val lenCreateTime = mkLens()
  println("-------")
  println(mkLens)
  val now = new Timestamp(System.currentTimeMillis())
  lenCreateTime.set(t)(now)
}

setCreateTime(u1)

def update[T, E](t: T)(e: E)(implicit mkLens: p.Lens[T, E]): T = mkLens().set(t)(e)