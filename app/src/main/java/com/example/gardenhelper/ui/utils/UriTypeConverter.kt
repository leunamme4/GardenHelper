package com.example.gardenhelper.ui.utils

import android.net.Uri

object UriTypeConverter {

    fun fromUri(uri: Uri): String {
        return uri.toString()
    }

    fun toUri(uriString: String): Uri {
        return uriString.let { Uri.parse(it) }
    }
}
