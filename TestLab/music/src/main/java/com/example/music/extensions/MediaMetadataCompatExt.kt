package com.example.music.extensions

import android.support.v4.media.MediaMetadataCompat

inline val MediaMetadataCompat.id: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)