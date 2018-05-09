
import core.AbstractRepo
import dao.Tables
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile
import slick.lifted.TableQuery

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

//  val userRepo = new AbstractRepo[JdbcProfile, User, UserTable, TableQuery[UserTable]](config, UserTable)
  val userRepo = new AbstractRepo(config, UserTable)

}