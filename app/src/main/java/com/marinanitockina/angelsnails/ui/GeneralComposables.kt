package com.marinanitockina.angelsnails.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

@Preview("Chip")
@Composable
fun ChipPreview() {
    Chip(text = "25.00 €")
}

