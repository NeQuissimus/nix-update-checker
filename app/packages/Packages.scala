package packages

import scala.util.matching.Regex

case class VersionFilter(filter: Regex, allowPrerelease: Boolean)
object VersionFilter {
    val anything = VersionFilter(""".*""".r, true)
    def prefixedAnything(prefix: String) = VersionFilter(s"""${prefix}.*""".r, true)
    val simpleNumbering = VersionFilter("""^[0-9]+(\.[0-9]+)+$""".r, true)
    val semanticVersioningNoPostfix = VersionFilter("""^v?([0-9]+)\.([0-9]+)\.([0-9]+)""".r, true)
    val semanticVersioning = VersionFilter("""^v?([0-9]+)\.([0-9]+)\.([0-9]+)(?:(\-[0-9A-Za-z-]+(?:\.[0-9A-Za-z-]+)*))?(?:\+[0-9A-Za-z-\-\.]+)?$""".r, true)
}

sealed trait PackageType
case class GitHubPackage(owner: String, repo: String, versionFilter: Option[VersionFilter] = None, t: GitHubType = GitHubRelease) extends PackageType {
    override def toString = s"$owner/$repo"
}

sealed trait GitHubType
case object GitHubRelease extends GitHubType
case object GitHubTag extends GitHubType

object Packages {
    import VersionFilter._

    val all = List(
        GitHubPackage("atom", "atom", Some(semanticVersioning.copy(allowPrerelease = false))),
        GitHubPackage("coreos", "rkt", Some(semanticVersioning.copy(allowPrerelease = false))),
        GitHubPackage("coreutils", "coreutils", Some(simpleNumbering), GitHubTag),
        GitHubPackage("curl", "curl", Some(prefixedAnything("curl-")), GitHubTag),
        GitHubPackage("docker", "docker", Some(semanticVersioningNoPostfix), GitHubTag),
        GitHubPackage("git", "git", Some(semanticVersioningNoPostfix), GitHubTag),
        GitHubPackage("gradle", "gradle", Some(semanticVersioningNoPostfix), GitHubTag),
        GitHubPackage("i3", "i3", Some(simpleNumbering), GitHubTag),
        GitHubPackage("i3", "i3status", Some(simpleNumbering), GitHubTag),
        GitHubPackage("hishamhm", "htop", Some(simpleNumbering), GitHubTag),
        GitHubPackage("JetBrains", "kotlin", Some(semanticVersioning)),
        GitHubPackage("lihaoyi", "Ammonite", Some(simpleNumbering), GitHubTag),
        GitHubPackage("mirror", "busybox", Some(anything), GitHubTag),
        GitHubPackage("mirror", "libX11", Some(prefixedAnything("libX11-")), GitHubTag),
        GitHubPackage("mirror", "wget", Some(semanticVersioning), GitHubTag),
        GitHubPackage("openssl", "openssl", Some(prefixedAnything("OpenSSL_1_1_")), GitHubTag),
        GitHubPackage("openssl", "openssl", Some(prefixedAnything("OpenSSL_1_0_2")), GitHubTag),
        GitHubPackage("openssl", "openssl", Some(prefixedAnything("OpenSSL_1_0_1")), GitHubTag),
        GitHubPackage("reorx", "httpstat", Some(simpleNumbering), GitHubTag),
        GitHubPackage("sbt", "sbt", Some(semanticVersioningNoPostfix), GitHubTag),
        GitHubPackage("scala", "scala", Some(semanticVersioningNoPostfix), GitHubTag),
        GitHubPackage("typesafehub", "activator", Some(semanticVersioning), GitHubTag),
        GitHubPackage("xmonad", "xmonad", Some(semanticVersioning), GitHubTag),
        GitHubPackage("zsh-users", "zsh", Some(prefixedAnything("zsh-")), GitHubTag)
    )
}
