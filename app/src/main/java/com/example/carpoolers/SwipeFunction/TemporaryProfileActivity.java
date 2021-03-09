package com.example.carpoolers.SwipeFunction;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.Telephony;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.carpoolers.R;
import com.example.carpoolers.Rate;


public class TemporaryProfileActivity extends AppCompatActivity {

    Rate ra = new Rate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temporary_profile);

        TextView textView = findViewById(R.id.textView);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        textView.setText(getIntent().getStringExtra("param"));
        ratingBar.setRating(getIntent().getFloatExtra("rating", 0.0f));


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                rate(rating);
                ratingBar.setIsIndicator(true);

                ratingBar.setRating(ra.getRating(getApplicationContext(), getIntent().getStringExtra("uid")));

                //ratings = String.valueOf(rating);

            }
        });
    }


    void rate(Float rating){
        ra.setRating(getApplicationContext(), getIntent().getStringExtra("uid"), rating);
    }


}