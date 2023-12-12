package org.guardteam.mentalguardians.presentation.signin

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.guardteam.mentalguardians.common.state.InputTextState
import org.guardteam.mentalguardians.common.utils.Result
import org.guardteam.mentalguardians.common.utils.isInvalid
import org.guardteam.mentalguardians.common.utils.isValidEmail
import org.guardteam.mentalguardians.presentation.component.InputText
import org.guardteam.mentalguardians.presentation.theme.fontFamily

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    navigateSignUp: () -> Unit = {},
    navigateToHome: () -> Unit = {},
    viewModel: SignInViewModel = hiltViewModel()
) {
    var passwordVisibility by remember { mutableStateOf(false) }
    var emailState by remember {
        mutableStateOf(InputTextState())
    }
    var passwordState by remember {
        mutableStateOf(InputTextState())
    }

    val result by viewModel.result.collectAsStateWithLifecycle()
    val isLogin by viewModel.isLogin.collectAsStateWithLifecycle()

    if (isLogin) {
        navigateToHome()
    }

    result.let {
        val context = LocalContext.current
        if (!result.hasBeenHandled) {
            when (val unhandled = result.getContentIfNotHandled()) {
                is Result.Error -> {
                    Toast.makeText(context, unhandled.error, Toast.LENGTH_SHORT).show()
                }

                is Result.Success -> {
                    viewModel.saveUseData(unhandled.data.loginResult)
                }

                else -> {}
            }
        }
    }

    Column(
        modifier = modifier
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Welcome Back",
            fontSize = 25.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = fontFamily,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 42.dp)
        )
        Text(
            text = "Please enter your email address and password for Login",
            fontFamily = fontFamily,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        InputText(
            value = emailState.value,
            onChange = { newValue ->
                emailState = emailState.copy(
                    value = newValue,
                    isError = !newValue.isValidEmail()
                )
            },
            label = "Enter your email",
            isError = emailState.isError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            trailingIcon = {
                if (emailState.value.isNotEmpty()) {
                    IconButton(onClick = { emailState = emailState.copy(value = "") }) {
                        Icon(imageVector = Icons.Outlined.Clear, contentDescription = "Clear")
                    }
                }
            },
            supportingText = {
                if (emailState.isError) {
                    Text(text = "Email not valid", fontFamily = fontFamily)
                }
            },
            modifier = Modifier.padding(top = 32.dp)
        )

        InputText(
            value = passwordState.value,
            onChange = { newValue ->
                passwordState = passwordState.copy(
                    value = newValue,
                    isError = newValue.length <= 6
                )
            },
            label = "Enter your password",
            isError = passwordState.isError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon =
                    if (passwordVisibility) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility
                val desc = if (passwordVisibility) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(imageVector = icon, contentDescription = desc)
                }
            },
            supportingText = {
                if (passwordState.isError) {
                    Text(text = "Password at least 6 characters", fontFamily = fontFamily)
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        )

        Button(
            enabled = result.peekContent() !is Result.Loading,
            onClick = {
                when {
                    emailState.isInvalid() -> emailState = emailState.copy(isError = true)
                    passwordState.isInvalid() -> passwordState = passwordState.copy(isError = true)
                    else -> viewModel.login(
                        email = emailState.value,
                        password = passwordState.value
                    )
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
        ) {
            Text(
                text = "Sign In",
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text(
                text = "Not Registrar Yet? ",
                fontFamily = fontFamily,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Sign Up",
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    navigateSignUp()
                }
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        SignInScreen()
    }
}