package com.elementzit.mc.ui.screens

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.elementzit.mc.R
import com.elementzit.mc.ui.theme.MarketplaceTheme
import kotlinx.coroutines.delay
import androidx.core.net.toUri
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController

@OptIn(UnstableApi::class)
@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onSplashFinished: () -> Unit = {},
    navController: NavController
) {

    val context = LocalContext.current


    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {

            val videoUri = "android.resource://${context.packageName}/${R.raw.mc_elementz}".toUri()

            val mediaItem = MediaItem.fromUri(videoUri)

            setMediaItem(mediaItem)

            prepare()

            playWhenReady = true

            repeatMode = Player.REPEAT_MODE_OFF
        }
    }

    DisposableEffect(
        AndroidView(
            factory = {
                PlayerView(it).apply {

                    player = exoPlayer

                    useController = false

                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    ) {
        val listener = object : Player.Listener {

            override fun onPlaybackStateChanged(playbackState: Int) {

                if (playbackState == Player.STATE_ENDED) {

                    navController.navigate("login") {

                        popUpTo("splash") {
                            inclusive = true
                        }
                    }
                }
            }
        }
        exoPlayer.addListener(listener)

        onDispose {

            exoPlayer.removeListener(listener)

            exoPlayer.release()
        }
    }

    /*val scale = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800)
        )
        delay(1500L)
        onSplashFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.ic_mc_background),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(180.dp)
                    .scale(scale.value)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Magicians Carts",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color(0xFFFF5722), //changing text color 0xFF002868
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            )
        }
    }*/
}

/*@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    MarketplaceTheme {
        SplashScreen(navController = navController)
    }
}*/
