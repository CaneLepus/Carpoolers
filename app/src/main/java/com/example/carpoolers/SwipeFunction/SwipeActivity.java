package com.example.carpoolers.SwipeFunction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.example.carpoolers.ChatFunction.ChatLogActivity;
import com.example.carpoolers.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SwipeActivity extends AppCompatActivity {

    ViewPager viewPager;
    Adapter adapter;
    Button button;
    List<Model> models;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    private String firstName;
    private String lastName;
    private String bio;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference users = db.collection("users");
    DocumentReference query = users.document(auth.getCurrentUser().getUid());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        users.get().addOnSuccessListener(documentSnapshot -> {
                    models = new ArrayList<>();

                    for (DocumentSnapshot document : documentSnapshot) {
                        firstName = (String) document.get("first");
                        lastName = document.get("last").toString();
                        bio = document.get("biography").toString();

                         ArrayList<Number> ratings  = (ArrayList<Number>) document.get("rating");
                         Float rating = 0.0f;

                        for(Number item : ratings){
                            Float it = item.floatValue();
                            rating += it;
                        }
                        rating /= ratings.size();

                        models.add(new Model(R.drawable.ic_profilepic, firstName + " " + lastName, "" + bio, rating));
                    }


                    adapter = new Adapter(models, this);

                    viewPager = findViewById(R.id.viewPagern);
                    viewPager.setAdapter(adapter);
                    viewPager.setPadding(130, 0, 130, 0);


                    viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            if (position < (adapter.getCount() - 1) && position < (colors.length - 1)) {
                                viewPager.setBackgroundColor(

                                        (Integer) argbEvaluator.evaluate(
                                                positionOffset,
                                                colors[position],
                                                colors[position + 1]
                                        )
                                );
                            } else {
                                viewPager.setBackgroundColor(colors[colors.length - 1]);
                            }
                        }

                        @Override
                        public void onPageSelected(int position) {

                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                }
        );

        button = findViewById(R.id.chatButton);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatLogActivity.class);
            startActivity(intent);
        });

        colors = new Integer[]{
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4),
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4),
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4),
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4)
        };

    }
}