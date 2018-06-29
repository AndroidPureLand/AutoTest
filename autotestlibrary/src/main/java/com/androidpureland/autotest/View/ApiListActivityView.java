package com.androidpureland.autotest.View;


import com.androidpureland.autotest.Entity.APIConfig;

/**
 *  ApiListActivity View层接口
 * Created by Administrator on 2018/6/21.
 */

public interface ApiListActivityView {

    /**
     * 显示菊花
     */
    void showLoadingDialog();
    /**
     * 关闭菊花
     */
    void dissmissLoadingDialog();
    /**
     * 局部更新
     * @param apiConfig
     */
    void updateItem(APIConfig apiConfig);
    /**
     * 搜索后添加到适配器
     * @param apiConfig
     */
    void addItemAfterSearch(APIConfig apiConfig);
    /**
     * 置空整个适配器
     */
    void notifyAdapterDataSetChangedNull();
    /**
     * 整个适配器更新
     */
    void notifyAdapterDataSetChanged();
}
