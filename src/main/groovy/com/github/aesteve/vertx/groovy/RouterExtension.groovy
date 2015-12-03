package com.github.aesteve.vertx.groovy

import groovy.transform.TypeChecked
import io.vertx.core.Handler
import io.vertx.core.http.HttpMethod
import io.vertx.groovy.ext.web.Route
import io.vertx.groovy.ext.web.Router

class RouterExtension {
	
	static Route putAt(Router self, String path, Object handler) {
		self.route(path).handler handler
	}
	
	static Route getAt(Router self, String path) {
		self.route(path)
	}
	
}
