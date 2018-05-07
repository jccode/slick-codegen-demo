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
        "demo",
        "Tables",
        "Tables.scala"
      )),
      20.seconds
    )
  }

  val db = H2Profile.api.Database.forURL(url,driver=jdbcDriver)
  // filter out desired tables
  val included = Seq("COFFEES","SUPPLIERS","COF_INVENTORY")
  val codegen = db.run{
    H2Profile.defaultTables.map(_.filter(t => included contains t.name.name)).flatMap( H2Profile.createModelBuilder(_,false).buildModel )
  }.map{ model =>
    new slick.codegen.SourceCodeGenerator(model){
      // customize Scala entity name (case class, etc.)
      override def entityName = dbTableName => dbTableName match {
        case "COFFEES" => "Coffee"
        case "SUPPLIERS" => "Supplier"
        case "COF_INVENTORY" => "CoffeeInventoryItem"
        case _ => super.entityName(dbTableName)
      }
      // customize Scala table name (table class, table values, ...)
      override def tableName = dbTableName => dbTableName match {
        case "COF_INVENTORY" => "CoffeeInventory"
        case _ => super.tableName(dbTableName)
      }
      // override generator responsible for tables
      override def Table = new Table(_){
        table =>
        // customize table value (TableQuery) name (uses tableName as a basis)
        override def TableValue = new TableValue{
          override def rawName = super.rawName.uncapitalize
        }
        // override generator responsible for columns
        override def Column = new Column(_){
          // customize Scala column names
          override def rawName = (table.model.name.table,this.model.name) match {
            case ("COFFEES","COF_NAME") => "name"
            case ("COFFEES","SUP_ID") => "supplierId"
            case ("SUPPLIERS","SUP_ID") => "id"
            case ("SUPPLIERS","SUP_NAME") => "name"
            case ("COF_INVENTORY","QUAN") => "quantity"
            case ("COF_INVENTORY","COF_NAME") => "coffeeName"
            case _ => super.rawName
          }
        }
      }
    }
  }
}