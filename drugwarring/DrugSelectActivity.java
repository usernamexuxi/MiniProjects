package nam2626.codenation.com.drugwarring;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DrugSelectActivity extends AppCompatActivity {
    private TextView txtName;
    private TextView txtContent;
    private DrugVO vo;
    private DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_select);

        dbHelper = new DBHelper(getApplicationContext());
        txtName = findViewById(R.id.select_txt_name);
        txtContent = findViewById(R.id.select_txt_content);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String content = intent.getStringExtra("content");
        int id = intent.getIntExtra("id",-1);

        vo = new DrugVO(id,name,content);
        txtName.setText(vo.getName());
        txtContent.setText(vo.getContent());

    }

    public void delete(View view){
        dbHelper.delete(vo.getId());
        Toast.makeText(getApplicationContext(),"약품 조회값 삭제 완료",Toast.LENGTH_SHORT).show();
        finish();
    }

    public void cancel(View view){
        finish();
    }
}
