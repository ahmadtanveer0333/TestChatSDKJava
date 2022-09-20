package com.ibex.testchatsdkjava;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import static com.ibex.testchatsdkjava.SecondActivity.RequestPermissionCode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;
import com.ibex.chatsdk.CallBack;
import com.ibex.chatsdk.HubConnectionResponse;
import com.ibex.chatsdk.ServiceManager;
import com.ibex.chatsdk.model.Message;

import java.io.File;
import java.util.ArrayList;

public class ThirdActivity extends AppCompatActivity implements CallBack, PickiTCallbacks {
        Button btn;
    ServiceManager manager;
    String filePath;
    PickiT pickiT;
    private static final String IMAGE_DIRECTORY = "/IbexChat_upload_gallery";
    private static final int BUFFER_SIZE = 1024 * 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        btn = findViewById(R.id.selectFileBtn);
        manager = new ServiceManager(this,this);
        pickiT = new PickiT(this, this, this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPermission()) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
               //     intent.setType("application/pdf");
                    startActivityForResult(intent,456);
                } else {
                    requestPermission();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 456 && resultCode == Activity.RESULT_OK && data.getData() != null) {


            Uri uri = data.getData();
            pickiT.getPath(uri, Build.VERSION.SDK_INT);
            Log.d("uripath", "onActivityResult: " + uri.getScheme());

            //  String path  = RealPathUtil.getDataColumn(ThirdActivity.this, uri,null,null);
            //    Log.d("uripath", "onActivityResult: path " + path);
            //   File myFile = new File(path);
            //  manager.sendImage(ThirdActivity.this,16405, 26638 , myFile);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;
                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(ThirdActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ThirdActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }

                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(ThirdActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, RequestPermissionCode);
    }

    @Override
    public void messageCallBack(Object object, boolean isSaveVisitorChat, boolean isVsitorChatDetail) {

    }

    @Override
    public void idCallBack(int visitorId, int sessionid) {

    }

    @Override
    public void hubConnectionCallBack(HubConnectionResponse status) {

    }

    @Override
    public void onRecieveMessage(String message) {

    }

    @Override
    public void onAgentConnected(String agentName, String welcomeMessage) {

    }

    @Override
    public void onChatClosedbyAgent(String agentName) {

    }


    @Override
    public void onAuthentication(String status) {

    }

    @Override
    public void onImageRecieving(String imageUrl) {

    }

    @Override
    public void onFileSent(String status, String fileUrl) {

    }

    @Override
    public void onSaveVisitor(String status, int visitorId, int sessionId) {

    }

    @Override
    public void onsendMessage(String status) {

    }

    @Override
    public void onretrievePreviousChat(String Status, ArrayList<Message> chatList) {

    }


    @Override
    public void PickiTonUriReturned() {

    }

    @Override
    public void PickiTonStartListener() {

    }

    @Override
    public void PickiTonProgressUpdate(int progress) {

    }

    @Override
    public void PickiTonCompleteListener(String path, boolean wasDriveFile, boolean wasUnknownProvider, boolean wasSuccessful, String Reason) {
        Log.d("uripath", "PickiTonCompleteListener: " + path);
          File myFile = new File(path);
          manager.sendFile( myFile);
    }

    @Override
    public void PickiTonMultipleCompleteListener(ArrayList<String> paths, boolean wasSuccessful, String Reason) {
        Log.d("uripath", "PickiTonMultipleCompleteListener: " + paths);

    }


}