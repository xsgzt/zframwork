package com.ztyb.framework.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MathUtils {


    /**
     * 保留小数点后两位
     */
//    public static double keep2number(double d) {
//        BigDecimal bg = new BigDecimal(d);
//        double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//        return f1;
//    }
    public static String keep2number(double v) {
        if (2 < 0) {
            throw new IllegalArgumentException(
                    "The   scale   must   be   a   positive   integer   or   zero");
        }
        if (2 == 0) {
            return new DecimalFormat("0").format(v);
        }
        String formatStr = "0.";
        for (int i = 0; i < 2; i++) {
            formatStr = formatStr + "0";
        }
        return new DecimalFormat(formatStr).format(v);
    }


    /**
     * app 金额显示 规则  小数点不为零展示   小数点为零截掉   带单位
     */

    public static String transFormStr(double v) {
        int iv = (int) v;
        if (iv == v) {
            //整数  不保留
            if (iv >= 10000) {
                return iv * 1.0 / 10000 + "万";
            }
            return iv + "元";
        } else {
            //小数 保留下
            if (iv >= 10000) {
                return iv * 1.0 / 10000 + "万";
            }
            return v + "元";
        }


    }


    /**
     * app 金额显示 规则  小数点不为零展示   小数点为零截掉   不带单位
     */

    public static String transForm(double v) {
        int iv = (int) v;
        if (iv == v) {
            //整数  不保留
            if (iv >= 10000) {
                return iv * 1.0 / 10000 + "";
            }
            return iv + "";
        } else {
            //小数 保留下
            if (iv >= 10000) {
                return iv * 1.0 / 10000 + "";
            }
            return v + "";
        }


    }


    /**
     *能转整的转整 不能转整的保留
     */

    public static String transFormStr2(double v) {
        int iv = (int) v;
        if (iv == v) {
            return iv + "元";
        } else {

            return v + "元";
        }


    }


}
