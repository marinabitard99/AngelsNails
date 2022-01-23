package com.marinanitockina.angelsnails.ui.generalcomposables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marinanitockina.angelsnails.ui.theme.DarkPink
import com.marinanitockina.angelsnails.ui.theme.Pink100

@Composable
fun Chip(
    modifier: Modifier = Modifier,
    text: String = ""
) {
    Surface(
        elevation = 8.dp,
        shape = RoundedCornerShape(16.dp),
        color = Pink100,
        border = BorderStroke(
            width = 2.dp,
            color = DarkPink
        ),
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            fontSize = 16.sp,
            color = DarkPink,
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 12.dp)
        )
    }
}

@Composable
fun ChipButton(text: String, modifier: Modifier = Modifier, onClickAction: () -> Unit = {}) {
    OutlinedButton(
        onClick = { onClickAction() },
        shape = RoundedCornerShape(25.dp),
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(backgroundColor = Pink100),
        border = BorderStroke(1.dp, DarkPink)
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            modifier = modifier,
            color = DarkPink
        )
    }
}

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

@Preview("Chip")
@Composable
fun ChipPreview() {
    Chip(text = "25.00 €")
}

@Preview("ChipButton")
@Composable
fun ChipButtonPreview() {
    Row {
        ChipButton(text = "SELECT", modifier = Modifier.align(Alignment.CenterVertically))
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

