package com.example.RealFilm.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.RealFilm.MyApplication;
import com.example.RealFilm.R;
import com.example.RealFilm.model.ApiResponse;
import com.example.RealFilm.model.Status;
import com.example.RealFilm.model.User;
import com.example.RealFilm.service.ApiService;
import com.example.RealFilm.service.UserService;
import com.example.RealFilm.utils.Constants;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText Edittext_email, Edittext_password;
    private TextInputLayout Tiplayout_login_email, Tiplayout_login_password;
    private TextView Tv_register;
    private Button Btn_login;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;


    private CheckBox CheckBox_rememberlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUi();
        initListener();
        rememberLogin();
    }

    private void initUi() {
        Tv_register = findViewById(R.id.tv_register);

        Edittext_email = findViewById(R.id.edittext_login_email);
        Edittext_password = findViewById(R.id.edittext_login_password);
        Btn_login = findViewById(R.id.btn_login);

        Tiplayout_login_email = findViewById(R.id.tiplayout_login_email);
        Tiplayout_login_password = findViewById(R.id.tiplayout_login_password);

        progressDialog = new ProgressDialog(this);

        sharedPreferences = getSharedPreferences("remember_login", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        CheckBox_rememberlogin = findViewById(R.id.checkBox_rememberlogin);

    }

    private void initListener() {
        Tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i1);
            }
        });

        btnLoginOnClick();

        CheckBox_rememberlogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    editor.putBoolean("checked", true);
                    editor.commit();
                } else {
                    editor.putBoolean("checked", false);
                    editor.commit();
                }
            }
        });

    }

    private void rememberLogin() {
        String shared_email = sharedPreferences.getString("email", "");
        String shared_password = sharedPreferences.getString("password", "");
        boolean checked = sharedPreferences.getBoolean("checked", false);

        if (checked) {
            if (shared_email != "" && shared_password != "") {
                Edittext_email.setText(shared_email);
                Edittext_password.setText(shared_password);
                CheckBox_rememberlogin.setChecked(true);
            }
        } else {
            if (shared_email != "" && shared_password != "") {
                Edittext_email.setText(shared_email);
                CheckBox_rememberlogin.setChecked(false);
            }
        }

    }

    public void btnLoginOnClick() {
        Btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Edittext_email.getText().toString().trim().equalsIgnoreCase("")) {
                    Tiplayout_login_email.setError(getString(R.string.error_not_be_empty));
                } else {
                    Tiplayout_login_email.setErrorEnabled(false);
                }
                if (Edittext_password.getText().toString().trim().equalsIgnoreCase("")) {
                    Tiplayout_login_password.setError(getString(R.string.error_not_be_empty));
                } else {
                    Tiplayout_login_password.setErrorEnabled(false);
                }

                if (Edittext_password.getText().toString().trim().length() >= 6) {
                    if (!Edittext_email.getText().toString().trim().equalsIgnoreCase("")
                            && !Edittext_password.getText().toString().trim().equalsIgnoreCase("")) {
                        login();
                    }
                } else {
                    Tiplayout_login_password.setError(getString(R.string.error_password_more_chr));
                }
            }
        });
    }

    private void login() {
        String strEmail = Edittext_email.getText().toString().trim();
        String strPass = Edittext_password.getText().toString().trim();
        progressDialog.setMessage(getString(R.string.progressDialog_logging));
        progressDialog.show();

        UserService userService = ApiService.createService(UserService.class);
        Call<ApiResponse<User>> call = userService.login(strEmail, strPass);
        call.enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                progressDialog.dismiss();
                ApiResponse<User> res = response.body();
                if (res.getStatus() == Status.SUCCESS) {
                    Toast.makeText(LoginActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    saveDataLogin(strEmail, strPass, res.getAccessToken());
                } else {
                    Toast.makeText(LoginActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {

            }
        });
    }

    public void saveDataLogin(String strEmail, String strPass, String token) {
        if (CheckBox_rememberlogin.isChecked()) {
            editor.putString("email", strEmail);
            editor.putString("password", strPass);
            editor.putString(Constants.ACCESS_TOKEN, token);
            MyApplication.saveToken(token);
            editor.commit();
        } else {
            editor.putString("email", strEmail);
            editor.commit();
        }
    }


}