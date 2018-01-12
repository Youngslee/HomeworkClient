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



import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import line.homework.R;


public class DisplayClien extends Activity {
    private final String targetUri = "http://10.70.25.20:8080";
    private CustomListViewAdapter adapter = new CustomListViewAdapter();
    HashMap<String,String> urlMapper = new HashMap<String,String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_clien);
        Intent intent = getIntent();
        String url="";
        url = intent.getStringExtra("url");
        if(url!=null)
            Log.d("dubugurl",url);
        if(chkPushMsg(url)){
            Intent transitionIntent = new Intent(DisplayClien.this, DetailClien.class);
            transitionIntent.putExtra("url",url);

            startActivity(transitionIntent);
        }
        ListView listview = (ListView) findViewById(R.id.customListView1);

        listview.setAdapter(adapter);

        Thread mThread = new Thread(){
            @Override
            public void run(){
                try{
                    URL url = new URL(targetUri);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    if(urlConnection.getResponseCode()==200){
                        //SUCCESS
                        InputStream inputstream= new BufferedInputStream(urlConnection.getInputStream());
                        Log.d("d","ok");
                        InputStreamReader inputReader = new InputStreamReader(inputstream,"UTF-8");
                        BufferedReader br = new BufferedReader(inputReader);

                        JsonReader jsonReader = new JsonReader(br);
                        jsonReader.beginObject();
                        while (jsonReader.hasNext()) {
                            String key = jsonReader.nextName();
                            String v1 = jsonReader.nextString();

                            adapter.addItem(key, "", "");
                            urlMapper.put(key, v1);
                        }
                    }else{
                        //ERROR
                        Log.d("err","connection 에러");
                    }
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


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                CustomListViewItem item = (CustomListViewItem) parent.getItemAtPosition(position) ;
                String titleStr = item.getTitle() ;
//                String writerStr = item.getWriter() ;
//                String viewsStr = item.getViews();

                Intent transitionIntent = new Intent(DisplayClien.this, DetailClien.class);
                transitionIntent.putExtra("url",urlMapper.get(titleStr));

                startActivity(transitionIntent);
            }
        }) ;

    }
    public boolean chkPushMsg(String url) {
        // 푸시메시지를 받았는지 체크
        return url!=null;
    }
}
