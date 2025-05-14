package com.example.netfix.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.netfix.R
import com.example.netfix.data.SharedPreferencesAuth
import com.example.netfix.model.User
import com.example.netfix.model.UserDatabase
import com.example.netfix.model.UserProfile
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * Registration screen for new users
 */
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onLoginClick: () -> Unit
) {
    val context = LocalContext.current
    val auth = remember { SharedPreferencesAuth(context) }
    val coroutineScope = rememberCoroutineScope()
    
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    val focusManager = LocalFocusManager.current
    
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
                text = "Create Account",
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
            
            // Name field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
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
            
            // Confirm Password field
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { 
                        focusManager.clearFocus()
                        // Attempt registration
                        validateAndRegister(
                            name, email, password, confirmPassword,
                            onLoading = { isLoading = it },
                            onError = { errorMessage = it },
                            onSuccess = { newUser ->
                                auth.saveLoginState(true, newUser.email, newUser.id)
                                onRegisterSuccess()
                            }
                        )
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
            
            // Register button
            Button(
                onClick = {
                    focusManager.clearFocus()
                    // Attempt registration
                    validateAndRegister(
                        name, email, password, confirmPassword,
                        onLoading = { isLoading = it },
                        onError = { errorMessage = it },
                        onSuccess = { newUser ->
                            auth.saveLoginState(true, newUser.email, newUser.id)
                            onRegisterSuccess()
                        }
                    )
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
                    Text("Sign Up")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Login link
            TextButton(
                onClick = onLoginClick,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = "Already have an account? Sign in",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}

private fun validateAndRegister(
    name: String,
    email: String,
    password: String,
    confirmPassword: String,
    onLoading: (Boolean) -> Unit,
    onError: (String) -> Unit,
    onSuccess: (User) -> Unit
) {
    onLoading(true)
    
    // Validate inputs
    when {
        name.isBlank() -> {
            onError("Please enter your name")
            onLoading(false)
            return
        }
        email.isBlank() -> {
            onError("Please enter your email")
            onLoading(false)
            return
        }
        !isValidEmail(email) -> {
            onError("Please enter a valid email address")
            onLoading(false)
            return
        }
        password.isBlank() -> {
            onError("Please enter a password")
            onLoading(false)
            return
        }
        password.length < 6 -> {
            onError("Password must be at least 6 characters")
            onLoading(false)
            return
        }
        password != confirmPassword -> {
            onError("Passwords do not match")
            onLoading(false)
            return
        }
        UserDatabase.getUserByEmail(email) != null -> {
            onError("Email already registered")
            onLoading(false)
            return
        }
    }
    
    // Create new user
    val userId = UUID.randomUUID().toString()
    val defaultProfile = UserProfile(
        id = UUID.randomUUID().toString(),
        name = name.split(" ").first(),
        preferredGenres = listOf()
    )
    
    val newUser = User(
        id = userId,
        email = email,
        password = password,
        name = name,
        profiles = listOf(defaultProfile)
    )
    
    // Add user to database
    UserDatabase.addUser(newUser)
    
    onLoading(false)
    onSuccess(newUser)
}

private fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    return email.matches(emailRegex.toRegex())
}
