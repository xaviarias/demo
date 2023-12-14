package com.example

import io.micronaut.data.model.Pageable
import io.micronaut.data.model.Sort

class MappedPageable(
    private val pageable: Pageable,
    private val mapping: Map<String, String>
) : Pageable by pageable {
    override fun getSort(): Sort = Sort.of(orderBy)

    override fun getOrderBy(): List<Sort.Order> {
        return pageable.orderBy.map {
            mapping[it.property]?.let { property ->
                Sort.Order(property, it.direction, it.isIgnoreCase)
            } ?: it
        }
    }
}
