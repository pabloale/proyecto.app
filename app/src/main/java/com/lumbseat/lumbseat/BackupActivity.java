package com.lumbseat.lumbseat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.CreateFileActivityOptions;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BackupActivity extends Activity {

    private static final String TAG = "drive_lumbseat";
    private static final String DATABASE_PATH = "/data/user/0/com.lumbseat.lumbseat/databases/bd_datos";
    private static final File DATA_DIRECTORY_DATABASE = new File(DATABASE_PATH);
    private static final String MIME_TYPE = "application/x-sqlite-3";
    private static final int REQUEST_DB_UPLOADER = 2;

    private SharedPreferences myPreferences;
    private TextView tvDiaBk;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(BackupActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_configuration:
                    intent = new Intent(BackupActivity.this, ConfigurationActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_exercises:
                    intent = new Intent(BackupActivity.this, ExercisesActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_backup:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        setTitle("LumbSeat");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_backup);
        navigation.setItemTextColor(ColorStateList.valueOf(Color.WHITE));


        myPreferences = PreferenceManager.getDefaultSharedPreferences(BackupActivity.this);
        tvDiaBk = (TextView) findViewById(R.id.tvDiaBk);
        tvDiaBk.setText(myPreferences.getString("FECHAULTIMOBACKUP", "-"));

        Button btnBackup = findViewById(R.id.btnBackup);
        btnBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CHEQUEO LA CONECTIVIDAD CON INTERNET
                ConnectivityManager conMgr =  (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                if (netInfo == null){
                    new AlertDialog.Builder(BackupActivity.this)
                        .setTitle(getResources().getString(R.string.app_name))
                        .setMessage(getResources().getString(R.string.internet_error))
                        .setPositiveButton("OK", null).show();
                }else{
                    guardarBdEnDrive();
                }
            }
        });
    }

    private void guardarBdEnDrive() {
        LoginActivity.mDriveResourceClient
            .createContents()
            .continueWithTask(
                    new Continuation<DriveContents, Task<Void>>() {
                        @Override
                        public Task<Void> then(@NonNull Task<DriveContents> task) throws Exception {
                            return createFileIntentSender(task.getResult());
                        }
                    })
            .addOnFailureListener(
                    new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Failed to create new contents.", e);
                        }
                    });
    }

    private Task<Void> createFileIntentSender(DriveContents driveContents) {
        Log.i(TAG, "New contents created.");
        // Get an output stream for the contents.
        OutputStream outputStream = driveContents.getOutputStream();

        BufferedOutputStream bos = new BufferedOutputStream(outputStream);
        byte[] buffer = new byte[1024];
        int n;

        try {
            FileInputStream is = new FileInputStream(DATA_DIRECTORY_DATABASE);
            BufferedInputStream bis = new BufferedInputStream(is);

            while ((n = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, n);
                //DialogFragment_Sync.setProgressText("Backing up...");
            }
            bos.close();
            Log.i(TAG, "Se cargo el buffer de salida");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.w(TAG, "Unable to write file contents.", e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.w(TAG, "Unable to write file contents.", e);
        }

        // Create the initial metadata - MIME type and title.
        // Note that the user will be able to change the title later.
        //String mimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType(MIME_TYPE);
        MetadataChangeSet metadataChangeSet =
                new MetadataChangeSet.Builder()
                        .setMimeType(MIME_TYPE)
                        .setTitle("bd_datos")
                        .build();
        // Set up options to configure and display the create file activity.
        CreateFileActivityOptions createFileActivityOptions =
                new CreateFileActivityOptions.Builder()
                        .setInitialMetadata(metadataChangeSet)
                        .setInitialDriveContents(driveContents)
                        .build();

        return LoginActivity.mDriveClient
                .newCreateFileActivityIntentSender(createFileActivityOptions)
                .continueWith(
                        new Continuation<IntentSender, Void>() {
                            @Override
                            public Void then(@NonNull Task<IntentSender> task) throws Exception {
                                startIntentSenderForResult(task.getResult(), REQUEST_DB_UPLOADER, null, 0, 0, 0);
                                return null;
                            }
                        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LoginActivity.mDriveResourceClient == null || LoginActivity.mDriveClient == null) {
            // Use the last signed in account here since it already have a Drive scope.
            LoginActivity.mDriveClient = Drive.getDriveClient(this, LoginActivity.account);
            // Build a drive resource client.
            LoginActivity.mDriveResourceClient = Drive.getDriveResourceClient(this, LoginActivity.account);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_DB_UPLOADER:
                Log.i(TAG, "Upload file request");
                // Called after a file is saved to Drive.
                if (resultCode == RESULT_OK) {

                    Date cDate = new Date();
                    String fechaHoy = new SimpleDateFormat("dd/MM/yyyy").format(cDate);

                    SharedPreferences.Editor myEditor = myPreferences.edit();
                    myEditor.putString("FECHAULTIMOBACKUP",fechaHoy);
                    myEditor.commit();
                    tvDiaBk.setText(fechaHoy);

                    Log.i(TAG, "DB file successfully saved.");
                    Toast.makeText(BackupActivity.this,"Sus datos han sido resguardados",Toast.LENGTH_SHORT).show();
                }else if(resultCode == RESULT_CANCELED){
                    Toast.makeText(BackupActivity.this,"Ha cancelado el Backup de sus datos",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
