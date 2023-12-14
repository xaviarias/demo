package com.example

import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.server.exceptions.ExceptionHandler
import jakarta.inject.Singleton

@Singleton
@Requires(classes = [ExceptionHandler::class, IllegalArgumentException::class])
class IllegalArgumentExceptionHandler : ExceptionHandler<IllegalArgumentException, HttpResponse<*>> {

    override fun handle(request: HttpRequest<*>, exception: IllegalArgumentException): HttpResponse<*> {
        return HttpResponse.badRequest(mapOf("errorMessage" to exception.message))
    }
}
