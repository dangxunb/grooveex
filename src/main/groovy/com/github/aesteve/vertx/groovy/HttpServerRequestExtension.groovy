package com.github.aesteve.vertx.groovy

import groovy.transform.TypeChecked
import io.vertx.groovy.core.MultiMap
import io.vertx.groovy.core.http.HttpServerRequest

@TypeChecked
class HttpServerRequestExtension {
	
	static MultiMap getParams(HttpServerRequest self) {
		self.params()
	}
	
	static MultiMap getHeaders(HttpServerRequest self) {
		self.headers()
	}
	
}
