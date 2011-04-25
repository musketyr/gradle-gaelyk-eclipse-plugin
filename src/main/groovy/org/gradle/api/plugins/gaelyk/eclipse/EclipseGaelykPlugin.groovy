package org.gradle.api.plugins.gaelyk.eclipse

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.Delete;

class EclipseGaelykPlugin implements Plugin<Project>{
	
	def sdkJars = [
		"jsr107cache",
		"datanucleus-core",
		"jdo2-api",
		"datanucleus-appengine",
		"appengine-jsr107cache",
		"geronimo-jta_1.1_spec",
		"geronimo-jpa_3.0_spec",
		"datanucleus-jpa",
		"appengine-api-labs",
		"appengine-api-1.0-sdk",
		"appengine-tools-sdk"
	]
	
	void apply(project) {
		project.convention.plugins.gaelykEclipse = new EclipseGaelykPluginConvention()
		
		project.apply plugin: 'groovy'
		project.apply plugin: 'gae'
		project.apply plugin: 'gaelyk'
		project.apply plugin: 'eclipse'
		
		configureEclipseClasspath(project)
		configureEclipseProject(project)
		
		project.task('gaelykEclipseLibs',type: Copy){
			group = "Gaelyk"
			description = "Copies runtime dependencies into the 'war/WEB-INF/lib' to be available for GAE Eclipse."
			from  project.configurations.runtime
			into 'war/WEB-INF/lib'
		 }
		 
		 
//		project.task('cleanGaelykEclipseLibs', type: Delete){
//			group = "Gaelyk"
//			description = "Deletes all jars from 'war/WEB-INF/lib' which are not part of runtime configuration."
//			delete(project.fileTree(dir: 'war/WEB-INF/lib', includes: ['*.jar'], excludes: project.configurations.runtime.collect{ it.name } + sdkJars.collect{ "${it}-*.jar"}))
//		}
		
		project.gaeRun.dependsOn('gaelykEclipseLibs')
		project.eclipseClasspath.dependsOn('gaelykEclipseLibs')
		
//		project.cleanEclipseClasspath.dependsOn('cleanGaelykEclipseLibs')
		
		project.sourceSets {
			main {
				groovy {
					srcDirs = ["src"]
				}
			}
			test {
				groovy {
					srcDirs = ["test"]
				}
			}
		}
		
		project.gae {
			warDir = new File("war")
		}
		
		
		project.webAppDirName = new File("war")
		
	}
	
	def configureEclipseClasspath(project){
		project.eclipseClasspath {
			withXml { xml ->
				xml.asNode().classpathentry.find{ it.@kind == "output" && it.@path == "bin" }.@path = "war/WEB-INF/classes"
				xml.asNode().appendNode("classpathentry", [kind:"con", path:"com.google.appengine.eclipse.core.GAE_CONTAINER"])
					.appendNode("attributes")
					.appendNode("attribute", [name: "org.eclipse.jst.component.nondependency", value: "/war/WEB-INF/lib"])
				xml.asNode().appendNode("classpathentry", [exported:"true", kind:"con", path:"GROOVY_SUPPORT"])
					.appendNode("attributes")
					.appendNode("attribute", [name: "org.eclipse.jst.component.nondependency", value: "/war/WEB-INF/lib"])
				xml.asNode().appendNode("classpathentry", [kind:"src", output:"war/WEB-INF/classes", path:"war/WEB-INF/groovy"])
				xml.asNode().classpathentry.findAll{ it.@path =~ sdkJars.collect{ java.util.regex.Pattern.quote(it)}.join("|")}*.replaceNode {}
				project.fileTree(dir: 'war/WEB-INF/lib', includes: ['*.jar'], excludes: project.configurations.runtime.collect{ it.name } + sdkJars.collect{ "${it}-*.jar"}).each{
					xml.asNode().appendNode("classpathentry", [kind:"lib", path: (it.path - project.rootDir)[1..-1]])
				}
			}
		}
	}
	
	def configureEclipseProject(project){
		project.eclipseProject {
			whenConfigured { eprj ->
				eprj.natures << 'com.google.appengine.eclipse.core.gaeNature'
				eprj.natures << 'com.google.gdt.eclipse.core.webAppNature'
				eprj.natures.remove('org.eclipse.wst.common.project.facet.core.nature')
				eprj.natures.remove('org.eclipse.wst.common.modulecore.ModuleCoreNature')
				eprj.natures.remove('org.eclipse.jem.workbench.JavaEMFNature')
				
				eprj.buildCommands << [name:'com.google.appengine.eclipse.core.enhancerbuilder']
				eprj.buildCommands << [name:'com.google.appengine.eclipse.core.projectValidator']
				eprj.buildCommands << [name:'com.google.gdt.eclipse.core.webAppProjectValidator̈́']
			}
		}
	}
}
