package wood.mike

import groovy.transform.CompileStatic
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono

@CompileStatic
@Controller("/github")
class GithubController {

    private final GithubLowLevelClient githubLowLevelClient
    private final GithubApiClient githubApiClient

    GithubController(GithubLowLevelClient githubLowLevelClient,
                     GithubApiClient githubApiClient) {
        this.githubLowLevelClient = githubLowLevelClient
        this.githubApiClient = githubApiClient
    }

    @Get("/releases-lowlevel")
    Mono<List<GithubRelease>> releasesWithLowLevelClient() {
        githubLowLevelClient.fetchReleases()
    }

    @Get(uri = "/releases", produces = MediaType.APPLICATION_JSON_STREAM)
    Publisher<GithubRelease> fetchReleases() {
        githubApiClient.fetchReleases()
    }
}