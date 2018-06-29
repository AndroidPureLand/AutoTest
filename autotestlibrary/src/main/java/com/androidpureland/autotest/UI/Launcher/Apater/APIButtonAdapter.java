package com.androidpureland.autotest.UI.Launcher.Apater;

import android.support.annotation.Nullable;
import android.view.View;


import com.androidpureland.autotest.HttpData.Bean.BaseUrlEntity;
import com.androidpureland.autotest.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2018/6/24.
 */

public class APIButtonAdapter extends BaseQuickAdapter<BaseUrlEntity, BaseViewHolder> {
    private OnAPIButtonAdapterOperationCallBack callBack;

    public void setCallBack(OnAPIButtonAdapterOperationCallBack callBack) {
        this.callBack = callBack;
    }

    public APIButtonAdapter(int layoutResId, @Nullable List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final BaseUrlEntity item) {
        helper.setText(R.id.button,item.getUrlName()+"\n"+item.getUrl());
        helper.getView(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack!=null){
                    callBack.onUrlClick(item,helper.getAdapterPosition());
                }
            }
        });
    }


    public interface OnAPIButtonAdapterOperationCallBack{

        void onUrlClick(BaseUrlEntity baseUrlEntity, int position);
    }
}
