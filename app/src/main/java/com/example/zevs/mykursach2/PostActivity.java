package com.example.zevs.mykursach2;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;


import java.util.Date;
import java.util.HashMap;

public class PostActivity extends AppCompatActivity {

    Uri imageUri;
    String myUrl ="";
    StorageTask uploadTask;
    StorageReference storageReference;


    ImageView close, image_added;
    TextView post,tvMin,tvMax;
    EditText description,amountVisit;
    CrystalRangeSeekbar rangeSeekbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        //Initialization of variables
        close = findViewById(R.id.close);
        image_added = findViewById(R.id.image_added);
        post = findViewById(R.id.post);
        description = findViewById(R.id.description);
        rangeSeekbar = findViewById(R.id.rangeSeekbar1);
        tvMin = findViewById(R.id.textMin1);
        tvMax = findViewById(R.id.textMax1);
        amountVisit = findViewById(R.id.amountVisitors);



        //Initialization of RangeSeekBar
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvMin.setText(String.valueOf(minValue));
                tvMax.setText(String.valueOf(maxValue));
            }
        });

        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>",String.valueOf(minValue)+ ":" + String.valueOf(maxValue));
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
                uploadImage();
            }
        });

        CropImage.activity()
                .setAspectRatio(1,1)
                .start(PostActivity.this);
    }


    private String getFileExtension(Uri uri){
        /*ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        */
        return MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        //return mime.getExtensionFromMimeType(contentResolver.getType(uri));
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
            Toast.makeText(this, "Что-то пошло не так",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PostActivity.this,MainActivity.class));
            finish();
        }
    }

    private void uploadImage(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Публикуем");
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
                        //String myVisitors = amountVisit.toString().trim();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("posts");

                        String postId = reference.push().getKey();

                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("postid",postId);
                        hashMap.put("postimage",myUrl);
                        hashMap.put("description",description.getText().toString());
                        hashMap.put("publisher",FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("amountvisitors",amountVisit.getText().toString());
                        hashMap.put("agefrom",tvMin.getText().toString());
                        hashMap.put("ageto",tvMax.getText().toString());

                        //Добавил
                        assert postId != null;
                        reference.child(postId).setValue(hashMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(PostActivity.this,MainActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(PostActivity.this,"Ошибочка)",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this,"Вы не выбрали фото",Toast.LENGTH_SHORT).show();
        }
    }



}
