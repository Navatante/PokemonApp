package com.jcm.pokemonapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

public class LoginActivity extends AppCompatActivity {

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            result -> onSignInResult(result)
    );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        //FirebaseAuth.getInstance().signOut(); // TODO this line is just to always ensure no user is already logged in, so i can check that both sign in methods works.
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            goToMainActivity();
        } else {
            startSignIn();
        }
    }

    private void goToMainActivity() {
        Log.d("LoginActivity", "Navigating to MainActivity");
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void startSignIn() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.ic_pokedex)      // Set logo drawable
                .setTheme(R.style.Theme_PokemonApp)      // Set theme
                .build();
        signInLauncher.launch(signInIntent);
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        Log.d("LoginActivity", "Sign-in result received");
        IdpResponse response = result.getIdpResponse();
        Log.d("LoginActivity", "Result code: " + result.getResultCode());
        if (response != null) {
            Log.d("LoginActivity", "Provider: " + response.getProviderType());
            if (response.getError() != null) {
                Log.e("LoginActivity", "Error: " + response.getError().getMessage());
            }
        }
        if (result.getResultCode() == RESULT_OK) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                Log.d("LoginActivity", "User: " + user.getDisplayName());
                goToMainActivity();
            } else {
                Log.e("LoginActivity", "User is null after successful login");
            }
        } else {
            Toast.makeText(this, "Sign-in canceled", Toast.LENGTH_SHORT).show();
        }
    }



}
