package com.example.music.common

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import com.example.music.R
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.util.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/*
 * (Media) Session events
 */
const val NETWORK_FAILURE = "com.example.music.common.media.session.NETWORK_FAILURE"

class MusicService: MediaBrowserServiceCompat() {
    private val TAG = this::class.java.simpleName

    private lateinit var notificationManager: MyNotificationManager

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    protected lateinit var mediaSession: MediaSessionCompat

    private var currentPlaylistItems: List<MediaMetadataCompat> = emptyList()
    private var currentMediaItemIndex: Int = 0

    private lateinit var storage: PersistentStorage

    private var isForegroundService = false

    private val myAudioAttributes = AudioAttributes.Builder()
        .setContentType(C.CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()
    private val playerListener = PlayerEventListener()
    private val exoPlayer: ExoPlayer by lazy {
        SimpleExoPlayer.Builder(this).build().apply {
            setAudioAttributes(myAudioAttributes, true)
            setHandleAudioBecomingNoisy(true)
            addListener(playerListener)
        }
    }

    override fun onCreate() {
        super.onCreate()

        //  빌드UI를 시작하는 데 사용할 수 있는 PendingIntent
        val sessionActivityPendingIntent =
            packageManager?.getLaunchIntentForPackage(packageName)?.let { sessionIntent ->
                PendingIntent.getActivity(this, 0, sessionIntent, 0)
            }

        mediaSession = MediaSessionCompat(this, "MusicService")
            .apply {
                setSessionActivity(sessionActivityPendingIntent)
                isActive = true
            }

        sessionToken = mediaSession.sessionToken

        notificationManager = MyNotificationManager(
            this,
            mediaSession.sessionToken,
            PlayerNotificationListener()
        )

        notificationManager.showNotificationForPlayer(exoPlayer)
    }

    /**
     * 이 코드는 최근 활동을 삭제할 때 UAMP의 재생을 중지하는 코드입니다.
     * 이를 위한 선택은 앱에 따라 다릅니다.
     * 일부 앱은 재생을 중지하는 반면, 다른 앱은 재생이 계속되도록 허용하고 사용자가 알림을 사용하여 재생을 중지할 수 있습니다.
     * */
    override fun onTaskRemoved(rootIntent: Intent?) {
        saveRecentSongToStorage()
        super.onTaskRemoved(rootIntent)

        /**
         * 재생을 중지하면 플레이어가 [Player.STATE_IDLE] 트리거링으로 전환됩니다
        [Player.EventListener.onPlayerStateChanged]을(를) 호출합니다.
        이것은 원인이 될 것입니다
        숨김 및 트리거할 알림
        [PlayerNotificationManager.NotificationListener.onNotificationCancelled] 호출됩니다.
        그러면 서비스가 포그라운드 서비스로 제거되고 호출됩니다
        [stopSelf].
         * */
        exoPlayer.stop(true)
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        TODO("Not yet implemented")
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        TODO("Not yet implemented")
    }

    private fun saveRecentSongToStorage() {

        // Obtain the current song details *before* saving them on a separate thread, otherwise
        // the current player may have been unloaded by the time the save routine runs.
        // 별도의 스레드에 저장하기 전* 현재 노래 세부 정보를 가져옵니다.
        // 그렇지 않으면 저장 루틴이 실행될 때까지 현재 플레이어가 언로드되었을 수 있습니다.
        if (currentPlaylistItems.isEmpty()) {
            return
        }
        val description = currentPlaylistItems[currentMediaItemIndex].description
        val position = exoPlayer.currentPosition

        serviceScope.launch {
            storage.saveRecentSong(
                description,
                position
            )
        }
    }



    private inner class PlayerNotificationListener :
        PlayerNotificationManager.NotificationListener {
        override fun onNotificationPosted(
            notificationId: Int,
            notification: Notification,
            ongoing: Boolean
        ) {
            Log.d(TAG, "onNotificationPosted()")
            if (ongoing && !isForegroundService) {
                ContextCompat.startForegroundService(
                    applicationContext,
                    Intent(applicationContext, this@MusicService.javaClass)
                )

                startForeground(notificationId, notification)
                isForegroundService = true
            }
        }

        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
            Log.d(TAG, "onNotificationCancelled()")
            stopForeground(true)
            isForegroundService = false
            stopSelf()
        }
    }

    private inner class PlayerEventListener : Player.Listener {

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            Log.d(TAG, "onPlayerStateChanged()")
            when (playbackState) {
                Player.STATE_BUFFERING,
                Player.STATE_READY -> {
                    notificationManager.showNotificationForPlayer(exoPlayer)
                    if (playbackState == Player.STATE_READY) {

                        // When playing/paused save the current media item in persistent
                        // storage so that playback can be resumed between device reboots.
                        // Search for "media resumption" for more information.
                        // 재생/일시 중지 시 현재 미디어 항목을 지속적으로 저장
                        // 장치 재부팅 사이에 재생을 다시 시작할 수 있도록 저장합니다.
                        // 자세한 내용은 "미디어 재개"를 검색하십시오.
                        saveRecentSongToStorage()

                        if (!playWhenReady) {
                            // If playback is paused we remove the foreground state which allows the
                            // notification to be dismissed. An alternative would be to provide a
                            // "close" button in the notification which stops playback and clears
                            // the notification.
                            // 재생이 일시 중지되면 알림을 해제할 수 있는 포그라운드 상태를 제거합니다.
                            // 또는 알림에 재생을 중지하고 알림을 지우는 "닫기" 버튼을 제공하는 것이 좋습니다.
                            stopForeground(false)
                            isForegroundService = false
                        }
                    }
                }
                else -> {
                    notificationManager.hideNotification()
                }
            }
        }

        override fun onEvents(player: Player, events: Player.Events) {
            Log.d(TAG, "onEvents()")
            if (events.contains(Player.EVENT_POSITION_DISCONTINUITY)
                || events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION)
                || events.contains(Player.EVENT_PLAY_WHEN_READY_CHANGED)) {
                currentMediaItemIndex = if (currentPlaylistItems.isNotEmpty()) {
                    Util.constrainValue(
                        player.currentMediaItemIndex,
                        /* min= */ 0,
                        /* max= */ currentPlaylistItems.size - 1
                    )
                } else 0
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            Log.d(TAG, "onPlayerError()")
            var message = R.string.generic_error;
            Log.e(TAG, "Player error: " + error.errorCodeName + " (" + error.errorCode + ")");
            if (error.errorCode == PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS
                || error.errorCode == PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND) {
                message = R.string.error_media_not_found;
            }
            Toast.makeText(
                applicationContext,
                message,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
