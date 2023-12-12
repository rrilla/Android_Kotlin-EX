package com.example.testwearapp.complication.dto

import com.google.gson.Gson


data class FcmCallNotification(
    val type: String,
    val data: RunnerNotification
) {
    companion object {
        fun fromMap(data: Map<String, String>): FcmCallNotification {
//            val type = when (data["type"]) {
//                RunnerNotificationType.KITCHEN.key -> RunnerNotificationType.KITCHEN
//                RunnerNotificationType.CUSTOMER.key -> RunnerNotificationType.CUSTOMER
//                else -> throw Exception("FCM data 'type' is null.")
//            }
            val runnerNotification = Gson().fromJson(data["data"], RunnerNotification::class.java)
            return FcmCallNotification(
                type = data["type"] ?: throw Exception("FCM data 'type' is null."),
                data = runnerNotification
            )
        }
    }
}

data class RunnerKitchenNotificationMessage (
    val text: String
)
data class RunnerStoreTableCallNotificationMessage(
    // TODO: StoreTable 타입으로 바꿔야됨.
    val text: String
)
data class RunnerNotification(
    val id: Int,
    val type: RunnerNotificationType,
    val message: Any,
    val createdAt: String,
    val approvedBy: String?
) {
    inline fun<reified T> getMessages():T {
        return Gson().fromJson(message.toString(), T::class.java)
    }
}

enum class RunnerNotificationType(val key: String) {
    KITCHEN("KITCHEN"),
    CUSTOMER("STORE_TABLE_CALL")
}