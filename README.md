# kotlin-default-args

## kotlin-default-args-util

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

[1]: https://kotlinlang.org/docs/reflection.html

[2]: https://github.com/Kotlin/kotlinx.reflect.lite