package com.javaistitute.mychatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.javaistitute.mychatapplication.Adapter.MessageAdapter;
import com.javaistitute.mychatapplication.Model.Chat;
import com.javaistitute.mychatapplication.Model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MessageActivity extends AppCompatActivity {

    private Toolbar toolbar;

    TextView username;
    ImageButton btn_send;
    EditText text_send;

    MessageAdapter messageAdapter;
    ArrayList<Chat> list;
    RecyclerView recyclerView;

    DatabaseReference databaseRef;

    FirebaseUser fuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        getWindow().setStatusBarColor(ContextCompat.getColor(MessageActivity.this, R.color.black));

        final String user_id = getIntent().getStringExtra("user_id");
        final String uname = getIntent().getStringExtra("username");

        username = findViewById(R.id.textView6);
        username.setText(uname);

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        btn_send = findViewById(R.id.imageButton);
        text_send = findViewById(R.id.message);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = text_send.getText().toString();
                if (!msg.equals("")) {
                    sendMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(), user_id, msg);
                } else {
                    Toast.makeText(MessageActivity.this, "You can't send empty message!", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });

        databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(user_id);

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                username.setText(user.getUsername());
                readMessage(fuser.getUid(),user_id);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView = findViewById(R.id.msg_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onKeyDown(int key_code, KeyEvent key_event) {
        if (key_code == KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(key_code, key_event);
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_back:
                Intent i = new Intent(MessageActivity.this, HomeActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendMessage(String sender, String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dateString = formatter.format(new Date());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("timestamp", dateString);

        HashMap<String, Object> hashMap2 = new HashMap<>();
        hashMap2.put("last_msg_timestamp", dateString);

        reference.child("Chats").push().setValue(hashMap);
        reference.child("Friends List").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(receiver).updateChildren(hashMap2);

    }

    private void readMessage(String myId,String userId){
        list = new ArrayList<>();

        databaseRef = FirebaseDatabase.getInstance().getReference("Chats");
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(myId)&&chat.getSender().equals(userId)||
                            chat.getReceiver().equals(userId)&&chat.getSender().equals(myId)){
                        list.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this,list);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}