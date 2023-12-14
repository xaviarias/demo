package com.example

import io.micronaut.context.annotation.Requires
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.core.bind.ArgumentBinder.BindingResult
import io.micronaut.core.bind.annotation.AbstractArgumentBinder
import io.micronaut.core.convert.ArgumentConversionContext
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.Sort
import io.micronaut.data.runtime.http.PageableRequestArgumentBinder
import io.micronaut.http.HttpRequest
import io.micronaut.http.bind.binders.AnnotatedRequestArgumentBinder
import io.micronaut.http.bind.binders.RequestArgumentBinder
import jakarta.inject.Singleton
import java.util.Optional

@Singleton
@Requires(classes = [RequestArgumentBinder::class])
class MappedPageableRequestArgumentBinder(
    private val pageableBinder: PageableRequestArgumentBinder
) : AbstractArgumentBinder<Pageable>(null),
    AnnotatedRequestArgumentBinder<PageableMapping, Pageable> {

    override fun getAnnotationType() = PageableMapping::class.java

    override fun bind(context: ArgumentConversionContext<Pageable>, request: HttpRequest<*>): BindingResult<Pageable> {
        val pageable = pageableBinder.bind(context, request).get()
        val orderMappings = findOrderMappings(context)
        validate(pageable.orderBy, orderMappings)

        return BindingResult {
            val propertyMapping = resolvePropertyMapping(orderMappings)
            val mappedPageable = MappedPageable(pageable, propertyMapping)
            Optional.of(mappedPageable)
        }
    }

    private fun findOrderMappings(context: ArgumentConversionContext<Pageable>): List<AnnotationValue<OrderMapping>> {
        val pageableMapping = context.argument.getAnnotation(PageableMapping::class.java)!!
        return pageableMapping.getAnnotations("orderBy", OrderMapping::class.java)
    }

    private fun validate(
        pageable: List<Sort.Order>,
        orderMappings: List<AnnotationValue<OrderMapping>>
    ) {
        val errors = pageable.map {
            it.property
        }.filter {
            orderMappings.none { mapping ->
                mapping["property"] == it && mapping.isTrue("sortable")
            }
        }
        if (errors.isNotEmpty()) {
            throw IllegalArgumentException("Property [${errors.joinToString()}] is not sortable")
        }
    }

    private fun resolvePropertyMapping(orderMappings: List<AnnotationValue<OrderMapping>>): Map<String, String> {
        return orderMappings.filter {
            it.stringValue("mappedName").isPresent
        }.associate {
            it["property"] to it["mappedName"]
        }
    }

    private operator fun AnnotationValue<OrderMapping>.get(name: String): String {
        return getRequiredValue(name, String::class.java)
    }
}
