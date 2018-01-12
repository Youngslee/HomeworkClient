/*
 * 클래스 이름 : LoginActivity
 *  - 로그인 Activity
 * 버전 정보
 *
 * 날짜 : 2018.01.06
 *
 */
package line.homework.logintest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.linecorp.linesdk.LineProfile;
import com.linecorp.linesdk.auth.LineLoginApi;
import com.linecorp.linesdk.auth.LineLoginResult;

import line.homework.R;
import line.homework.clien.DisplayClien;

public class LoginActivity extends Activity {
    private static final int REQUEST_CODE = 1;
    private DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // DBHelper class : 라인 프로필 관련 데이터베이스 관리 클래스
        dbHelper= new DBHelper(this.getApplicationContext(), "LoginInfo.db", null, 1);

        String[] dbInfo=dbHelper.checkLogin();
        if(dbInfo[0]!=null){
            /*
                checkLogin() 수행 후, 첫번째 element에 데이터가 없다면 초기 실행으로 간주
                그렇지 않다면, 초기 실행 이후 실행으로 간주
             */
            /*
                Homework 1 LoginTest에 관련된 intent 처리
             */
//            Intent transitionIntent = new Intent(this, LoginResultActivity.class);
//
//            transitionIntent.putExtra("line_profile_id", dbInfo[0]);
//            transitionIntent.putExtra("line_profile_name", dbInfo[1]);
//            transitionIntent.putExtra("line_profile_url", dbInfo[2]);

            Intent transitionIntent = new Intent(this, DisplayClien.class);
            startActivity(transitionIntent);
        }

        final Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                try{
                    // App-to-app login
                    Intent loginIntent = LineLoginApi.getLoginIntent(v.getContext(), "1555845553");
                    startActivityForResult(loginIntent, REQUEST_CODE);
                }
                catch(Exception e) {
                    Log.e("ERROR","에러");
                    Log.e("ERROR", e.toString());
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_CODE) {
            Log.e("ERROR", "Unsupported Request");
            return;
        }
        LineLoginResult result = LineLoginApi.getLoginResultFromIntent(data);
        switch (result.getResponseCode()) {
            case SUCCESS:
                // Login successful
                String accessToken = result.getLineCredential().getAccessToken().getAccessToken();
                LineProfile profile = result.getLineProfile();
                dbHelper.insert(profile.getUserId(),profile.getDisplayName(),profile.getPictureUrl().toString());
//                Intent transitionIntent = new Intent(this, LoginResultActivity.class);
//                transitionIntent.putExtra("line_profile_id", profile.getUserId());
//                transitionIntent.putExtra("line_profile_name", profile.getDisplayName());
//                transitionIntent.putExtra("line_profile_url", profile.getPictureUrl().toString());
//                startActivity(transitionIntent);
                Intent transitionIntent = new Intent(this, DisplayClien.class);
                startActivity(transitionIntent);

                break;
            case CANCEL:
                // Login canceled by user
                Log.e("ERROR", "LINE Login Canceled by user!!");
                break;
            default:
                // Login canceled due to other error
                Log.e("ERROR", "Login FAILED!");
                Log.e("ERROR", result.getErrorData().toString());
        }
    }


}
