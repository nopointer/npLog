package demo.nopointer.npLog;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CodeUtils {
    public static String startDouble() {
        StringBuffer stringBuffer = new StringBuffer();
        List<String> oneList = new ArrayList<>();
        for (int i = 1; i <= 33; i++) {
            oneList.add(i + "");
        }
        for (int i = 0; i < 6; i++) {
            Random rand = new Random();
            int randomIndex = rand.nextInt(oneList.size());
            stringBuffer.append(oneList.get(randomIndex)).append(" , ");
            oneList.remove(randomIndex);
        }
        Random rand = new Random();
        stringBuffer.append(rand.nextInt(16) + 1);
        return stringBuffer.toString();
    }


    public static String startBig() {
        StringBuffer stringBuffer = new StringBuffer();
        List<String> oneList = new ArrayList<>();
        for (int i = 1; i <= 35; i++) {
            oneList.add(i + "");
        }
        for (int i = 0; i < 5; i++) {
            Random rand = new Random();
            int randomIndex = rand.nextInt(oneList.size());
            stringBuffer.append(oneList.get(randomIndex)).append(" , ");
            oneList.remove(randomIndex);
        }

        oneList = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            oneList.add(i + "");
        }
        for (int i = 0; i < 2; i++) {
            Random rand = new Random();
            int randomIndex = rand.nextInt(oneList.size());
            stringBuffer.append(oneList.get(randomIndex));
            if (i == 0) {
                stringBuffer.append(" , ");
            }
            oneList.remove(randomIndex);
        }
        return stringBuffer.toString();
    }


}
