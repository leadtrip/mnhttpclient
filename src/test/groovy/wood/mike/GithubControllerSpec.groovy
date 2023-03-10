package wood.mike

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Shared
import spock.lang.Specification
import java.util.regex.Pattern

@MicronautTest
class GithubControllerSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient client

    @Shared
    Pattern MICRONAUT_RELEASE = Pattern.compile("[Micronaut|Micronaut Framework] [0-9].[0-9].[0-9]([0-9])?( (RC|M)[0-9])?");


    void 'verify github releases can be fetched with low level HttpClient'() {
        when:
            HttpRequest request = HttpRequest.GET('/github/releases-lowlevel')

            HttpResponse<List<GithubRelease>> rsp = client.toBlocking().exchange(request,
                    Argument.listOf(GithubRelease))

        then: 'the endpoint can be accessed'
            rsp.status == HttpStatus.OK
            rsp.body()

        when:
            List<GithubRelease> releases = rsp.body()

        then:
            releases
            releases.stream()
                    .map(GithubRelease::getName)
                    .allMatch(name -> MICRONAUT_RELEASE.matcher(name)
                            .find())
    }

    void 'verify github releases can be fetched with compile-time autogenerated @Client'() {
        when:
            HttpRequest request = HttpRequest.GET('/github/releases-lowlevel')

            List<GithubRelease> githubReleases = client.toBlocking().retrieve(request, Argument.listOf(GithubRelease))

        then:
            githubReleases.stream()
                    .map(GithubRelease::getName)
                    .allMatch(name -> MICRONAUT_RELEASE.matcher(name)
                            .find())
    }
}