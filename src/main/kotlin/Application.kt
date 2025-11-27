package com.example

import com.example.db.Database
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

fun Application.module() {
    configureMonitoring()
    configureSerialization()

    Database.init()

    configureRouting()

}
