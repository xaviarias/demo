package com.example

import io.micronaut.data.model.Pageable
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller
class TestController {

    @Get("/test")
    fun test(
        @PageableMapping(
            orderBy = [
                OrderMapping(
                    property = "name",
                    mappedName = "mappedName"
                )
            ]
        ) pageable: Pageable
    ): HttpResponse<TestDto> {
        // Run JPA queries with the pageable argument sorted by the mapped name
        return HttpResponse.ok(TestDto("test"))
    }
}
