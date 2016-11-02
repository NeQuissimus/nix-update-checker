package services

import javax.inject._

import packages._

case class CheckResult(p: PackageType, latestVersion: String)

@Singleton
class UpdateCheck @Inject() (github: GitHubUpdateCheck) {
    def checkAll(ghToken: String) = {
        val api = GitHubApi.api(ghToken)

        Packages.all.par.flatMap(p => {
            p match {
                case p @ GitHubPackage(_, _, _, _) => github.check(p, api)
            }
        })
    }
}
