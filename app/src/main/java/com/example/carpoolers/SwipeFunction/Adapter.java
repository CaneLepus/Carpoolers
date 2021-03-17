package com.example.carpoolers.SwipeFunction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.carpoolers.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Adapter extends PagerAdapter {

    List<Model> models;
    Context context;

    public Adapter(List<Model> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.activity_swipe_profile_basic, container, false);


        ImageView imageView;
        TextView title, desc, distance;
        RatingBar ratingBar;

        imageView = view.findViewById(R.id.image);
        title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.desc);
        ratingBar = view.findViewById(R.id.ratingBar);
        distance = view.findViewById(R.id.distance);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(models.get(position).getImage());


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


        title.setText(models.get(position).getTitle());
        desc.setText(models.get(position).getDesc());
        ratingBar.setRating(models.get(position).getRating());
        distance.setText(String.format("%.1f km", models.get(position).getDist()));

        view.setOnClickListener(v -> {
            Intent intent = new Intent(context, SwipeProfileDetailedActivity.class);
            intent.putExtra("title", models.get(position).getTitle());
            intent.putExtra("rating", models.get(position).getRating());
            intent.putExtra("desc", models.get(position).getDesc());
            intent.putExtra("uid", models.get(position).getUid());
            intent.putExtra("image", models.get(position).getImage());
            intent.putExtra("dist", models.get(position).getDist());
            intent.putExtra("numOfRates", models.get(position).getNumberOfRatings());
            context.startActivity(intent);
            //finish();
        });

        container.addView(view, 0);
        return view;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        // Returning POSITION_NONE means the current data does not matches the data this fragment is showing right now.  Returning POSITION_NONE constant will force the fragment to redraw its view layout all over again and show new data.
        return POSITION_NONE;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}