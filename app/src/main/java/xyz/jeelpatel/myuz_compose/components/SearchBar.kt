package xyz.jeelpatel.myuz_compose.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import xyz.jeelpatel.myuz_compose.viewModels.SearchListViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(screens: Array<SearchListViewModel>) {
    var text by remember { mutableStateOf(TextFieldValue()) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                screens.forEach { it.init(text.text) }
                keyboardController?.hide()
            },
        ),
        singleLine = true,
        placeholder = { Text(text = "Search") },
        modifier = Modifier.fillMaxWidth()
    )
}
