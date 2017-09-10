# BaseFrame
便于开发

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
	        compile 'com.github.DerekYanJ:BaseFrame:1-beta1'
	}
