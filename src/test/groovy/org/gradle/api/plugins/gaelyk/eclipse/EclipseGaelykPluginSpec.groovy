package org.gradle.api.plugins.gaelyk.eclipse

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;

import spock.lang.Specification;

class EclipseGaelykPluginSpec extends Specification {
	
	def plugin = new EclipseGaelykPlugin()
	def project = ProjectBuilder.builder().build()
	
	def setup(){
		plugin.apply(project)
	}
	
	def "Plugin applies right plugins"(){
		expect:
		project.eclipseClasspath
		project.gaeRun
		project.gaelykInstallPlugin
	}

}
