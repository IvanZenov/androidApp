package com.example.zevs.mykursach2.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.zevs.mykursach2.Model.Chat;
import com.example.zevs.mykursach2.Model.User;
import com.example.zevs.mykursach2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupMessageAdapter extends RecyclerView.Adapter<GroupMessageAdapter.ViewHolder> {
    public static  final int MSG_TYPE_LEFT = 0;
    public static  final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Chat> mChat;
    private FirebaseUser firebaseUser;
    private DatabaseReference mDatabasePost;


    FirebaseUser fuser;

    public GroupMessageAdapter(Context mContext, List<Chat> mChat) {
        this.mContext = mContext;
        this.mChat = mChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, viewGroup, false);
            return new GroupMessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, viewGroup, false);
            return new GroupMessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = mChat.get(position);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        holder.show_message.setText(chat.getMessage());
        long millisecond = chat.getTimestamp();
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        holder.message_time.setText(df.format("hh:mm MM.dd", millisecond).toString());

//        if (position == mChat.size()-1){
//            if (chat.isIsseen()){
//                holder.txt_seen.setText("Seen");
//            } else {
//                holder.txt_seen.setText("Delivered");
//            }
//        } else {
//            holder.txt_seen.setVisibility(View.GONE);
//        }

        senderInfo(holder.profile_image,holder.sender_name,chat.getSender());


    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message,message_time,sender_name;
        public CircleImageView profile_image;
        public TextView txt_seen;

        public ViewHolder(View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen);
            message_time = itemView.findViewById(R.id.message_time);
            sender_name = itemView.findViewById(R.id.sender_name);

        }
    }

    private void senderInfo(final CircleImageView imageProfile, final TextView nameOfUser, final String userId)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(mContext).load(user.getImageUrl()).into(imageProfile);

                nameOfUser.setText(user.getUsername());



            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}