package proto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
   
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = br.readLine();
        int N = Integer.parseInt(str.split(" ")[0])/10; // 此处总钱数除以10，后续单价同样除以10
        int m = Integer.parseInt(str.split(" ")[1]);
        int[] v= new int[m+1];
        int[] p= new int[m+1];
        int[] q= new int[m+1];
        boolean[] flags = new boolean[m+1];
        int[][] res = new int[m+1][N+1];
        for (int i = 1; i <= m; i++) {
            String[] strings = br.readLine().split(" ");
            v[i] = (Integer.parseInt(strings[0]))/10;  //价格都是10的倍数，可以减少循环次数
            p[i] = Integer.parseInt(strings[1]) * v[i];    // Integer.parseInt(strings[1]):重要度，只可能为12345  p[i]表示所求总和的单个元素
            q[i] = Integer.parseInt(strings[2]);      // 标识主件还是附件，p=0为主件，p>0为附件，对应主件编号，此处对应索引i
        }
            
        // 动态规划思想
        for (int i = 1; i <= m; i++) {
            for(int j = 1; j<=N; j++){ // j表示可支配的钱
                if(q[i] == 0) { // 主件
                    if(v[i] <= j){ // 当前物品价格有足够的钱支付
                        res[i][j] = Math.max(res[i-1][j], res[i-1][j-v[i]] + p[i]);
                    }
                }else{ // 附件， 此时q[i]表示所对应的主件的编号
                    if(v[i] + v[q[i]] <= j){  // 附件的价值加上主件的价值不少于可支配的钱，即可以一起购买附件
                        res[i][j] = Math.max(res[i-1][j], res[i-1][j-v[i]] + p[i]);
                    }
                }
            }
        }
        System.out.println(res[m][N] * 10);
    }
}