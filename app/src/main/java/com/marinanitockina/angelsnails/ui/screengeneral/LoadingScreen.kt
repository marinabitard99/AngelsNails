package com.marinanitockina.angelsnails.ui.screengeneral

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.marinanitockina.angelsnails.ui.theme.DarkPink

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
    ) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = DarkPink)
    }
}