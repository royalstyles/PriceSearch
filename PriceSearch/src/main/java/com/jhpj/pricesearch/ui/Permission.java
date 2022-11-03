package com.jhpj.pricesearch.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permission extends AppCompatActivity {
    private String[] permissionList;
    private static final int SINGLE_PERMISSION = 1004;
    private static final int MULTIPLE_PERMISSION = 1005;

    private final String TAG = this.getClass().getSimpleName();

    //권한 체크 함수
    public void checkPermission_storage() {
        // 현재 버전 6.0 미만이면 종료 --> 6이후 부터 권한 허락
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        permissionList = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        // 각 권한 허용 여부를 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissionList, SINGLE_PERMISSION);
        } else {
            // 권한 있음
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case SINGLE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한이 이미 있는 경우
                    Log.e(TAG, "권한이 이미 있음" + permissionList);
                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setTitle("앱 권한 설정");
                    alertDialog.setMessage("설정으로 이동합니다.");
                    alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 이 부분은 설정으로 이동하는 코드이므로 안드로이드 운영체제 버전에 따라 상이할 수 있다.
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            dialog.cancel();
                        }
                    });
                    alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                }
        }
    }
}
