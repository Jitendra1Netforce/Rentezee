package com.rentezee.navigation.chat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rentezee.helpers.AppPreferenceManager;
import com.rentezee.helpers.BaseActivity;
import com.rentezee.helpers.PreferenceKeys;
import com.rentezee.main.DashboardContainer;
import com.rentezee.main.Login;
import com.rentezee.main.R;
import com.rentezee.pojos.User;

import java.util.ArrayList;

public class ChatActivity extends BaseActivity
{

    TextView toolbar_title_txt;
    RelativeLayout chat_layout;
    RecyclerView chat_recyclerview;
    public static ArrayList<ChatData> chatWithusDatas = new ArrayList<>();
    ChatAdapter chatWithusAdapter;
    ImageView sendMessage;
    EditText editTextMessage;
    Context context;
    public static String id, user_id;
    long userId;
    DashboardContainer dashboardContainer;
    User user;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context=this;

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Connect with us");
        }

        user = (User) new AppPreferenceManager(getApplicationContext()).getObject(PreferenceKeys.savedUser, User.class);

        if (user != null)
        {

            userId = user.getUserId();
            user_id = Long.toString(userId);

        }
        else{

            Intent i = new Intent(ChatActivity.this, Login.class);
            startActivity(i);
            finish();

        }

        dashboardContainer = new DashboardContainer();
        dashboardContainer.count_cart();




        show_data();

        editTextMessage = (EditText) findViewById(R.id.messageEditext);

        sendMessage = (ImageView) findViewById(R.id.sendMessage);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = editTextMessage.getText().toString();
                post_messae(message);

            }
        });

        setup_layout();

        setup_recyclerview();

    }

    public void show_data() {

        chatWithusDatas.clear();
        showProgressBar(context);


        Ion.with(ChatActivity.this)
                .load("https://netforcesales.com/renteeze/webservice/users/chats/"+user_id)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result)
                    {

                        System.out.println("result--------------"+result.toString());


                        for(int i=0; i<result.size(); i++)
                        {
                            JsonObject jsonObject = (JsonObject) result.get(i);

                            String sender_id = jsonObject.get("sender_id").getAsString();
                            String sender_name = jsonObject.get("sender_name").getAsString();
                            String sender_photo = jsonObject.get("sender_photo").getAsString();
                            String message = jsonObject.get("message").getAsString();
                            String reciever_name = jsonObject.get("reciever_name").getAsString();
                            String reciever_photo = jsonObject.get("reciever_photo").getAsString();
                            String date_time =  jsonObject.get("chat_time").getAsString();
                            chatWithusDatas.add(new ChatData(sender_id,sender_name,sender_photo,message,reciever_name,reciever_photo,date_time));

                        }

                        chatWithusAdapter.notifyDataSetChanged();
                        chat_recyclerview.scrollToPosition(chatWithusDatas.size()-1);
                        dismissProgressBar();
                    }



                });
    }


    private void setup_recyclerview()
    {

        chat_recyclerview = (RecyclerView) findViewById(R.id.chat_recyclerview);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        chat_recyclerview.setLayoutManager(mLayoutManager);

        chatWithusAdapter = new ChatAdapter(getApplicationContext(), chatWithusDatas);
        chat_recyclerview.setAdapter(chatWithusAdapter);

    }

    private void setup_layout()
    {
        chat_layout = (RelativeLayout) findViewById(R.id.chat_layout_data);
        chat_recyclerview = (RecyclerView) findViewById(R.id.chat_recyclerview);



    }




    private void post_messae(String message)
    {

        chatWithusDatas.clear();
        showProgressBar(context);

        editTextMessage.setText("");

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("reciever_id", "1");
        jsonObject.addProperty("message", message);

        Ion.with(ChatActivity.this)
                .load("https://netforcesales.com/renteeze/webservice/users/chats/"+user_id)
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

                            String sender_id = jsonObject.get("sender_id").getAsString();
                            String sender_name = jsonObject.get("sender_name").getAsString();
                            String sender_photo = jsonObject.get("sender_photo").getAsString();
                            String message = jsonObject.get("message").getAsString();
                            String reciever_name = jsonObject.get("reciever_name").getAsString();
                            String reciever_photo = jsonObject.get("reciever_photo").getAsString();
                            String date_time =  jsonObject.get("chat_time").getAsString();

                            System.out.println("sender id ======="+sender_id);

                            chatWithusDatas.add(new ChatData(sender_id,sender_name,sender_photo,message,reciever_name,reciever_photo,date_time));

                        }

                        chatWithusAdapter.notifyDataSetChanged();
                        chat_recyclerview.scrollToPosition(chatWithusDatas.size()-1);
                        dismissProgressBar();
                    }



                });
    }

}
