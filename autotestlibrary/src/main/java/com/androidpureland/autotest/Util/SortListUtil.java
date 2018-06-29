package com.androidpureland.autotest.Util;


import com.androidpureland.autotest.Entity.APIConfig;
import com.androidpureland.autotest.HttpData.HttpData;

import java.util.Collections;
import java.util.Comparator;

/**
 * 排序专用
 * Created by Administrator on 2018/6/22.
 */

public class SortListUtil {

    /**
     * 返回状态排序其他在前
     */
    public static void OtherInTheFront() {
        Collections.sort(HttpData.getInstance().getObservableList(), new Comparator<APIConfig>() {
            @Override
            public int compare(APIConfig o1, APIConfig o2) {
                if (o1.isSuccessOrFail() && !o2.isSuccessOrFail()) {
                    return 1;
                } else if (!o1.isSuccessOrFail() && o2.isSuccessOrFail()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
    }

    /**
     * 返回状态排序正确在前
     */
    public static void rightInTheFront() {
        Collections.sort(HttpData.getInstance().getObservableList(), new Comparator<APIConfig>() {
            @Override
            public int compare(APIConfig o1, APIConfig o2) {
                if (o1.isSuccessOrFail() && !o2.isSuccessOrFail()) {
                    return -1;
                } else if (!o1.isSuccessOrFail() && o2.isSuccessOrFail()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
    }

    /**
     * 耗时排序由短到长
     */
    public static void shortToLong() {
        Collections.sort(HttpData.getInstance().getObservableList(), new Comparator<APIConfig>() {
            @Override
            public int compare(APIConfig o1, APIConfig o2) {
                return (int) (o1.getTime() - o2.getTime());
            }
        });
    }

    /**
     * 耗时排序由长到短
     */
    public static void longToShort() {
        Collections.sort(HttpData.getInstance().getObservableList(), new Comparator<APIConfig>() {
            @Override
            public int compare(APIConfig o1, APIConfig o2) {
                return (int) (o2.getTime() - o1.getTime());
            }
        });
    }

    /**
     * 返回Code排序正序
     */
    public static void positiveSequence() {
        Collections.sort(HttpData.getInstance().getObservableList(), new Comparator<APIConfig>() {
            @Override
            public int compare(APIConfig o1, APIConfig o2) {
                return (int) (o1.getCode() - o2.getCode());
            }
        });
    }

    /**
     * 返回Code排序倒序
     */
    public static void reverseOrder() {
        Collections.sort(HttpData.getInstance().getObservableList(), new Comparator<APIConfig>() {
            @Override
            public int compare(APIConfig o1, APIConfig o2) {
                return (int) (o2.getCode() - o1.getCode());

            }
        });
    }
}
