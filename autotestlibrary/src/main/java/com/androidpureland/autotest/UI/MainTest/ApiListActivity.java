package com.androidpureland.autotest.UI.MainTest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;


import com.androidpureland.autotest.Constant;
import com.androidpureland.autotest.Custom.DialogAsk;
import com.androidpureland.autotest.Custom.YWLoadingDialog;
import com.androidpureland.autotest.Entity.APIConfig;
import com.androidpureland.autotest.Entity.DialogAskEntity;
import com.androidpureland.autotest.HttpData.Bean.BaseUrlEntity;
import com.androidpureland.autotest.HttpData.Dao.OnInterfaceTestPrepareCallBack;
import com.androidpureland.autotest.HttpData.Dao.OnUpdateItemCallBack;
import com.androidpureland.autotest.HttpData.HttpData;
import com.androidpureland.autotest.R;
import com.androidpureland.autotest.UI.MainTest.Apater.APIListAdapter;
import com.androidpureland.autotest.UI.MainTest.Presenter.ApiListActivityPresenter;
import com.androidpureland.autotest.View.ApiListActivityView;

import java.util.ArrayList;
import java.util.List;

public class ApiListActivity extends AppCompatActivity implements ApiListActivityView ,View.OnClickListener {

    private LinearLayout activityApiListLayoutTop;
    private ImageButton back;
    private EditText input;
    private Spinner spinner;
    private ImageButton test;
    private LinearLayout codeClick;
    private LinearLayout timeClick;
    private LinearLayout statusClick;
    private LinearLayout testClick;
    private RecyclerView recyclerview;


    private ApiListActivityPresenter presenter;
    private APIListAdapter mainAdapter;
    private YWLoadingDialog ywLoadingDialog;//菊花dialog
    private int count;//一共要测试的接口数量（用户checkbox选中状态）
    private int temp;//实际测试完毕的接口数量（仅仅用于当等于count的时候dissmiss菊花）
    List<DialogAskEntity> checkedList = new ArrayList<>();//全/反选
    List<DialogAskEntity> statusList = new ArrayList<>();//状态
    List<DialogAskEntity> timeList = new ArrayList<>();//耗时
    List<DialogAskEntity> codeList = new ArrayList<>();//返回码
    private DialogAsk dialogAsk;//筛选弹窗

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_list);


        activityApiListLayoutTop = (LinearLayout) findViewById(R.id.activity_api_list_layout_top);
        back = (ImageButton) findViewById(R.id.back);
        input = (EditText) findViewById(R.id.input);
        spinner = (Spinner) findViewById(R.id.spinner);
        test = (ImageButton) findViewById(R.id.test);
        codeClick = (LinearLayout) findViewById(R.id.code_click);
        timeClick = (LinearLayout) findViewById(R.id.time_click);
        statusClick = (LinearLayout) findViewById(R.id.status_click);
        testClick = (LinearLayout) findViewById(R.id.test_click);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);

        back.setOnClickListener(this);
        testClick.setOnClickListener(this);
        statusClick.setOnClickListener(this);
        codeClick.setOnClickListener(this);
        timeClick.setOnClickListener(this);

        presenter = new ApiListActivityPresenter();
        dialogAsk = new DialogAsk(this, R.style.RcDialog);
        /********************************************************************************/
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mainAdapter = new APIListAdapter(R.layout.item_api_list_layout);
        recyclerview.setAdapter(mainAdapter);
        /********************************************************************************/
        presenter.sortDialogTXT(checkedList,statusList,timeList,codeList);
        setListener();
        test.setBackgroundResource(R.mipmap.icon_test_stop);
        presenter.prepareTest();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialogAsk != null) {
            dialogAsk.clear();
        }
        dialogAsk = null;
        if (presenter != null) {
            presenter.clear();
        }
        presenter = null;
        HttpData.getInstance().clear();
    }

    /**
     * 各种回调
     */
    private void setListener() {
        //设置P层回调
        presenter.setApiListActivityView(this);
        //设置测试回调
        HttpData.getInstance().setOnInterfaceTestCallBack(new OnInterfaceTestPrepareCallBack() {
            /**
             * 准备测试
             * @param baseUrlEntity 测试的服务器域名
             */
            @Override
            public void onPrepareStart(BaseUrlEntity baseUrlEntity) {
                showLoadingDialog();
            }

            /**
             * 准备完成
             * @param baseUrlEntity 测试的服务器域名
             * @param list          测试的接口
             */
            @Override
            public void onPrepareComplete(BaseUrlEntity baseUrlEntity, List list) {
                dissmissLoadingDialog();
                mainAdapter.setNewData(list);
                isConsistentWithTheTestConditions();
            }
        });
        //设置接口列表adapter操作回调
        mainAdapter.setCallBack(new APIListAdapter.OnAPIListAdapterCallBack() {
            /**
             * 点击选中事件
             * @param apiConfig 选中的API接口
             * @param isChecked 是否被选中
             */
            @Override
            public void onChecked(APIConfig apiConfig, boolean isChecked) {
                for (int i = 0; i < HttpData.getInstance().getObservableList().size(); i++) {
                    if (HttpData.getInstance().getObservableList().get(i) == apiConfig) {
                        ((APIConfig)HttpData.getInstance().getObservableList().get(i)).setChecked(isChecked);
                    }
                }
                isConsistentWithTheTestConditions();
            }

            /**
             * item点击事件
             * @param apiConfig 选中的API接口
             */
            @Override
            public void onClick(APIConfig apiConfig) {
                presenter.readyDialog(apiConfig, ApiListActivity.this);
            }
        });
        //设置排序dialog回调
        dialogAsk.setCallBack(new DialogAsk.OnDialogAskOperationCallBack() {
            @Override
            public void onOkey(DialogAskEntity entity, List<DialogAskEntity> list) {
                //todo  根据意图需求去排序
                //记录下用户当前的排序意图，配合Constant.isAutoSort来决定用户下次测试完成后是否自动根据上次的排序状态来进行排序
                Constant.currentSortModel = entity.getAction();
                presenter.sort();
                mainAdapter.setNewData(HttpData.getInstance().getObservableList());
                isConsistentWithTheTestConditions();
            }

            @Override
            public void onDoNothing() {
                // TODO: 不做任何事，内部已经dissmiss了
            }
        });
        //为搜索框添加监听
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.inputWatcher(input);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    /**
     * 判断是否符合测试条件（如果不符合,将测试按钮置灰并置空点击事件）
     */
    private void isConsistentWithTheTestConditions() {
        count = 0;
        for (int i = 0; i < HttpData.getInstance().getObservableList().size(); i++) {
            if (((APIConfig)HttpData.getInstance().getObservableList().get(i)).isChecked()) {
                count++;
            }
        }
        if (count > 0) {
            test.setBackgroundResource(R.mipmap.icon_test);
            test.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dissmissLoadingDialog();
                    showLoadingDialog();
                    temp = 0;
//                    for (int i = 0; i < HttpData.getInstance().getObservableList().size(); i++) {
//                        ((APIConfig)HttpData.getInstance().getObservableList().get(i)).setCode(-1);
//                        ((APIConfig)HttpData.getInstance().getObservableList().get(i)).setTime(-1);
//                    }
                    mainAdapter.setNewData(HttpData.getInstance().getObservableList());
                    presenter.startTest();
                }
            });
        } else {
            test.setBackgroundResource(R.mipmap.icon_test_stop);
            test.setOnClickListener(null);
        }
    }


    @Override
    public void showLoadingDialog() {
        //todo 显示菊花
        if (ywLoadingDialog == null) {
            ywLoadingDialog = new YWLoadingDialog(ApiListActivity.this);
        }
        ywLoadingDialog.show();
    }

    @Override
    public void dissmissLoadingDialog() {
        // todo 关闭菊花
        if (ywLoadingDialog != null) {
            ywLoadingDialog.dismiss();
        }
    }

    @Override
    public void updateItem(APIConfig apiConfig) {
        //todo  局部更新
        temp++;
        for (int i = 0; i < mainAdapter.getData().size(); i++) {
            if (mainAdapter.getData().get(i) == apiConfig) {
                mainAdapter.notifyItemChanged(i);
                break;
            }
        }
        //判断网络返回的条数是否等于测试的接口数量，如果等于要测试的接口数量，则关闭菊花dialog且开始排序
        if (temp >= count) {
            dissmissLoadingDialog();
            //测试完成后根据配置来决定是否需要排序
            if (Constant.isAutoSort) presenter.sort();
        }
    }

    @Override
    public void addItemAfterSearch(APIConfig apiConfig) {
        //todo 搜索后添加到适配器
        mainAdapter.addData(apiConfig);
    }

    @Override
    public void notifyAdapterDataSetChangedNull() {
        //todo 置空整个适配器
        mainAdapter.setNewData(null);
    }

    @Override
    public void notifyAdapterDataSetChanged() {
        //todo  通知适配器更新
        mainAdapter.setNewData(HttpData.getInstance().getObservableList());
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.back) {
            finish();

        } else if (i == R.id.test_click) {
            dialogAsk.show("全/反选", checkedList);

        } else if (i == R.id.status_click) {
            dialogAsk.show("状态排序", statusList);

        } else if (i == R.id.code_click) {
            dialogAsk.show("返回码排序", codeList);

        } else if (i == R.id.time_click) {
            dialogAsk.show("耗时排序", timeList);

        }
    }
}
