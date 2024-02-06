## TT `{{ tag }} template`

A quick and easy to use library for those, who only want named {{ tag }} replacement in a string.

For a more advanced template processor, please look at my mustache templating
engine [L-Briand/KTM](https://github.com/L-Briand/KTM).

Default rendering:

```kotlin
val render = TT("Hello {{ name }}!", "name" to "John")
assert("Hello John!" == render)

val data = mapOf("name" to "John", "ag e" to 33)
assert("John, 33 y/o" == TT("{{ name }}, {{ ag e }} y/o", data))

data class User(val name: String)
assert("User(name=John)" == TT("{{ class }}", "class" to User("John")))
```

Custom delimiters:

```kotlin
val format = TT(start = "%%", stop = "%")
val render = format("Hello %% name %!", "name" to "John")
assert("Hello John!" == render)
```

Escape HTML:

```kotlin
val format = TT(HtmlEscape)
val render = format("{{ script }}", "script" to "<script>alert('Hello')</script>")
assert("&lt;script&gt;alert(&#x27;Hello&#x27;)&lt;/script&gt;", render)
```

## Import from maven

### Multiplatform

```kotlin
repositories {
    mavenCentral()
}
val commonMain by getting {
    dependencies {
        implementation("net.orandja.kt:tt:1.0.0")
    }
}
```

### Jvm

```kotlin
repositories {
    mavenCentral()
}
dependencies {
    implementation("net.orandja.kt:tt:1.0.0")
}
```