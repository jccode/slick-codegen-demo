import org.scalatest.{FlatSpec, Matchers}


class SomeTest extends FlatSpec with Matchers {

  "Make sure split " should "works as expected" in {
    val pat = ",|;"
    "hello,world".split(pat) should be (Seq("hello", "world"))
    "hello;world".split(pat) should be (Seq("hello", "world"))
  }
}
