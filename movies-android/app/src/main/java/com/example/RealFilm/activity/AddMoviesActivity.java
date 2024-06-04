package com.example.RealFilm.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.RealFilm.adapter.GenreAdapter;
import com.example.RealFilm.databinding.ActivityAddMoviesBinding;
import com.example.RealFilm.model.ApiResponse;
import com.example.RealFilm.model.Country;
import com.example.RealFilm.model.Genre;
import com.example.RealFilm.model.Status;
import com.example.RealFilm.service.ApiService;
import com.example.RealFilm.service.CommonService;
import com.example.RealFilm.service.MovieService;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMoviesActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_1 = 1;
    private static final int REQUEST_IMAGE_2 = 2;
    private ActivityAddMoviesBinding binding;
    private GenreAdapter genreAdapter;
    private int LINK_NEXT = 2;
    private boolean validImage1Selected = false;
    private boolean validImage2Selected = false;

    private String selectedCountryId;
    private List<String> categoryIds = new ArrayList<>();
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMoviesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        // ...rest action

        btnAddOnClick();
        btnAddMoreLinkOnClick();
        btnRemoveLinkOnClick();
        addPoster();
        btnBackOnClick();

        // ...rest call api
        getCountries();
        getGenres();
        // ...

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCountry = (String) parent.getItemAtPosition(position);
                selectedCountryId = (categoryIds.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void btnBackOnClick() {
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void btnAddOnClick() {
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validImage1Selected | !validImage2Selected | !validCountry() | !validLinkMovie() | !validGenre() | !validName() | !validActor() | !validDirector() | !validTime() | !validReleaseYear() | !validCountry() | !validDescription() | !validTrailerLink()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddMoviesActivity.this);
                    builder.setMessage("Các trường là bắt buộc");
                    builder.setCancelable(true);
                    builder.setPositiveButton(
                            "OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDelete = builder.create();
                    alertDelete.show();
                    return;
                }
                handleAdd();
            }
        });
    }

    private void handleAdd() {

        String base64Image1 = convertBitmapToBase64(((BitmapDrawable) binding.image1.getDrawable()).getBitmap());
        String base64Image2 = convertBitmapToBase64(((BitmapDrawable) binding.image2.getDrawable()).getBitmap());

        MovieService movieService = ApiService.createService(MovieService.class);

        String title = binding.movieName.getText().toString().trim();
        String description = binding.movieDesc.getText().toString().trim();
        String director = binding.director.getText().toString().trim();
        String releaseYear = binding.releaseYear.getText().toString().trim();
        Integer duration = Integer.valueOf(binding.time.getText().toString());
        String country = selectedCountryId;
        String actors = binding.actors.getText().toString().trim();
        String trailerURL = binding.trailerLink.getText().toString().trim();
        List<String> genres = genreAdapter.getSelectedGenres();

        List<String> movieLinks = new ArrayList<>();
        for (int i = 0; i < binding.movieLinks.getChildCount(); i++) {
            View view = binding.movieLinks.getChildAt(i);
            if (view instanceof EditText) {
                EditText editText = (EditText) view;
                String link = editText.getText().toString().trim();
                if (!link.isEmpty()) {
                    movieLinks.add(link);
                }
            }
        }


        Call<ApiResponse> call = movieService.
                addMovie(title, description, director, releaseYear, duration, country, actors, trailerURL, genres, movieLinks, base64Image1, base64Image2);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                ApiResponse<ApiResponse> res = response.body();
                if (res.getStatus() == Status.SUCCESS) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddMoviesActivity.this);
                    builder.setMessage(res.getMessage());
                    builder.setCancelable(true);
                    builder.setPositiveButton(
                            "OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    onBackPressed();
                                }
                            });
                    AlertDialog alertDelete = builder.create();
                    alertDelete.show();
                } else {

                    Toast.makeText(AddMoviesActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
            }
        });


    }

    private void btnAddMoreLinkOnClick() {
        binding.btnAddLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createInputLink();
            }
        });
    }

    private void createInputLink() {
        EditText editText;
        editText = new EditText(this);
        editText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        editText.setHint("link " + LINK_NEXT);
        editText.setId(LINK_NEXT);
        binding.movieLinks.addView(editText);
        LINK_NEXT++;
    }


    private void addPoster() {
        binding.btnImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(REQUEST_IMAGE_1);
            }
        });

        binding.btnImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(REQUEST_IMAGE_2);
            }
        });

    }

    private void openGallery(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case REQUEST_IMAGE_1:
                    handleImageResult(data, binding.image1);
                    validImage1Selected = true;
                    break;
                case REQUEST_IMAGE_2:
                    handleImageResult(data, binding.image2);
                    validImage2Selected = true;
                    break;
            }
        }
    }

    private void handleImageResult(Intent data, ImageView imageView) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            Glide.with(this).load(bitmap).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return "data:image/png;base64," + base64Image;
    }

    private void btnRemoveLinkOnClick() {
        binding.btnRemoveLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LINK_NEXT > 2) {
                    LINK_NEXT--;
                    binding.movieLinks.removeView(findViewById(LINK_NEXT));
                }
            }
        });
    }

    boolean validLinkMovie() {
        boolean valid = true;
        String checked = binding.link.getText().toString().trim();
        if (checked.isEmpty()) return valid = false;
        EditText editText;
        if (LINK_NEXT > 2) {
            for (int i = 2; i < LINK_NEXT; i++) {
                editText = binding.movieLinks.findViewById(i);
                if (editText.getText().toString().trim().isEmpty()) {
                    valid = false;
                }
            }
        }
        return valid;
    }

    boolean validGenre() {
        if (genreAdapter.getSelectedGenres().size() > 0) {
            return true;
        }
        return false;
    }

    boolean validName() {
        String checked = binding.movieName.getText().toString().trim();
        if (checked.isEmpty()) return false;
        else return true;
    }

    boolean validActor() {
        String checked = binding.actors.getText().toString().trim();
        if (checked.isEmpty()) return false;
        else return true;
    }

    boolean validDirector() {
        String checked = binding.director.getText().toString().trim();
        if (checked.isEmpty()) return false;
        else return true;
    }

    boolean validTime() {
        String checked = binding.time.getText().toString().trim();
        if (checked.isEmpty()) return false;
        else return true;
    }

    boolean validReleaseYear() {
        String checked = binding.releaseYear.getText().toString().trim();
        if (checked.isEmpty()) return false;
        else return true;
    }

    boolean validCountry() {
        if (selectedCountryId.trim().isEmpty()) return false;
        else return true;
    }

    boolean validDescription() {
        String checked = binding.movieDesc.getText().toString().trim();
        if (checked.isEmpty()) return false;
        else return true;
    }

    boolean validTrailerLink() {
        String checked = binding.trailerLink.getText().toString().trim();
        if (checked.isEmpty()) return false;
        else return true;
    }


    private void getCountries() {
        CommonService commonService = ApiService.createService(CommonService.class);
        Call<ApiResponse<List<Country>>> call = commonService.getCountries();


        call.enqueue(new Callback<ApiResponse<List<Country>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Country>>> call, Response<ApiResponse<List<Country>>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<List<Country>> res = response.body();
                    List<Country> countries = response.body().getData();

                    List<String> categories = new ArrayList<>();
                    for (Country country : countries) {
                        categories.add(country.getName());
                        categoryIds.add(country.getCode());
                    }

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddMoviesActivity.this, android.R.layout.simple_spinner_item, categories);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinner.setAdapter(dataAdapter);

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Country>>> call, Throwable t) {

            }
        });

    }

    private void getGenres() {
        CommonService commonService = ApiService.createService(CommonService.class);
        Call<ApiResponse<List<Genre>>> call = commonService.getGenres();

        call.enqueue(new Callback<ApiResponse<List<Genre>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Genre>>> call, Response<ApiResponse<List<Genre>>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<List<Genre>> res = response.body();
                    List<Genre> genres = response.body().getData();
                    Log.d("GENRE", genres.size() + "");
                    genreAdapter = new GenreAdapter(genres);
                    binding.rvGenres.setAdapter(genreAdapter);
                    binding.rvGenres.setLayoutManager(new GridLayoutManager(AddMoviesActivity.this, 2));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Genre>>> call, Throwable t) {

            }
        });
    }
}