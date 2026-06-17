package com.mattg.wahuntingregs.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.io.InputStream

// AssetImage for displaying images.  PointsDiagramScreen, IdentificationScreen use this
@Composable
fun AssetImage(fileName: String) {
    val context = LocalContext.current

    var isFullscreen by remember { mutableStateOf(false) }

    val bitmap = remember(fileName) {
        try {
            val inputStream: InputStream = context.assets.open(fileName)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            null
        }
    }

    bitmap?.let { bmp ->

        // Normal Image
        Image(
            bitmap = bmp.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().height(300.dp).clickable { isFullscreen = !isFullscreen }
        )

        // Fullscreen Image
        if (isFullscreen) {
            Box(
                modifier = Modifier.fillMaxSize().clickable { isFullscreen = !isFullscreen }
            ) {
                Image(
                    bitmap = bmp.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}