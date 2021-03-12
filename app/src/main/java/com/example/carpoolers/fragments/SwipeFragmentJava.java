package com.example.carpoolers.fragments;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.carpoolers.R;
import com.example.carpoolers.SwipeFunction.Adapter;
import com.example.carpoolers.SwipeFunction.Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        View v = getView();

        final int[] counter = {1};

//        int count = Integer.parseInt((rooms.document().getId()));


        button = v.findViewById(R.id.imageViewLike);
        button.setOnClickListener(v0 -> {
            //Intent intent = new Intent(getActivity(), ChatLogActivity.class).putExtra("roomID", "1001");
            //startActivity(intent);

            //    Map<Any, Any> roomStore = Map.of(
            //          "user1", auth.getCurrentUser().getUid(),
            //        "user2", models.get(currentPos).getUid());

            Map<Object, Object> myMap = createMap(auth.getCurrentUser().getUid(), models.get(currentPos).getUid());

            Log.d("TAG", "User id: ${user.uid}");
            //rooms.document(String.valueOf(count++)).set(myMap);
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

        listenForUpdates();

    }

    private void listenForUpdates() {
        db.collection("users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e);
                            return;
                        }

                        List<String> cities = new ArrayList<>();
                        models = new ArrayList<>();
                        View v = getView();

                        for (DocumentSnapshot document : value) {
                            String uid = document.getId();

                            if (!uid.equals(auth.getCurrentUser().getUid())) {

                                firstName = (String) document.get("first");
                                lastName = document.get("last").toString();
                                bio = document.get("biography").toString();

                                ArrayList<Number> ratings = (ArrayList<Number>) document.get("rating");
                                Float rating = 0.0f;

                                for (Number item : ratings) {
                                    Float it = item.floatValue();
                                    rating += it;
                                }
                                rating /= ratings.size();

                                models.add(new Model("images/" + uid, uid, firstName + " " + lastName, "" + bio, rating));
                            }
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

                                currentPos = position;
                            }

                            @Override
                            public void onPageSelected(int position) {

                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });
                        Log.d("TAG", "Current cites in CA: " + cities);
                    }
                });
    }


    private static Map<Object, Object> createMap(String user1, String user2) {
        Map<Object, Object> myMap = new HashMap<>();
        myMap.put("user1", user1);
        myMap.put("user2", user2);
        myMap.put("chatAlive", false);
        return myMap;
    }

}