package com.magarex.easyly;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.cenkgun.chatbar.ChatBarView;
import com.magarex.easyly.Adapters.ChatAdapter;
import com.magarex.easyly.Models.ChatModel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by HP on 12/30/2017.
 */

public class ChatActivity extends AppCompatActivity {

    private RecyclerView rv_chat;
    private ChatBarView txtMessageBox;
    private static DataOutputStream out;
    private static DataInputStream in;
    private ArrayList<ChatModel> chat = new ArrayList<>();
    private ChatAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final int cat = getIntent().getExtras().getInt("Category");

        String category = "";
        switch (cat) {
            case 0:
                category = "grocery";
                break;
        }

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        FetchMessage fetchMessage = new FetchMessage();
        fetchMessage.execute(category);

        rv_chat = findViewById(R.id.RvMessages);

        txtMessageBox = findViewById(R.id.txtMessageBox);
        txtMessageBox.setMessageBoxHint("Enter message");
        txtMessageBox.setSendClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = txtMessageBox.getMessageText();

                try {
                    out.writeUTF(msg);
                } catch (IOException e) {
                    Toast.makeText(getBaseContext(), "Sorry Message Could not be Send", Toast.LENGTH_LONG).show();
                }

                ChatModel m = new ChatModel(msg, true);
                chat.add(m);
                adapter = new ChatAdapter(getBaseContext(), chat);
                rv_chat.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                rv_chat.setLayoutManager(linearLayoutManager);
            }
        });

    }

    class FetchMessage extends AsyncTask<String, Void, Void> {

        Socket socket;

        @Override
        protected Void doInBackground(String... strings) {
            try {
                String category = strings[0];
                socket = new Socket("192.168.1.9", 1990);
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF("customer" + "#" + SplashScreen.id + "#" + category);

                Listening listening = new Listening();
                listening.start();

            } catch (IOException e) {
                e.printStackTrace();
                Intent i = new Intent(ChatActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
            return null;
        }

        class Listening extends Thread {

            public void run() {
                while (true) {
                    try {
                        runOnUiThread(new Runnable() {
                            String resp = in.readUTF();

                            @Override
                            public void run() {
                                ChatModel m = new ChatModel(resp, false);
                                chat.add(m);
                                adapter = new ChatAdapter(getBaseContext(), chat);
                                rv_chat.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                rv_chat.setLayoutManager(linearLayoutManager);
                                playBeep();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void playBeep() {
        try {
            Uri notification = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
                    notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



