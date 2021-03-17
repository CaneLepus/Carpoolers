package com.example.carpoolers.fragments;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.carpoolers.R;
import com.example.carpoolers.Singleton;
import com.example.carpoolers.SwipeFunction.Adapter;
import com.example.carpoolers.SwipeFunction.Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    private int numberOfRatings = 0;

    private int currentPos;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference users = db.collection("users");
    CollectionReference rooms = db.collection("rooms");
    DocumentReference query = users.document(auth.getCurrentUser().getUid());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_swipe, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        View v = getView();

        final int[] counter = {1};


//        int count = Integer.parseInt((rooms.document().getId()));


        button = v.findViewById(R.id.imageViewLike);

        //button2 = v.findViewById(R.id.)
        button.setOnClickListener(v0 -> {
            rooms.get()
                    .addOnSuccessListener(result -> {


                        long largest = 0;
                        for (DocumentSnapshot document : result) {
                            if (document.getLong("roomNumber") > largest) {
                                largest = document.getLong("roomNumber");
                            }
                        }
                        Map<Object, Object> myMap = createMap(auth.getCurrentUser().getUid(), models.get(currentPos).getUid(), ++largest);
                        rooms.document().set(myMap);
                        users.document(auth.getCurrentUser().getUid())
                                .get()
                                .addOnSuccessListener(snapshot -> {
                                    ArrayList<String> temp = new ArrayList<>();
                                    if (snapshot.get("roomsWith") != null) {
                                        temp = (ArrayList<String>) snapshot.get("roomsWith");
                                    }
                                    temp.add(models.get(currentPos).getUid());
                                    Map<Object, Object> data = new HashMap();
                                    data.put("roomsWith", temp);
                                    users.document(auth.getCurrentUser().getUid()).set(data, SetOptions.merge());
                                });
                        users.document(models.get(currentPos).getUid())
                                .get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    ArrayList<String> temp = new ArrayList<>();
                                    if (documentSnapshot.get("roomsWith") != null) {
                                        temp = (ArrayList<String>) documentSnapshot.get("roomsWith");

                                    }
                                    temp.add(auth.getCurrentUser().getUid());
                                    Map<Object, Object> data = new HashMap();
                                    data.put("roomsWith", temp);
                                    users.document(models.get(currentPos).getUid()).set(data, SetOptions.merge());
                                });
                        Toast.makeText(getContext(), "Liked! A chat request has been sent", Toast.LENGTH_LONG).show();
                    });
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
        db.collection("users").document(auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(document -> {
                    initList(document.getDouble("latitude"), document.getDouble("longitude"));
                });


    }

    private void initList(double userLat, double userLong) {
        models = new ArrayList<>();
        View v = getView();
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

                currentPos = position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        listenForUpdates(userLat, userLong);

    }

    private void listenForUpdates(double userLat, double userLong) {


        db.collection("users")
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        Log.w("TAG", "Listen failed.", e);
                        return;
                    }
                    models.clear();
                    button.setVisibility(View.INVISIBLE);
                    for (DocumentSnapshot document : value) {
                        String uid = document.getId();
                        double latitude = 0.0;
                        if (document.getDouble("latitude") != null) {
                            latitude = document.getDouble("latitude");
                        }
                        double longitude = 0.0;
                        if (document.getDouble("longitude") != null) {
                            longitude = document.getDouble("longitude");
                        }
                        ArrayList<String> mates = (ArrayList<String>) document.get("roomsWith");
                        if (mates == null) {
                            mates = new ArrayList<String>();
                        }


                        if (auth.getCurrentUser() != null) {
                            if (!uid.equals(auth.getCurrentUser().getUid()) && !mates.contains(auth.getCurrentUser().getUid())) {
                                if (distance(userLat, userLong, latitude, longitude) < Singleton.INSTANCE.getSwipeDistance()) {

                                    firstName = (String) document.get("first");
                                    lastName = document.get("last").toString();
                                    bio = document.get("biography").toString();

                                    ArrayList<Number> ratings = (ArrayList<Number>) document.get("rating");
                                    Float rating = 0.0f;

                                    for (Number item : ratings) {
                                        Float it = item.floatValue();
                                        numberOfRatings++;
                                        rating += it;
                                    }
                                    rating /= ratings.size();
                                    models.add(new Model("images/" + uid, uid, firstName + " " + lastName, "" + bio, rating, numberOfRatings, distance(userLat, userLong, latitude, longitude)));
                                    button.setVisibility(View.VISIBLE);


                                }
                            }
                        }
                    }
                    Log.d("TAG", "update recieved " + models.size());
                    Objects.requireNonNull(viewPager.getAdapter()).notifyDataSetChanged();
                });
    }


    private static Map<Object, Object> createMap(String user1, String user2, long roomNumber) {
        Map<Object, Object> myMap = new HashMap<>();
        myMap.put("user1", user1);
        myMap.put("user2", user2);
        myMap.put("roomNumber", roomNumber);
        return myMap;
    }

    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 6371; // in kilometers

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double dist = earthRadius * c;

        return dist; // output distance, in kilometers
    }

}