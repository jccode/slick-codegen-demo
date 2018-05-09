import slick.jdbc.H2Profile

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object HelloWorld extends App {

  def repo(): Unit = {
    val repo = new Repo(H2Profile)
    Await.result( repo.allCoffees().map(_.foreach(println)), 1 second)
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
    Await.result(repo.userRepo.all.map(_.foreach(println)), 1 second)
    Await.result(repo.userRepo.get(1).map(_.foreach(println)), 1 second)

//    val u = repo.User(0,"fff", "fff", "fff", None, null, null)
//    Await.result(repo.userRepo.insert(u), 1 second)
//    Await.result(repo.userRepo.all.map(_.foreach(println)), 1 second)

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