package xyz.jeelpatel.myuz_compose.views

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier


@Composable
fun BackPressHandler(
    backPressedDispatcher: OnBackPressedDispatcher? =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
    onBackPressed: () -> Unit
) {
    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }

    DisposableEffect(key1 = backPressedDispatcher) {
        backPressedDispatcher?.addCallback(backCallback)

        onDispose {
            backCallback.remove()
        }
    }
}


@Composable
fun MusicPlayer(toggle: Boolean,onBackPressed: () -> Unit){
    if(toggle) BackPressHandler(onBackPressed = onBackPressed)
    AnimatedVisibility(toggle,
        enter = slideInVertically(
            initialOffsetY = { 50 }
        ) + fadeIn(initialAlpha = 0.3f),
        exit = fadeOut()
    ) {
        Text("MUSIC PLAYER UI TO BE ADDED",modifier = Modifier.background(MaterialTheme.colors.background).fillMaxSize())
        // TODO : CONTENT BE ADDED
    }
}