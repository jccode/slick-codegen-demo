/**
  * MyTest4
  *
  * @author 01372461
  */
object MyTest4 extends App {

  import shapeless._

  val external_id_witness = Witness('external_id)
  type external_id_t = external_id_witness.T

  trait ExtId {
    def external_id: String
  }


  def hashingFunction(oldId: String): String = oldId + "#"

  def hashExternalIds[A <: ExtId]
  (rdd: Seq[A])
  (implicit mkLens: MkFieldLens.Aux[A, external_id_t, String]) = {
    val external_id_lens = mkLens()
    rdd.groupBy(external_id_lens.get).flatMap { case (id_to_hash, as) =>
      as.map(external_id_lens.set(_)(hashingFunction(id_to_hash)))
    }
  }

  case class Foo(external_id: String) extends ExtId
  case class Bar(id: Int, size: Int, external_id: String) extends ExtId


  case class DoesNotCompile1(external_id: Int)
  case class DoesNotCompile2(there_is_no_id: String = "yep")

  println(hashExternalIds(Seq(Foo("123"))))
  println(hashExternalIds(Seq(Bar(1, 14, "qq"), Bar(2, 0, "zz"))))

//  println(hashExternalIds(Seq(DoesNotCompile1(123))))
}
