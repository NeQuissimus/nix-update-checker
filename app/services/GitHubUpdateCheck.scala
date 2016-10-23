package services

import javax.inject._

import scala.collection.JavaConverters._
import scala.util.{ Failure, Success, Try }

import org.kohsuke.github._

import packages._

@Singleton
class GitHubUpdateCheck {
    def check(p: GitHubPackage) = {
        def checkRelease(p: GitHubPackage): Option[CheckResult] = {
            GitHubApi.api.flatMap(
                _.getRepository(s"${p.owner}/${p.repo}")
                .listReleases.withPageSize(30)
                .asScala
                .filter(r => p.versionFilter.map(f => f.filter.pattern.matcher(r.getName).matches).getOrElse(false))
                .filter(r => p.versionFilter.map(f => f.allowPrerelease).getOrElse(false) || !r.isPrerelease)
                .take(1)
                .toSeq
                .headOption
            )
            .map(r => CheckResult(GitHubPackage(r.getOwner.getOwner.getLogin, r.getOwner.getName), r.getName))
        }

        def checkTag(p: GitHubPackage): Option[CheckResult] = {
            GitHubApi.api.flatMap(
                _.getRepository(s"${p.owner}/${p.repo}")
                .listTags.withPageSize(30)
                .asScala
                .filter(r => p.versionFilter.map(f => f.filter.pattern.matcher(r.getName).matches).getOrElse(false))
                .take(1)
                .toSeq
                .headOption
            )
            .map(r => CheckResult(GitHubPackage(r.getOwner.getOwner.getLogin, r.getOwner.getName), r.getName))
        }

        p.t match {
            case GitHubRelease => checkRelease(p)
            case GitHubTag => checkTag(p)
        }
    }
}

object GitHubApi {
    lazy val api = Try {
        val token = "b22839f38321b0208a0a142df2b947493722601e"
        GitHub.connectUsingOAuth(token)
    } match {
        case Success(a) => Some(a)
        case Failure(t) => println(t); None
    }
}
