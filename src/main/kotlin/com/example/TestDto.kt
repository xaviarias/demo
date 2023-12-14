package com.example

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class TestDto(val name: String)
