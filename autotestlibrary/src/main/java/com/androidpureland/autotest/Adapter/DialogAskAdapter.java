package com.androidpureland.autotest.Adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.androidpureland.autotest.Entity.DialogAskEntity;
import com.androidpureland.autotest.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 *
 * Created by Administrator on 2018/6/22.
 */

public class DialogAskAdapter extends BaseQuickAdapter<DialogAskEntity, BaseViewHolder> {
    private OnDialogAskAdapterOperationCallBack callBack;

    public void setCallBack(OnDialogAskAdapterOperationCallBack callBack) {
        this.callBack = callBack;
    }

    public DialogAskAdapter(int layoutResId, @Nullable List<DialogAskEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final DialogAskEntity item) {
        helper.setText(R.id.txt_item_dialog_ask, item.getTxt());
        helper.setChecked(R.id.checkbox_item_dialog_ask,item.isChecked());
        helper.getView(R.id.parent_item_dialog_ask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.setChecked(R.id.checkbox_item_dialog_ask,item.isChecked());
                if (callBack != null) {
                    callBack.onDoAction(helper.getAdapterPosition());
                }
            }
        });
    }

    public interface OnDialogAskAdapterOperationCallBack {
        void onDoAction(int position);
    }

}
