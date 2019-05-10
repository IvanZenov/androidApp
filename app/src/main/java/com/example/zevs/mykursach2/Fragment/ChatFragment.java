package com.example.zevs.mykursach2.Fragment;

import android.nfc.Tag;
import android.os.Bundle;
import android.os.FileObserver;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;

import com.example.zevs.mykursach2.Adapter.GroupAdapter;
import com.example.zevs.mykursach2.Adapter.UserAdapter;
import com.example.zevs.mykursach2.Model.Chat;
import com.example.zevs.mykursach2.Model.ChatList;
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

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private List<User> mUsers;
    private List <Post> mPosts;
    private List <User> tmpUser;
    List<User> userList;
    List <String> listPostId;
    private List<String> idList;
    private List<String> idPst;
    FirebaseUser firebaseUser;

    private GroupAdapter groupAdapter;



    private List<Post> postList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);


        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        idList = new ArrayList<>();
        userList = new ArrayList<>();
        listPostId = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mUsers = new ArrayList<>();
        mPosts = new ArrayList<>();
        tmpUser = new ArrayList<>();

        postList = new ArrayList<>();

        getFollowers();
        //showGroup();
        //readUsers();
        return view;
    }


    private void getFollowers() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow")
                .child(firebaseUser.getUid()).child("followers");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                idList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    idList.add(snapshot.getKey());
                }
                //showUsers();
                showGroup();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showGroup() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("posts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    for (String id : idList){
                        if (post.getPostid().equals(id)){
                            postList.add(post);
                        }
                    }
                }
                groupAdapter = new GroupAdapter(getContext(),postList);
                recyclerView.setAdapter(groupAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void showUsers() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    for (String id : idList){
                        if (user.getId().equals(id)){
                            userList.add(user);
                        }
                    }
                }
                userAdapter = new UserAdapter(getContext(),userList);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



//        for (int i=0;i<mPosts.size();i++){
//            Post post = mPosts.get(i);
//
//            DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Follow").child(post.getPostid()).child("following");
//            for (int j =0;j<tmpUser.size();j++) {
//                final User user = tmpUser.get(j);
//                ref1.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                        if (dataSnapshot.child(user.getId()).exists()) {
//                            mUsers.add(user);
//
//                        }
//                        userAdapter = new UserAdapter(getContext(), mUsers);
//                        recyclerView.setAdapter(userAdapter);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//            }
//        }



}
