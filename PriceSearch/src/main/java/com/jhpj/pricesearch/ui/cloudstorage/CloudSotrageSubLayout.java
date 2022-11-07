package com.jhpj.pricesearch.ui.cloudstorage;

import androidx.annotation.Nullable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.jhpj.pricesearch.R;

public class CloudSotrageSubLayout extends LinearLayout {


    public CloudSotrageSubLayout(Context context, AttributeSet attrs, @Nullable Items items) {
        super(context, attrs);
        init(context, items);
    }

    public CloudSotrageSubLayout(Context context, @Nullable Items items) {
        super(context);
        init(context, items);
    }

    private void init(Context context, Items items) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_cloudsotrage_image, this, true);

        ImageView img_image = (ImageView) findViewById(R.id.img_image);

        Glide.with(this)
                .load(items.getImagUrl().toString())
                .override(300, 300)
                .centerCrop()
                .into(img_image);
    }
}