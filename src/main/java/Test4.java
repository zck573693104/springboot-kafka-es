import java.util.Scanner;

public class Test4 {
    public static void main(String []args){

        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()){
            String value = Integer.toBinaryString(Integer.parseInt(sc.nextLine()));

            int count = 0;
            for (int i =0 ;i < value.length();i++){
                 if (value.charAt(i) == '1'){
                     count++;
                 }

            }
            System.out.println(count);

        }
    }
}
