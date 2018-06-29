package com.androidpureland.autotest.Custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.androidpureland.autotest.R;


/**
 * 加载dialog
 *
 * @author leilei
 * @Date 2016-04-01
 */
public class YWLoadingDialog extends Dialog {
    private TextView tv_load;// 加载的文字展示

    public YWLoadingDialog(Context context) {
        super(context, R.style.YWLoadingDialogTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commom_loading_layout);
        setCanceledOnTouchOutside(false);
        tv_load = findViewById(R.id.tv_load);
    }

    public void dismiss() {
        super.dismiss();
        tv_load = null;
    }

    public void setTitle(String str) {
        if (tv_load != null) {
            tv_load.setText(str);
        }

    }
}

