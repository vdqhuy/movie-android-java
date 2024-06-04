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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.RealFilm.R;
import com.example.RealFilm.adapter.GenreAdapter;
import com.example.RealFilm.model.ApiResponse;
import com.example.RealFilm.model.Country;
import com.example.RealFilm.model.Genre;
import com.example.RealFilm.model.Movie;
import com.example.RealFilm.service.ApiService;
import com.example.RealFilm.service.CommonService;
import com.example.RealFilm.service.MovieService;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMovieActivity extends AppCompatActivity {

    private ImageView ivPosterHorizontal, ivPosterVertical;
    private ImageButton btnPosterHorizontal, btnPosterVertical, btnAddLink, btnRemoveLink, btnBack;
    private Spinner spCountry;
    private EditText edtTitle, edtActors, edtDirector, edtDuration, edtReleaseYear, edtDescription, edtTrailerURL, edtMovieDesc2;
    private Button btnEdit;
    private LinearLayout movieLinksLayout;
    private RecyclerView rvGenre;
    private int LINK_NEXT = 1;
    private boolean callApiSuccess = false;
    private String defaultCountry;
    private String selectedCountryId;
    private GenreAdapter genreAdapter;
    private List<Genre> genres;

    private static final int REQUEST_IMAGE_H = 1;
    private static final int REQUEST_IMAGE_V = 2;
    private final List<String> countryIds = new ArrayList<>();
    private final List<String> countryNames = new ArrayList<>();
    private final List<String> selectedGenres = new ArrayList<>();
    private boolean isPosterHorizontalChanged = false;
    private boolean isPosterVerticalChanged = false;
    private Integer movieId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movie);
        //
        movieId = getIntent().getIntExtra("movieId", 0);

        // ...mapping
        initUi();

        //...rest api
        getMovie();
        getCountries();
        getGenres();

        // ...handle
        btnEditOnclick();
        handleGenreSelection();
        handleChangePoster();
        btnEditOnClick();
        btnBackOnClick();

        if (callApiSuccess = true) btnAddMoreLinkOnClick();
        if (callApiSuccess = true) btnRemoveLinkOnClick();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case REQUEST_IMAGE_H:
                    handleImageResult(data, ivPosterHorizontal);
                    isPosterHorizontalChanged = true;
                    break;
                case REQUEST_IMAGE_V:
                    handleImageResult(data, ivPosterVertical);
                    isPosterVerticalChanged = true;
                    break;
            }
        }
    }

    private void btnEditOnClick() {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validLinkMovie() |
                        !validGenre() |
                        !validName() |
                        !validActor() |
                        !validDirector() |
                        !validDuration() |
                        !validReleaseYear() |
                        !validCountry() |
                        !validDescription() |
                        !validTrailerURL()
                ) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditMovieActivity.this);
                    builder.setMessage(getString(R.string.required_fields_message));
                    builder.setCancelable(true);
                    builder.setPositiveButton(
                            getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDelete = builder.create();
                    alertDelete.show();
                    return;

                }
                handleEditMovie();

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

    private void handleEditMovie() {

        String base64ImageVertical = convertBitmapToBase64(((BitmapDrawable) ivPosterVertical.getDrawable()).getBitmap());
        String base64ImageHorizontal = convertBitmapToBase64(((BitmapDrawable) ivPosterHorizontal.getDrawable()).getBitmap());

        String title = edtTitle.getText().toString().trim();
        String actors = edtActors.getText().toString().trim();
        String director = edtDirector.getText().toString().trim();
        Integer duration = Integer.valueOf(edtDuration.getText().toString());
        String releaseYear = edtReleaseYear.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        String trailerURL = edtTrailerURL.getText().toString().trim();

        String country = selectedCountryId;

        List<String> movieLinks = new ArrayList<>();
        for (int i = 0; i < movieLinksLayout.getChildCount(); i++) {
            View view = movieLinksLayout.getChildAt(i);
            if (view instanceof EditText) {
                EditText editText = (EditText) view;
                String link = editText.getText().toString().trim();
                if (!link.isEmpty()) {
                    movieLinks.add(link);
                }
            }
        }

        List<String> genres = genreAdapter.getSelectedGenres();


        MovieService movieService = ApiService.createService(MovieService.class);
        Call<ApiResponse> call = movieService.
                updateMovie(movieId,
                        title,
                        description,
                        director,
                        releaseYear,
                        duration,
                        country,
                        actors,
                        trailerURL,
                        genres,
                        movieLinks,
                        base64ImageVertical,
                        base64ImageHorizontal

                );

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(EditMovieActivity.this, getString(R.string.edit_success), Toast.LENGTH_SHORT).show();

                    onBackPressed();

                } else {
                    // Error
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                // Error
            }
        });

    }

    boolean validLinkMovie() {
        boolean valid = true;
        EditText editText;
        if (LINK_NEXT > 1) {
            for (int i = 1; i < LINK_NEXT; i++) {
                editText = movieLinksLayout.findViewById(i);
                if (editText.getText().toString().trim().isEmpty()) {
                    valid = false;
                }
            }
        }
        return valid;
    }

    boolean validGenre() {
        return genreAdapter.getSelectedGenres().size() > 0;
    }

    boolean validName() {
        String checked = edtTitle.getText().toString().trim();
        return !checked.isEmpty();
    }

    boolean validActor() {
        String checked = edtActors.getText().toString().trim();
        return !checked.isEmpty();
    }

    boolean validDirector() {
        String checked = edtDirector.getText().toString().trim();
        return !checked.isEmpty();
    }

    boolean validDuration() {
        String checked = edtDuration.getText().toString().trim();
        return !checked.isEmpty();
    }

    boolean validReleaseYear() {
        String checked = edtReleaseYear.getText().toString().trim();
        return !checked.isEmpty();
    }

    boolean validCountry() {
        return !selectedCountryId.trim().isEmpty();
    }

    boolean validDescription() {
        String checked = edtDescription.getText().toString().trim();
        return !checked.isEmpty();
    }

    boolean validTrailerURL() {
        String checked = edtTrailerURL.getText().toString().trim();
        return !checked.isEmpty();
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

    private void initUi() {
        // ...
        spCountry = findViewById(R.id.sp_country);
        movieLinksLayout = findViewById(R.id.movie_links_layout);
        rvGenre = findViewById(R.id.rv_genre);

        // ... ImageButton or Button
        btnRemoveLink = findViewById(R.id.btn_remove_link);
        btnAddLink = findViewById(R.id.btn_add_link);
        btnPosterVertical = findViewById(R.id.btn_poster_vertical);
        btnPosterHorizontal = findViewById(R.id.btn_poster_horizontal);
        btnEdit = findViewById(R.id.btn_edit);
        btnBack = findViewById(R.id.btn_back);

        // ... ImageView
        ivPosterVertical = findViewById(R.id.iv_poster_vertical); // poster 3x4
        ivPosterHorizontal = findViewById(R.id.iv_poster_horizontal); // poster 4x3

        // ... EditText
        edtTitle = findViewById(R.id.edt_title);
        edtActors = findViewById(R.id.edt_actors);
        edtDirector = findViewById(R.id.edt_director);
        edtDuration = findViewById(R.id.edt_duration);
        edtReleaseYear = findViewById(R.id.edt_release_year);
        edtDescription = findViewById(R.id.edt_description);
        edtTrailerURL = findViewById(R.id.edt_trailer_url);

    }

    private void btnBackOnClick(){
      btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void btnRemoveLinkOnClick() {
        btnRemoveLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LINK_NEXT > 2) {
                    LINK_NEXT--;
                    movieLinksLayout.removeView(findViewById(LINK_NEXT));
                }

            }
        });
    }

    private void btnAddMoreLinkOnClick() {
        btnAddLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = new EditText(EditMovieActivity.this);
                editText.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                editText.setHint(getString(R.string.link, String.valueOf(LINK_NEXT)));
                editText.setId(LINK_NEXT);
                movieLinksLayout.addView(editText);
                LINK_NEXT++;
            }
        });
    }

    private void btnEditOnclick() {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setCountryDefaultValue() {
        if (defaultCountry != null && !countryNames.isEmpty()) {
            int defaultIndex = countryNames.indexOf(defaultCountry);
            spCountry.setSelection(defaultIndex);
            // set default id
            selectedCountryId = countryIds.get(defaultIndex);
        }
    }

    private void setGenreDefaultValue() {
        if (selectedGenres != null && genres != null && !genres.isEmpty()) {
            List<String> genreIds = new ArrayList<>();
            for (String selected : selectedGenres) {
                for (Genre genre : genres) {
                    if (selected.equals(genre.getName())) {
                        genreIds.add(genre.getCode());
                        break;

                    }
                }

            }

            genreAdapter = new GenreAdapter(genres);
            genreAdapter.setSelectedGenres(genreIds);
            rvGenre.setAdapter(genreAdapter);
            rvGenre.setLayoutManager(new GridLayoutManager(EditMovieActivity.this, 2));
        }
    }

    private void handleGenreSelection() {
        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCountryId = countryIds.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });

    }

    private void openGallery(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    private void handleChangePoster() {
        btnPosterHorizontal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(REQUEST_IMAGE_H);
            }
        });

        btnPosterVertical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(REQUEST_IMAGE_V);
            }
        });

    }

    private void getMovie() {
        MovieService movieService = ApiService.createService(MovieService.class);
        Call<ApiResponse<Movie>> call = movieService.getMovie(movieId);

        call.enqueue(new Callback<ApiResponse<Movie>>() {
            @Override
            public void onResponse(Call<ApiResponse<Movie>> call, Response<ApiResponse<Movie>> response) {
                if (response.isSuccessful()) {
                    Movie movie = response.body().getData();
                    if (movie != null) {
                        List<String> movieLinks = movie.getVideoURL();
                        for (String link : movieLinks) {
                            EditText editText = new EditText(EditMovieActivity.this);
                            editText.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            ));
                            editText.setHint(getString(R.string.link, String.valueOf(LINK_NEXT)));
                            editText.setId(LINK_NEXT);
                            editText.setText(link);
                            movieLinksLayout.addView(editText);
                            LINK_NEXT++;

                        }

                        // ImageView
                        Glide.with(EditMovieActivity.this).load(movie.getPosterHorizontal()).into(ivPosterHorizontal);
                        Glide.with(EditMovieActivity.this).load(movie.getPosterVertical()).into(ivPosterVertical);

                        // EditText
                        edtTitle.setText(movie.getTitle());
                        edtActors.setText(movie.getActors());
                        edtDirector.setText(movie.getDirector());
                        edtDuration.setText(movie.getDuration());
                        edtReleaseYear.setText(movie.getReleaseYear());
                        edtDescription.setText(movie.getDescription());
                        edtTrailerURL.setText(movie.getTrailerURL());

                        defaultCountry = movie.getCountry();
                        String selectedGenresString = movie.getGenre();
                        for (String genreName : selectedGenresString.split(",")) {
                            selectedGenres.add(genreName.trim());
                        }

                        setCountryDefaultValue();
                        setGenreDefaultValue();
                        callApiSuccess = true;
                    } else {
                        // Error
                        callApiSuccess = false;
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Movie>> call, Throwable t) {
                // Error
                callApiSuccess = false;
            }
        });
    }

    private void getCountries() {
        CommonService commonService = ApiService.createService(CommonService.class);
        Call<ApiResponse<List<Country>>> call = commonService.getCountries();


        call.enqueue(new Callback<ApiResponse<List<Country>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Country>>> call, Response<ApiResponse<List<Country>>> response) {
                if (response.isSuccessful()) {
                    List<Country> countries = response.body().getData();
                    for (Country country : countries) {
                        countryNames.add(country.getName());
                        countryIds.add(country.getCode());
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(EditMovieActivity.this, android.R.layout.simple_spinner_item, countryNames);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spCountry.setAdapter(dataAdapter);

                    setCountryDefaultValue();


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
                    genres = response.body().getData();

                    setGenreDefaultValue();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Genre>>> call, Throwable t) {

            }
        });
    }

}