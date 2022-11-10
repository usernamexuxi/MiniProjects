package nam2626.codenation.com.drugwarring;

import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Button btn;
    Button btnSearchList;
    TextView txtResult;
    EditText search;
    InputMethodManager imm;
    private DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);
        btn = findViewById(R.id.btn_search);
        txtResult = findViewById(R.id.result);
        btnSearchList = findViewById(R.id.btn_search_list);
        btnSearchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(new Intent(getApplicationContext(), SearchListActivity.class));
                startActivity(intent);
            }
        });
        search = findViewById(R.id.edit_drug);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(search.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(),"검색할약품명을 입력하세요",Toast.LENGTH_SHORT).show();
                else{
                    Drug drug = new Drug();
                    drug.execute();
                    imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
                }

            }
        });

    }
    public class Drug extends AsyncTask<String, Void, String> {



        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String clientId = "hpOVfNem4MVro1QdBZTMTq%2FMZs%2B8yylSvxNQlqPiEQec%2Bo99WRRbIvrVqLltto5W0TmluoxR7uQHpHFNZ146qg%3D%3D";// 애플리케이션
            BufferedReader br = null;
            try {
                String text = search.getText().toString();
                String apiURL = "http://apis.data.go.kr/1470000/MdcinSdefctInfoService/getMdcinSdefctInfoList?serviceKey="+clientId+"&col_001="+text+"&pageNo=1&startPage=1&numOfRows=3&pageSize=3";
                URL url = new URL(apiURL);
                Log.i("result", "doInBackground: "+url.toString());
                /*
                //내용 읽어오는 부분
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
                String line;
                while((line = br.readLine())!= null){
                    result += line + "\n";
                }*/
                InputStream is= url.openStream();
                XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
                XmlPullParser xpp= factory.newPullParser();
                xpp.setInput(new InputStreamReader(is, "UTF-8"));

                String tag;
                StringBuilder buffer = new StringBuilder();
                xpp.next();
                int eventType= xpp.getEventType();
                String content="";
                DrugVO vo = new DrugVO();
                while( eventType != XmlPullParser.END_DOCUMENT ){
                    switch( eventType ){
                        case XmlPullParser.START_DOCUMENT:
                            break;

                        case XmlPullParser.START_TAG:
                            tag= xpp.getName();
                            Log.i("tag", "doInBackground: "+tag);
                            if(tag.equals("item")) ;
                            else if(tag.equals("COL_001")){
                                xpp.next();
                                if(buffer.toString().indexOf(xpp.getText())==-1){
                                    buffer.append("약품명 : ");
                                    buffer.append(xpp.getText()+"\n");
                                    vo.setName(xpp.getText());
                                }

                            }
                            else if(tag.equals("COL_005")){
                                buffer.append("부작용 : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                content += xpp.getText()+"\n\t";
                            }

                        case XmlPullParser.TEXT:
                            break;

                        case XmlPullParser.END_TAG:
                            tag= xpp.getName();

                            if(tag.equals("item")) buffer.append("\n");
                            break;
                    }

                    eventType= xpp.next();
                }
                vo.setContent(content);
                if(vo.getName() != null)
                    addDrugVO(vo);
                result = buffer.toString().equals("") ? "아직까지는 부작용에 대한 정보 없음" : buffer.toString();
                Log.i("result", "doInBackground: "+result);
            }catch (Exception e){
                Log.i("result", "doInBackground: "+e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            txtResult.setText(s);
        }
    }

    private void addDrugVO(DrugVO vo) {
        dbHelper.add(vo);
    }
}
