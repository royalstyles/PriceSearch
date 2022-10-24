package com.jhpj.pricesearch.push;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.jhpj.pricesearch.R;

import org.json.JSONObject;

// TODO [서버에서 푸시알림을 보내는 형식 정의]
    /*
    {
        "data" : {
                "title" : "Android Push Test", // [타이틀]
                "body"  : "안드로이드 푸시 테스트입니다.", // [내용]
                "sort"  : 2, // [진동, 알림 종류]
                "msgType" : 2, // [메시지 타입]
                "messageId" : "dsfe223" // [메시지 아이디]
        },
        "to":"d2fBYJVLSV6mgiyTh", // [토큰]
        "Android": { // [알림 중요도]
            "priority": "high"
        },
        "priority": 10
    }
    */

public class MyFirebaseMsgService extends FirebaseMessagingService {

    private final String TAG = this.getClass().getSimpleName();

    String type; // [푸시 수시 받은 타입]
    String title; // [푸시 타이틀]
    String messagae; // [푸시 내용]

    int sort = 2; // [무음, 진동, 소리 여부 설정 : 디폴트 = 소리]
    int msgType = 0; // [메시지 타입]
    String messageId = ""; // [메시지 아이디]
    int notiId = 0; // [푸시 알림 아이디 값]

    @Override
    //onNewToken()은 클라우드 서버에 등록되었을 때 호출
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        // TODO: Implement this method to send token to your app server.
        // FCM 이 아닌 서버간 토큰 연동 작업 실행
        Log.d(TAG, "NEW_TOKEN: " + token);
    }

    @Override
    // onMessageReceived()는 클라우드 서버에서 메시지를 전송하면 자동으로 호출
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        Log.d(TAG, "From: " + message.getFrom());

        /* 새메세지를 알림기능을 적용하는 부분 */

        if (message.getNotification() != null) {
            Log.d(TAG, "From: " + message.getFrom());
            Log.d(TAG, "Message Notification Body: " + message.getNotification().getBody());

            title = message.getNotification().getTitle();
            messagae = message.getNotification().getBody();
        } else {
            type = "NONE"; // [타입 저장]
            title = ""; // [타이틀 저장]
            messagae = ""; // [메시지 저장]
            sort = 2; // [푸시 알림 설정 여부]
            msgType = 0; // [메시지 타입]
            messageId = ""; // [메시지 아이디]
        }

        JSONObject sendRecive = null;
        try {
            sendRecive = new JSONObject();
            sendRecive.put("title", title); // [푸시 타이틀]
            sendRecive.put("message", messagae); // [푸시 내용]
            sendRecive.put("msgType", msgType); // [메시지 타입]
            sendRecive.put("messageId", messageId); // [메시지 아이디]
        } catch (Exception e) {
            e.printStackTrace();
        }

        // [포그라운드 브로드 캐스트 알림 전달 실시]
        /*
        try {
            if (sendRecive != null){
                Intent intent = new Intent(S_FinalData.NOTI_RECEIVE_PUSH_MESSAGE);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("message", sendRecive.toString());
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        */

        // [SEARCH FAST] : [오레오 버전 분기 처리]
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // [오레오 버전 이상 노티피케이션 호출]
            // [0 == 무음 / 1 == 진동 / 2 == 소리 / 3 == 진동 및 소리]
            startForegroundService(sort);
        } else { // [오레오 이하 노티피케이션 호출]
            // [0 == 무음 / 1 == 진동 / 2 == 소리 / 3 == 진동 및 소리]
            startBackgroundService(sort); // [진동/소리]
        }
    }

    public void startForegroundService(int flag) {
        // [노티피케이션 알림 클릭 시 인텐트 설정 정의]
        Intent intent = null;
        PendingIntent pendingIntent = null;

        try {
//            intent = new Intent(getApplication(), P_PushAlert.class);
            intent.putExtra("id", String.valueOf(notiId)); // [푸시 알림 화면에서 푸시 아이디 값 지우기 위해 데이터 전달]

            // [추가 인텐트 설정 실시]
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            // [pendingIntent 설정]
            pendingIntent = PendingIntent.getActivity(getApplication(), 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            // -----------------------------------------
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // TODO [오레오 버전 이상 >> 채널 필요]
            int importance; // [알림 중요도]
            int prior; // [알림 중요도]
            String Noti_Channel_ID = ""; // [알림 채널 아이디]
            String Noti_Channel_Group_ID = ""; // [알림 채널 그룹 아이디]

            if (flag == 0 || flag == 1) {
                // [0 == [무음] / 1 == [진동]]
                importance = NotificationManager.IMPORTANCE_LOW; // [알림 중요도 설정 (알림음 없이 설정 >> 알림바도 내려오지 않음)]
                prior = NotificationCompat.PRIORITY_LOW;
                Noti_Channel_ID = "Low_Noti_Setting"; // [알림 채널 아이디]
                Noti_Channel_Group_ID = "Low_Group_Setting"; // [알림 채널 그룹 아이디]
            }
            // -----------------------------------------
            else {
                // [2 = [소리 / 진동]]
                importance = NotificationManager.IMPORTANCE_HIGH; // [알림 중요도 설정 (알림바 표시)]
                prior = NotificationCompat.PRIORITY_HIGH;
                Noti_Channel_ID = "Hight_Noti_Setting"; // [알림 채널 아이디]
                Noti_Channel_Group_ID = "Hight_Group_Setting"; // [알림 채널 그룹 아이디]
            }

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // [노티피케이션 알림 서비스 객체 생성]
            NotificationChannel notificationChannel = new NotificationChannel(Noti_Channel_ID, Noti_Channel_Group_ID, importance); // [알림 채널 설정]
            notificationChannel.setVibrationPattern(new long[]{0}); // [알림 진동 발생안함 설정 > 동적으로 진동 메소드 발생 시킴]
            //notificationChannel.setVibrationPattern(new long[]{0, 300, 250, 300, 250, 300});
            notificationChannel.enableVibration(true); // [노티피케이션 진동 설정]
            //notificationChannel.setShowBadge(true); // 뱃지 카운트 실시 (디폴트 true)

            if (notificationManager.getNotificationChannel(Noti_Channel_ID) != null) { // [이미 만들어진 채널이 존재할 경우]
                Log.i("", "\n" + "[" + String.valueOf(TAG) + " >> startForegroundService() :: 오레오 버전 [이상] 채널 상태 확인]");
                Log.i("", "\n" + "[상 태 :: " + "채널이 이미 존재합니다" + "]");
            } else {
                Log.i("", "\n" + "[" + String.valueOf(TAG) + " >> startForegroundService() :: 오레오 버전 [이상] 채널 상태 확인]");
                Log.i("", "\n" + "[상 태 :: " + "채널이 없어서 만듭니다" + "]");
                notificationManager.createNotificationChannel(notificationChannel); // [알림 채널 생성 실시]
            }

            notificationManager.createNotificationChannel(notificationChannel);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), Noti_Channel_ID) // [NotificationCompat.Builder 객체 생성]
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground)) // [메시지 박스에 아이콘 표시]
                    .setSmallIcon(R.drawable.ic_launcher_foreground) // [타이틀 창 부분에 화이트 아이콘]
                    .setColor(ContextCompat.getColor(this, R.color.teal_700)) // [화이트 아이콘 색상 지정]
                    .setWhen(System.currentTimeMillis()) // [알림 표시 시간 설정]
                    .setShowWhen(true) // [푸시 알림 받은 시간 커스텀 설정 표시]
                    .setAutoCancel(true) // [알림 클릭 시 삭제 여부]
                    //.setOngoing(true) // [사용자가 알림 못지우게 설정 >> 클릭해야 메시지 읽음 상태]
                    .setPriority(prior) // [알림 중요도 설정]
                    .setContentTitle(title) // [알림 제목]
                    //.setNumber(Integer.parseInt(S_Preference.getString(getApplication(), "BadgeCount"))) // [뱃지 카운트 실시 (확인하지 않은 알림 갯수)]
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL) // [뱃지 아이콘 타입 지정]
                    .setChannelId(Noti_Channel_ID) // [알림 채널 지정]

                    .setStyle(new NotificationCompat.BigTextStyle().bigText(messagae)) // TODO [다중 멀티 라인 적용 위함 : 내용이 길면 멀티라인 및 \n 개행 적용]
                    //.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).setSummaryText(messagae)) // TODO [사진 표시]

                    .setContentText(messagae); // [알림 내용 지정]

            builder.setContentIntent(pendingIntent); // TODO [알림 개별 인텐트 적용]
            builder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL; // [노티 알림 삭제 시 자동으로 푸시 뱃지 표시 지움]
            notificationManager.notify(notiId, builder.build()); // [아이디값이 달라야 노티피케이션이 여러개 표시된다]

            if (flag == 0) { // [무음]
                Log.i("", "\n" + "[STATE :: " + "무음" + "]");
                PushCallDisplay(); // [화면 강제로 깨우기 실시]
            }
            // -----------------------------------------
            else if (flag == 1) { // [진동]
                Log.i("", "\n" + "[STATE :: " + "진동" + "]");
                PushCallDisplay(); // [화면 강제로 깨우기 실시]
                PushCallVibrator(); // [진동 수행 실시]
            }
            // -----------------------------------------
            else if (flag == 2) { // [소리 + 진동]
                Log.i("", "\n" + "[STATE :: " + "소리 + 진동" + "]");
                PushCallDisplay(); // [화면 강제로 깨우기 실시]
                PushCallVibrator(); // [진동 수행 실시]
                PushCallSound(); // [알림 소리 재생 실시]
                // [앱 소리 설정 시 자동으로 소리남] [중요도는 HIGHT]
            }
            // -----------------------------------------
            else {
                Log.i("", "\n" + "[STATE :: " + "기본 >> 소리 + 진동" + "]");
                PushCallDisplay(); // [화면 강제로 깨우기 실시]
                PushCallVibrator(); // [진동 수행 실시]
                PushCallSound(); // [알림 소리 재생 실시]
                // [앱 소리 설정 시 자동으로 소리남] [중요도는 HIGHT]
            }
        }
    }

    // TODO [오레오버전 미만 알림 표시 처리]
    private void startBackgroundService(int flag) {
        // [노티피케이션 알림 클릭 시 인텐트 설정 정의]
        Intent intent = null;
        PendingIntent pendingIntent = null;

        try {
            intent.putExtra("id", String.valueOf(notiId)); // [푸시 알림 화면에서 푸시 아이디 값 지우기 위해 데이터 전달]
            // [추가 인텐트 설정 실시]
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            // [pendingIntent 설정]
            pendingIntent = PendingIntent.getActivity(getApplication(), 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            // -----------------------------------------
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) { //TODO [오레오 버전 미만 >> 설정 실시]
            int prior; // [알림 중요도]
            String Noti_Channel_ID = ""; // [알림 채널]
            // -----------------------------------------
            if (flag == 0 || flag == 1) {
                // [0 == [무음] / 1 == [진동]]
                prior = NotificationCompat.PRIORITY_LOW;
                Noti_Channel_ID = "Low_Noti_Setting"; // [알림 채널 아이디]
            }
            // -----------------------------------------
            else {
                // [2 = [소리 / 진동]]
                prior = NotificationCompat.PRIORITY_HIGH;
                Noti_Channel_ID = "Hight_Noti_Setting"; // [알림 채널 아이디]
            }
            Log.i("", "\n" + "[" + String.valueOf(TAG) + " >> startBackgroundService() :: 오레오 버전 [미만] 채널 명칭 확인]");
            Log.i("", "\n" + "[Noti_Channel_ID :: " + String.valueOf(Noti_Channel_ID) + "]");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), Noti_Channel_ID) // [NotificationCompat.Builder 객체 생성]
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground)) // [메시지 박스에 아이콘 표시]
                    .setSmallIcon(R.drawable.ic_launcher_foreground) // [타이틀 창 부분에 화이트 아이콘]
                    .setColor(ContextCompat.getColor(this, R.color.teal_700)) // [화이트 아이콘 색상 지정]
                    .setWhen(System.currentTimeMillis()) // [알림 표시 시간 설정]
                    .setShowWhen(true) // [푸시 알림 받은 시간 커스텀 설정 표시]
                    .setAutoCancel(true) // [알림 클릭 시 삭제 여부]
                    //.setOngoing(true) // [사용자가 알림 못지우게 설정 >> 클릭해야 메시지 읽음 상태]
                    .setPriority(prior) // [알림 중요도 설정]
                    .setDefaults(Notification.DEFAULT_LIGHTS) // [알림 진동 발생안함 설정]
                    .setVibrate(new long[]{0L}) // [알림 진동 발생안함 설정]
                    .setContentTitle(title) // [알림 제목]
                    //.setNumber(Integer.parseInt(S_Preference.getString(getApplication(), "BadgeCount"))) // [뱃지 카운트 실시 (확인하지 않은 알림 갯수)]
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL) // [뱃지 아이콘 타입 지정]

                    .setStyle(new NotificationCompat.BigTextStyle().bigText(messagae)) // TODO [다중 멀티 라인 적용 위함 : 내용이 길면 멀티라인 및 \n 개행 적용]
                    //.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).setSummaryText(messagae)) // TODO [사진 표시]

                    .setContentText(messagae); // [알림 내용 지정]

            builder.setContentIntent(pendingIntent); // TODO [알림 개별 인텐트 적용]
            builder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL; // [노티 알림 삭제 시 자동으로 푸시 뱃지 표시 지움]
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notiId, builder.build()); // [아이디값이 달라야 노티피케이션이 여러개 표시된다]
            // -----------------------------------------
            if (flag == 0) { // [무음]
                Log.i("", "\n" + "[STATE :: " + "무음" + "]");
                PushCallDisplay(); // [화면 강제로 깨우기 실시]
            }
            // -----------------------------------------
            else if (flag == 1) { // [진동]
                Log.i("", "\n" + "[STATE :: " + "진동" + "]");
                PushCallDisplay(); // [화면 강제로 깨우기 실시]
                PushCallVibrator(); // [진동 수행 실시]
            }
            // -----------------------------------------
            else if (flag == 2) { // [소리 + 진동]
                Log.i("", "\n" + "[STATE :: " + "소리 + 진동" + "]");
                PushCallDisplay(); // [화면 강제로 깨우기 실시]
                PushCallVibrator(); // [진동 수행 실시]
                PushCallSound(); // [알림 소리 재생 실시]
            }
            // -----------------------------------------
            else {
                Log.i("", "\n" + "[STATE :: " + "기본 >> 소리 + 진동" + "]");
                PushCallDisplay(); // [화면 강제로 깨우기 실시]
                PushCallVibrator(); // [진동 수행 실시]
                PushCallSound(); // [알림 소리 재생 실시]
            }
            // -----------------------------------------
        }
    }

    // TODO [화면 강제로 기상 실시 메소드]
    public void PushCallDisplay() {

        /**
         * // -----------------------------------------
         * [PushCallDisplay 메소드 설명]
         * // -----------------------------------------
         * 1. 모바일 디스플레이 화면 강제 기상 깨우기 수행 메소드
         * // -----------------------------------------
         * 2. 필요 퍼미션 :
         *   - <uses-permission android:name="android.permission.WAKE_LOCK"/>
         * // -----------------------------------------
         * */

        try {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, TAG);
            //wakelock.acquire(5000);
            wakelock.acquire(); // [화면 즉시 켜기]
            wakelock.release(); // [WakeLock 해제]
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    // TODO [모바일 진동 강제 발생 메소드]
    Handler mHandler = new Handler(Looper.getMainLooper());

    public void PushCallVibrator() {

        /**
         * // -----------------------------------------
         * [PushCallVibrator 메소드 설명]
         * // -----------------------------------------
         * 1. 모바일 진동 발생 수행 메소드
         * // -----------------------------------------
         * 2. 필요 퍼미션 :
         *   - <uses-permission android:name="android.permission.VIBRATE" />
         * // -----------------------------------------
         * */

        try {
            // Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
            // vibrator.vibrate(1000); // [miliSecond, 지정한 시간동안 진동]
            Vibrator mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            long[] pattern = {100, 1000, 100, 1000};
            //long[] pattern = {0};
            if (mVibrator != null) {
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM) //key
                        .build();
                mVibrator.vibrate(pattern, 0, audioAttributes);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mVibrator.cancel(); // 진동 취소 실시
                    }
                }, 1000); //1초뒤 실행 (작업 예약)
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    // TODO [모바일 알림음 강제 발생 메소드]
    public void PushCallSound() {
        /**
         * // -----------------------------------------
         * [PushCallSound 메소드 설명]
         * // -----------------------------------------
         * 1. 모바일 기본 알림음 수행 메소드
         * // -----------------------------------------
         * */

        try {
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), defaultSoundUri);
            ringtone.play(); // [사운드 재생]
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
}
