addSbtPlugin("com.dwolla.sbt" % "dwolla-docker-app" % "2.7.0")

resolvers += Resolver.url("artifacts-ivy", url("https://artifacts.dwolla.net/repository/ivy-public"))(Resolver.ivyStylePatterns)
