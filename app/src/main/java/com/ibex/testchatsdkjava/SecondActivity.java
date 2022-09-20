package com.ibex.testchatsdkjava;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ibex.chatsdk.CallBack;
import com.ibex.chatsdk.HubConnectionResponse;
import com.ibex.chatsdk.ServiceManager;
import com.ibex.chatsdk.model.Message;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class SecondActivity extends AppCompatActivity  implements CallBack {

    Button buttonStart, buttonStop, buttonPlayLastRecordAudio,
            buttonStopPlayingRecording ;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder ;
    Random random ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer ;
    ServiceManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        buttonStart = (Button) findViewById(R.id.button);
        buttonStop = (Button) findViewById(R.id.button2);
        buttonPlayLastRecordAudio = (Button) findViewById(R.id.button3);
        buttonStopPlayingRecording = (Button)findViewById(R.id.button4);
        manager = new ServiceManager(this,this);
        buttonStop.setEnabled(false);
        buttonPlayLastRecordAudio.setEnabled(false);
        buttonStopPlayingRecording.setEnabled(false);

        random = new Random();

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkPermission()) {

                    AudioSavePathInDevice = getExternalCacheDir().getAbsolutePath() + "/audiorecordtest.wav";
                    MediaRecorderReady();
                    try {

                        mediaRecorder.prepare();
                        mediaRecorder.start();

                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    buttonStart.setEnabled(false);
                    buttonStop.setEnabled(true);

                    Toast.makeText(SecondActivity.this, "Recording started",
                            Toast.LENGTH_LONG).show();
                } else {
                    requestPermission();
                }

            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaRecorder.stop();
                buttonStop.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);
                Toast.makeText(SecondActivity.this, "Recording Completed",
                        Toast.LENGTH_LONG).show();
            }
        });

        buttonPlayLastRecordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException,
                    SecurityException, IllegalStateException {

                buttonStop.setEnabled(false);
                buttonStart.setEnabled(false);
                buttonStopPlayingRecording.setEnabled(true);
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(AudioSavePathInDevice);
                    Log.d("recording path", "onClick: " + AudioSavePathInDevice.toString());
                    File file = new File(AudioSavePathInDevice);
                    Log.d("secondActivity", "onActivityResult: " + file.toString());
                    manager.sendFile(file);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();
                Toast.makeText(SecondActivity.this, "Recording Playing",
                        Toast.LENGTH_LONG).show();
            }
        });

        buttonStopPlayingRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonStop.setEnabled(false);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);

                if(mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    MediaRecorderReady();
                }
            }
        });
    }


    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(SecondActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(SecondActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SecondActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }

                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
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
        Log.d("hubstatus", "onFileSentTTTT: " + fileUrl);
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


}