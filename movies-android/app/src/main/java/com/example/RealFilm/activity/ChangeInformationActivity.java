package com.example.RealFilm.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.RealFilm.R;
import com.example.RealFilm.model.ApiResponse;
import com.example.RealFilm.model.User;
import com.example.RealFilm.service.ApiService;
import com.example.RealFilm.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeInformationActivity extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();
    private ProgressDialog progressDialog;
    private ScrollView croll;
    private ImageView dropdown, Btn_change_information_back, Btn_save_information;
    private EditText Edittext_change_infor_birthday, Edittext_change_infor_name, Edittext_change_infor_email;
    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private CardView cardView_avatar;
    private ImageView imageView_avatar;
    private String userId, email, name, birthday;
    private final boolean drop = false;
    private final int SELECT_PICTURE = 200;
    private Uri ImageUri;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_information);

        initUi();
        getUserInfor();
        btnBackOnclick();
        btnSaveOnClick();
        btnCardViewAvatarOnClick();
        pickDate();

    }

    private void initUi() {
        progressDialog = new ProgressDialog(this);
        croll = findViewById(R.id.croll);
        Btn_change_information_back = findViewById(R.id.btn_change_information_back);
        Btn_save_information = findViewById(R.id.btn_save_information);
        Edittext_change_infor_birthday = findViewById(R.id.edittext_change_infor_birthday);
        Edittext_change_infor_name = findViewById(R.id.edittext_change_infor_name);
        Edittext_change_infor_email = findViewById(R.id.edittext_change_infor_email);
        cardView_avatar = findViewById(R.id.cardView_avatar);
        imageView_avatar = findViewById(R.id.imageView_avatar);
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
    }

    private void btnBackOnclick() {
        Btn_change_information_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void btnCardViewAvatarOnClick() {
        cardView_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });
    }

    private void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    imageView_avatar.setImageURI(selectedImageUri);
                    ImageUri = selectedImageUri;
                }
            }
        }
    }

    public void btnSaveOnClick() {
        Btn_save_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserProfile();
            }
        });
    }
    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return "data:image/png;base64," + base64Image;
    }
    private void updateUserProfile() {

        String photoURL = convertBitmapToBase64(((BitmapDrawable) imageView_avatar.getDrawable()).getBitmap());
        String emailUpdate = Edittext_change_infor_email.getText().toString().trim();
        String nameUpdate = Edittext_change_infor_name.getText().toString().trim();
        String birthdaylUpdate = Edittext_change_infor_birthday.getText().toString().trim();

        UserService userService = ApiService.createService(UserService.class);
        Call<ApiResponse<User>> call = userService.updateUser(nameUpdate, emailUpdate, birthdaylUpdate, photoURL);

        call.enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<User> res = response.body();
                    Toast.makeText(ChangeInformationActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {

            }
        });
    }

    private void getUserInfor() {
        progressDialog.setMessage("Đang xử lý...");
        progressDialog.show();

        UserService userService = ApiService.createService(UserService.class);
        Call<ApiResponse<User>> call = userService.getUser();

        call.enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<User> res = response.body();
                    User user = res.getData();

                    name = user.getName();
                    email = user.getEmail();
                    birthday = user.getBirthday();
                    String avatar = user.getPhotoURL();
                    Glide.with(ChangeInformationActivity.this).load(avatar).into(imageView_avatar);

                    Edittext_change_infor_name.setText(name);
                    Edittext_change_infor_email.setText(email);
                    Edittext_change_infor_birthday.setText(birthday);

                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    public void pickDate() {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };
        Edittext_change_infor_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ChangeInformationActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        Edittext_change_infor_birthday.setText(dateFormat.format(myCalendar.getTime()));
    }
}