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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.iid.FirebaseInstanceId;
import com.linecorp.linesdk.LineProfile;
import com.linecorp.linesdk.auth.LineLoginApi;
import com.linecorp.linesdk.auth.LineLoginResult;

import line.homework.R;
import line.homework.clien.DetailClien;
import line.homework.clien.DisplayClien;

public class LoginActivity extends Activity {
    private static final int REQUEST_CODE = 1;
    private DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseInstanceId.getInstance().getToken();

        /*
            push 알림을 받고, 해당 키워드의 url을 받기
         */
        String url="";
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if( bundle != null){
            if(bundle.getString("url") != null && !bundle.getString("url").equalsIgnoreCase("")) {
                url = bundle.getString("url");
            }
        }

        // DBHelper class : 라인 프로필 관련 데이터베이스 관리 클래스
        dbHelper= new DBHelper(this.getApplicationContext(), "LoginInfo.db", null, 1);

        if(chkLogin()){
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

            if(chkPushMsg(url)){
                /*
                    push 알림으로 어플리케이션을 구동했는지 검사,
                    알림에서 받은 url을 전달해 이후에 해당 키워드에 대한 상세내용 출력
                 */
                Intent transitionIntent = new Intent(this, DisplayClien.class);
                transitionIntent.putExtra("url",url);
                startActivity(transitionIntent);
            }else {
                Intent transitionIntent = new Intent(this, DisplayClien.class);
                startActivity(transitionIntent);
            }

//            Intent transitionIntent = new Intent(this, DisplayClien.class);
//            startActivity(transitionIntent);
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
    public boolean chkLogin() {
        /*
            이 전에 인증을 수행했는지 검사.
         */
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = dbHelper.select();
        String[] dbInfo = new String[3];

        while(cursor.moveToNext()){
            dbInfo[0]=cursor.getString(1); // userId
            dbInfo[1]=cursor.getString(2); // userName
            dbInfo[2]=cursor.getString(3); // pictureUrl
        }
        return dbInfo[0]!=null;
    }
    public boolean chkPushMsg(String url) {
        // 푸시메시지를 받았는지 체크
        return url.length()>0;
    }


}
