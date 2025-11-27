package com.example.dto

import com.example.model.Customer
import kotlinx.serialization.Serializable

@Serializable
data class CustomerDto(
    val id: Long? = null,
    val customerName: String? = null,
    val description: String? = null
)

fun Customer.toDto() = CustomerDto(
    id = id,
    customerName = customerName,
    description = description
)

fun CustomerDto.toEntity(existing: Customer? = null): Customer {
    val entity = existing ?: Customer()
    entity.customerName = customerName ?: ""
    entity.description = description ?: ""
    return entity
}