package com.example.zevs.mykursach2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class RegistrationActivity extends AppCompatActivity {
    EditText username,email,password,age;
    Button registrate;
    TextView txt_login;
    ProgressDialog pd;
    CircularProgressButton btn;
    RadioGroup radioGenderGroup;
    RadioButton radioGenderButton;



    FirebaseAuth auth;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        radioGenderGroup = findViewById(R.id.radioGender);
        radioGenderButton = findViewById(radioGenderGroup.getCheckedRadioButtonId());


        username = findViewById(R.id.registrationName);
        email = findViewById(R.id.registrationEmail);
        password = findViewById(R.id.registrationPassword);
        registrate = findViewById(R.id.registrate);
        txt_login = findViewById(R.id.yetRegistrate);
        age = findViewById(R.id.regisrationAge);
        auth = FirebaseAuth.getInstance();
        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
            }
        });

        registrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(RegistrationActivity.this);
                pd.setMessage(getString(R.string.waiting));
                pd.show();

                String str_username = username.getText().toString();
                String str_password = password.getText().toString();
                String str_email = email.getText().toString();
                String str_age = age.getText().toString();
                String str_gender = radioGenderButton.getText().toString();


                if (TextUtils.isEmpty(str_username)||TextUtils.isEmpty(str_email)
                        ||TextUtils.isEmpty(str_password)||TextUtils.isEmpty(str_age)){
                    Toast.makeText(RegistrationActivity.this,getString(R.string.notPassOrEmail),Toast.LENGTH_SHORT).show();
                }
                else if(str_password.length()<6)
                {
                    Toast.makeText(RegistrationActivity.this,getString(R.string.passwordCriteria),Toast.LENGTH_SHORT).show();
                }
                else {
                    registrate(str_username,str_email,str_password,str_age,str_gender);
                }

            }
        });
    }
    private void registrate (final String username, String email ,
                             String password, final String age, final String gender)
    {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userId = firebaseUser.getUid();
                    reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                    HashMap<String,Object> hashMap = new HashMap<>();

                    hashMap.put("id",userId);
                    hashMap.put("username",username);
                    hashMap.put("bio",gender);
                    hashMap.put("age",age);
                    hashMap.put("imageUrl","https://firebasestorage.googleapis.com/v0/b/kursach2-5ec81.appspot.com/o/placeholder.png?alt=media&token=60832b31-59c5-4871-80b7-cc9a5c1c097b");

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                pd.dismiss();
                                Intent intent = new Intent(RegistrationActivity.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    });

                }
                else {
                    pd.dismiss();
                    Toast.makeText(RegistrationActivity.this,getString(R.string.passwordEmailWarning),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
