package com.example.apidemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apidemo.Package.ApiClient;
import com.example.apidemo.Service.UserService;
import com.example.apidemo.SignUpPojo.Model;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    EditText editTextEmail,editTextDeviceType,editTextCPassword;
    Button buttonSignUp;
    ProgressDialog progressDialog;
    UserService userService;
    AlertDialog.Builder alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initViews();
        buttonSignUp.setOnClickListener(v->{
           if(editTextEmail.getText().equals("")
                ||editTextCPassword.getText().equals("")
                ||editTextDeviceType.getText().equals("")){
               Toast.makeText(SignUpActivity.this, "Fill the all field!", Toast.LENGTH_SHORT).show();
           }else {
               registerUser();
           }
        });
    }

    private void initViews() {
        editTextEmail=findViewById(R.id.et_Email);
        editTextDeviceType=findViewById(R.id.et_Type);
        editTextCPassword=findViewById(R.id.et_CTPass);
        editTextDeviceType.setText("Android");
        alertDialog=new AlertDialog.Builder(this);
        buttonSignUp=findViewById(R.id.button);
        progressDialog=new ProgressDialog(this);
        userService = ApiClient.getClient().create(UserService.class);
    }

    public void registerUser(){
        progressDialog.show();
        userService.callbackRegister(editTextEmail.getText().toString(),editTextCPassword.getText().toString()).enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if(response.isSuccessful()){
                    Model model = response.body();
                    Toast.makeText(SignUpActivity.this, "Registration successfully..."+ model.getBody().getEmail(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
                    finish();
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Registration successfully...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                alertDialog.setMessage(t.getMessage());
                alertDialog.show();
                alertDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SignUpActivity.this,"Okey",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}