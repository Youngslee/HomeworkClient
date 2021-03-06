/*
 * 클래스 이름 : FirebaseMessagingService
 *  - 앱의 알림을 수신, 데이터 수신 등을 위한 클래스
 * 버전 정보
 *
 * 날짜 : 2018.01.12
 *
 */
package line.homework.clien;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import line.homework.R;
import line.homework.logintest.LoginActivity;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "FirebaseMsgService";

    private String msg;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        JSONObject json = new JSONObject(remoteMessage.getData());
        try {
            sendNotification(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void sendNotification(JSONObject json) throws JSONException {
        String title = null;
        try {
            title = URLDecoder.decode(json.get("title").toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = json.get("URL").toString();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("pushURL",url);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(url)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{1, 1000})
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, mBuilder.build());
    }
}