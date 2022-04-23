# TT

A basic template processor.

## implementation

In build.gradle.kts :

```kotlin
dependencies {
    implementation("net.orandja.tt:TT:0.0.2")
}
```

## Usage

Really simple usage :

```kotlin
// Create a group containing all information needed to render.
val templates = TT.group(
    "userTemplate" to "Hello {{ lastName }} {{ firstName }} !",
    "firstName" to TT.value("Jon"),
    "lastName" to TT.value("Gena"),
)

// prints "Hello Jon Gena !"
println(templates.renderToString("userTemplate")) 
```

For a more exhaustive list of things that can be done, look at all [samples](./sample/src/main/kotlin/net/orandja/tt/sample/Main.kt).