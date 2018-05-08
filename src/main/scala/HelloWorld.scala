import slick.jdbc.H2Profile

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object HelloWorld extends App {
  val repo = new Repo(H2Profile)
  Await.result( repo.allCoffees().map(_.foreach(println)), 1 second)

  val foo = new Foo() with FooSupport
  foo.fooEx()
  println(foo)
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