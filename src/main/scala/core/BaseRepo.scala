package core

import slick.basic.{BasicProfile, DatabaseConfig}
import slick.jdbc.JdbcProfile
import slick.lifted.TableQuery

import scala.concurrent.Future

/**
  * BaseRepo
  *
  * @author 01372461
  */
trait BaseRepo[T <: slick.lifted.AbstractTable[_], Q <: TableQuery[T]] {

  def all(): Future[Seq[T#TableElementType]]

  def get(id: Int): Future[Option[T#TableElementType]]

  def insert(entity: T#TableElementType): Future[Int]

  def update(entity: T#TableElementType): Future[Int]

  def delete(id: Int): Future[Int]

}


// (implicit ev: Q =:= TableQuery[T])
class AbstractRepo[P <: JdbcProfile, E <: BaseEntity, T <: P#Table[E] with AbstractTable, Q <: TableQuery[T]](val dbConfig: DatabaseConfig[P], val elements: Q) extends BaseRepo[T, Q] {

  import dbConfig._
  import dbConfig.profile.api._

  implicit def action2Future[T](action: DBIO[T]): Future[T] = db.run(action)

  private def byId(id: Int) = elements.filter(_.id === id)

  private def returnId = elements returning elements.map(_.id)

  override def all(): Future[Seq[E]] = db.run(elements.result)

  override def get(id: Int): Future[Option[E]] = db.run(byId(id).result.headOption)

  override def insert(entity: E): Future[Int] = {
    db.run(returnId += entity)
  }

  override def update(entity: E): Future[Int] = db.run(byId(entity.id).update(entity))

  override def delete(id: Int): Future[Int] = db.run(byId(id).delete)
}