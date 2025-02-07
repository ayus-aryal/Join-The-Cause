package com.example.jointhecause.myviewmodels

import androidx.lifecycle.ViewModel
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.example.jointhecause.R
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.util.UUID

class GoogleSignInViewModel : ViewModel() {


    // Function to handle Google Sign-In
    fun handleGoogleSignIn(context: Context, navController: NavController) {
        viewModelScope.launch {

            // Collect the result of the Google Sign-In process
            googleSignIn(context).collect { result ->
                result.fold( // It allows you to specify actions for both success and failure cases of the operation, making it easy to manage the different outcomes.
                    onSuccess = { authResult ->
                        // Handle successful sign-in

                            // Show success message
                            Toast.makeText(
                                context,
                                "Account created successfully!",
                                Toast.LENGTH_LONG
                            ).show()
                            // Navigate to the home screen
                            navController.navigate("fill_your_details"){
                                popUpTo("welcome_screen") { inclusive = true }
                                launchSingleTop = true
                            }


                    },
                    onFailure = { e ->
                        // Handle sign-in error
                        Toast.makeText(
                            context,
                            "Something went wrong: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d("Issue", "handleGoogleSignIn: ${e.message}")
                    }
                )
            }
        }
    }

    // Function to perform Google Sign-In and return a Flow of AuthResult
    private suspend fun googleSignIn(context: Context): Flow<Result<AuthResult>> {
        // Initialize Firebase Auth instance
        val firebaseAuth = FirebaseAuth.getInstance()

        // Return a Flow that emits the result of the Google Sign-In process
        return callbackFlow {
            try {
                // Initialize Credential Manager
                val credentialManager: CredentialManager = CredentialManager.create(context)

                // Generate a nonce (a random number used once) for security
                val ranNonce: String = UUID.randomUUID().toString()
                val bytes: ByteArray = ranNonce.toByteArray()
                val md: MessageDigest = MessageDigest.getInstance("SHA-256")
                val digest: ByteArray = md.digest(bytes)
                val hashedNonce: String = digest.fold("") { str, it -> str + "%02x".format(it) }

                // Set up Google ID option with necessary parameters
                val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false) // To give the user the option to choose from any Google account on their device, not just the ones they've used with your app before.
                    .setServerClientId(context.getString(R.string.web_client_id)) // This is required to identify the app on the backend server.
                    .setNonce(hashedNonce) // A nonce is a unique, random string used to ensure that the ID token received is fresh and to prevent replay attacks.
                    .setAutoSelectEnabled(true) // Which allows the user to be automatically signed in without additional user interaction if there is a single eligible account.
                    .build()

                // Create a credential request with the Google ID option
                val request: GetCredentialRequest = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                // Get the credential result from the Credential Manager
                val result = credentialManager.getCredential(context, request)
                val credential = result.credential

                // Check if the received credential is a valid Google ID Token
                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    // Extract the Google ID Token credential
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    // Create an auth credential using the Google ID Token
                    val authCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                    // Sign in with Firebase using the auth credential
                    val authResult = firebaseAuth.signInWithCredential(authCredential).await() // .await() -> allows the coroutine to wait for the result of the authentication operation before proceeding.
                    // Send the successful result
                    trySend(Result.success(authResult)) // Is used to send the result of the Firebase sign-in operation to the Flow's collectors.
                } else {
                    // Throw an exception if the credential type is invalid
                    throw RuntimeException("Received an invalid credential type.")
                }

            } catch (e: GetCredentialCancellationException) {
                // Handle sign-in cancellation
                trySend(Result.failure(Exception("Sign-in was canceled.")))
            } catch (e: Exception) {
                // Handle other exceptions
                trySend(Result.failure(e))
            }

            // When a collector starts collecting from the callbackFlow, the flow remains open and ready to emit values until the awaitClose block is reached or the flow is cancelled.
            // Even though the current block is empty, in other scenarios, you might use the awaitClose block to unregister listeners or release resources associated with the callback-based API.
            awaitClose { }
        }
    }

}