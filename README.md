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

Run `cleanGaelykEclipseLibs gaelykEclipseLibs` to ensure the lib directory
contains only the required jars instead of all the Google Eclipse plugin adds in.

## The hard work
What exactly does the plugin?

* setups the Gaelyk and GAE plugins as described in their wikis

* fixes `.project` and `.classpath` files to follow ones distributed with Gaelyk Template Project

* adds `testRuntime` dependencies to the classpath

* maps test folder to `test`

* includes jars in lib dir into the classpath

* helps managing the lib dir to contain only jars which could not be fetched from dependencies (useful for VCS)

* adds two methods to the build file

    * `gitHub()` to add url resolver from the GitHub (in format which can be seen above)
    
    * `gaelykMinimal()` which supplies the minimal dependencies to run Gaelyk (except Groovy). You can specify Gaelyk version by passing `gaelykVersion` parameter as well as the GAE version by passing `gaeVersion` parameter.
