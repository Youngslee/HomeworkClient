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
import android.os.AsyncTask;
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

        /*
            thread -> asyncTask 수정 필요
         */
        LoginAsyncTask loginAsync = new LoginAsyncTask();
        loginAsync.execute();
    }
    class LoginAsyncTask extends AsyncTask<Void, Integer, Void> {
        // doInBackground 메소드가 실행되기 전에 실행되는 메소드
        @Override
        protected void onPreExecute () {
            super.onPreExecute();
        }

        // 실제 비즈니스 로직이 처리될 메소드(Thread 부분이라고 생각하면 됨)
        @Override
        protected Void doInBackground (Void...params){
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
            return null;
        }

        // doInBackground에서 넘긴 values 값을 받아서 처리하는 부분
        @Override
        protected void onProgressUpdate (Integer...values){
        }

        // 모든 작업이 끝난 후 처리되는 메소드
        @Override
        protected void onPostExecute (Void result){
            super.onPostExecute(result);
            userPicture.setImageBitmap(bitmap_picture);
        }
    }
}
