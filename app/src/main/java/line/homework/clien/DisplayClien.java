/*
 * 클래스 이름 : DisplayClien
 *  - 크롤링한 데이터를 리스트 뷰로 출력
 * 버전 정보
 *
 * 날짜 : 2018.01.11
 *
 */
package line.homework.clien;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import line.homework.R;


public class DisplayClien extends Activity {
    private final String targetUri = "http://10.70.25.20:8080";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_clien);

        ListView listview = (ListView) findViewById(R.id.customListView1);
        CustomListViewAdapter adapter = new CustomListViewAdapter();


        listview.setAdapter(adapter);

//        Thread mThread = new Thread(){
//            @Override
//            public void run(){
//                try{
//                    URL url = new URL(targetUri);
//                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                    if(urlConnection.getResponseCode()==200){
//                        //SUCCESS
//                        InputStream inputstream= new BufferedInputStream(urlConnection.getInputStream());
//                        InputStreamReader inputReader = new InputStreamReader(inputstream,"UTF-8");
//                        JsonReader jsonReader = new JsonReader(inputReader);
//
//                    }else{
//                        //ERROR
//                        Log.d("err","connection 에러");
//                    }
//                }  catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        mThread.start();
//        try{
//            mThread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        adapter.addItem("라이프체인1","이영섭","1");
        adapter.addItem("라이프체인2","정찬구","2");
        adapter.addItem("라이프체인3","고태건","3");

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                CustomListViewItem item = (CustomListViewItem) parent.getItemAtPosition(position) ;
                String titleStr = item.getTitle() ;
                String writerStr = item.getWriter() ;
                String viewsStr = item.getViews();

                Intent transitionIntent = new Intent(DisplayClien.this, DetailClien.class);
                transitionIntent.putExtra("url","https://www.clien.net/service/board/park/11649394?po=0&od=T31&sk=&sv=&category=&groupCd=&articlePeriod=default&pt=0");

                startActivity(transitionIntent);
            }
        }) ;

    }
}
