package com.ibex.testchatsdkjava;

import static com.ibex.chatsdk.Constatnts.RETRIEVE_IMAGES;
import static com.ibex.chatsdk.Constatnts.URL;
import static com.ibex.chatsdk.Constatnts.VISITOR_ID;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ibex.chatsdk.CallBack;
import com.ibex.chatsdk.HubConnectionResponse;
import com.ibex.chatsdk.HubConnectivity;
import com.ibex.chatsdk.PrefConfig;
import com.ibex.chatsdk.RealPathUtil;
import com.ibex.chatsdk.ServiceManager;
import com.ibex.chatsdk.model.Message;
import com.ibex.chatsdk.model.SaveVisitorModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements CallBack {

    private static final String TAG = "MainActivityTag";
    Button button, imgBtn;
    ServiceManager manager;
    HubConnectivity hubConnectivity;
    int visitorId, sessionId;
    EditText message_Et;
    private MessageAdapter messageAdapter;
    private RecyclerView recyclerView;
    TextView agentTypingalert;
    String imagePath;


    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("Chat Closed")
                .setMessage("Are you sure to quit chat ?")
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        hubConnectivity.VisitorClosedWindowInvoke(visitorId);

                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
//                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.sendBtn);
        imgBtn = findViewById(R.id.imgBtn);
        message_Et = findViewById(R.id.messageEdit);
        agentTypingalert = findViewById(R.id.typingalert_tv);
        manager = new ServiceManager(this,this);
        recyclerView = findViewById(R.id.recyclerView);
        messageAdapter = new MessageAdapter(getLayoutInflater(), this);
        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        hubConnectivity = new HubConnectivity(this,this);
        hubConnectivity.getAuthorizedAndMakeConenction("token","test13", "test13@gmail.com", "03211234567");
        message_Et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                hubConnectivity.TypingAlertInvoke(visitorId,"finalTest");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.sendMessage(message_Et.getText().toString());
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("message", message_Et.getText().toString());
                //    Log.d("persondetails", "PersonDetails sending: "+ name);
                    jsonObject.put("isSent", false);
                    messageAdapter.addItem(jsonObject);
                    recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                    resetMessageEdit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this,SecondActivity.class));

//                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        == PackageManager.PERMISSION_GRANTED){
//                    Intent intent = new Intent();
//           //         intent.setType("image/*");
//                    intent.setType("*/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(Intent.createChooser(intent, "Select File"), 555);
//                }else {
//                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
//                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 555 && resultCode== Activity.RESULT_OK){
            Uri uri = data.getData();
            // For Image
            imagePath = RealPathUtil.getRealPath(MainActivity.this, uri);
            // for file

         //   imagePath = data.getData().getPath();
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            File file = new File(imagePath);
            Log.d(TAG, "onActivityResult: " + file.toString());
            manager.sendFile(file);

        }
    }

    private void resetMessageEdit() {
        message_Et.setText("");
    }

    @Override
    public void messageCallBack(Object object, boolean isSaveVisitorChat, boolean isVsitorChatDetail) {
//        if(!isSaveVisitorChat && !isVsitorChatDetail) {
//            SaveVisitorModel model = (SaveVisitorModel) object;
//            Toast.makeText(this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
//        }
//        if(isVsitorChatDetail){
//            ArrayList<Message> messageArrayList = (ArrayList<Message>) object;
//            try {
//
//                for (int i = 0; i <messageArrayList.size() ; i++) {
//                    Log.d("apiresponse", "messages: " + messageArrayList.get(i).getMessage());
//                    Log.d("apiresponse", "isSent" +  messageArrayList.get(i).isReply());
//
//                    if(messageArrayList.get(i).getMessage().contains(RETRIEVE_IMAGES)){
//                        JSONObject jsonObject = new JSONObject();
//                        jsonObject.put("image", URL+messageArrayList.get(i).getMessage());
//                        jsonObject.put("isSent", messageArrayList.get(i).isReply());
//                        messageAdapter.addItem(jsonObject);
//
//                    }else {
//
//                        JSONObject jsonObject = new JSONObject();
//                        jsonObject.put("message", messageArrayList.get(i).getMessage());
//                        jsonObject.put("isSent", messageArrayList.get(i).isReply());
//                        messageAdapter.addItem(jsonObject);
//
//                    }
//                }
//                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            Toast.makeText(this, "User already Registered Successfully  " + messageArrayList.size(), Toast.LENGTH_SHORT).show();
//
//        }
    }

    @Override
    public void idCallBack(int visitorId, int sessionid) {
        this.visitorId = visitorId;
        this.sessionId = sessionid;

    }

    @Override
    public void hubConnectionCallBack(HubConnectionResponse status) {

    }

    @Override
    public void onRecieveMessage(String message) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("message", message);
            jsonObject.put("isSent", true);
            messageAdapter.addItem(jsonObject);
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onAgentConnected(String agentName, String welcomeMessage) {
        Log.d(TAG, "onMessageReceived: agentconnected  " +welcomeMessage);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("message", welcomeMessage);
            jsonObject.put("isSent", true);
            messageAdapter.addItem(jsonObject);
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onChatClosedbyAgent(String agentName) {
        Log.d(TAG, "onMessageReceived: Chat Closed by Agent " + agentName);
        new PrefConfig(this).clear();
        Log.d(TAG, "onMessageReceived: Chat Closed by Agent " + new PrefConfig(this).getData(VISITOR_ID) );
 //       alertMessage(agentName);
//        new AlertDialog.Builder(this)
//                .setTitle("Chat Closed")
//                .setMessage(agentName + " closed the chat session")
//                // Specifying a listener allows you to take an action before dismissing the dialog.
//                // The dialog is automatically dismissed when a dialog button is clicked.
//                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Continue with delete operation
//                        button.setAlpha(.5f);
//                        button.setClickable(false);
//                        message_Et.setClickable(false);
//                        message_Et.setAlpha(.5f);
//                    }
//                })
//
//                // A null listener allows the button to dismiss the dialog and take no further action.
////                .setNegativeButton(android.R.string.no, null)
////                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();
    }

    private void alertMessage(String agentName) {

        new AlertDialog.Builder(this)
                .setTitle("Chat Closed")
                .setMessage("Chat closed by agent")
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        hubConnectivity.VisitorClosedWindowInvoke(visitorId);

                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
//                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

//    @Override
//    public void onAgentTyping(String message) {
//        Log.d(TAG, "onMessageReceived: Agent is Typing");
//        agentTypingalert.setVisibility(View.VISIBLE);
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // Do something after 5s = 5000ms
//                agentTypingalert.setVisibility(View.GONE);
//            }
//        }, 2000);
//
//    }

    @Override
    public void onAuthentication(String status) {
        Log.d("hubStatus", "onAuthentication: " + status);
    }

    @Override
    public void onImageRecieving(String imageUrl) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("image", imageUrl);
            jsonObject.put("isSent", false);
            messageAdapter.addItem(jsonObject);
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFileSent(String status, String fileUrl) {
        Toast.makeText(this, "File Sent Successfuly", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onFileSent: " + fileUrl);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("image", fileUrl);
            jsonObject.put("isSent", false);
            messageAdapter.addItem(jsonObject);
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSaveVisitor(String status, int visitorId, int sessionId) {
        Toast.makeText(this, "User Registered Successfully " + visitorId +"  "+ sessionId, Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onsendMessage(String status) {
        Toast.makeText(this, "Message Sent Successfuly", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onretrievePreviousChat(String Status, ArrayList<Message> chatList) {
            ArrayList<Message> messageArrayList = chatList;
            try {

                for (int i = 0; i <messageArrayList.size() ; i++) {
                    Log.d("apiresponse", "messages: " + messageArrayList.get(i).getMessage());
                    Log.d("apiresponse", "messages: " + messageArrayList.get(i).isMediaFile());
                    Log.d("apiresponse", "isSent" +  messageArrayList.get(i).isReply());
                if(messageArrayList.get(i).getMessage()!=null) {
                    if (messageArrayList.get(i).isMediaFile()) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("image", URL + messageArrayList.get(i).getUrl());
                        jsonObject.put("isSent", messageArrayList.get(i).isReply());
                        messageAdapter.addItem(jsonObject);

                    } else {

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("message", messageArrayList.get(i).getMessage());
                        jsonObject.put("isSent", messageArrayList.get(i).isReply());
                        messageAdapter.addItem(jsonObject);

                    }
                }
                }
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "User already Registered Successfully  " + messageArrayList.size(), Toast.LENGTH_SHORT).show();

    }






}