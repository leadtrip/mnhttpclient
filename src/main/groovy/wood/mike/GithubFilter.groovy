package wood.mike

import groovy.transform.CompileStatic
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpRequest
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.ClientFilterChain
import io.micronaut.http.filter.HttpClientFilter
import org.reactivestreams.Publisher

@CompileStatic
@Filter("/repos/**")
@Requires(property = "github.username")
@Requires(property = "github.token")
class GithubFilter implements HttpClientFilter {

    private final GithubConfiguration configuration

    GithubFilter(GithubConfiguration configuration) {
        this.configuration = configuration
    }

    @Override
    Publisher<? extends HttpResponse<?>> doFilter(MutableHttpRequest<?> request, ClientFilterChain chain) {
        chain.proceed(request.basicAuth(configuration.getUsername(), configuration.getToken()))
    }
}
