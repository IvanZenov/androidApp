package com.example.zevs.mykursach2.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.zevs.mykursach2.MainActivity;
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

import java.util.Locale;
import java.util.Calendar;



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
        //SET TIME TO POST TIME
         viewHolder.filter.setText("Type:" + post.getType());
         long millisecond = post.getTimestamp();
         android.text.format.DateFormat df = new android.text.format.DateFormat();
         viewHolder.blog_time.setText(df.format("dd.MM.yyyy hh:mm", millisecond).toString());


        if(post.getAmountvisitors().equals(""))
        {
            viewHolder.blog_visitors.setVisibility(View.GONE);
        }
        else{
            viewHolder.blog_visitors.setVisibility(View.VISIBLE);
            viewHolder.blog_visitors.setText("Visitors: " + post.getAmountvisitors());
        }

        if (post.getDescription().equals(""))
        {
            viewHolder.description.setVisibility(View.GONE);
        }
        else {
            viewHolder.description.setVisibility(View.VISIBLE);
            viewHolder.description.setText(post.getDescription());
        }



        viewHolder.btn_follow.setVisibility(View.VISIBLE);
        if(post.getPublisher().equals(firebaseUser.getUid())){
            viewHolder.btn_follow.setVisibility(View.GONE);
        }

        isFollowing(post.getPostid(),viewHolder.btn_follow);

        viewHolder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


                //animateButton(viewHolder.btn_follow);

                if (viewHolder.btn_follow.getText().toString().equals("follow")) {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(post.getPostid())
                            .child("following").child(firebaseUser.getUid()).setValue(true);

                   FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("followers").child(post.getPostid()).setValue(true);


                } else {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(post.getPostid())
                            .child("following").child(firebaseUser.getUid()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("followers").child(post.getPostid()).removeValue();

                }
            }

        });
        publisherInfo(viewHolder.imageProfile,viewHolder.username,post.getPublisher());
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public ImageView imageProfile,postImage;
        public TextView username,description,blog_visitors,blog_time,filter;
        public CircularProgressButton btn_follow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.blog_user_image);
            postImage = itemView.findViewById(R.id.blog_image);
            username = itemView.findViewById(R.id.blog_user_name);
            description = itemView.findViewById(R.id.blog_desc);
            blog_visitors = itemView.findViewById(R.id.blog_visitor_count);
            blog_time = itemView.findViewById(R.id.blog_date);
            filter = itemView.findViewById(R.id.criteria);
            btn_follow = itemView.findViewById(R.id.btn_follow);
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

    private void animateButton (final CircularProgressButton btn){
        AsyncTask<String,String,String> followPost = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                try {
                    Thread.sleep(1500);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                return "follow";
            }

            @Override
            protected void onPostExecute(String s) {
                if (s.equals("follow")){
                    Toast.makeText(btn.getContext(),"Application accepted",Toast.LENGTH_SHORT).show();
                    btn.doneLoadingAnimation(Color.parseColor("#333639"),BitmapFactory.decodeResource(btn.getResources(),R.drawable.ic_done_white_48dp));
                }
            }
        };

        btn.startAnimation();
        followPost.execute();
    }
    
    private void isFollowing(final String postId, final CircularProgressButton button){

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(postId).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()){
                    button.setText("following");

                } else{
                    button.setText("follow");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
