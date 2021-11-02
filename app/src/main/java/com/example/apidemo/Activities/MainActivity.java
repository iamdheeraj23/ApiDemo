package com.example.apidemo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.apidemo.Package.ApiClient;
import com.example.apidemo.R;
import com.example.apidemo.Service.UserService;
import com.example.apidemo.SignUpPojo.LogoutModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Button logButton;
    UserService userService;
    AlertDialog.Builder alert;
    int userId=0;
    String token="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void logoutUserId() {
        userService.callLogoutUser(String.valueOf(userId)).enqueue(new Callback<LogoutModel>() {
            @Override
            public void onResponse(Call<LogoutModel> call, Response<LogoutModel> response) {
                if(response.isSuccessful()){
                    startActivity(new Intent(MainActivity.this, SignInActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                }else{
                    Toast.makeText(MainActivity.this, "Error occured :-"+response.errorBody(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LogoutModel> call, Throwable t) {
                alert.setMessage(t.getMessage());
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "Thank you...", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void initViews() {
//        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
//        String status = sh.getString("status", "");
        logButton= findViewById(R.id.loginButton);
        alert=new AlertDialog.Builder(this);
        userId=getIntent().getIntExtra("id",0);
        token=getIntent().getStringExtra("token");
        userService= ApiClient.getClientTokn(token).create(UserService.class);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_profile,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoutButton:
                logoutUserId();
                return true;

            case R.id.Profile:
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                return true;
        }
        return false;
    }
}