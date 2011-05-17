package org.gradle.api.plugins.gaelyk.eclipse

import org.gradle.api.internal.artifacts.dependencies.DefaultClientModule;


class EclipseGaelykPluginConvention {
	
	def gitHub(){
		def resolver = new org.apache.ivy.plugins.resolver.URLResolver()
		resolver.name = "GitHub"
		resolver.addArtifactPattern 'http://cloud.github.com/downloads/[organization]/[module]/[module]-[revision].[ext]'
		resolver
	}
	
	def gaelykMinimal(conf = [:]){
		def gaelyk = new DefaultClientModule('glaforge','gaelyk',conf.gaelykVersion ?: '0.7')
		gaelyk.addDependency new DefaultClientModule('com.google.appengine','appengine-api-1.0-sdk', conf.gaeVersion ?: '1.5.0')
		gaelyk.addDependency new DefaultClientModule('com.google.appengine','appengine-api-labs',conf.gaeVersion ?: '1.5.0')
		gaelyk
	}
	
}
