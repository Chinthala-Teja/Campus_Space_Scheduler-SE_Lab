package com.example.campus_space_scheduler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campus_space_scheduler.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth auth;
    private GoogleSignInClient googleClient;

    private static final int RC_GOOGLE = 100;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        GoogleSignInOptions options =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

        googleClient = GoogleSignIn.getClient(this, options);

        // Map the button from your layout
        binding.buttonLoginViaGoogle.setOnClickListener(v -> googleLogin());
    }

    private void googleLogin() {
        Log.d(TAG, "googleLogin: Initiating");
        googleClient.signOut().addOnCompleteListener(this, task -> {
            Intent signInIntent = googleClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_GOOGLE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account.getIdToken());
                }
            } catch (ApiException e) {
                Log.e(TAG, "Google login failed. Status Code: " + e.getStatusCode());
                toast("Google login failed (Error " + e.getStatusCode() + ")");
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnSuccessListener(this, result -> verifyUser(result.getUser()))
                .addOnFailureListener(this, e -> {
                    Log.e(TAG, "Firebase Auth failed", e);
                    toast("Google auth failed");
                });
    }

    private void verifyUser(FirebaseUser user) {
        if (user == null) return;

        String email = user.getEmail();
        // Restriction 1: Must end with @nitc.ac.in
        if (email == null || !email.endsWith("@nitc.ac.in")) {
            toast("Please use your NITC email (@nitc.ac.in)");
            signOutUser();
            return;
        }

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Restriction 2: Must exist in database
                if (snapshot.exists()) {
                    // Restriction 3: isBlocked must be false
                    Boolean isBlocked = snapshot.child("blocked").getValue(Boolean.class);
                    if (isBlocked != null && isBlocked) {
                        toast("Your account is blocked");
                        signOutUser();
                    } else {
                        String role = snapshot.child("role").getValue(String.class);
                        if (role == null) role = "student";

                        toast("Welcome "+ user.getDisplayName());
                        navigateToDashboard(role);
                    }
                } else {
                    toast("User not authorized. Contact Admin.");
                    signOutUser();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                toast("Database error: " + error.getMessage());
            }
        });
    }

    private void signOutUser() {
        auth.signOut();
        googleClient.signOut();
    }

    private void navigateToDashboard(String role) {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.putExtra("ROLE", role);
        startActivity(intent);
        finish();
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
