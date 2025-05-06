package demo.nopointer.npLog;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import npLog.nopointer.core.NpLog;

public class DoubleUtils {


    private static ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    public static void startDouble(int count, Callback callback) {
        mExecutor.execute(() -> {
            HashSet<String> createList = new HashSet<>();
            for (int i = 0; i < count; i++) {
                List<Integer> codeList = create();
                String codeJson = new Gson().toJson(codeList);
                createList.add(codeJson);
                if (callback != null) {
                    callback.onProgress((i + 1.0f) / count);
                }
            }
            NpLog.log("count = " + count + " , createList.size() = " + createList.size());

            boolean isNotAdd = true;
            while (isNotAdd) {
                List<Integer> codeList = create();
                String codeJson = new Gson().toJson(codeList);
                if (!createList.contains(codeJson)) {
                    isNotAdd = false;
                    if (callback != null) {
                        callback.onGetCode(codeList);
                    }
                } else {
                    NpLog.log("包含在里面了，继续循环");
                }
            }
        });
    }


    private static List<Integer> create() {
        List<Integer> result = new ArrayList<>();
        result.addAll(CodeUtils.create(33, 6));//红
        result.addAll(CodeUtils.create(16, 1));//篮
        return result;
    }


    public static String formatHtml(List<Integer> codeList) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<font color='#FF0000'>");
        for (int i = 0; i < codeList.size() - 1; i++) {
            stringBuffer.append(codeList.get(i)).append(" ");
        }
        stringBuffer.append("</font>");
        stringBuffer.append("<font color='#0000FF'>");
        stringBuffer.append(codeList.get(codeList.size() - 1));
        stringBuffer.append("</font>");
        return stringBuffer.toString();
    }

    public interface Callback {
        void onGetCode(List<Integer> code);

        void onProgress(float progress);
    }
}
