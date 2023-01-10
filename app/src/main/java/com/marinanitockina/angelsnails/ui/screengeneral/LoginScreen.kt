package com.marinanitockina.angelsnails.ui.screengeneral

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.marinanitockina.angelsnails.R
import com.marinanitockina.angelsnails.ui.theme.AngelsNailsTheme
import com.marinanitockina.angelsnails.ui.theme.DarkPink

// UI for login screen
@Composable
fun LoginScreen(loginMethod: (AuthCredential) -> Unit = {}, isLoading: Boolean = false) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {

            Image(
                painterResource(R.drawable.logo),
                "Logo",
                modifier = Modifier.padding(top = 100.dp, bottom = 300.dp)
            )

            if (!isLoading) {
                GoogleSignInButton(loginMethod)
            } else {
                CircularProgressIndicator(color = DarkPink)
            }

        }
    )

}

// Button that starts log in process
@Composable
fun GoogleSignInButton(loginMethod: (AuthCredential) -> Unit = {}) {
    val context = LocalContext.current
    val token = stringResource(R.string.default_web_client_id)

    // Equivalent of onActivityResult
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                loginMethod(credential)
            } catch (e: ApiException) {
                Log.w("TAG", "Google sign in failed", e)
            }
        }

    OutlinedButton(
        border = BorderStroke(1.dp, DarkPink),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        onClick = {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(token)
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(context, gso)
            launcher.launch(googleSignInClient.signInIntent)
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    Icon(
                        tint = Color.Unspecified,
                        painter = painterResource(id = R.drawable.googleg_standard_color_18),
                        contentDescription = null,
                    )
                    Text(
                        style = MaterialTheme.typography.button,
                        color = DarkPink,
                        text = "Sign in with Google"
                    )
                    Icon(
                        tint = Color.Transparent,
                        imageVector = Icons.Default.MailOutline,
                        contentDescription = null,
                    )
                }
            )
        }
    )

}

// Login screen preview
@Preview(name = "Login screen", showBackground = true)
@Composable
fun LoginScreenPreview() {
    AngelsNailsTheme {
        LoginScreen(isLoading = false)
    }
}

// Google Button preview
@Preview(name = "Google Button", showBackground = true)
@Composable
fun GoogleButtonPreview() {
    AngelsNailsTheme {
        GoogleSignInButton()
    }
}