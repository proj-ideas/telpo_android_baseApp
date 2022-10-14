package ke.co.tracom.telpotest;

import static ke.co.tracom.telpotest.Constant.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.telpo.emv.EmvParam;
import com.telpo.emv.EmvService;
import com.telpo.emv.EmvTLV;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.ISOUtil;
import org.jpos.tlv.TLVList;

import java.io.IOException;

import ke.co.tracom.telpotest.card.Channel;
import ke.co.tracom.telpotest.card.TestIsoPackager;
import ke.co.tracom.telpotest.card.TransactionData;
import ke.co.tracom.telpotest.config.Country;
import ke.co.tracom.telpotest.config.TelpoConfig;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEditAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
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
//                runTransaction();
                EMVAction emvAction = new EMVAction();
                emvAction.init(this);
                emvAction.startEmv(this);

        }
    }


}