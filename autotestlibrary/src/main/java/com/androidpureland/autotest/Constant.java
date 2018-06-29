package com.androidpureland.autotest;

/**
 * Created by Administrator on 2018/6/22.
 */

public class Constant {

    public static boolean isDebug=true;//是否是调试模式
    public static int CUSTOM_ERROR_CODE=10086;//网络访问失败时的自定义Code
    public static boolean isAutoSort=true;//测试完全部选中的连接后是否需要自动排序
    public static  int currentSortModel=-1;//当前排序类型
    public static final int ACTION_ALL_CHECKED = 1;//全选
    public static final int ACTION_UNCHECKED = 2;//反选
    public static final int ACTION_OTHER_IN_THE_FRONT=3;//其他在前
    public static final int ACTION_RIGHT_IN_THE_FRONT=4;//正确在前
    public static final int ACTION_SHORT_TO_LONG=5;//由短到长
    public static final int ACTION_LONG_TO_SHORT=6;//由长到短
    public static final int POSITIVE_SEQUENCE=7;//正序
    public static final int REVERSE_ORDER=8;//倒序


    public static final int HANDLER_CHECK_TEST_CONDITIONS=0x0001A;//是否符合测试条件
}
