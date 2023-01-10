package wood.mike

import groovy.transform.CompileStatic
import io.micronaut.serde.annotation.Serdeable

@Serdeable
@CompileStatic
class GithubRelease {
    String name
    String url
}
