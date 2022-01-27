package com.marinanitockina.angelsnails.ui.generalcomposables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marinanitockina.angelsnails.ui.theme.DarkPink

// https://stackoverflow.com/questions/68600128/remove-default-padding-on-jetpack-compose-textfield
@Composable
fun BottomOutlineTextField(
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    Column {

        BasicTextField(
            modifier = modifier,
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                color = DarkPink,
                fontSize = 16.sp
            ),
            enabled = false,
            decorationBox = { innerTextField ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = Color.LightGray,
                            fontSize = 16.sp,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }
                innerTextField()
            }
        )

        Divider(color = DarkPink, thickness = 1.dp, modifier = modifier)
    }

}

@Preview("BottomOutlineText")
@Composable
fun BottomOutlineTextPreview() {
    BottomOutlineTextField(
        placeholder = "Hello",
        value = "",
        onValueChange = {}
    )
}