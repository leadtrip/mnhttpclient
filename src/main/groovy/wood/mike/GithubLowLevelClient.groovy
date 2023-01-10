package wood.mike

import groovy.transform.CompileStatic
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import jakarta.inject.Singleton
import reactor.core.publisher.Mono

import static io.micronaut.http.HttpHeaders.ACCEPT
import static io.micronaut.http.HttpHeaders.USER_AGENT

@Singleton
@CompileStatic
class GithubLowLevelClient {

    private final HttpClient httpClient
    private final URI uri

    GithubLowLevelClient(@Client(GithubConfiguration.GITHUB_API_URL) HttpClient httpClient,
                         GithubConfiguration configuration) {
        this.httpClient = httpClient
        uri = UriBuilder.of("/repos")
                .path(configuration.organization)
                .path(configuration.repo)
                .path("releases")
                .build()
    }

    Mono<List<GithubRelease>> fetchReleases() {
        HttpRequest<?> req = HttpRequest.GET(uri)
                .header(USER_AGENT, "Micronaut HTTP Client")
                .header(ACCEPT, "application/vnd.github.v3+json, application/json")
        Mono.from(httpClient.retrieve(req, Argument.listOf(GithubRelease)))
    }
}
