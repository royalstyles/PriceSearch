package com.jhpj.pricesearch;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.jhpj.pricesearch.databinding.ActivityMainBinding;
import com.jhpj.pricesearch.ui.login.LoginActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    // 마지막으로 뒤로가기 버튼을 눌렀던 시간 저장
    private long backKeyPressedTime = 0;
    // 첫 번째 뒤로가기 버튼을 누를때 표시
    private Toast toast;

    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");

                // String으로 받아서 넣기
                String sendMessage = "이렇게 스트링으로 만들어서 넣어주면 됩니다.";
                intent.putExtra(Intent.EXTRA_TEXT, sendMessage);

                Intent shareIntent = Intent.createChooser(intent, "share");
                startActivity(shareIntent);
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_news, R.id.nav_movie, R.id.nav_naversearch, R.id.nav_realtimedb)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // FireBase Token 토큰처리
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "토큰 생성 실패", task.getException());
                }

                // 새로운 토큰 생성 성공 시
                String token = task.getResult();
                Log.d(TAG, "MessageToken : " + token, null);
            }
        });

        // 로그인 시 로그인 정보를 메인 화면에 가져온다.
//        View navheaderView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        View navheaderView = navigationView.getHeaderView(0);
        TextView navtitle = navheaderView.findViewById(R.id.navtitle);
        TextView navemail = navheaderView.findViewById(R.id.navemail);

        Intent receiveIntent = getIntent();
        Log.d(TAG, "receiveIntent.getStringExtra(\"navtitle\") : " + receiveIntent.getStringExtra("navtitle"));

        if (receiveIntent.getStringExtra("navtitle") != null) {
            navtitle.setText(receiveIntent.getStringExtra("navtitle"));
            navemail.setText(receiveIntent.getStringExtra("navemail"));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d(getClass().getName(), "KJH : " + Thread.currentThread().getStackTrace()[2].getMethodName());
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_login) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

//            // Custom 레이아웃 연결 위해 LayoutInflater 객체 생성
//            LayoutInflater inflater = getLayoutInflater();
//            // Custom 레이아웃 Imflatation '인플레이션', 레이아웃 메모리에 객체화
//            View layout = inflater.inflate(R.layout.toast_custom, (ViewGroup) findViewById(R.id.toast_custom_layout));
//            // 보여줄 메시지 설정 위해 TextView 객체 연결, 인플레이션해서 생성된 View를 통해 findViewById 실행
//            TextView message = layout.findViewById(R.id.toast_custom_message);
//            message.setText("로그인 세팅중");
//            // 보여줄 이미지 설정 위해 ImageView 연결
//            ImageView image = layout.findViewById(R.id.toast_custom_image);
//            image.setBackgroundResource(R.mipmap.ic_launcher);
//
//            // Toast 객체 생성
//            Toast toast = new Toast(this);
//            // 위치설정, Gravity - 기준지정(상단,왼쪽 기준 0,0) / xOffset, yOffset - Gravity기준으로 위치 설정
//            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 16);
//            // Toast 보여줄 시간 'Toast.LENGTH_SHORT 짧게'
//            toast.setDuration(Toast.LENGTH_LONG);
//            // CustomLayout 객체 연결
//            toast.setView(layout);
//            // Toast 보여주기
//            toast.show();
        } else if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        Log.d(getClass().getName(), "KJH : " + Thread.currentThread().getStackTrace()[2].getMethodName());
        DrawerLayout drawer = binding.drawerLayout;
        // 네비게이션 뷰가 열려있으면 닫는다.
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
        } else {
//             기존 뒤로가기 버튼의 기능을 막기위해 주석처리 또는 삭제
//             super.onBackPressed();
//
//             마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
//             마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지났으면 Toast Show
//             2000 milliseconds = 2 seconds

            @SuppressLint("SimpleDateFormat") SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Log.d(getClass().getName(),
                    "KJH : " + "System.currentTimeMillis() : " + timeformat.format(new Date(System.currentTimeMillis())) + "\n"
                            + "backKeyPressedTime : " + timeformat.format(new Date(backKeyPressedTime)));

            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                toast = Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
                toast.show();
            }
            // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
            // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
            // 현재 표시된 Toast 취소
//            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            else {
                finish();
                toast.cancel();
                moveTaskToBack(true);
            }
        }
    }

    private void sendNotification(String messageBody) {
        //알림 클릭시 실행될 액티비티 (PendingIntent)
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle(getString(R.string.fcm_message))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        //노티 메니저로 알림 팝업 띄우기
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // 오래오 버전 이상부터 channelId 값이 필수가 됨
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}