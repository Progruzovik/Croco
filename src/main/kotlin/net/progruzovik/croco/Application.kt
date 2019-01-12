package net.progruzovik.croco

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java)
}

inline fun <reified T: Any> getLogger(): Logger = LoggerFactory.getLogger(T::class.java)
