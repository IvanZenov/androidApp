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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText email,password;
    Button login,btn_signUp;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;

//If the user has ever logged in, we immediately redirect him to his profile.


    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //get current user from Firebase
        //redirect if user is not null
        if (firebaseUser!=null)
        {
            startActivity( new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Get all id from layout
        email = findViewById(R.id.login);
        password = findViewById(R.id.password);
        btn_signUp = findViewById(R.id.signUp);
        login = findViewById(R.id.signIn);
        auth = FirebaseAuth.getInstance();



        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,
                        RegistrationActivity.class));

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Create Progress dialog
                final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                pd.setMessage(getString(R.string.waiting));
                pd.show();

                //Get email and password like String
                String str_email = email.getText().toString();
                String str_password = password.getText().toString();

                //Action, if user writing is not all field
                if (TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password))
                {
                    Toast.makeText(LoginActivity.this,getString(R.string.notPassOrEmail),Toast.LENGTH_SHORT).show();
                }
                //If All is okey
                else
                    {
                        //Registration with Email
                        auth.signInWithEmailAndPassword(str_email,str_password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    //Create folder in RealTime Database (Firebase)
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                                            .child(auth.getCurrentUser().getUid());

                                    reference.addValueEventListener(new ValueEventListener() {
                                        //IF All oke
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            pd.dismiss(); //Close progress dialog
                                            //Open next Activity
                                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            pd.dismiss();
                                        }
                                    });
                                }
                                //If something wrong
                                else
                                    {
                                        //Close progress dialog
                                        pd.dismiss();
                                        Toast.makeText(LoginActivity.this,getString(R.string.someIsWrong),Toast.LENGTH_SHORT).show();
                                    }
                            }
                        });
                    }
            }
        });
    }

}
