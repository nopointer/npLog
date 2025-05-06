package demo.nopointer.npLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class CodeUtils {

    public static List<Integer> create(int maxNumber, int needCount) {
        List<Integer> oneList = new ArrayList<>();
        for (int i = 1; i <= maxNumber; i++) {
            oneList.add(i);
        }

        List<Integer> resultList = new ArrayList<>();

        for (int i = 0; i < needCount; i++) {
            Random rand = new Random();
            int randomIndex = rand.nextInt(oneList.size());
            resultList.add(oneList.get(randomIndex));
            oneList.remove(randomIndex);
        }

        Collections.sort(resultList, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        return resultList;
    }


}
