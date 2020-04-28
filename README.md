


# Location Utils

### Android location extensions and helper classes

[![Kotlin](https://img.shields.io/badge/Kotlin-1.3.72-blue.svg)](https://kotlinlang.org) 
Minimum Android API 21

Compiled Android API 29

## Usage
1. Add JitPack to your project build.gradle

```gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
   }
}
```

2. Add the dependency in the application build.gradle

```gradle
dependencies {

    implementation 'com.github.CraZyLegenD:LocationUtils:version'
    
  }
```

3. To not run into any issues in your application build.gradle add

```gradle
   compileOptions {
        sourceCompatibility = 1.8
        targetCompatibility = 1.8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    androidExtensions {
        experimental = true
    }
```
4. Additionally you can include
```gradle
    kapt {	
	correctErrorTypes = true
        useBuildCache = true
    }

     defaultConfig {
     	 vectorDrawables.useSupportLibrary = true
    }
```  
5. Inside gradle.properties

```gradle
kapt.incremental.apt=true
org.gradle.parallel=true // if you're modularizing your projects
org.gradle.caching=true
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
[MIT](https://choosealicense.com/licenses/mit/)
