/*
 * 클래스 이름 : LoginResultActivity
 *  - 로그인 결과 출력 Activity
 * 버전 정보
 *
 * 날짜 : 2018.01.06
 *
 */

package line.homework.logintest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.linecorp.linesdk.LineProfile;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import line.homework.R;


public class LoginResultActivity extends Activity {
    private String intent_userId;
    private String intent_userName;
    private String intent_userPictureUrl;
    private EditText userId;
    private EditText userName;
    private ImageView userPicture;
    Bitmap bitmap_picture=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);

        userId = (EditText) findViewById(R.id.userId);
        userName = (EditText) findViewById(R.id.userName);
        userPicture = (ImageView) findViewById(R.id.userImage);
        Intent intent = getIntent();

        intent_userId = intent.getStringExtra("line_profile_id");
        intent_userName = intent.getStringExtra("line_profile_name");
        intent_userPictureUrl = intent.getStringExtra("line_profile_url");


        userId.setText(intent_userId);
        userName.setText(intent_userName);


        Thread mThread = new Thread(){
            @Override
            public void run(){
                try{
                    URL url = new URL(intent_userPictureUrl);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap_picture = BitmapFactory.decodeStream(is);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        mThread.start();
        try{
            mThread.join();
            userPicture.setImageBitmap(bitmap_picture);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
