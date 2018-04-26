package com.example.adamflynn.finalproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText firstName, lastName, email, password;
    private Button createActBtn;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        firstName = (EditText)findViewById(R.id.firstNameTxt);
        lastName = (EditText)findViewById(R.id.lastNameTxt);
        email = (EditText)findViewById(R.id.emailText2);
        password = (EditText)findViewById(R.id.passTxt2);
        createActBtn = (Button)findViewById(R.id.createActBtn2);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("mUsers");
        mAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if(mUser != null){
                    Toast.makeText(CreateAccountActivity.this, "Signed in", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(CreateAccountActivity.this, "Not signed in", Toast.LENGTH_LONG).show();
                }
            }
        };


        createActBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });
    }

    private void createNewAccount() {
        final String fName = firstName.getText().toString();
        final String lName = lastName.getText().toString();
        final String em = email.getText().toString();
        String pwd = password.getText().toString();

        if(!TextUtils.isEmpty(fName) && !TextUtils.isEmpty(lName) && !TextUtils.isEmpty(em)
                && !TextUtils.isEmpty(pwd)) {
//        if(!fName.equals("") && !lName.equals("") && !em.equals("")
//                && !pwd.equals("")){
            mProgressDialog.setMessage("Creating Account..");
            mProgressDialog.show();

            mAuth.createUserWithEmailAndPassword(em,pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    if(authResult != null){
                        String userid = mAuth.getCurrentUser().getUid();
                        DatabaseReference currentUserDB = mDatabaseReference.child(userid);
                        currentUserDB.child("firstname").setValue(fName);
                        currentUserDB.child("lastname").setValue(lName);
                        currentUserDB.child("email").setValue(em);

                        mProgressDialog.dismiss();

                        //Send users to map
                        Intent intent  = new Intent(CreateAccountActivity.this, Map.class);
                        startActivity(intent);
                    }
                }
            });
//            mAuth.createUserWithEmailAndPassword(em, pwd)
//                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                String userid = mAuth.getCurrentUser().getUid();
//                                DatabaseReference currentUserDB = mDatabaseReference.child(userid);
//                                currentUserDB.child("firstname").setValue(fName);
//                                currentUserDB.child("lastname").setValue(lName);
//                                currentUserDB.child("email").setValue(em);
//
//                                mProgressDialog.dismiss();
//
//                                //Send users to map
//                                Intent intent = new Intent(CreateAccountActivity.this, Map.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(intent);
//                            } else {
//                                // If sign in fails, display a message to the user.
//                                //Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                                Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
        }
    }
}
