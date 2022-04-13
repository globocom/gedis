val compilerVersion = "2.12.15"

val buildSettings = Seq(
  name := "Gedis",
  scalaVersion := compilerVersion,
  description := "Scala connector for Redis",
  organization := "com.globo.bigdata",
  sonatypeProfileName := "com.globo",
  organizationName := "Globo.com",
  useGpg := true,
  pomExtra in ThisBuild := {
    <url>https://github.com/globocom/gedis</url>
    <scm>
      <connection>scm:git:github.com/globocom/gedis.git</connection>
      <developerConnection>scm:git:git@github.com/globocom/gedis.git</developerConnection>
      <url>https://github.com/globocom/gedis</url>
    </scm>
    <developers>
      <developer>
        <id>dmvieira</id>
        <name>Diogo Munaro Vieira</name>
        <url>https://github.com/dmvieira</url>
      </developer>
    </developers>
  },
  resolvers := Resolvers.defaultResoulvers,
  libraryDependencies ++= Dependencies.rootDependencies,
  run in Compile := Defaults.runTask(fullClasspath in Compile, mainClass in(Compile, run), runner in(Compile, run)).evaluated,
  publishTo := Some(
    if (isSnapshot.value) {
      Opts.resolver.sonatypeSnapshots
    }
    else {
      Opts.resolver.sonatypeStaging
    }
  ),
  licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0")),
  publishMavenStyle := true
)

lazy val root = (project in file(".")).settings(buildSettings: _*)

scapegoatVersion in ThisBuild := "1.3.3"
scalaBinaryVersion in ThisBuild := "2.12"
scapegoatDisabledInspections := Seq("ExpressionAsStatement", "OptionGet")
