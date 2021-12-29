package com.marinanitockina.angelsnails.ui

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.marinanitockina.angelsnails.AppViewModel
import com.marinanitockina.angelsnails.R
import com.marinanitockina.angelsnails.model.LoadingState
import com.marinanitockina.angelsnails.ui.theme.AngelsNailsTheme

@Composable
fun LoginScreen(viewModel: AppViewModel) {

    val snackbarHostState = remember { SnackbarHostState() }
    val state by viewModel.loadingState.collectAsState()

    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
        topBar = {
            LoginToolbar(state = state)
        },
        content = {
            EmailPasswordSignIn(viewModel = viewModel, state)
        }
    )
}

@Composable
fun LoginToolbar(state: LoadingState) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TopAppBar(
            backgroundColor = Color.White,
            elevation = 1.dp,
            title = {
                Text(text = "Login")
            },
            navigationIcon = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = null,
                    )
                }
            },
            actions = {
                IconButton(onClick = { Firebase.auth.signOut() }) {
                    Icon(
                        imageVector = Icons.Rounded.ExitToApp,
                        contentDescription = null,
                    )
                }
            }
        )
        if (state.status == LoadingState.Status.RUNNING) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun EmailPasswordSignIn(viewModel: AppViewModel, state: LoadingState) {

    var userEmail by remember { mutableStateOf("") }
    var userPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = userEmail,
                label = {
                    Text(text = "Email")
                },
                onValueChange = {
                    userEmail = it
                }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                value = userPassword,
                label = {
                    Text(text = "Password")
                },
                onValueChange = {
                    userPassword = it
                }
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = userEmail.isNotEmpty() && userPassword.isNotEmpty(),
                content = {
                    Text(text = "Login")
                },
                onClick = {
                    viewModel.signInWithEmailAndPassword(userEmail.trim(), userPassword.trim())
                }
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.caption,
                text = "Login with"
            )

            Spacer(modifier = Modifier.height(18.dp))

            GoogleSignInButton(viewModel = viewModel, state = state)

        }
    )
}

@Composable
fun GoogleSignInButton(viewModel: AppViewModel, state: LoadingState) {
    val context = LocalContext.current
    val token = stringResource(R.string.default_web_client_id)

    // Equivalent of onActivityResult
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                viewModel.signWithCredential(credential)
            } catch (e: ApiException) {
                Log.w("TAG", "Google sign in failed", e)
            }
        }

    OutlinedButton(
        border = ButtonDefaults.outlinedBorder.copy(width = 1.dp),
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
                        color = MaterialTheme.colors.onSurface,
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

    when (state.status) {
        LoadingState.Status.SUCCESS -> {
            Text(text = "Success")
        }
        LoadingState.Status.FAILED -> {
            Text(text = state.msg ?: "Error")
        }
        else -> {}
    }

}


@Preview(name = "Regular Sign In", showBackground = true)
@Composable
fun RegularSignInPreview() {
    AngelsNailsTheme {
        EmailPasswordSignIn(viewModel = AppViewModel(), LoadingState.IDLE)
    }
}

@Preview(name = "Google Button", showBackground = true)
@Composable
fun GoogleButtonPreview() {
    AngelsNailsTheme {
        GoogleSignInButton(viewModel = AppViewModel(),  LoadingState.IDLE)
    }
}