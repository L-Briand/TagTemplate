package net.orandja.templator.sample

import net.orandja.templator.TT
import net.orandja.templator.asGroup
import net.orandja.templator.asRoll
import net.orandja.templator.renderToString
import net.orandja.templator.rollWithTemplate


class Main : Runnable {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Main().run()
        }
    }

    data class UserInformation(val firstName: String, val lastName: String)

    override fun run() {
        val user = UserInformation("ABC", "FOO")
        val users = listOf(
            user,
            UserInformation("DEF", "FOO"),
            UserInformation("ABC", "BAR"),
        )

        val userTemplate = TT.template("Hello {{ firstName }}, is your lastname {{ lastName }} ?\n")

        val templates = with(TT) {
            val userInfo1 = group(
                mapOf(
                    "firstName" to value(user.firstName),
                    "lastName" to value(user.lastName),
                )
            )
            val userInfo2 = user.asGroup()

            val usersInfo1 = roll(users.map {
                it.asGroup()
            })

            val usersInfo2 = users.asRoll()

            group(
                "template" to template("Test : {{ users3 }}"),
                "user" to onContext(userInfo1, userTemplate),
                "user2" to onContext(userInfo2, userTemplate),
                "users1" to onContext(usersInfo1, repeat(users.size, userTemplate)),
                "users2" to onContext(usersInfo2, repeat(users.size, userTemplate)),
                "users3" to users.rollWithTemplate(userTemplate),
            )
        }

        println(templates.renderToString("template"))
    }
}