package com.example.zevs.mykursach2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//Here method create group
public class PostActivity extends AppCompatActivity {

    Uri imageUri;
    String myUrl ="";
    StorageTask uploadTask;
    StorageReference storageReference;
    private FirebaseUser firebaseUser;


    DatabaseReference reference1;
    ImageView close, image_added;
    TextView post;
    EditText description,amountVisit,name;
    Spinner dropdown;
    String itemvalue;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        //Initialization of variables
        close = findViewById(R.id.close);
        image_added = findViewById(R.id.image_added);
        post = findViewById(R.id.post);
        description = findViewById(R.id.description);
        amountVisit = findViewById(R.id.amountVisitors);
        dropdown = findViewById(R.id.spinner);
        name = findViewById(R.id.event_name);
        reference1= FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();





        List <String> items = new ArrayList<>();
        items.add("Sport");
        items.add("Party");
        items.add("Date");
        items.add("Own");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,items);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        dropdown.setAdapter(arrayAdapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemvalue = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                itemvalue = "Own";

            }
        });



        storageReference = FirebaseStorage.getInstance().getReference("posts");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostActivity.this,MainActivity.class));
                finish();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_name = name.getText().toString();

                //Action, if user writing is not all field
                if (TextUtils.isEmpty(str_name))
                {
                    Toast.makeText(PostActivity.this,"Please, entry the name of event",Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadImage();
                }


            }
        });

        CropImage.activity()
                .setAspectRatio(1,1)
                .start(PostActivity.this);
    }


    private String getFileExtension(Uri uri){
        return MimeTypeMap.getFileExtensionFromUrl(uri.toString());
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            image_added.setImageURI(imageUri);

        }else {
            Toast.makeText(this, "Something gone is wrong",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PostActivity.this,MainActivity.class));
            finish();
        }
    }




    private void uploadImage(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Publishing");
        progressDialog.show();

        if (imageUri != null)
        {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"."+ getFileExtension(imageUri));
            uploadTask = fileReference.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        assert downloadUri != null;
                        myUrl = downloadUri.toString().trim();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("posts");
                        String postId = reference.push().getKey();



                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("postid",postId);
                        hashMap.put("postimage",myUrl);
                        hashMap.put("description",description.getText().toString());
                        hashMap.put("publisher",FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("amountvisitors",amountVisit.getText().toString());
                        hashMap.put("type",itemvalue);
                        hashMap.put("timestamp", ServerValue.TIMESTAMP);
                        hashMap.put("name",name.getText().toString());
                        assert postId != null;
                        reference.child(postId).setValue(hashMap);


                        progressDialog.dismiss();
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                                .child("followers").child(postId).setValue(true);

                        startActivity(new Intent(PostActivity.this,MainActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(PostActivity.this,getString(R.string.error),Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(this,getString(R.string.photoBad),Toast.LENGTH_SHORT).show();
        }
    }



}