package com.jhpj.pricesearch.ui.cloudstorage;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.jhpj.pricesearch.databinding.FragmentCloudstorageBinding;

public class CloudStorageFragment extends Fragment {
    private FragmentCloudstorageBinding binding;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference rootRef = storage.getReference();
    private LinearLayout linear_imageContainer;
    private CloudSotrageSubLayout subLayout;
    private ImageView img_loadview;

    private final String TAG = this.getClass().getSimpleName();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCloudstorageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        img_loadview = binding.imgLoadview;

        StorageReference imgRef = rootRef.child("image.jpeg");
        Log.w(TAG, "imgRef : " + imgRef.getName());

        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getActivity()).load(uri).into(img_loadview);
            }
        });

        linear_imageContainer = binding.linearImageContainer;
        StorageReference listRef = rootRef.child("/gallery/");
        listRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item : listResult.getItems()) {
                    item.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "task.getResult() : " + task.getResult());
//                                Glide.with(getActivity()).load(task.getResult()).into(img_loadview);
                                Items items = new Items(task.getResult().toString(), task.getResult().toString());
                                subLayout = new CloudSotrageSubLayout(getContext(), items);
                                linear_imageContainer.addView(subLayout);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "item.getDownloadUrl() is Fail");
                        }
                    });
                }
            }
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}