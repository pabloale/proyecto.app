package com.lumbseat.lumbseat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private SignInButton signInButton;

    private GoogleSignInClient mGoogleSignInClient;
    public static GoogleSignInAccount account;
    static final int RC_SIGN_IN = 1;

    public static DriveClient mDriveClient;
    public static DriveResourceClient mDriveResourceClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInButton = findViewById(R.id.sign_in_button);

        setGooglePlusButtonText(signInButton, "Loguearse");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Drive.SCOPE_FILE)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButton.setOnClickListener(this);
    }

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }

        //TODO: escomentar arriba y borrar esto de abajo
        //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        //startActivity(intent);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        switch (requestCode) {
            case RC_SIGN_IN:
                if (resultCode == RESULT_OK) {
                    // The Task returned from this call is always completed, no need to attach
                    // a listener.
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    try {
                        account = task.getResult(ApiException.class);
                        // Use the last signed in account here since it already have a Drive scope.
                        mDriveClient = Drive.getDriveClient(this, account);
                        // Build a drive resource client.
                        mDriveResourceClient = Drive.getDriveResourceClient(this, account);
                        // Signed in successfully, show authenticated UI.
                        updateUI(account);
                    } catch (ApiException e) {
                        e.printStackTrace();
                        Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
                        updateUI(null);
                    }
                }
                break;
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            String personName = account.getDisplayName();

            Toast.makeText(getApplicationContext(),"Bienvenido " + personName,Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(LoginActivity.this, ConfigPesoActivity.class);
            intent.putExtra("ACCOUNT_OBJECT", account);
            startActivity(intent);
        }
    }
}
