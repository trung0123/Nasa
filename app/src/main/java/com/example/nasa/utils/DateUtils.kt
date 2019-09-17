package com.example.nasa.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    val americanTimeZone: TimeZone = TimeZone.getTimeZone("America/Los_Angeles")
    private val americanDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
        timeZone = americanTimeZone
    }
    private val appDateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)

    fun formatDate(date: Date): String = americanDateFormat.format(date)

    fun formatToAppDate(date: Date): String = appDateFormat.format(date)

    fun parseDate(date: String): Date = americanDateFormat.parse(date)!!
}

fun Calendar.isAfter(`when`: Calendar): Boolean {
    val date1 = DateUtils.parseDate(DateUtils.formatDate(this.time))
    val date2 = DateUtils.parseDate(DateUtils.formatDate(`when`.time))
    return date1.after(date2)
}