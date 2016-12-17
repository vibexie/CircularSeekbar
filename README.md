### CircularSeekbar for Android

In this library, i did not use attr to set some values, because i do not know the effect that you want. so, you need to modify a little to meet the requirements. if you do not how to modify, issue please!
 
![](http://qiniu.vibexie.com/github/circularseekbar_p1.gif)

#### For gradle：
Step 1. Add the JitPack repository in your root build.gradle at the end of repositories:
``` xml
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

Step 2. Add the dependency
``` xml
compile 'com.github.vibexie:CircularSeekbar:v1.1'
```
