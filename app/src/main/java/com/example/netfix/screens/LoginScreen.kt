package com.example.netfix.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.netfix.R
import com.example.netfix.data.SharedPreferencesAuth
import com.example.netfix.model.UserDatabase
import kotlinx.coroutines.launch

/**
 * Login screen for user authentication
 */
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val context = LocalContext.current
    val auth = remember { SharedPreferencesAuth(context) }
    val coroutineScope = rememberCoroutineScope()
    
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    val focusManager = LocalFocusManager.current
    
    // Check if already logged in
    LaunchedEffect(Unit) {
        if (auth.isLoggedIn.value) {
            onLoginSuccess()
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Text(
                text = "NETFIX",
                color = Color(0xFFE50914), // Netflix red
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 48.dp)
            )
            
            // Title
            Text(
                text = "Sign In",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // Error message
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            
            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.DarkGray,
                    unfocusedContainerColor = Color.DarkGray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            
            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { 
                        focusManager.clearFocus()
                        // Attempt login
                        if (email.isNotEmpty() && password.isNotEmpty()) {
                            isLoading = true
                            val user = UserDatabase.validateCredentials(email, password)
                            if (user != null) {
                                auth.saveLoginState(true, user.email, user.id)
                                isLoading = false
                                onLoginSuccess()
                            } else {
                                isLoading = false
                                errorMessage = "Invalid email or password"
                            }
                        } else {
                            errorMessage = "Please enter both email and password"
                        }
                    }
                ),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.DarkGray,
                    unfocusedContainerColor = Color.DarkGray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )
            
            // Sign In button
            Button(
                onClick = {
                    focusManager.clearFocus()
                    // Attempt login
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        isLoading = true
                        val user = UserDatabase.validateCredentials(email, password)
                        if (user != null) {
                            auth.saveLoginState(true, user.email, user.id)
                            isLoading = false
                            onLoginSuccess()
                        } else {
                            isLoading = false
                            errorMessage = "Invalid email or password"
                        }
                    } else {
                        errorMessage = "Please enter both email and password"
                    }
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE50914) // Netflix red
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Sign In")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Register link
            TextButton(
                onClick = onRegisterClick,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = "New to Netflix? Sign up now",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
            
            // Demo credentials
            Text(
                text = "Demo: demo@example.com / password",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 24.dp)
            )
        }
    }
}
