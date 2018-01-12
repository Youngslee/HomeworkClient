/*
 * 클래스 이름 : DetailClien
 *  - 선택된 아이템에 대한 세부사항 출력
 * 버전 정보
 *
 * 날짜 : 2018.01.11
 *
 */
package line.homework.clien;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import line.homework.R;



public class DetailClien extends Activity {
    private TextView detailTitle;
    private Document doc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_clien);
        detailTitle = (TextView) findViewById(R.id.detailtitle);
        Intent intent = getIntent();
        final String targetUrl = intent.getStringExtra("url");

        Thread mThread = new Thread(){
            @Override
            public void run(){
                try{
                    doc = Jsoup.connect(targetUrl).get();
                }  catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        mThread.start();
        try{
            mThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        detailTitle.setText(doc.html());
    }
}
