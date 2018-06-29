package com.androidpureland.autotest.Custom;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.androidpureland.autotest.Adapter.DialogAskAdapter;
import com.androidpureland.autotest.Entity.DialogAskEntity;
import com.androidpureland.autotest.R;
import com.androidpureland.autotest.Util.ToastUtils;

import java.util.List;

/**
 *  排序意图dialog
 * Created by Administrator on 2018/6/22.
 */

public class DialogAsk extends Dialog {
    private DialogAskAdapter dialogAskAdapter;
    private View view;
    private RecyclerView recyclerview_dialog_ask;
    private TextView tip_dialog_ask, yes_dialog_ask, cancel_dialog_ask;
    private OnDialogAskOperationCallBack callBack;
    private DialogAskEntity dialogAskEntity;

    public void setCallBack(OnDialogAskOperationCallBack callBack) {
        this.callBack = callBack;
    }

    public DialogAsk(@NonNull Context context) {
        super(context);
    }

    public DialogAsk(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DialogAsk(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    /**
     * 显示dialog
     * @param title
     * @param list
     */
    public void show(String title, final List<DialogAskEntity> list) {
        check(list);
        if (view == null) {
            setCanceledOnTouchOutside(false);
            view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_ask, null);
            tip_dialog_ask = view.findViewById(R.id.tip_dialog_ask);
            recyclerview_dialog_ask = view.findViewById(R.id.recyclerview_dialog_ask);
            recyclerview_dialog_ask.setLayoutManager(new LinearLayoutManager(getContext()));
            yes_dialog_ask = view.findViewById(R.id.yes_dialog_ask);
            cancel_dialog_ask = view.findViewById(R.id.cancel_dialog_ask);
            yes_dialog_ask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialogAskEntity == null) {
                        ToastUtils.showShortToast(getContext(), "请选中需要的选项");
                        return;
                    }
                    if (callBack != null) {
                        callBack.onOkey(dialogAskEntity, list);
                    }
                    dismiss();
                }
            });
            cancel_dialog_ask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callBack != null) {
                        callBack.onDoNothing();
                    }
                    dismiss();
                }
            });
            super.setContentView(view);
        }
        tip_dialog_ask.setText(title);
        //考虑到内存原因，本来想全局仅仅只new一个adapter，但因为某些莫名其妙的问题，最后翻车，只能先置空adapter，再次new新的adapter
        dialogAskAdapter = null;
        dialogAskAdapter = new DialogAskAdapter(R.layout.item_dialog_ask, null);
        recyclerview_dialog_ask.setAdapter(dialogAskAdapter);
        dialogAskAdapter.setCallBack(new DialogAskAdapter.OnDialogAskAdapterOperationCallBack() {
            @Override
            public void onDoAction(int position) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setChecked(false);
                }
                list.get(position).setChecked(true);
                dialogAskAdapter.setNewData(list);
                for (int i = 0; i < list.size(); i++) {
                }
                dialogAskEntity = list.get(position);
            }
        });
        dialogAskAdapter.setNewData(list);
        super.show();
    }


    /**
     * 判断是否被选中
     *
     * @param list
     * @return
     */
    private void check(List<DialogAskEntity> list) {
        dialogAskEntity = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isChecked()) {
                dialogAskEntity = list.get(i);
            }
        }
    }

    /**
     * 清理乱七八糟的东西
     */
    public void clear() {
        view = null;
        dialogAskAdapter=null;
        cancel_dialog_ask = null;
        yes_dialog_ask = null;
        tip_dialog_ask = null;
        recyclerview_dialog_ask=null;
        callBack=null;dialogAskEntity=null;
    }


    public interface OnDialogAskOperationCallBack {

        void onOkey(DialogAskEntity entity, List<DialogAskEntity> list);

        void onDoNothing();
    }

}
