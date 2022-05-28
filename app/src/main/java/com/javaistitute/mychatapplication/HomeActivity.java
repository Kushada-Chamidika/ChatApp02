package com.javaistitute.mychatapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.javaistitute.mychatapplication.Adapter.MyAdapter;
import com.javaistitute.mychatapplication.Model.FriendsList;
import com.javaistitute.mychatapplication.Model.User;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;

    RecyclerView recyclerView;
    ArrayList<FriendsList> list;

    DatabaseReference databaseRef;

    private FirebaseAuth mAuth;

    FloatingActionButton fab;
    public MyAdapter userAdapter;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        pd = new ProgressDialog(this);

        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AddFriend.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        getWindow().setStatusBarColor(ContextCompat.getColor(HomeActivity.this, R.color.black));

        databaseRef = FirebaseDatabase.getInstance().getReference("Friends List").child(mAuth.getCurrentUser().getUid());

        recyclerView = findViewById(R.id.my_friends_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    FriendsList user = dataSnapshot.getValue(FriendsList.class);
                    if (!user.getFriend_id().equals(mAuth.getCurrentUser().getUid())) {
                        list.add(user);
                    }
                }
                userAdapter = new MyAdapter(HomeActivity.this, list);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
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
        inflater.inflate(R.menu.menu_1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                signOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void signOut() {
        pd.setMessage("Please Wait!");
        pd.show();

        HashMap<String, Object> map = new HashMap<>();
        map.put("active_status", "Offline");

        FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    pd.dismiss();
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(HomeActivity.this, "Log-out Success!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(i);
                }else {
                    pd.dismiss();
                    Toast.makeText(HomeActivity.this, "Couldn't logout! try again", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}