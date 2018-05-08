
import dao.Tables
import slick.jdbc.JdbcProfile

class Repo(val profile: JdbcProfile) extends Tables {

  import profile._
  import profile.api._

  val db = Database.forConfig("mydb")

  def allCoffees() = {
    db.run(UserTable.result)
  }
}