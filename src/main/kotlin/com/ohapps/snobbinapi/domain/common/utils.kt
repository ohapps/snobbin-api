package com.ohapps.snobbinapi.domain.common

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.math.round

val <T : Any> T.log: Logger get() = LoggerFactory.getLogger(javaClass)

fun generateId() = UUID.randomUUID()

fun Double.roundToIncrement(increment: Float) = when (increment) {
    0.25F -> round(this * 4) / 4
    0.5F -> round(this * 2) / 2
    else -> round(this)
}
