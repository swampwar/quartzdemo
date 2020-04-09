package wind.yang.quartzdemo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class QuartzStringUtils {
    public static String dateToString(Date src){
        String rslt = "";
        if(src == null){
            return rslt;
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            rslt = sdf.format(src);
        }
        return rslt;
    }
}
