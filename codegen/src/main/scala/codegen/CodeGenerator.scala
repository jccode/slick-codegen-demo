package codegen

import codegen.Config._
import slick.jdbc.H2Profile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._

/**
  *  This customizes the Slick code generator. We only do simple name mappings.
  *  For a more advanced example see https://github.com/cvogt/slick-presentation/tree/scala-exchange-2013
  */
object CodeGenerator{
  def main(args: Array[String]): Unit = {
    Await.ready(
      codegen.map(_.writeToFile(
        "slick.driver.H2Driver",
        args(0),
        args(1),
        "Tables",
        "Tables.scala"
      )),
      20.seconds
    )
  }

  val db = H2Profile.api.Database.forURL(url,driver=jdbcDriver)
  // filter out desired tables
  val included = Seq("COFFEES","SUPPLIERS","COF_INVENTORY")

  val customImports = "import core._\n"

  val codegen = db.run{
    H2Profile.defaultTables
      // .map(_.filter(t => included contains t.name.name))
      .flatMap( H2Profile.createModelBuilder(_,false).buildModel )
  }.map{ model =>
    new slick.codegen.SourceCodeGenerator(model) {
      override def entityName: String => String = (dbName: String) => dbName.toCamelCase

      override def tableName: String => String = (dbName: String) => dbName.toCamelCase + "Table"

      override def code: String = customImports+super.code

      override def Table = new Table(_) {
        table =>
        override def EntityType = new EntityType {
          override def parents: Seq[String] = Seq("BaseEntity")
        }

        override def TableClass = new TableClassDef {
          override def parents: Seq[String] = Seq("BaseTable")
        }

        override def TableValue = new TableValueDef {
          override def rawName: String = model.name.table.toCamelCase.uncapitalize.toPlural
        }
      }
    }
  }

  implicit class StringExt(val s: String) {
    def toPlural: String = if(s.matches(".*(x|ch|ss|sh|o)$")) { s"${s}es" } else { s"${s}s" }
  }

}