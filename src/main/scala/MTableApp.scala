

import slick.basic.DatabaseConfig
import slick.dbio.DBIO
import slick.jdbc.JdbcProfile
import slick.jdbc.meta.MTable

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global


object MTableApp extends App {

  def exec[T](future: Future[T]) = Await.result(future, Duration.Inf)
  implicit def action2Future[T](action: DBIO[T]): Future[T] = db.run(action)

  val dbConfig = DatabaseConfig.forConfig[JdbcProfile]("mydb")
  import dbConfig.profile.api._
  val db = dbConfig.db

  val included = Seq("PRODUCT","USER","ACCOUNT")
  exec(db.run(MTable.getTables.map(_.filter(included contains _.name.name))).map(_.foreach(println)))

}
