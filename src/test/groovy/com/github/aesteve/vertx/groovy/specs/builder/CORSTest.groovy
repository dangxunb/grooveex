package com.github.aesteve.vertx.groovy.specs.builder

import io.vertx.groovy.core.buffer.Buffer
import io.vertx.groovy.core.http.HttpClientRequest
import io.vertx.groovy.ext.unit.TestContext
import org.junit.Test

import static io.vertx.core.http.HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN
import static io.vertx.core.http.HttpHeaders.ORIGIN

class CORSTest extends BuilderTestBase {

	@Test
	void testAllowOrigin(TestContext context) {
		context.async { async ->
			HttpClientRequest req = client['/cors/test']
			req >> { response ->
				assertEquals 200, response.statusCode()
				assertEquals '*', response.headers[ACCESS_CONTROL_ALLOW_ORIGIN]
				response.headers.each { key, value ->
					println "$key = $value"
				}
				response >>> { Buffer buffer ->
					assertEquals buffer as String, "CORS"
					async++
				}
			}
			req.headers[ORIGIN] = "vertx.io"
			req++
		}
	}

}
