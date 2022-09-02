package com.ibex.testchatsdkjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ibex.chatsdk.CallBack;
import com.ibex.chatsdk.HubConnectionResponse;
import com.ibex.chatsdk.HubConnectivity;
import com.ibex.chatsdk.ServiceManager;
import com.ibex.chatsdk.model.SaveVisitorModel;


public class MainActivity extends AppCompatActivity implements CallBack {

    Button button;
    ServiceManager manager;
    HubConnectivity hubConnectivity;
    int visitorId, sessionId;
    EditText message_Et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        message_Et = findViewById(R.id.message_et);
        manager = new ServiceManager(this,this);
        hubConnectivity = new HubConnectivity(this,this);
        hubConnectivity.createHubConnection("test1", "test1@gmail.com", "03161234567");
        message_Et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                hubConnectivity.HubProxyInvoke(visitorId,"finalTest");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.saveVisitorChat(MainActivity.this,sessionId,visitorId,message_Et.getText().toString());

            }
        });


    }



    @Override
    public void messageCallBack(Object object, boolean isSaveVisitorChat) {
        if(!isSaveVisitorChat) {
            SaveVisitorModel model = (SaveVisitorModel) object;
            Toast.makeText(this, "User Registered Successfully", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void idCallBack(int visitorId, int sessionid) {
        this.visitorId = visitorId;
        this.sessionId = sessionid;
    }

    @Override
    public void hubConnectionCallBack(HubConnectionResponse status) {

    }
}