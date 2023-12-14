package com.example

@Retention(AnnotationRetention.RUNTIME)
annotation class OrderMapping(
    val property: String,
    val mappedName: String = "",
    val sortable: Boolean = true
)
