object Test4 extends App {

  import shapeless._

  val external_id_witness = Witness('external_id)
  type external_id_t = external_id_witness.T


  def hashingFunction(oldId: String): String = oldId + "#"

  def hashExternalIds[A]
  (rdd: Seq[A])
  (implicit mkLens: MkFieldLens.Aux[A, external_id_t, String]) = {
    val external_id_lens = mkLens()
    rdd.groupBy(external_id_lens.get).flatMap { case (id_to_hash, as) =>
      as.map(external_id_lens.set(_)(hashingFunction(id_to_hash)))
    }
  }

  case class Foo(external_id: String)
  case class Bar(id: Int, size: Int, external_id: String)


  case class DoesNotCompile1(external_id: Int)
  case class DoesNotCompile2(there_is_no_id: String = "yep")

  println(hashExternalIds(Seq(Foo("123"))))
  println(hashExternalIds(Seq(Bar(1, 14, "qq"), Bar(2, 0, "zz"))))

  // These two won't compile with the followng message

  // could not find implicit value for parameter mkLens:
  // shapeless.MkFieldLens.Aux[ScalaFiddle.DoesNotCompile1,
  // ScalaFiddle.external_id_t,String]

  // println(hashExternalIds(Seq(DoesNotCompile1(1))))
  // println(hashExternalIds(Seq(DoesNotCompile2())))

}
