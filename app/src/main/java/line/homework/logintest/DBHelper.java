/*
 * 클래스 이름 : DBHelper
 *  - 데이터베이스에 저장된 프로필 데이터를 관리하는 인터페이스
 * 버전 정보
 *
 * 날짜 : 2018.01.06
 *
 */
package line.homework.logintest;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        db.execSQL("CREATE TABLE LOGININFO (id INTEGER PRIMARY KEY AUTOINCREMENT, userId TEXT, userName TEXT, pictureUrl TEXT);");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void insert(String userId, String userName, String pictureUrl) {

        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가

        db.execSQL("INSERT INTO LOGININFO VALUES(null, '" + userId + "', '" + userName +"', '" + pictureUrl  +"');");
        db.close();
    }

    public Cursor select() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM LOGININFO", null);
        return cursor;
    }
}

