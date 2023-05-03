package com.example.testchart.data

import java.util.*

data class StatsTargetDay(
    val averageScore: Float,
    val stats: List<StatsTargetInfo>,
    val targetDistance: Int,
    val targetUnit: String
){
    fun getTargetDistanceToMeter(): Float {
        return if (targetUnit.lowercase(Locale.getDefault())
                .compareTo("yard") == 0 || targetUnit.lowercase(
                Locale.getDefault()
            ).compareTo("yd") == 0
        ) {
            targetDistance * 1.09361f
        } else targetDistance.toFloat()
    }
}