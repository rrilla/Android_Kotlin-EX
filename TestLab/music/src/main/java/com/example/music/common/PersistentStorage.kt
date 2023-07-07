package com.example.music.common

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PersistentStorage private constructor(val context: Context) {

    /**
     * 가장 최근에 재생된 노래와 같이 다시 시작할 때까지 유지해야 하는 모든 데이터를 저장합니다.
     * */
    private var preferences: SharedPreferences = context.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    companion object {

        @Volatile
        private var instance: PersistentStorage? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: PersistentStorage(context).also { instance = it }
            }
    }

    suspend fun saveRecentSong(description: MediaDescriptionCompat, position: Long) {
        withContext(Dispatchers.IO) {

            /**
             * After booting, Android will attempt to build static media controls for the most
             * recently played song. Artwork for these media controls should not be loaded
             * from the network as it may be too slow or unavailable immediately after boot. Instead
             * we convert the iconUri to point to the Glide on-disk cache.
             */
            val localIconUri = Glide.with(context).asFile().load(description.iconUri)
                .submit(NOTIFICATION_LARGE_ICON_SIZE, NOTIFICATION_LARGE_ICON_SIZE).get()
                .asAlbumArtContentUri()

            preferences.edit()
                .putString(RECENT_SONG_MEDIA_ID_KEY, description.mediaId)
                .putString(RECENT_SONG_TITLE_KEY, description.title.toString())
                .putString(RECENT_SONG_SUBTITLE_KEY, description.subtitle.toString())
                .putString(RECENT_SONG_ICON_URI_KEY, localIconUri.toString())
                .putLong(RECENT_SONG_POSITION_KEY, position)
                .apply()
        }
    }

    fun loadRecentSong(): MediaBrowserCompat.MediaItem? {
        val mediaId = preferences.getString(RECENT_SONG_MEDIA_ID_KEY, null)
        return if (mediaId == null) {
            null
        } else {
            val extras = Bundle().also {
                val position = preferences.getLong(RECENT_SONG_POSITION_KEY, 0L)
                it.putLong(MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS, position)
            }

            MediaBrowserCompat.MediaItem(
                MediaDescriptionCompat.Builder()
                    .setMediaId(mediaId)
                    .setTitle(preferences.getString(RECENT_SONG_TITLE_KEY, ""))
                    .setSubtitle(preferences.getString(RECENT_SONG_SUBTITLE_KEY, ""))
                    .setIconUri(Uri.parse(preferences.getString(RECENT_SONG_ICON_URI_KEY, "")))
                    .setExtras(extras)
                    .build(), MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
            )
        }
    }
}

private const val PREFERENCES_NAME = "uamp"
private const val RECENT_SONG_MEDIA_ID_KEY = "recent_song_media_id"
private const val RECENT_SONG_TITLE_KEY = "recent_song_title"
private const val RECENT_SONG_SUBTITLE_KEY = "recent_song_subtitle"
private const val RECENT_SONG_ICON_URI_KEY = "recent_song_icon_uri"
private const val RECENT_SONG_POSITION_KEY = "recent_song_position"