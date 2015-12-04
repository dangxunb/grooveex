package com.github.aesteve.vertx.groovy.specs

import io.vertx.core.file.OpenOptions
import io.vertx.groovy.core.buffer.Buffer
import io.vertx.groovy.core.file.AsyncFile
import io.vertx.groovy.core.http.WebSocket
import io.vertx.groovy.ext.unit.Async
import io.vertx.groovy.ext.unit.TestContext

import org.junit.Test

class SockJSSocketSpec extends TestBase {

	@Test
	public void testSocketSugar(TestContext context) {
		Async async = context.async()
		Buffer buff = "test" as Buffer
		client().websocket "/sock/websocket", { WebSocket sock -> 
			sock.handler {
				context.assertEquals buff, it
				async.complete()
			}
			sock << buff
		}
	}

	@Test
	public void testPipeSugar(TestContext context) {
		String filePath = "src/test/resources/file.txt"
		Async async = context.async()
		Buffer fileBuff = vertx.fileSystem().readFileBlocking filePath
		Buffer buff = Buffer.buffer()
		client().websocket "/sock/websocket", { WebSocket sock ->
			sock.handler {
				buff += it // buff << it
				if (buff.length() >= fileBuff.length()) {
					async.complete()
				}
			}
			vertx.fileSystem().open filePath, [:], { (it.result() | sock).start() }
		}
	}

}
