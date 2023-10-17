# kotlin-default-args

[![Discord](https://discord.com/api/guilds/939962855476846614/widget.png)](https://discord.gg/pEGGF785zp)
[![](https://jitpack.io/v/Revxrsal/kotlin-default-args-util.svg)](https://jitpack.io/#Revxrsal/kotlin-default-args-util)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Build](https://github.com/Revxrsal/kotlin-default-args-util/actions/workflows/gradle.yml/badge.svg)](https://github.com/Revxrsal/kotlin-default-args-util/actions/workflows/gradle.yml)

**kotlin-default-args-util** is a library that attempts to bring two of the most prominent features in Kotlin to the
Java reflection world:

- Named parameters
- Default parameters

### Background

<details>
  <summary>Click to expand</summary>
Default and named parameters are undeniably one of the most favored features in Kotlin, and while it is possible to
interop them with Java using annotations such as `@JvmOverload`, using them in Java's Reflection API is very tricky to
get right, as it requires dealing with synthetic compiler functions, classes and arguments, and accommodating the many
edge cases with it.

The official solution proposed by JetBrains is [kotlin-reflect][1]; a library that introspects Kotlin classes and
metadata to allow easy and ergonomic access to functions and properties.

The problem, however, was with the slow performance and vast bundle size of `kotlin-reflect` (~2.8 MB). JetBrains
addresses this problem by providing a simpler, smaller, and lighter version of kotlin-reflect, [kotlin.reflect.lite][2].
While it sounds promising, it has been marked as experimental, possibly abandoned, and far from being production-ready.

Out of the need for something small that gets the job done, kotlin-default-args-util was born.
</details>

## Features

1. **Small size 🔥**: The library is tiny, measuring about 30 kilobytes.
2. **Zero dependencies 🔥**: The library does not require _any_ dependencies, not even the Kotlin standard library. This
   makes it convenient to bundle anywhere.
3. **Lazy reflection**: Generating reflection elements is a resource-intensive process. The library caches reflection
   elements and only fetches them on demand.
4. **Thread-safe**: All API classes are immutable and thread-safe, and lazy fetching is synchronized on first call
5. **Support for Kotlin ergonomics**:
    1. Suspend functions
    2. Named arguments
    3. Default arguments
    4. Index-based arguments
    5. `object`s and `companion object`s
    6. `@JvmStatic` functions
6. Friendly and accurate error messages
7. A nice and convenient API

## Usage

### pom.xml

```xml
<!-- Add JitPack repository -->
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<!-- Add the dependency -->
<dependency>
    <groupId>com.github.Revxrsal</groupId>
    <artifactId>kotlin-default-args-util</artifactId>
    <version>(version)</version>
</dependency>
```

### build.gradle

```groovy
repositories {
    maven { url = "https://jitpack.io" }
}

dependencies {
    implementation("com.github.Revxrsal:kotlin-default-args-util:(version)")
}
```

### build.gradle.kts

```kotlin
repositories {
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation("com.github.Revxrsal:kotlin-default-args-util:(version)")
}
```

## Examples
### Calling a function with default parameters
Function class:
```kotlin
class Test {

    companion object {

        /**
         * Greets the given name
         */
        fun greet(name: String = "John") {
            println("Hello, $name!")
        }
    }
}
```

Calling the function:
```java
// fetch the function
Method greetMethod = Test.Companion.getClass().getDeclaredMethod("greet", String.class);

// the KotlinFunction wrapper
KotlinFunction greet = KotlinFunction.wrap(greetMethod);

// note: the instance can be null if the function has @JvmStatic.
greet.call(
        /* instance = */ Test.Companion,
        /* arguments = */ emptyList(),
        /* isOptional = */ parameter -> true // All parameters are optional
);

greet.call(
        /* instance = */ Test.Companion,
        /* arguments = */ singletonList("my friend"),
        /* isOptional = */ parameter -> true // All parameters are optional
);
```

Output:
```
Hello, John! (*default parameter was used*)
Hello, my friend!
```

### Call function by parameter names

> **Warning ⚠️**: Parameter names at runtime may not necessarily match the ones at compile-time, in which
> cases, the function will throw an exception if an invalid name was provided. To prevent this, configure
> the compiler to preserve parameter names at runtime

Function class
```kotlin
object Numbers {
    
    fun numbers(
        a: Int = 10,
        b: Int = 30,
        c: Int = -5
    ) {
        println("A: $a")
        println("B: $b")
        println("C: $c")
    }
}
```

Calling the function
```java
// fetch the function
Method sumMethod = Numbers.class.getDeclaredMethod("numbers", int.class, int.class, int.class);

// the KotlinFunction wrapper
KotlinFunction sum = KotlinFunction.wrap(sumMethod);

// note: the instance can be null if the function has @JvmStatic.
sum.callByNames(
        /* instance = */ Numbers.INSTANCE,
        /* arguments = */ new HashMap<String, Object>() {{
            put("a", 20);
            put("c", 400);
        }},
        /* isOptional = */ parameter -> true // All parameters are optional
);
```

Output:
```
A: 20
B: 30
C: 400
```

### Call the function by parameter indices
Call the function using the indices of parameters. Zero represents the first parameter.
```java
// fetch the function
Method sumMethod = Numbers.class.getDeclaredMethod("numbers", int.class, int.class, int.class);

// the KotlinFunction wrapper
KotlinFunction sum = KotlinFunction.wrap(sumMethod);

// note: the instance can be null if the function has @JvmStatic.
sum.callByIndices(
        /* instance = */ Numbers.INSTANCE,
        /* arguments = */ new HashMap<Integer, Object>() {{
            put(0, 20);  // parameter 'a'
            put(2, 400); // parameter 'c'
        }},
        /* isOptional = */ parameter -> true // All parameters are optional
);
```

# Caveats

1. Due to the hairy nature of the Kotlin synthetics, this library tries its best to find
   the correct candidates for functions, singletons, parameters, etc. While it should
   work well in most cases, it is not perfect. In such cases, please feel free to file an issue
   with code that reproduces the problem
2. To avoid any sort of dependency on `kotlin-reflect`, the library has no way of knowing
   which parameters are optional and which ones are not, which is why all `call___` functions
   require an `isOptional` parameter.
3. Interface methods that have default values are not supported yet.

[1]: https://kotlinlang.org/docs/reflection.html

[2]: https://github.com/Kotlin/kotlinx.reflect.lite
