package br.com.smart.schedule.exception

import java.util.*

data class ExceptionResponse(
    val timestamp: Date? = null,
    val message: String? = null,
    val details: String? = null
)
