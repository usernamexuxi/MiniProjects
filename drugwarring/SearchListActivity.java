package nam2626.codenation.com.drugwarring;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class SearchListActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private LinearLayout linearLayout;
    private List<DrugVO> list;
    private Button btnAllRemove;
    private Button btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        linearLayout = findViewById(R.id.main_scroll_linear);
        btnAllRemove = findViewById(R.id.btn_remove);
        btnAllRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.delete();
                finish();
            }
        });
        btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        dbHelper = new DBHelper(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        linearLayout.removeAllViewsInLayout();
        list = dbHelper.selectAllDrugVO();
        for(int i=0;i<list.size();i++){
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            String content = "";
            content = "약품명 : " + list.get(i).getName() +System.getProperty ("line.separator")+ System.getProperty ("line.separator")+"부작용" +System.getProperty ("line.separator") +System.getProperty ("line.separator") +"\t\t"+list.get(i).getContent();
            TextView txt = new TextView(this);
            txt.setText(content);
            txt.setTextSize(15);
            txt.setPadding(30,0,0,0);

            TextView id = new TextView(this);
            String mId = String.valueOf(list.get(i).getId());
            id.setText(mId);
            id.setTextSize(30);
            id.setVisibility(View.INVISIBLE);
            row.addView(id);
            row.addView(txt);
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectDrugVO(view);
                }
            });
            linearLayout.addView(row);
            Log.i("LIST", "onCreate: "+list.get(i));
        }
    }

    private void selectDrugVO(View view) {
        LinearLayout linear = (LinearLayout) view;
        TextView txt = (TextView) linear.getChildAt(0);
        Log.i("TEST", "updateselectDrugVO: "+txt.getText());
        int no = Integer.parseInt(txt.getText().toString());
        DrugVO vo = getDrugVO(no);
        if(vo == null) return;

        Intent intent = new Intent(new Intent(getApplicationContext(), DrugSelectActivity.class));
        intent.putExtra("name",vo.getName());
        intent.putExtra("content",vo.getContent());
        intent.putExtra("id",vo.getId());
        startActivity(intent);
    }

    private DrugVO getDrugVO(int no) {
        for(int i=0;i<list.size();i++){
            if(list.get(i).getId() == no)
                return list.get(i);
        }
        return null;
    }
}
