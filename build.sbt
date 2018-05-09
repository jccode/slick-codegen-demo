name := "slick-codegen-demo"

version := "0.1"

val slickVersion = "3.2.1"


lazy val root = (project in file("."))
  .settings(sharedSettings)
  .dependsOn(codegen)


lazy val codegen = (project in file("codegen"))
  .settings(sharedSettings)
  .settings(libraryDependencies += "com.typesafe.slick" %% "slick-codegen" % slickVersion)


lazy val sharedSettings = Seq(
  scalaVersion := "2.12.6",
  libraryDependencies ++= Seq(
    "com.typesafe.slick" %% "slick" % slickVersion,
    "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
    "com.h2database" % "h2" % "1.4.196",
    "com.typesafe" % "config" % "1.3.1",
    "org.slf4j" % "slf4j-nop" % "1.7.10",
  )
)



lazy val slick = taskKey[Seq[File]]("gen-tables")  // register manual sbt command
//slick := {
//  val (dir, cp, r, s) = ((sourceManaged in Compile).value, (dependencyClasspath in Compile).value, (runner in Compile).value, streams.value)
//  val pkg = "dao"
//  val slickProfile = "slick.jdbc.H2Profile"
//  val jdbcDriver = "org.h2.Driver"
//  val url = "jdbc:h2:mem:sample;INIT=RUNSCRIPT FROM 'src/main/resources/sql/drop-tables.sql'\\;RUNSCRIPT FROM 'src/main/resources/sql/create-tables.sql';"
//  val user = "sa"
//  val password = ""
//  r.run("slick.codegen.SourceCodeGenerator", cp.files, Array(slickProfile, jdbcDriver, url, dir.getPath, pkg, user, password), s.log)
//  val outputFile = dir / pkg.replace(".", "/") / "Tables.scala"
//  Seq(outputFile)
//}

//sourceGenerators in Compile += slick  // register automatic code generation on every compile, remove for only manual use



// custom codegen; must be in another sub project.
slick := {
  val (dir, cp, r, s) = ((sourceManaged in Compile).value, (dependencyClasspath in Compile).value, (runner in Compile).value, streams.value)
  val pkg = "dao"
  r.run("codegen.CodeGenerator", cp.files, Array(dir.getPath, pkg), s.log)
  val outputFile = dir / pkg.replace(".", "/") / "Tables.scala"
  Seq(outputFile)
}
sourceGenerators in Compile += slick