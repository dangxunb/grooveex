package com.github.aesteve.vertx.groovy.specs

import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.groovy.ext.unit.Async
import io.vertx.groovy.ext.unit.TestContext
import io.vertx.groovy.ext.web.Router
import org.junit.Test

class RoutingContextSpec extends TestBase {
	
	final static String KEY = 'dog'
	final static String VAL = 'Snoopy'
	final static String PATH = '/routingcontexttest'
	final static String FAILED_STATUS = '/failWithStatus'
	final static String FAILED_THROW = '/failWithThrowable'
	final static String ASYNC_SERVICE = '/asyncService'
	final static int STATUS = 400
	final static Throwable ex = new RuntimeException("ex")

	void fakeServiceMethod(Boolean fail, Handler<AsyncResult<String>> handler) {
		if (fail) {
			handler.handle Future.failedFuture(ex)
		} else {
			handler.handle Future.succeededFuture(VAL)
		}
	}

	@Override
	void router() {
		router = Router.router vertx
		router[PATH] = {
			it[KEY] = VAL
			it++
		}
		router[PATH] = {
			response << it[KEY]
		}
		router[FAILED_STATUS] = {
			it -= STATUS
		}
		router[FAILED_THROW] = {
			it -= ex
		}
		router[ASYNC_SERVICE] = {
			fakeServiceMethod( request.params['fail'].toBoolean(), it >> { res ->
				response << res
			})
		}
	}
	
	@Test
	void testGetPutAndCall(TestContext context) {
		context.async { async ->
			client.getNow PATH, { response -> 
				response >>> {
					assertEquals it as String, VAL
					async++
				}
			}
		}
	}
	
	@Test
	void testFailWithStatus(TestContext context) {
		context.async { async ->
			client.getNow FAILED_STATUS, { response ->
				assertEquals response.statusCode, STATUS
				async++
			}
		}
	}

	@Test
	void testFailWithThrowable(TestContext context) {
		context.async { async ->
			client.getNow FAILED_THROW, { response ->
				assertEquals response.statusCode, 500
				async++
			}
		}
	}

	@Test
	void testFailAsyncService(TestContext context) {
		context.async { async ->
			client.getNow "${ASYNC_SERVICE}?fail=true", { response ->
				assertEquals response.statusCode, 500
				async++
			}
		}
	}

	@Test
	void testSuccessAsyncService(TestContext context) {
		context.async { async ->
			client.getNow "${ASYNC_SERVICE}?fail=false", { response ->
				assertEquals response.statusCode, 200
				response >>> {
					assertEquals it as String, VAL
					async++
				}
			}
		}
	}
}
