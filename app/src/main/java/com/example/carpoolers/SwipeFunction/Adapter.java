package com.example.carpoolers.SwipeFunction;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
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

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.swipe_item, container, false);



        ImageView imageView;
        TextView title, desc;
        RatingBar ratingBar;

        imageView = view.findViewById(R.id.image);
        title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.desc);
        ratingBar = view.findViewById(R.id.ratingBar);

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

        view.setOnClickListener(v -> {
            Intent intent = new Intent(context, TemporaryProfileActivity.class);
            intent.putExtra("param", models.get(position).getTitle());
            intent.putExtra("rating", models.get(position).getRating());
            intent.putExtra("uid", models.get(position).getUid());
            context.startActivity(intent);
            //finish();
        });

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}