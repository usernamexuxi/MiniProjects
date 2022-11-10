package nam2626.codenation.com.drugwarring;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Drug";
    public DBHelper(Context context){
        super(context,DB_NAME,null ,2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name ='ADDRESS'" , null);
        cursor.moveToFirst();

        if(cursor.getCount()>0){
            Log.i("DB_TABLE", "onCreate: 해당 테이블이 이미 존재");
        }else{
            Log.i("DB_TABLE", "onCreate: 해당 테이블이 생성");
            String sql = "CREATE TABLE Drug(ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,CONTENT TEXT)";
            sqLiteDatabase.execSQL(sql);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql =
                "DROP TABLE IF EXISTS ADDRESS";
        sqLiteDatabase.execSQL(sql);

        onCreate(sqLiteDatabase);

    }
    public void add(DrugVO vo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("NAME", vo.getName());
        values.put("CONTENT", vo.getContent());

        db.insert("Drug", null, values);
        db.close();
    }
    public void delete() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("Drug","",null);
        db.close();
    }
    public void delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("Drug","id = ?",new String[]{Integer.toString(id)});
        db.close();
    }

    public List<DrugVO> selectAllDrugVO(){
        ArrayList<DrugVO> list = new ArrayList<DrugVO>();
        String sql = "SELECT * FROM Drug";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {

            do {
                list.add(new DrugVO(Integer.parseInt(cursor.getString(0)), cursor.getString(1),cursor.getString(2)));
            } while (cursor.moveToNext());
        }

        return list;
    }

    public List<DrugVO> selectDrugVO(String str) {
        ArrayList<DrugVO> list = new ArrayList<DrugVO>();
        String sql = "SELECT * FROM Drug WHERE NAME LIKE '%"+str+"%'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {

            do {
                list.add(new DrugVO(Integer.parseInt(cursor.getString(0)), cursor.getString(1),cursor.getString(2)));
            } while (cursor.moveToNext());
        }

        return list;
    }
}
