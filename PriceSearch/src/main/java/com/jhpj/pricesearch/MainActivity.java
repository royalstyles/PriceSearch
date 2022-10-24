package com.jhpj.pricesearch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.jhpj.pricesearch.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    // 마지막으로 뒤로가기 버튼을 눌렀던 시간 저장
    private long backKeyPressedTime = 0;
    // 첫 번째 뒤로가기 버튼을 누를때 표시
    private Toast toast;

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
                R.id.nav_news, R.id.nav_movie, R.id.nav_naversearch)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
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
            // Custom 레이아웃 연결 위해 LayoutInflater 객체 생성
            LayoutInflater inflater = getLayoutInflater();
            // Custom 레이아웃 Imflatation '인플레이션', 레이아웃 메모리에 객체화
            View layout = inflater.inflate(R.layout.toast_custom, (ViewGroup) findViewById(R.id.toast_custom_layout));
            // 보여줄 메시지 설정 위해 TextView 객체 연결, 인플레이션해서 생성된 View를 통해 findViewById 실행
            TextView message = layout.findViewById(R.id.toast_custom_message);
            message.setText("로그인 세팅중");
            // 보여줄 이미지 설정 위해 ImageView 연결
            ImageView image = layout.findViewById(R.id.toast_custom_image);
            image.setBackgroundResource(R.mipmap.ic_launcher);

            // Toast 객체 생성
            Toast toast = new Toast(this);
            // 위치설정, Gravity - 기준지정(상단,왼쪽 기준 0,0) / xOffset, yOffset - Gravity기준으로 위치 설정
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 16);
            // Toast 보여줄 시간 'Toast.LENGTH_SHORT 짧게'
            toast.setDuration(Toast.LENGTH_LONG);
            // CustomLayout 객체 연결
            toast.setView(layout);
            // Toast 보여주기
            toast.show();
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
}