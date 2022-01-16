package com.marinanitockina.angelsnails.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

