package com.javaistitute.mychatapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.javaistitute.mychatapplication.MessageActivity;
import com.javaistitute.mychatapplication.Model.Chat;
import com.javaistitute.mychatapplication.Model.FriendsList;
import com.javaistitute.mychatapplication.Model.User;
import com.javaistitute.mychatapplication.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    List<FriendsList> list;

    public MyAdapter(Context context, List<FriendsList> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        FriendsList user = list.get(position);
        holder.username.setText(user.getFriend_username());
        if (user.getLast_msg_timestamp() != null) {
            String[] s = user.getLast_msg_timestamp().split(" ");
            holder.time.setText(s[1]);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, MessageActivity.class);
                i.putExtra("user_id", user.getFriend_id());
                i.putExtra("username", user.getFriend_username());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView username, time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.textView);
            time = itemView.findViewById(R.id.textView2);
        }
    }
}
