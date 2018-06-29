package com.androidpureland.autotest.UI.MainTest.Apater;

import android.view.View;

import com.androidpureland.autotest.Entity.APIConfig;
import com.androidpureland.autotest.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;


public class APIListAdapter extends BaseQuickAdapter<APIConfig, BaseViewHolder> {
    private OnAPIListAdapterCallBack callBack;

    public void setCallBack(OnAPIListAdapterCallBack callBack) {
        this.callBack = callBack;
    }

    public APIListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final APIConfig item) {
        if (item.getCode() == -1) {
            helper.setText(R.id.item_api_list_text_code, "-");
            helper.setImageResource(R.id.item_api_list_image_type, R.drawable.path_ask);
        } else {
            helper.setText(R.id.item_api_list_text_code, item.getCode() + "");
            if(item.isSuccessOrFail()){
                helper.setImageResource(R.id.item_api_list_image_type,R.drawable.path_exactness);
            }else{
                helper.setImageResource(R.id.item_api_list_image_type,R.drawable.path_error);
            }
        }


        if (item.getTime() == -1) {
            helper.setText(R.id.item_api_list_text_time, "-");
        } else {
            helper.setText(R.id.item_api_list_text_time, item.getTime() + "");
        }

        helper.setChecked(R.id.item_api_list_checkbox, item.isChecked());
        helper.setText(R.id.item_api_list_text_introduce, item.getIntroduce());
        helper.setText(R.id.item_api_list_text_address, item.getAddress());
        helper.getView(R.id.item_api_list_checkbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null) {
                    callBack.onChecked(item, !item.isChecked());
                    notifyDataSetChanged();
                }
            }
        });
        helper.getView(R.id.item_api_list_text_parent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null) {
                    callBack.onClick(item);
                }
            }
        });
    }


    public interface OnAPIListAdapterCallBack {

        /**
         * 点击选中事件
         *
         * @param apiConfig 选中的API接口
         * @param isChecked 是否被选中
         */
        void onChecked(APIConfig apiConfig, boolean isChecked);

        /**
         * item点击事件
         *
         * @param apiConfig 选中的API接口
         */
        void onClick(APIConfig apiConfig);
    }
}
