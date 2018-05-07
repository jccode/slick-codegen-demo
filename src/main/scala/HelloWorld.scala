import slick.jdbc.H2Profile

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object HelloWorld extends App {
  val repo = new Repo(H2Profile)
  Await.result( repo.allCoffees().map(_.foreach(println)), 1 second)

}
