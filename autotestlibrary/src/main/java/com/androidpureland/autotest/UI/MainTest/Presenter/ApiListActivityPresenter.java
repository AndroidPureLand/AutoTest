package com.androidpureland.autotest.UI.MainTest.Presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.androidpureland.autotest.Constant;
import com.androidpureland.autotest.Custom.FlycoDialog.dialog.listener.OnOperItemClickL;
import com.androidpureland.autotest.Custom.FlycoDialog.dialog.widget.ActionSheetDialog;
import com.androidpureland.autotest.Entity.APIConfig;
import com.androidpureland.autotest.Entity.DialogAskEntity;
import com.androidpureland.autotest.HttpData.Dao.OnUpdateItemCallBack;
import com.androidpureland.autotest.HttpData.HttpData;
import com.androidpureland.autotest.UI.TestReport.TestReport;
import com.androidpureland.autotest.Util.SortListUtil;
import com.androidpureland.autotest.Util.StringUtils;
import com.androidpureland.autotest.View.ApiListActivityView;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/6/21.
 */

public class ApiListActivityPresenter {
    private ApiListActivityView apiListActivityView;
    private ActionSheetDialog dialog;


    public void clear() {
        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = null;
        apiListActivityView = null;
    }


    public void setApiListActivityView(ApiListActivityView apiListActivityView) {
        this.apiListActivityView = apiListActivityView;

    }

    /**
     * 排序dialog的各种文字提示
     */
    public void sortDialogTXT(List<DialogAskEntity> checkedList, List<DialogAskEntity> statusList, List<DialogAskEntity> timeList, List<DialogAskEntity> codeList) {
        checkedList.add(new DialogAskEntity("全选", true, Constant.ACTION_ALL_CHECKED));
        checkedList.add(new DialogAskEntity("反选", false, Constant.ACTION_UNCHECKED));
        /*------------------------------------------------------------------------------*/
        statusList.add(new DialogAskEntity("其他在前", false, Constant.ACTION_OTHER_IN_THE_FRONT));
        statusList.add(new DialogAskEntity("正确在前", false, Constant.ACTION_RIGHT_IN_THE_FRONT));
        /*------------------------------------------------------------------------------*/
        timeList.add(new DialogAskEntity("由短到长", false, Constant.ACTION_SHORT_TO_LONG));
        timeList.add(new DialogAskEntity("由长到短", false, Constant.ACTION_LONG_TO_SHORT));
        /*------------------------------------------------------------------------------*/
        codeList.add(new DialogAskEntity("正序", false, Constant.POSITIVE_SEQUENCE));
        codeList.add(new DialogAskEntity("倒序", false, Constant.REVERSE_ORDER));
    }

    /**
     * 准备测试
     */
    public void prepareTest() {
        HttpData.getInstance().prepareTest();
    }

    /**
     * 批量测试
     */
    public void startTest() {
        for (int i = 0; i < HttpData.getInstance().getObservableList().size(); i++) {
            ((APIConfig) HttpData.getInstance().getObservableList().get(i)).init();
            if (((APIConfig) HttpData.getInstance().getObservableList().get(i)).isChecked()) {
                test(((APIConfig) HttpData.getInstance().getObservableList().get(i)), false);
            }
        }
    }

    /**
     * 单条测试
     *
     * @param apiConfig
     * @param needShowLoadingDialog 是否需要显示菊花(仅在单条测试的时候才可开启)
     */
    public void test(final APIConfig apiConfig, final boolean needShowLoadingDialog) {
        HttpData.getInstance().test(apiConfig, new OnUpdateItemCallBack() {
            @Override
            public void onStart() {
                if (needShowLoadingDialog && apiListActivityView != null) {
                    apiListActivityView.showLoadingDialog();
                }
            }

            @Override
            public void onError(APIConfig apiConfig) {
                if (apiListActivityView != null) {
                    apiListActivityView.updateItem(apiConfig);
                }
                if (needShowLoadingDialog && apiListActivityView != null) {
                    apiListActivityView.dissmissLoadingDialog();
                }
            }

            @Override
            public void onNext(APIConfig apiConfig) {
                if (apiListActivityView != null) {
                    apiListActivityView.updateItem(apiConfig);
                }
                if (needShowLoadingDialog && apiListActivityView != null) {
                    apiListActivityView.dissmissLoadingDialog();
                }
            }
        });
    }

    /**
     * 排序
     */
    public void sort() {
        switch (Constant.currentSortModel) {
            case Constant.ACTION_ALL_CHECKED:
                for (int i = 0; i < HttpData.getInstance().getObservableList().size(); i++) {
                    ((APIConfig) HttpData.getInstance().getObservableList().get(i)).setChecked(true);
                }
                break;
            case Constant.ACTION_UNCHECKED:
                for (int i = 0; i < HttpData.getInstance().getObservableList().size(); i++) {
                    ((APIConfig) HttpData.getInstance().getObservableList().get(i)).setChecked(!((APIConfig) HttpData.getInstance().getObservableList().get(i)).isChecked());
                }
                break;
            case Constant.ACTION_RIGHT_IN_THE_FRONT:
                SortListUtil.rightInTheFront();
                break;
            case Constant.ACTION_OTHER_IN_THE_FRONT:
                SortListUtil.OtherInTheFront();
                break;
            case Constant.ACTION_SHORT_TO_LONG:
                SortListUtil.shortToLong();
                break;
            case Constant.ACTION_LONG_TO_SHORT:
                SortListUtil.longToShort();
                break;
            case Constant.POSITIVE_SEQUENCE:
                SortListUtil.positiveSequence();
                break;
            case Constant.REVERSE_ORDER:
                SortListUtil.reverseOrder();
                break;
        }
        //排序完成通知适配器更新
        if (apiListActivityView != null) {
            apiListActivityView.notifyAdapterDataSetChanged();
        }

    }


    /**
     * 搜索框监听事件
     *
     * @param input
     */
    public void inputWatcher(EditText input) {
        if (apiListActivityView == null) {
            return;
        }
        if (StringUtils.isEmpty(input.getText().toString().trim())) {
            //输入框清空时通知适配器更新显示全部列表
            apiListActivityView.notifyAdapterDataSetChanged();
        } else {
            //每输入一个字，首先清空适配器，再来搜索匹配的item
            apiListActivityView.notifyAdapterDataSetChangedNull();
            for (int i = 0; i < HttpData.getInstance().getObservableList().size(); i++) {
                if (((APIConfig) HttpData.getInstance().getObservableList().get(i)).getIntroduce().contains(input.getText().toString().trim()) ||
                        ((APIConfig) HttpData.getInstance().getObservableList().get(i)).getAddress().contains(input.getText().toString().trim())) {
                    apiListActivityView.addItemAfterSearch(((APIConfig) HttpData.getInstance().getObservableList().get(i)));
                }
            }
        }
    }

    /**
     * 弹出测试窗口
     *
     * @param apiConfig
     * @param context
     */
    public void readyDialog(final APIConfig apiConfig, final Context context) {
        String[] stringItems;
        if (apiConfig.getCode() == -1) {
            stringItems = new String[]{"测试该接口"};
        } else {
            if (apiConfig.getNetworkResponseEntity() == null) {
                stringItems = new String[]{"测试该接口", "查看简明测试报告"};
            } else {
                stringItems = new String[]{"测试该接口", "查看详细测试报告"};
            }
        }


        dialog = new ActionSheetDialog(context, stringItems, null)
                .isTitleShow(false)
                .itemTextColor(Color.parseColor("#e83e41"))
                .setmCancelBgColor(Color.parseColor("#e83e41"))
                .cancelText(Color.WHITE);
        dialog.titleTextSize_SP(14.5f).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                if (position == 0) {
                    test(apiConfig, true);
                } else if (position == 1) {
                    context.startActivity(new Intent(context, TestReport.class)
                            .putExtra("successOrFail", apiConfig.isSuccessOrFail())
                            .putExtra("address", apiConfig.getAddress())
                            .putExtra("introduce", apiConfig.getIntroduce())
                            .putExtra("time", apiConfig.getTime())
                            .putExtra("result_message", apiConfig.getHttpMessage())
                            .putExtra("result_code", apiConfig.getHttpCode())
                            .putExtra("networkResponseEntity", apiConfig.getNetworkResponseEntity())
                    );
                }
            }
        });
    }

    /**
     * 插入观察者
     *
     * @param observable
     * @param observer
     * @param <T>
     */
    private <T> void setSubscribe(Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())//子线程访问网络
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(observer);
    }
}
