package com.marinanitockina.angelsnails.ui.screengeneral

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.marinanitockina.angelsnails.R
import com.marinanitockina.angelsnails.ui.theme.DarkPink

// UI to notify user about lack of records
@Composable
fun EmptyRecordsList(caption: String = "No records yet!") {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        // Column with animation and text
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.empty_records))
            LottieAnimation(
                composition,
                iterations = LottieConstants.IterateForever
            )
            Text(
                text = caption,
                style = MaterialTheme.typography.h6,
                color = DarkPink,
                modifier = Modifier.padding(start = 5.dp)
            )
        }
    }
}

// Preview for showing Empty Records UI
@Preview("Empty records")
@Composable
fun EmptyRecordsListPreview() {
    EmptyRecordsList("No records yet!")
}