# TT

A basic template processor.

## implementation

In build.gradle.kts :

```kotlin
dependencies {
    implementation("net.orandja.tt:TT:0.1.0")
}
```

## Usage

For an exhaustive list of things that can be done, look at all the [samples](./sample/src/main/kotlin/net/orandja/tt/sample).

A simple usage :

```kotlin
// Create a template 
val template = TT.template("Hello {{ firstName }} {{ lastName }} !")
// Create a group containing all information needed to render.
val context = TT.group(
    "firstName" to TT.value("John"),
    "lastName" to TT.value("Cena"),
)

// bind them together
template bindTo context

// render it.
template.renderToString() // Hello John Cena !
```

With reflection, it's even more easy.

```kotlin
data class Person(val firstName: String, val lastName: String)
val user = Person("John", "Cena")
val template = TT.template("Hello {{ firstName }} {{ lastName }} !")
val userTemplate = user.astemplate(template)
userTemplate.renderToString() // Hello John Cena !
```

Reflection also works with lists.

```kotlin
val users = listOf(Person("John", "Cena"), Person("Carl", "Bert"))
val template = TT.template("<li>{{ firstName }} {{ lastName }}</li>")
val usersTemplate = users.asTemplate(template)
userTemplate.renderToString() // <li>John Cena</li><li>Carl Bert</li>
```