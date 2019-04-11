package com.example.zevs.mykursach2.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.zevs.mykursach2.EditProfileActivity;
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

import org.w3c.dom.Text;

public class ProfileFragment extends Fragment {

    ImageView imageProfile,options;
    TextView name,posts;
    Button editProfile;


    FirebaseUser firebaseUser;
    String profileId;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        profileId = firebaseUser.getUid();

        imageProfile = view.findViewById(R.id.image_profile);
        options = view.findViewById(R.id.options);
        name = view.findViewById(R.id.username);
        posts = view.findViewById(R.id.postsId);
        editProfile = view.findViewById(R.id.editProfile);
        userInfo();
        getCurrentPosts();



        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EditProfileActivity.class));
            }
        });

        return view;
    }
    private void userInfo () {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(profileId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (getContext() == null){
                    return;
                }/*
                RequestOptions placeholderOption = new RequestOptions();
                placeholderOption.placeholder(R.drawable.profile_placeholder);
                //String xx = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();
                User user = dataSnapshot.getValue(User.class);

                Glide.with(getContext()).applyDefaultRequestOptions(placeholderOption).load(user.getImageurl()).into(imageProfile);
                name.setText(user.getUsername() +"," + user.getAge());
                */
                User user = dataSnapshot.getValue(User.class);
                Glide.with(getContext()).load(user.getImageUrl()).into(imageProfile);
                name.setText(user.getUsername() + "," + user.getAge());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getCurrentPosts(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i =0;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    if (post.getPublisher().equals(profileId)){
                        i++;
                    }
                }
                posts.setText("Кол-во постов " + i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
