package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Nickylai
 * @version 1.0.0
 * @description
 * @time 2019/5/11 15:38
 * @see
 */
public class CreateCode {
    public static List<String> genCodes(int length, long num){

        List<String> results=new ArrayList<String>();

        for(int j=0;j<num;j++){
            String val = "";
            Random random = new Random();
            for(int i = 0; i < length; i++) {
                // 输出字母还是数字
                String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
                // 字符串
                if("char".equalsIgnoreCase(charOrNum)) {
                    //取得大写字母还是小写字母
                    int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                    val += (char) (choice + random.nextInt(26));
                }
                // 数字
                else if("num".equalsIgnoreCase(charOrNum))
                {
                    val += String.valueOf(random.nextInt(10));
                }
            }
            val=val.toLowerCase();
            if(results.contains(val)){
                continue;
            }else{
                results.add(val);
            }
        }
        return results;


    }

}
