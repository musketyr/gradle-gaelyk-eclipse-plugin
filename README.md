# Gradle Gaelyk Eclipse plugin

The plugin integrates [Gradle Gaelyk plugin](http://github.com/bmuschko/gradle-gaelyk-plugin) into the Eclipse. It extends
the Gradle Gaelyk, Gradle GAE, Groovy and Eclipse plugins.

## Usage

To use the Gaelyk Eclipse plugin, set up the minimal build file:

	buildscript {
		repositories{
			add(new org.apache.ivy.plugins.resolver.URLResolver()) {
				name = "GitHub"
				addArtifactPattern 'http://cloud.github.com/downloads/[organisation]/[module]/[module]-[revision].[ext]'
			}
		}
		
	    dependencies {
	    	classpath "bmuschko:gradle-gae-plugin:0.4"
	    	classpath "bmuschko:gradle-gaelyk-plugin:0.1"
	        classpath "musketyr:gradle-gaelyk-eclipse-plugin:0.1"
	    }
	}
	
	apply plugin: 'gaelyk-eclipse'
	
	repositories {
		mavenCentral()
		add gitHub()
	}
	
	
	dependencies {
		groovy 	"org.codehaus.groovy:groovy-all:1.7.10"
		compile gaelykMinimal()
	}

Use standard `cleanEclipse eclipse` tasks to setup the Eclipse project.


## The hard work
What exactly does the plugin?

* setups the Gaelyk and GAE plugins as described in their wikis

* fixes `.project` and `.classpath` files to follow ones distributed with Gaelyk Template Project

* adds `testRuntime` dependencies to the classpath

* maps test folder to `test`

* includes jars in lib dir into the classpath

* adds two methods to the build file

    * `gitHub()` to add url resolver from the GitHub (in format which can be seen above)
    
    * `gaelykMinimal()` which supplies the minimal dependencies to run Gaelyk (except Groovy). You can specify Gaelyk version by passing `gaelykVersion` parameter as well as the GAE version by passing `gaeVersion` parameter.

## Release notes

* 0.1.1 - Removed libs cleaning task until its working more reliably

* 0.1 - Initial version
