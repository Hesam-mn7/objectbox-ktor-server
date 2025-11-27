package com.example

import com.example.db.Database
import com.example.dto.CustomerDto
import com.example.dto.toDto
import com.example.dto.toEntity
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.application.call
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.event.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("ObjectBox Server is running")
        }

        route("/customers") {

            get {
                val box = Database.box(com.example.model.Customer::class.java)
                val all = box.all.map { it.toDto() }
                call.respond(all)
            }

            get("{id}") {
                val id = call.parameters["id"]?.toLongOrNull()
                if (id == null) {
                    call.respondText("Invalid id", status = HttpStatusCode.BadRequest)
                    return@get
                }
                val box = Database.box(com.example.model.Customer::class.java)
                val customer = box.get(id)
                if (customer == null) {
                    call.respondText("Not found", status = HttpStatusCode.NotFound)
                } else {
                    call.respond(customer.toDto())
                }
            }

            post {

                val dto = call.receive<CustomerDto>()

                val box = Database.box(com.example.model.Customer::class.java)
                val entity = dto.toEntity()
                val id = box.put(entity)

                // بهتره جواب رو از entity.toDto() بسازیم، که مطمئن باشیم id درست خورده:
                call.respond(HttpStatusCode.Created, entity.toDto())
            }

            put("{id}") {
                val id = call.parameters["id"]?.toLongOrNull()
                if (id == null) {
                    call.respondText("Invalid id", status = HttpStatusCode.BadRequest)
                    return@put
                }

                val box = Database.box(com.example.model.Customer::class.java)
                val existing = box.get(id)
                if (existing == null) {
                    call.respondText("Not found", status = HttpStatusCode.NotFound)
                    return@put
                }

                val dto = call.receive<CustomerDto>()
                val updated = dto.toEntity(existing).apply { this.id = id }
                box.put(updated)
                call.respond(updated.toDto())
            }

            delete("{id}") {
                val id = call.parameters["id"]?.toLongOrNull()
                if (id == null) {
                    call.respondText("Invalid id", status = HttpStatusCode.BadRequest)
                    return@delete
                }
                val box = Database.box(com.example.model.Customer::class.java)
                val removed = box.remove(id)
                if (removed) {
                    call.respondText("Deleted")
                } else {
                    call.respondText("Not found", status = HttpStatusCode.NotFound)
                }
            }
        }
    }
}