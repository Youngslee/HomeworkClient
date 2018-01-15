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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import line.homework.R;


public class DisplayClien extends Activity {
    private final String targetUri = "http://10.70.39.21:8080/browse";
    private CustomListViewAdapter adapter = new CustomListViewAdapter();
    private HashMap<String,String> urlMapper = new HashMap<String,String>();
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_clien);

        /*
            push 메시지의 data에 대한 세부 내용을 바로 도식하기 위한 처리
         */
        Intent intent = getIntent();
        String url="";
        if(intent.getStringExtra("url")!=null){
            url = intent.getStringExtra("url");
            if(chkPushMsg(url)){
                Intent transitionIntent = new Intent(DisplayClien.this, DetailClien.class);
                transitionIntent.putExtra("url",url);
                startActivity(transitionIntent);
            }
        }

        listview = (ListView) findViewById(R.id.customListView1);
        listview.setAdapter(adapter);


        //        DetailAsyncTask detailAsync = new DetailAsyncTask();
        //        detailAsync.execute();
        /*
            thread -> asyncTask 수정 필요
         */
        Thread mThread = new Thread(){
            @Override
            public void run(){
                try{
                    URL url = new URL(targetUri);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    if(urlConnection.getResponseCode()==200){
                        //SUCCESS
                        InputStream inputstream= new BufferedInputStream(urlConnection.getInputStream());
                        InputStreamReader inputReader = new InputStreamReader(inputstream,"UTF-8");
                        BufferedReader br = new BufferedReader(inputReader);

                        JsonReader jsonReader = new JsonReader(br);
                        jsonReader.beginObject();
                        while (jsonReader.hasNext()) {
                            String key = jsonReader.nextName();
                            String v1 = jsonReader.nextString();
                            // String v2 =jsonReader.nextName();
                            adapter.addItem(key, "", "");
                            urlMapper.put(key, v1);
                        }
                    }else{
                        //ERROR
                        new AlertDialog.Builder(DisplayClien.this).setTitle("ERROR")
                                .setMessage("Connection Error!!")
                                .setNeutralButton("닫기", new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dlg, int something){

                                    }
                                }).show();
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
                Log.d("debugItem",parent+":"+v+":"+position+":"+id);
                // get item
                CustomListViewItem item = (CustomListViewItem) parent.getItemAtPosition(position) ;
                String titleStr = item.getTitle() ;
                /*
                    작성자와 작성 시간 추가 필요
                 */
//                String writerStr = item.getWriter() ;
//                String viewsStr = item.getViews();

                Intent transitionIntent = new Intent(DisplayClien.this, DetailClien.class);
                transitionIntent.putExtra("url",urlMapper.get(titleStr));

                startActivity(transitionIntent);
            }
        }) ;

    }
    /*
        AsyncTask로 수정 중
     */
//    class DetailAsyncTask extends AsyncTask<Void, Integer, Void> {
//        // doInBackground 메소드가 실행되기 전에 실행되는 메소드
//        @Override
//        protected void onPreExecute () {
//            super.onPreExecute();
//            try{
//                listview = (ListView) findViewById(R.id.customListView1);
//                listview.setAdapter(adapter);
//                URL url = new URL(targetUri);
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                if(urlConnection.getResponseCode()==200){
//                    //SUCCESS
//                    InputStream inputstream= new BufferedInputStream(urlConnection.getInputStream());
//                    InputStreamReader inputReader = new InputStreamReader(inputstream,"UTF-8");
//                    BufferedReader br = new BufferedReader(inputReader);
//
//                    JsonReader jsonReader = new JsonReader(br);
//                    jsonReader.beginObject();
//                    while (jsonReader.hasNext()) {
//                        String key = jsonReader.nextName();
//                        String v1 = jsonReader.nextString();
//                        Log.d("key",key+":"+v1);
//                        adapter.addItem(key, "", "");
//                        urlMapper.put(key, v1);
//                    }
//                }else{
//                    //ERROR
//                    Log.d("err","connection 에러");
//                }
//            }  catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        // 실제 비즈니스 로직이 처리될 메소드(Thread 부분이라고 생각하면 됨)
//        @Override
//        protected Void doInBackground (Void...params){
//
//            return null;
//        }
//
//        // doInBackground에서 넘긴 values 값을 받아서 처리하는 부분
//        @Override
//        protected void onProgressUpdate (Integer...values){
//        }
//
//        // 모든 작업이 끝난 후 처리되는 메소드
//        @Override
//        protected void onPostExecute (Void result){
//            super.onPostExecute(result);
//            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView parent, View v, int position, long id) {
//                    Log.d("debugItem",parent+":"+v+":"+position+":"+id);
//                    // get item
//                    CustomListViewItem item = (CustomListViewItem) parent.getItemAtPosition(position) ;
//                    String titleStr = item.getTitle() ;
////                String writerStr = item.getWriter() ;
////                String viewsStr = item.getViews();
//
//                    Intent transitionIntent = new Intent(DisplayClien.this, DetailClien.class);
//                    transitionIntent.putExtra("url",urlMapper.get(titleStr));
//
//                    startActivity(transitionIntent);
//                }
//            }) ;
//        }
//    }
    public boolean chkPushMsg(String url) {
        // 푸시메시지를 받았는지 체크
        return url!=null;
    }
}
