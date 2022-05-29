# TT

A basic template processor written in kotlin.

## implementation

In build.gradle.kts :

```kotlin
dependencies {
    implementation("net.orandja.tt:TT:0.1.0")
}
```

## Usage

**To learn more please, take a look at all [samples](https://github.com/L-Briand/TT/blob/main/sample/src/main/kotlin/net/orandja/tt/Main.kt#L11).**

A simple usage :

```kotlin
// Create a template 
val template = TT.template("Hello {{ firstName }} {{ lastName }} !")
// Create a group containing all information needed to render.
val context = TT.group(
    "firstName" to TT.value("It's me"),
    "lastName" to TT.value("Mario"),
)

// bind them together
template bindTo context

// render it.
template.renderToString() // Hello It's me Mario !
```

With reflection, it's even easier.

```kotlin
data class Person(val firstName: String, val lastName: String)
val user = Person("Mario", "Smith")
val template = TT.template("Hello {{ firstName }} {{ lastName }} !")
val userTemplate = template bindToData user
userTemplate.renderToString() // Hello Mario Smith !
```

Reflection also works with lists.

```kotlin
val users = listOf(Person("Mario", "Smith"), Person("Luigi", "Smith"))
val template = TT.template("<li>{{ firstName }} {{ lastName }}</li>")
val usersTemplate = template bindToDataList users
userTemplate.renderToString() // <li>Mario Smith</li><li>Luigi Smith</li>
```