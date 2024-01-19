package com.ravimhzn.androidmockserver.services

import android.util.Log
import com.ravimhzn.androidmockserver.model.HttpMethod
import com.ravimhzn.androidmockserver.model.MockResponse
import com.ravimhzn.androidmockserver.model.RestError
import com.ravimhzn.androidmockserver.utils.GsonConverter
import com.ravimhzn.androidmockserver.utils.Util
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.util.pipeline.PipelineContext
import org.slf4j.event.Level

fun Application.module() {
    configureSerialization()
    configureRouting()
    configureLogging()
}

fun Application.configureRouting() {
    routing {
        handleApiRouting() //Todo handle api routing as per category later
    }
}

fun Route.handleApiRouting() {
    val config = MockConfig()
    val mockResponses = config.getResponses()

    for (responses in mockResponses) {
        handleHttpMethod(responses)
    }
}

private fun Route.handleHttpMethod(responses: MockResponse) {
    val routeHandler: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        try {
            responses.getSelected()?.let { Util.jsonFileReader(it) }
                ?.let {
                    if (Util.isErrorResponse(it)) {
                        val restError = GsonConverter.fromJson(it, RestError::class.java)
                        val httpStatusCode: HttpStatusCode = when (restError.statusCode) {
                            "301" -> HttpStatusCode.MovedPermanently
                            "404" -> HttpStatusCode.NotFound
                            "400" -> HttpStatusCode.BadRequest
                            "423" -> HttpStatusCode.Locked
                            "406" -> HttpStatusCode.NotAcceptable
                            "401" -> HttpStatusCode.Unauthorized
                            "403" -> HttpStatusCode.Forbidden
                            "501" -> HttpStatusCode.NotImplemented
                            "503" -> HttpStatusCode.ServiceUnavailable
                            "504" -> HttpStatusCode.GatewayTimeout
                            else -> HttpStatusCode.InternalServerError
                        }

                        call.respond(httpStatusCode, it)

                    } else {
                        call.respond(it)
                    }
                }
        } catch (e: Exception) {
            call.respondText("${e.message}")
            Log.e("Network Error", e.message.toString())
        }
    }

    when (responses.httpMethod) {
        HttpMethod.GET -> get(responses.path, routeHandler)
        HttpMethod.POST -> post(responses.path, routeHandler)
        // Add more HTTP methods as needed later
        else -> {}
    }
}

fun Application.configureLogging() {
    //Todo
    install(CallLogging) {
        level = Level.INFO
    }
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}
