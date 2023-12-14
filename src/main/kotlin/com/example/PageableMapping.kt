package com.example

import io.micronaut.core.bind.annotation.Bindable

@Bindable
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class PageableMapping(
    val orderBy: Array<OrderMapping>
)
