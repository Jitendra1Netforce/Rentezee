package com.rentezee.fragments.chat;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rentezee.main.R;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    Context context;
    TextView toolbar_title_txt;
    Button continueButton;
    RelativeLayout chat_layout;
    RecyclerView chat_recyclerview;
    public static ArrayList<ChatWithusData> chatWithusDatas = new ArrayList<>();
    ChatWithusAdapter chatWithusAdapter;
    EditText editTextMessage;
    ImageView sendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Select Address");
        }


        setup_layout();

        setup_recyclerview();

        editTextMessage = (EditText) findViewById(R.id.messageEditext);

        sendMessage = (ImageView) findViewById(R.id.sendMessage);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                post_messae();

            }
        });

    }

    private void post_messae()
    {

        chatWithusDatas.clear();

        System.out.println("data=============="+ editTextMessage.getText().toString());

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("reciever_id", "1");
        jsonObject.addProperty("message", editTextMessage.getText().toString());

        Ion.with(ChatActivity.this)
                .load("http://netforce.biz/renteeze/webservice/users/chats/56")
                .setJsonObjectBody(jsonObject)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result)
                    {

                        System.out.println("result--------------"+result.toString());


                        for(int i=0; i<result.size(); i++)
                        {
                            JsonObject jsonObject = (JsonObject) result.get(i);

                            String chat_id = jsonObject.get("chat_id").getAsString();
                            String sender_name = jsonObject.get("sender_name").getAsString();
                            String sender_photo = jsonObject.get("sender_photo").getAsString();
                            String message = jsonObject.get("message").getAsString();
                            String reciever_name = jsonObject.get("reciever_name").getAsString();
                            String reciever_photo = jsonObject.get("reciever_photo").getAsString();

                            chatWithusDatas.add(new ChatWithusData(chat_id,sender_name,sender_photo,message,reciever_name,reciever_photo));

                        }

                        chatWithusAdapter.notifyDataSetChanged();
                    }



                });
    }

    private void setup_recyclerview()
    {

        chat_recyclerview = (RecyclerView) findViewById(R.id.chat_recyclerview);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        chat_recyclerview.setLayoutManager(mLayoutManager);

        chatWithusAdapter = new ChatWithusAdapter(getApplicationContext(), chatWithusDatas);
        chat_recyclerview.setAdapter(chatWithusAdapter);

    }

    private void setup_layout()
    {
        chat_layout = (RelativeLayout) findViewById(R.id.chat_layout);
        chat_recyclerview = (RecyclerView) findViewById(R.id.chat_recyclerview);


    }

















}
