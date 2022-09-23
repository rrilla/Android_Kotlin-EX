package com.example.testchart.util

import java.util.*

class Utils {
    companion object {

        enum class UNIT {
            METER, YARD, FEET, DEGREE, MILES_PER_HOUR, METER_PER_SEC
        }
        val UNIT_YARD = 1.09361f
        val UNIT_FEET = 3.28084f
        val UNIT_MILE = 2.23694f
        val CLUB_NAME = arrayOf(
            "W1", "W3", "W4", "W5", "W6", "W7",
            "U3", "U4", "U5", "U6", "U7", "I3",
            "I4", "I5", "I6", "I7", "I8", "I9",
            "PW", "GW", "SW", "LW"
        )
        val CLUB_APEX_METER = intArrayOf(
            75, 75, 75, 75, 75,
            75, 60, 60, 60, 60,
            60, 60, 60, 60, 60,
            60, 45, 45, 45, 45,
            45, 45
        )
        val CLUB_APEX_FEET = intArrayOf(
            240, 240, 225, 225, 210,
            210, 210, 210, 195, 195,
            180, 180, 180, 180, 165,
            165, 150, 150, 150, 150,
            135, 135
        )

        private val mapUnit: Map<String, UNIT> =
            HashMap<String, UNIT>()

        fun floatRoundPosition(value: Float, position: Int): Float {
            if (position < 0) throw RuntimeException("Position less than 0")
            return (Math.round(value * (10 * position)) / 10.0 * position).toFloat()
        }

        // TODO: Pref에 저장하고 관리해야 됨 - 기기에 설정된 단위
        // 거리 단위
        fun getSettingUnit(): Byte {
            val locale = Locale.getDefault()
            return when (locale.language) {
                Locale.KOREA.language -> 4
                Locale.JAPAN.language -> 7
                else -> 0
            }
        }

        // 단위에 따른 값
        fun valueForUnit(value: Float, toUnit: String): Float {
            if (toUnit == null) return value
            return when (toUnit) {
                "yd" -> value * UNIT_YARD
                "ft" -> value * UNIT_FEET
                "mph" -> value * UNIT_MILE
                else -> value
            }
        }

        fun getClubApex(clubCode: Int, unit: Int): Int {
            return if (clubCode < 0 || clubCode >= CLUB_NAME.size) 0 else {
                if (unit > 3) {
                    CLUB_APEX_METER[clubCode]
                } else CLUB_APEX_FEET[clubCode]
            }
        }
    }
}