
import java.sql.Timestamp

import core.AbstractRepo
import dao.Tables
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile
import slick.lifted.TableQuery

import scala.concurrent.Future

class Repo(val profile: JdbcProfile) extends Tables {

  import profile._
  import profile.api._

  val db = Database.forConfig("mydb")

  def allCoffees() = {
    db.run(UserTable.result)
  }
}


trait UserRepoSupport extends Tables {

  val config = DatabaseConfig.forConfig[JdbcProfile]("mydb")

  class UserRepo extends AbstractRepo[JdbcProfile, User, UserTable, TableQuery[UserTable]](config, UserTable) {
    import dbConfig.profile.api._

    def findByName(name: String): Future[Seq[User]] = elements.filter(_.name === name).result

//    override protected def beforeInsert(entity: User): User = entity.withUpdateTime(new Timestamp(123123123L)).withCreateTime(new Timestamp(System.currentTimeMillis()))
  }

  val userRepo = new UserRepo

  //  val userRepo = new AbstractRepo[JdbcProfile, User, UserTable, TableQuery[UserTable]](config, UserTable)

}