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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import line.homework.R;



public class DetailClien extends Activity {
    private final String keywordURl = "http://10.70.39.21:8080/keywordChange";
    private TextView detailTitle, detailnickname, detailcontents;
    private Document doc;
    private String[] results = {"", "", ""};
    private ImageView nickPicture;
    Bitmap bitmap_picture = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_clien);
        CheckBox star = (CheckBox) findViewById(R.id.btn_star);
        star.setOnCheckedChangeListener(mStarCheckedChanceChangeListener);
        detailTitle = (TextView) findViewById(R.id.detailtitle);
        detailnickname = (TextView) findViewById(R.id.detailnickname);
        detailcontents = (TextView) findViewById(R.id.detailcontents);
        nickPicture = (ImageView) findViewById(R.id.nickImage);
        Intent intent = getIntent();
        final String targetUrl = intent.getStringExtra("url");

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
                    e.printStackTrace();
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
        nickPicture.setImageBitmap(bitmap_picture);
    }

    private CompoundButton.OnCheckedChangeListener mStarCheckedChanceChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // TODO Cyril: Not implemented yet!
            final EditText etEdit = new EditText(DetailClien.this);
            AlertDialog.Builder dialog = new AlertDialog.Builder(DetailClien.this);
            dialog.setTitle("키워드 입력");
            dialog.setView(etEdit);
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    final String inputValue = etEdit.getText().toString();
                    /*
                        thread -> asyncTask 수정 필요
                    */
                    Thread mThread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                doc = Jsoup.connect(keywordURl+"?word="+inputValue).get();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    mThread.start();
                    try {
                        mThread.join();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            dialog.show();
        }
    };
}


