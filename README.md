# Slidingmenuhbh
一款简单易用的侧滑菜单
How to
To get a Git project into your build:

gradle

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.monkeyLittleMonkey:Slidingmenuhbh:V1.0'
	}
  
  
  
  maven
  
  Step 1. Add the JitPack repository to your build file

	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
Step 2. Add the dependency

	<dependency>
	    <groupId>com.github.monkeyLittleMonkey</groupId>
	    <artifactId>Slidingmenuhbh</artifactId>
	    <version>V1.0</version>
	</dependency>
  
  
