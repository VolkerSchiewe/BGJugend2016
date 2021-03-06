package de.schiewe.volker.bgjugend2016.models

import de.schiewe.volker.bgjugend2016.formatDate
import java.text.SimpleDateFormat
import java.util.*

open class BaseEvent : Comparable<BaseEvent> {
    override fun compareTo(other: BaseEvent): Int = compareValuesBy(this, other, { it.startDate }, { it.endDate }, { it.title })

    open var title: String = ""
    open var startDate: Long? = null
    open var endDate: Long? = null
    open var place: String = ""
    open var dateText: String? = null

    protected val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)

    fun dateString(): String {
        this.dateText?.let { return it }

        if (startDate != null && endDate != null) {
            return "${formatDate(this.startDate, sdf)} - ${formatDate(this.endDate, sdf)}"
        }
        return ""
    }

    companion object Factory {
        fun create(): BaseEvent = BaseEvent()
    }
}
