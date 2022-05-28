package com.javaistitute.mychatapplication.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.javaistitute.mychatapplication.Model.FriendsList;
import com.javaistitute.mychatapplication.Model.User;
import com.javaistitute.mychatapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.MyViewHolder2> {

    DatabaseReference db_reference= FirebaseDatabase.getInstance().getReference();

    Context context;
    List<User> list;

    public MyAdapter2(Context context, List<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_2, parent, false);
        return new MyViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder2 holder, int position) {
        User user = list.get(position);
        holder.username.setText(user.getUsername());
        holder.email.setText(user.getEmail());

        db_reference.child("Friends List").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(user.getU_id()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    holder.addUserBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String friend_id = user.getU_id();
                            String friend_email = user.getEmail();
                            String friend_username = user.getUsername();

                            HashMap<String, Object> map = new HashMap<>();
                            map.put("friend_id", friend_id);
                            map.put("friend_email", friend_email);
                            map.put("friend_username", friend_username);

                            db_reference.child("Friends List").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(user.getU_id()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(context, "User successfully added as your friend!", Toast.LENGTH_SHORT).show();
                                        holder.addUserBtn.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });

                        }
                    });
                }else{
                    holder.addUserBtn.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, error.getMessage());
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder2 extends RecyclerView.ViewHolder {

        public TextView username, email;
        public ImageButton addUserBtn;

        public MyViewHolder2(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.textView);
            email = itemView.findViewById(R.id.textView3);
            addUserBtn = itemView.findViewById(R.id.add_user_button);

        }
    }
}
