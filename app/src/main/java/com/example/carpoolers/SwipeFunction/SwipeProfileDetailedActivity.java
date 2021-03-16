package com.example.carpoolers.SwipeFunction;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carpoolers.R;
import com.example.carpoolers.Rate;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;


public class SwipeProfileDetailedActivity extends AppCompatActivity {

    Rate ra = new Rate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_profile_detailed);

        TextView textView = findViewById(R.id.tvTitle);
        TextView textViewDesc = findViewById(R.id.tvDesc);
        TextView textViewDist = findViewById(R.id.tvDistance);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        ImageView imageView = findViewById(R.id.imageView);

        textViewDesc.setText(getIntent().getStringExtra("desc"));
        textViewDist.setText(String.format("%.1f km", (getIntent().getDoubleExtra("dist", 0.0))));
        textView.setText(getIntent().getStringExtra("title"));

        ratingBar.setRating(getIntent().getFloatExtra("rating", 0.0f));

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(getIntent().getStringExtra("image"));

        try {
            File file = File.createTempFile("test", "jpg");
            storageReference.getFile(file).addOnSuccessListener(taskSnapshot -> {
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        imageView.setImageBitmap(bitmap);
                    }

            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                rate(rating);
                ratingBar.setIsIndicator(true);

                //ratings = String.valueOf(rating);

            }
        });
    }


    void rate(Float rating) {
        ra.setRating(getApplicationContext(), getIntent().getStringExtra("uid"), rating);
    }


}