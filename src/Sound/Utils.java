/*Author: Chris Brown
* Date: 17/03/2016
* Description: Utils class for generic functions*/
package Sound;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class Utils {

    /*Function taken from http://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places*/
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    //TODO make better use of this function
    public static double mean(double... vals){
        double sum = 0;
        for(double val : vals){
            sum += val;
        }
        return sum / vals.length;
    }
}
