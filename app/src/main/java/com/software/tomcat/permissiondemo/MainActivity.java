package com.software.tomcat.permissiondemo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


// https://stackoverflow.com/questions/34342816/android-6-0-multiple-permissions
// https://stackoverflow.com/questions/44944559/cant-create-folder-in-android-7-0?answertab=active#tab-top
// https://stackoverflow.com/questions/34040355/how-to-check-the-multiple-permission-at-single-request-in-android-m
// https://stackoverflow.com/questions/32347532/android-m-permissions-confused-on-the-usage-of-shouldshowrequestpermissionrati
// https://stackoverflow.com/questions/32347532/android-m-permissions-confused-on-the-usage-of-shouldshowrequestpermissionrati

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int    MULTIPLE_PERMISSIONS = 10;

    String[] permissions = new String[]
    {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreate()...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkPermissions())
        {
            Toast.makeText(this, "onCreate(),\n permissions OK. ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.d(TAG, "onActivityResult()...");
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        Log.d(TAG, "onRequestPermissionsResult()------------, \n" + Arrays.toString(permissions) +
                ",\n Results: " + Arrays.toString(grantResults));
        switch (requestCode)
        {
            case MULTIPLE_PERMISSIONS:
                if ((grantResults.length > 0) &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED))
                {
                    Toast.makeText(this, "permissions: " + grantResults[0], Toast.LENGTH_SHORT).show();
                }
                else if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                {
                    if (!shouldShowRequestPermissionRationale(permissions[0]))
                    {
                        Toast.makeText(this, "Please grant by manual\n in Android settings ->\n App -> permission",
                                Toast.LENGTH_LONG).show();
                        showDeniedDialog(permissions[0]);
                    }
                }
                else
                {
                    String permis = "";
                    for (String per : permissions)
                    {
                        permis += "\n" + per;
                    }
                    showDeniedDialog(permis);
                }
                return;

            default:
                break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    //-----------------------    User define function    -----------------------------------//
    private boolean checkPermissions()
    {
        Log.d(TAG, "checkPermissions()...");
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();

        for (String p:permissions)
        {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED)
            {
                //if (!shouldShowRequestPermissionRationale(p))
                //{
                //    Toast.makeText(this, "Please grant the permission this time", Toast.LENGTH_LONG).show();
                //}
                listPermissionsNeeded.add(p);
            }
        }
        Log.d(TAG, "listPermissionsNeeded size: " + listPermissionsNeeded.size() + ",\nItem:" +
                Arrays.toString(listPermissionsNeeded.toArray()));

        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    MULTIPLE_PERMISSIONS);
            return false;
        }

        return true;
    }

    private void showDeniedDialog(String msg)
    {
        Log.d(TAG, "showDeniedDialog()...");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Permission Note");
        builder.setMessage(msg);
        builder.setNeutralButton(" OK ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }


}

