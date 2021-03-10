package com.example.carpoolers.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.carpoolers.ChatFunction.ChatLogActivity;
import com.example.carpoolers.R;
import com.example.carpoolers.SwipeFunction.Adapter;
import com.example.carpoolers.SwipeFunction.Model;
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

public class SwipeFragmentJava extends Fragment {

    ViewPager viewPager;
    Adapter adapter;
    ImageView button;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_swipe,container,false);
        return view;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        View v = getView();

        users.get().addOnSuccessListener(documentSnapshot -> {
                    models = new ArrayList<>();

                    for (DocumentSnapshot document : documentSnapshot) {


                        String uid = document.getId();
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


                        models.add(new Model("images/"+uid+".jpg",uid ,  firstName + " " + lastName, "" + bio, rating));
                        Log.i("MAGMA v3 debug>>", "Image URL: image/" + uid);

                    }

                    adapter = new Adapter(models, getContext());

                    viewPager = v.findViewById(R.id.viewPagern);
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

        button = v.findViewById(R.id.imageViewLike);
        button.setOnClickListener(v0 -> {
            Intent intent = new Intent(getActivity(), ChatLogActivity.class).putExtra("roomID", "1001");
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
