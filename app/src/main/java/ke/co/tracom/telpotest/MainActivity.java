package ke.co.tracom.telpotest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEditAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.emv_ic_process);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back);
        toolbar.setNavigationOnClickListener(
                v -> onBackPressed()
        );
        mEditAmount = findViewById(R.id.edit_amount);
        TextView mTvShowInfo = findViewById(R.id.tv_info);
        Button mBtnOperate = findViewById(R.id.mb_ok);

        findViewById(R.id.mb_ok).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        final int id = view.getId();
        switch (id){
            case R.id.mb_ok:
                Log.d(Constant.TAG,"*********************************************************");
                Log.d(Constant.TAG, "***************** START EMV PROCESS *********************");
                Log.d(Constant.TAG, "*********************************************************");
                String amount = mEditAmount.getText().toString();
                EMVAction emvAction = new EMVAction();
                emvAction.init(this);
                EMVAction.startEmv();

        }
    }
}