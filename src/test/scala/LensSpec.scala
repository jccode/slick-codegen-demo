import org.scalatest.{FlatSpec, Matchers}
import shapeless._
import shapeless.record._
import shapeless.syntax.singleton._

class LensSpec extends FlatSpec with Matchers {

  "Generic lens on base trait" should "works" in {
    trait A { val id: Int }
    case class B(id: Int) extends A
    case class C(id: Int, name: String) extends A

    implicit val lgenA = new LabelledGeneric[A] {
      type Repr = Record.`'id -> Int`.T
      def to(a: A) : Repr = ('id ->> a.id) :: HNil
      def from(r: Repr): A = new A { val id = r('id) }
    }

    val idLens = lens[A] >> 'id
    val b = B(7)
    idLens.get(b) should be (7)
  }

  "Implicit mkLens " should "as follow" in {
    val external_id_witness = Witness('external_id)
    type external_id_t = external_id_witness.T

    def changeExternalId[A](a: A, value: String)(implicit mkLens: MkFieldLens.Aux[A, external_id_t, String]) = {
      val external_id_lens = mkLens()
      external_id_lens.set(a)(value)
    }

    case class Foo(external_id: String)
    case class Bar(id: Int, size: Int, external_id: String)

    val foo = Foo("12")
    val bar = Bar(1, 20, "15")

    changeExternalId(foo, "13") should be (Foo("13"))
    changeExternalId(bar, "20") should be (bar.copy(external_id = "20"))
  }

}
