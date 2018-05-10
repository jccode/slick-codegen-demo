object Test2 extends App {

  import shapeless._
  import lens._
  import record._
  import syntax.singleton._

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
  println(idLens.get(b)) // 7
  println(idLens.set(b)(8))
}
