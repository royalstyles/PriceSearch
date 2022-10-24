package com.jhpj.pricesearch.push;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMsgService extends FirebaseMessagingService {

    private final String TAG = this.getClass().getSimpleName();

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

            String messageBody = message.getNotification().getBody();
            String messageTitle = message.getNotification().getTitle();
        }
    }
}
