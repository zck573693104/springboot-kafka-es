
import java.util.HashMap;
import java.util.Map;

public class MyTest {

    public static void main(String[] args) {

        int[] a = new int[]{256, 257, 258, 269, 260, 261, 262, 263, 264, 265};
        int c = 3, b = 4;

        Map<String, Integer> countMap = new HashMap<>();
        for (int i = 0; i < a.length; i++) {
            String binaryA = Integer.toHexString(a[i]);
            String hexA = String.format("%08x", Integer.valueOf(binaryA));
            String[] splitHexA = split(hexA);

            int intA = 0;
            for (int j = 0; j < splitHexA.length; j++) {
                intA += Integer.parseInt(splitHexA[j], 16);
            }

            int afterNum = intA % b;

            if (afterNum < c) {
                Integer num = countMap.get(String.valueOf(afterNum));
                if (null == num) {
                    num = 1;
                } else {
                    num = num + 1;
                }
                countMap.put(String.valueOf(afterNum), num);
            }
        }

        int maxNum = 0;
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            if (maxNum < entry.getValue()) {
                maxNum = entry.getValue();
            }
        }

        System.out.println(maxNum);
    }

    private static String[] split(String fourByteNum) {

        String[] byteData = new String[4];
        int i = 0;
        while (fourByteNum.length() >= 2) {
            byteData[i] = fourByteNum.substring(0, 2);
            fourByteNum = fourByteNum.substring(2);
            i++;
        }

        return byteData;
    }
}
