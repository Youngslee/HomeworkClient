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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import line.homework.R;



public class DetailClien extends Activity {
    private final String keywordURl = "http://10.70.38.128:8080/keywordChange";
    private TextView detailTitle, detailnickname, detailcontents;
    private Document doc;
    private String[] results = {"", "", ""};
    private ImageView contentImg;
    Bitmap bitmap_picture = null;
    private Button keywordBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_clien);
        CheckBox star = (CheckBox) findViewById(R.id.btn_star); // 키워드 추가시 클릭
        star.setOnCheckedChangeListener(mStarCheckedChanceChangeListener);
        detailTitle = (TextView) findViewById(R.id.detailtitle); // 타이틀
        detailnickname = (TextView) findViewById(R.id.detailnickname); // 닉네임
        detailcontents = (TextView) findViewById(R.id.detailcontents); // 세부 내용
        contentImg = (ImageView) findViewById(R.id.contentImg);
        Intent intent = getIntent();
        final String targetUrl = intent.getStringExtra("url");
        keywordBtn = (Button)findViewById(R.id.keywordbutton);

        // 버튼에 이벤트 지정
        keywordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText etEdit = new EditText(DetailClien.this);

                AlertDialog.Builder dialog2 = new AlertDialog.Builder(DetailClien.this);
                dialog2.setTitle("키워드 입력");
                dialog2.setView(etEdit);
                dialog2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final String inputValue = etEdit.getText().toString();
//                    KeywordAsyncTask keyAsync = new KeywordAsyncTask(inputValue);
//                    keyAsync.execute();
                    /*
                        thread -> asyncTask 수정 필요
                    */
                        Thread mThread = new Thread() {
                            @Override
                            public void run() {
                                try {
                                    String token = FirebaseInstanceId.getInstance().getToken();
                                    Log.d("dtoken", token + "");
                                    int tokenSize = token.length();
                                    String[] tokens = {"", "", "", ""};
                                    tokens[0] = token.substring(0, 38);
                                    tokens[1] = token.substring(38, 76);
                                    tokens[2] = token.substring(76, 114);
                                    tokens[3] = token.substring(114, 152);

                                    Log.d("dTokenSize", tokenSize + "");
                                    Log.d("debugToken&keyword", token + ":" + inputValue);
                                    doc = Jsoup.connect(keywordURl + "?tokenId1=" + tokens[0] + "&tokenId2=" + tokens[1]
                                            + "&tokenId3=" + tokens[2] + "&tokenId4=" + tokens[3] + "&word=" + inputValue).get();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        mThread.start();
                        try {
                            mThread.join();

                        } catch (InterruptedException e) {
                            //ERROR
                            new AlertDialog.Builder(DetailClien.this).setTitle("ERROR")
                                    .setMessage("Connection Error!!")
                                    .setNeutralButton("닫기", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dlg, int something) {

                                        }
                                    }).show();
                        }
                    }
                });
                dialog2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog2.show();
            }
        });

        /*
            thread -> asyncTask 수정 필요
         */

        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    doc = Jsoup.connect(targetUrl).get();

                    Elements title = doc.getElementsByClass("post_subject");
                    for (Element e : title) {
                        results[0] += "Title : " + e.text();
                    }
                    Elements nickname = doc.getElementsByClass("nickname");
                    for (Element e : nickname) {
                        results[1] += "NickName : " + e.text();
                    }
                    Elements contents = doc.getElementsByClass("post_article fr-view");
                    for (Element e : contents) {
                        results[2] += "Contents : " + e.text();
                        Elements img = e.getElementsByTag("img");
                        for (Element i : img) {
                            URL url = new URL(i.attr("src"));
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setDoInput(true);
                            conn.connect();
                            InputStream is = conn.getInputStream();
                            bitmap_picture = BitmapFactory.decodeStream(is);

                        }
                    }
                } catch (IOException e) {
                    //ERROR
                    new AlertDialog.Builder(DetailClien.this).setTitle("ERROR")
                            .setMessage("Connection Error!!")
                            .setNeutralButton("닫기", new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dlg, int something){

                                }
                            }).show();
                }
            }
        };
        mThread.start();
        try {
            mThread.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        detailTitle.setText(results[0]);
        detailnickname.setText(results[1]);
        detailcontents.setText(results[2]);
        contentImg.setImageBitmap(bitmap_picture);
    }
    private CompoundButton.OnCheckedChangeListener mStarCheckedChanceChangeListener = new CompoundButton.OnCheckedChangeListener() {
        /*
            키워드 저장을 위한 버튼에 대한 처리
            키워드를 입력하고 OK를 클릭하면 키워드와 관련된 내용들이 push메시지로 서버로부터 receive
         */
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // TODO Cyril: Not implemented yet!
            // 서버에서 받아올 아이템
            final CharSequence[] items = new CharSequence[]{"화폐", "블록체인"};
            AlertDialog.Builder dialog = new AlertDialog.Builder(DetailClien.this);
            dialog.setTitle("키워드 이벤트");
            dialog.setItems(items, new DialogInterface.OnClickListener() {
                // 리스트 선택 시 이벤트
                public void onClick(DialogInterface dialog, int which) {

                }
            });
//            dialog.setPositiveButton("추가",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            Toast.makeText(DetailClien.this, "!!!", Toast.LENGTH_SHORT).show();
//                            final EditText etEdit = new EditText(DetailClien.this);
//                            AlertDialog.Builder dialog2 = new AlertDialog.Builder(DetailClien.this);
//                            dialog2.setTitle("키워드 입력");
//                            dialog2.setView(etEdit);
//                            dialog2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    final String inputValue = etEdit.getText().toString();
////                    KeywordAsyncTask keyAsync = new KeywordAsyncTask(inputValue);
////                    keyAsync.execute();
//                    /*
//                        thread -> asyncTask 수정 필요
//                    */
//                                    Thread mThread = new Thread() {
//                                        @Override
//                                        public void run() {
//                                            try {
//                                                String token = FirebaseInstanceId.getInstance().getToken();
//                                                int tokenSize = token.length();
//                                                String[] tokens = {"","","",""};
//                                                tokens[0]=token.substring(0,38);
//                                                tokens[1]=token.substring(38,76);
//                                                tokens[2]=token.substring(76,114);
//                                                tokens[3]=token.substring(114,152);
//
//                                                Log.d("dTokenSize",tokenSize+"");
//                                                Log.d("debugToken&keyword",token+":"+inputValue);
//                                                doc = Jsoup.connect(keywordURl+"?tokenId="+tokens+"&word="+inputValue).get();
//                                            } catch (IOException e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                    };
//                                    mThread.start();
//                                    try {
//                                        mThread.join();
//
//                                    } catch (InterruptedException e) {
//                                        //ERROR
//                                        new AlertDialog.Builder(DetailClien.this).setTitle("ERROR")
//                                                .setMessage("Connection Error!!")
//                                                .setNeutralButton("닫기", new DialogInterface.OnClickListener(){
//                                                    public void onClick(DialogInterface dlg, int something){
//
//                                                    }
//                                                }).show();
//                                    }
//                                }
//                            });
//                            dialog2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                }
//                            });
//                        }
//                    });

            dialog.show();
//            final EditText etEdit = new EditText(DetailClien.this);
//            AlertDialog.Builder dialog = new AlertDialog.Builder(DetailClien.this);
//            dialog.setTitle("키워드 입력");
//            dialog.setView(etEdit);
//            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    final String inputValue = etEdit.getText().toString();
////                    KeywordAsyncTask keyAsync = new KeywordAsyncTask(inputValue);
////                    keyAsync.execute();
//                    /*
//                        thread -> asyncTask 수정 필요
//                    */
//                    Thread mThread = new Thread() {
//                        @Override
//                        public void run() {
//                            try {
//                                String token = FirebaseInstanceId.getInstance().getToken();
//                                int tokenSize = token.length();
//                                String[] tokens = {"","","",""};
//                                tokens[0]=token.substring(0,41);
//                                tokens[1]=token.substring(41,82);
//                                tokens[2]=token.substring(82,123);
//                                tokens[3]=token.substring(123,152);
//
//                                Log.d("dTokenSize",tokenSize+"");
//                                Log.d("debugToken&keyword",token+":"+inputValue);
//                                doc = Jsoup.connect(keywordURl+"?tokenId="+tokens+"&word="+inputValue).get();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    };
//                    mThread.start();
//                    try {
//                        mThread.join();
//
//                    } catch (InterruptedException e) {
//                        //ERROR
//                        new AlertDialog.Builder(DetailClien.this).setTitle("ERROR")
//                                .setMessage("Connection Error!!")
//                                .setNeutralButton("닫기", new DialogInterface.OnClickListener(){
//                                    public void onClick(DialogInterface dlg, int something){
//
//                                    }
//                                }).show();
//                    }
//                }
//            });
//            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.cancel();
//                }
//            });
//            dialog.show();
        }
    };
//    class KeywordAsyncTask extends AsyncTask<Void, Integer, Void> {
//        // doInBackground 메소드가 실행되기 전에 실행되는 메소드
//        String inputValue="";
//        KeywordAsyncTask(String inputValue){
//            this.inputValue=inputValue;
//        }
//        @Override
//        protected void onPreExecute () {
//            super.onPreExecute();
//        }
//
//        // 실제 비즈니스 로직이 처리될 메소드(Thread 부분이라고 생각하면 됨)
//        @Override
//        protected Void doInBackground (Void...params){
//            try {
//                String token = FirebaseInstanceId.getInstance().getToken();
//                Log.d("debugToken&keyword",token+":"+inputValue);
//                doc = Jsoup.connect(keywordURl+"?tokenId="+token+"&word="+inputValue).get();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
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
//        }
//    }
}



