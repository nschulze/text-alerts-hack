
name := "text-alerts"
homepage := Option(url("https://ask-me-later.com"))
maintainer := "dev@dwolla.com"
scalaVersion := "2.12.8"

libraryDependencies ++= {
  val akkaHttpVersion = "10.0.11"
  val slickVersion = "3.2.1"

  Seq(
    "com.dwolla" %% "server" % "6.4.34",
    "com.dwolla" %% "eventbus" % "2.10.36",
    "com.amazonaws" % "aws-java-sdk" % "1.11.572",
    "com.dwolla" %% "concurrent" % "1.4.9",
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
    "ch.qos.logback" %  "logback-classic" % "1.2.3",
    "org.scalatest" %% "scalatest" % "3.0.5" % "it,test",
    "org.mockito" % "mockito-core" % "2.24.5" % "it,test",
    "com.typesafe.slick" %% "slick" % slickVersion,
  )
}

dwollaAppPrimaryServicePort := Option(ExposedPort(11691))
dwollaAppAdminPort := Option(ExposedPort(11692))

val app = (project in file("."))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings: _*)
  .enablePlugins(DwollaDockerApp)
