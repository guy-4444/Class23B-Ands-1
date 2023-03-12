package com.guy.class23b_and_1;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Activity_SMS extends AppCompatActivity {

    private final int REQUEST_CODE_SMS = 23;

    private Button btn;
    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);


        btn = findViewById(R.id.btn);
        info = findViewById(R.id.info);

        btn.setOnClickListener(v -> start());

        updateUI();
    }

    private void updateUI() {
        String str = "";
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);

        if (result == PackageManager.PERMISSION_GRANTED) {
            str += "READ_SMS GRANTED";
        } else {
            str += "READ_SMS DENIED";
        }

        str += "\n\nShould Show Request Permission Rationale:\n" + ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS);

        info.setText(str);
    }




    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog. Save the return value, an instance of
    // ActivityResultLauncher, as an instance variable.
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    permissionDenied();
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
                updateUI();
            });

    private void start() {
        requestPermissionLauncher.launch(Manifest.permission.READ_SMS);
    }

    private void permissionDenied() {
        Toast.makeText(Activity_SMS.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setCancelable(true);
            alertBuilder.setMessage("Write calendar permission is necessary to write event!!!");
            alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    start();
                }
            });
            alertBuilder.setNeutralButton("No", null);
            alertBuilder.show();
        } else {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setCancelable(true);
            alertBuilder.setMessage("Don't ask again state. please grant permission manually");
            alertBuilder.setPositiveButton("Got It", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    openPermissionSettings();
                }
            });
            alertBuilder.show();
        }

        // permission denied, boo! Disable the
        // functionality that depends on this permission.
    }

    private void openPermissionSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}