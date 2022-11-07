package com.jhpj.pricesearch.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Arrays;

public class PermissionSupport {
    private Context context;
    private Activity activity;

    private final int SINGLE_PERMISSION = 1004;
    private final int MULTIPLE_PERMISSION = 1005;

    private final String TAG = this.getClass().getSimpleName();

    private String[] permissionList = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    public PermissionSupport(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    //권한 체크 함수
    public boolean runtimeCheckPermission(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    //권한 체크 함수
    public void Permission_CheckOne() {
        // 현재 버전 6.0 미만이면 종료 --> 6이후 부터 권한 허락
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 각 권한 허용 여부를 확인
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(permissionList, SINGLE_PERMISSION);
            } else {
                // 권한 있음
            }
        }
    }

    public boolean PermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "requestCode " + requestCode + "\n"
                + "permissions " + Arrays.toString(permissions) + "\n"
                + "grantResults " + Arrays.toString(grantResults) + "\n"
        );
        switch (requestCode) {
            case SINGLE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한이 이미 있는 경우
                    Log.d(TAG, "권한이 이미 있음 " + Arrays.toString(permissions));
                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("앱 권한 설정");
                    alertDialog.setMessage("필수 권한이 없습니다." + "\n" + "권한을 설정해 주세요." + "\n" + "설정으로 이동합니다.");
                    alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 이 부분은 설정으로 이동하는 코드이므로 안드로이드 운영체제 버전에 따라 상이할 수 있다.
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + activity.getPackageName()));
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
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
            case MULTIPLE_PERMISSION:
                int index = 0;
                for (Integer result : grantResults) {
                    if (grantResults.length > 0 && result == PackageManager.PERMISSION_GRANTED) {
                        // 권한이 이미 있는 경우
                        Log.d(TAG, "권한이 이미 있음 " + permissions[index]);
                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("앱 권한 설정");
                        alertDialog.setMessage("필수 권한이 없습니다." + "\n" + "권한을 설정해 주세요." + "\n" + "설정으로 이동합니다.");
                        alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 이 부분은 설정으로 이동하는 코드이므로 안드로이드 운영체제 버전에 따라 상이할 수 있다.
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + activity.getPackageName()));
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                                dialog.cancel();
                            }
                        });
                        alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(context);
                                alertDialog2.setTitle("경고");
                                alertDialog2.setMessage("필수 권한이 없습니다." + "\n" + "앱을 다시 실행시켜 필수권한을 설정해 주세요." + "\n" + "앱을 종료 합니다.");
                                alertDialog2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        activity.finish();
                                    }
                                });
                                alertDialog2.show();
                            }
                        });
                        alertDialog.show();
                    }
                    index++;
                }
        }
        return true;
    }

    // GPS가 켜져있는지 확인
    public boolean checkGPSService() {
        Log.d(getClass().getName(), "KJH : " + Thread.currentThread().getStackTrace()[2].getMethodName());
        boolean isGPS = false;
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isGPS = true;
        }
        return isGPS;
    }

    // 네트워크 연결 상태인지 확인
    public boolean checkNetworkService() {
        Log.d(getClass().getName(), "KJH : " + Thread.currentThread().getStackTrace()[2].getMethodName());
        boolean isOnline = false;
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo.State mobile = manager.getNetworkInfo(0).getState();
            if (mobile == NetworkInfo.State.CONNECTED) {
                isOnline = true;
            }

            NetworkInfo.State wifi = manager.getNetworkInfo(1).getState();
            if (wifi == NetworkInfo.State.CONNECTED) {
                isOnline = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isOnline;
    }

    public String[] getPermissionList() {
        return permissionList;
    }

    public int getSINGLE_PERMISSION() {
        return SINGLE_PERMISSION;
    }

    public int getMULTIPLE_PERMISSION() {
        return MULTIPLE_PERMISSION;
    }
}
