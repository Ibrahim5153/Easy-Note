package com.aqube.notes.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.aqube.notes.core.R

@ExperimentalComposeUiApi
@Composable
fun SearchTextField(
    text: String,
    hint: String,
    modifier: Modifier = Modifier,
    isHintVisible: Boolean = true,
    onValueChange: (String) -> Unit,
    onTextChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = true,
    onFocusChange: (FocusState) -> Unit,
    widthPercentage: Float = 1f
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Box(modifier) {

        TextField(
            value = text,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth(widthPercentage)
                .onFocusChanged { onFocusChange(it) },
            textStyle = textStyle,
            leadingIcon = {
                Icon(
                    painterResource(R.drawable.ic_serach),
                    contentDescription = "",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(0.dp),
                    tint = Color.DarkGray
                )
            },
            trailingIcon = {
                if (text.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            onTextChange("")
                            // Remove text from TextField when you press the 'X' icon
                        }
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_cross),
                            contentDescription = "",
                            modifier = Modifier
                                .size(20.dp)
                                .padding(0.dp),
                            tint = Color.DarkGray
                        )
                    }
                }
            },
            singleLine = singleLine,
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                backgroundColor = Color.White,
                cursorColor = Color.Black,
                disabledLabelColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search,
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onTextChange(text)
                    keyboardController?.hide()
                },
            )
        )

        if (isHintVisible)
            Text(
                text = hint,
                style = textStyle,
                color = Color.DarkGray,
                modifier = Modifier.padding(start = 50.dp, top = 16.dp)
            )

    }
}