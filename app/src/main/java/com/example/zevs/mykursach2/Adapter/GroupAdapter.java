package com.example.zevs.mykursach2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zevs.mykursach2.MessageActivity;
import com.example.zevs.mykursach2.Model.Chat;
import com.example.zevs.mykursach2.Model.Post;
import com.example.zevs.mykursach2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private Context mContext;
    private List<Post> mPosts;

    String theLastMessage;
    ArrayList <Chat> chats = new ArrayList<>();

    public GroupAdapter(Context mContext, List<Post> mPosts) {
        this.mContext = mContext;
        this.mPosts = mPosts;
    }

    @NonNull
    @Override
    public GroupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_message_item, viewGroup, false);
        return new GroupAdapter.ViewHolder(view);
    }
    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final GroupAdapter.ViewHolder viewHolder, int i) {
        final Post post = mPosts.get(i);

        viewHolder.groupName.setText(post.getName());
        Glide.with(mContext).load(post.getPostimage()).into(viewHolder.groupImage);
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
//        Query lastQuery = databaseReference.child(post.getPostid()).orderByKey().limitToLast(1);
//        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String message = dataSnapshot.child("message").getValue().toString();
//                viewHolder.lastMessage.setText(message);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                //Handle possible errors.
//            }
//        });






        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("postid",post.getPostid());
                mContext.startActivity(intent);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView groupName,lastMessage;
        public ImageView groupImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.group_name);
            groupImage = itemView.findViewById(R.id.group_image);
            lastMessage = itemView.findViewById(R.id.last_message);


        }
    }




}
