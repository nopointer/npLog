package demo.nopointer.npLog;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import npLog.nopointer.core.NpLog;

public class CodeActivity extends Activity {

    String date = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        tvDoubleCode = findViewById(R.id.tvDoubleCode);
        tvBigCode = findViewById(R.id.tvBigCode);

        date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        initDouble();
        initBig();
    }

    private TextView tvDoubleCode;
    private TextView tvBigCode;

    private void initDouble() {
        MMKV kv = MMKV.defaultMMKV();
        Button btnCodeDouble = findViewById(R.id.btnCodeDouble);
        String key = date + "_Double";
        String codeDouble = kv.decodeString(key, null);
        RadioGroup radioGroup = findViewById(R.id.doubleGp);

        if (!TextUtils.isEmpty(codeDouble)) {
            tvDoubleCode.setText(Html.fromHtml(codeDouble));
            btnCodeDouble.setText("完成");
            btnCodeDouble.setEnabled(false);
        } else {
            btnCodeDouble.setOnClickListener(v -> {
                int count = 1000;
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.double1:
                        count = 100;
                        break;
                    case R.id.double2:
                        count = 1000;
                        break;
                    case R.id.double3:
                        count = 10000;
                        break;
                    case R.id.double4:
                        count = 100000;
                        break;
                }
                DoubleUtils.startDouble(count, new DoubleUtils.Callback() {
                    @Override
                    public void onGetCode(List<Integer> codeList) {
                        String code = DoubleUtils.formatHtml(codeList);
                        runOnUiThread(() -> {
                            tvDoubleCode.setText(Html.fromHtml(code));
                            btnCodeDouble.setText("完成");
                            kv.encode(key, code);
                            btnCodeDouble.setEnabled(false);
                        });
                    }

                    @Override
                    public void onProgress(float progress) {
                        tvDoubleCode.setText(String.format("%d%%", (int) (progress * 100)));
                    }
                });
            });
        }
    }

    private void initBig() {
        MMKV kv = MMKV.defaultMMKV();
        Button btnCodeBig = findViewById(R.id.btnCodeBig);
        String key = date + "_Big";
        String codeBig = kv.decodeString(key, null);

        RadioGroup bigGp = findViewById(R.id.bigGp);

        if (!TextUtils.isEmpty(codeBig)) {
            NpLog.log("codeBig = " + codeBig);
            btnCodeBig.setText("完成");
            tvBigCode.setText(Html.fromHtml(codeBig));
            btnCodeBig.setEnabled(false);
        } else {
            btnCodeBig.setOnClickListener(v -> {
                int count = 1000;
                switch (bigGp.getCheckedRadioButtonId()) {
                    case R.id.big1:
                        count = 100;
                        break;
                    case R.id.big2:
                        count = 1000;
                        break;
                    case R.id.big3:
                        count = 10000;
                        break;
                    case R.id.big4:
                        count = 100000;
                        break;
                }
                BigUtils.startBig(count, new BigUtils.Callback() {
                    @Override
                    public void onGetCode(List<Integer> codeList) {
                        String code = BigUtils.formatHtml(codeList);
                        runOnUiThread(() -> {
                            tvBigCode.setText(Html.fromHtml(code));
                            btnCodeBig.setText("完成");
                            kv.encode(key, code);
                            btnCodeBig.setEnabled(false);
                        });
                    }

                    @Override
                    public void onProgress(float progress) {
                        tvBigCode.setText(String.format("%d%%", (int) (progress * 100)));
                    }
                });
            });
        }
    }
}
