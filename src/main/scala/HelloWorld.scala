import java.sql.Timestamp

import slick.jdbc.H2Profile

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object HelloWorld extends App {

  def exec[T](future: Future[T]) = Await.result(future, 5 seconds)

  def repo(): Unit = {
    val repo = new Repo(H2Profile)
    exec( repo.allCoffees().map(_.foreach(println)) )
  }

  def ext(): Unit = {
    val foo = new Foo() with FooSupport
    foo.fooEx()
    println(foo)
  }


  def repo2(): Unit = {
    val repo = new UserRepoSupport {
      override val profile = H2Profile
    }
    val userRepo = repo.userRepo

    val now = new Timestamp(System.currentTimeMillis())
    val u = repo.User(3,"fff", "fff", "fff", None, null, null)

    exec(userRepo.all().map(_.foreach(println)))
//    exec(userRepo.get(1).map(_.foreach(println)))
//    exec(userRepo.update(u))
//    exec(userRepo.delete(3))
//    exec(userRepo.all.map(_.foreach(println)))

    exec(userRepo.insert(u).map(println(_)))
    println("------")
    exec(userRepo.findByName("fff").map(_.foreach(println)))
    exec(userRepo.all().map(_.foreach(println)))
  }
  repo2()

}


trait FooSupport { this: Foo =>
  def fooEx(): Unit = {
    foo()
    println("fooEx")
  }
}

class Foo() {
  def foo(): Unit = {
    println("foo")
  }
}