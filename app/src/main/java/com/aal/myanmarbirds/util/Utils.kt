package com.aal.myanmarbirds.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun Long.toReadableDate(): String {
    val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH)

    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .format(formatter)
}