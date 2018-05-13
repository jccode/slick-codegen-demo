
import core.AbstractRepo
import dao.Tables
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile
import slick.lifted.TableQuery

import scala.concurrent.Future


trait UserRepoSupport extends Tables {

  val config = DatabaseConfig.forConfig[JdbcProfile]("mydb")

  class UserRepo extends AbstractRepo[JdbcProfile, User, UserTable, TableQuery[UserTable]](config, users) {
    import dbConfig.profile.api._

    def findByName(name: String): Future[Seq[User]] = elements.filter(_.name === name).result

  }

  val userRepo = new UserRepo

}