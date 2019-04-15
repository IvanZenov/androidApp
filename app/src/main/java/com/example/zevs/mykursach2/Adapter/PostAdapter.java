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
import com.example.zevs.mykursach2.Model.Post;
import com.example.zevs.mykursach2.Model.User;
import com.example.zevs.mykursach2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{



    public Context mContext;
    public List<Post> mPost;

    private FirebaseUser firebaseUser;
    private DatabaseReference mDatabasePost;


    public PostAdapter(Context mContext, List<Post> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item,viewGroup,false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Post post = mPost.get(i);
        Glide.with(mContext).load(post.getPostimage()).into(viewHolder.postImage);

        viewHolder.filter.setText(post.getType());

        if(post.getAmountvisitors().equals(""))
        {
            viewHolder.blog_visitors.setVisibility(View.GONE);
        }
        else{
            viewHolder.blog_visitors.setVisibility(View.VISIBLE);
            viewHolder.blog_visitors.setText("Amount of visitors: " + post.getAmountvisitors());
        }



        if (post.getDescription().equals(""))
        {
            viewHolder.description.setVisibility(View.GONE);
        }
        else {
            viewHolder.description.setVisibility(View.VISIBLE);
            viewHolder.description.setText(post.getDescription());
        }

        publisherInfo(viewHolder.imageProfile,viewHolder.username,post.getPublisher());

    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public ImageView imageProfile,postImage;
        public TextView username,description,blog_visitors,blog_time,filter;






        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.blog_user_image);
            postImage = itemView.findViewById(R.id.blog_image);
            username = itemView.findViewById(R.id.blog_user_name);
            description = itemView.findViewById(R.id.blog_desc);
            blog_visitors = itemView.findViewById(R.id.blog_visitor_count);
            blog_time = itemView.findViewById(R.id.blog_date);
            filter = itemView.findViewById(R.id.criteria);
        }

    }
    private void publisherInfo(final ImageView imageProfile, final TextView username, String userId)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                RequestOptions placeholderOption = new RequestOptions();
                placeholderOption.placeholder(R.drawable.profile_placeholder);

                Glide.with(mContext).load(user.getImageUrl()).into(imageProfile);
                //Glide.with(mContext).load(user.getImageurl()).into(imageProfile);
                username.setText(user.getUsername() + "," + user.getAge());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


}
