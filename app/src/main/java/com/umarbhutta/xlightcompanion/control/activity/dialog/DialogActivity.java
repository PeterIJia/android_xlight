package com.umarbhutta.xlightcompanion.control.activity.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.umarbhutta.xlightcompanion.App;
import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.control.activity.AddControlRuleActivity;
import com.umarbhutta.xlightcompanion.control.adapter.DialogListAdapter;
import com.umarbhutta.xlightcompanion.control.bean.NewRuleItemInfo;
import com.umarbhutta.xlightcompanion.control.bean.Ruleconditions;
import com.umarbhutta.xlightcompanion.okHttp.model.Condition;

/**
 * Created by Administrator on 2017/3/15.
 * 弹出框Activity
 */

public class DialogActivity extends Activity {

    private int type;
    DialogListAdapter dialogConditionListAdapter;
    ListView dialoglist;
    private Condition mCondition;
    private Ruleconditions ruleconditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        ((App) getApplicationContext()).setActivity(this);
        initViews();
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        dialoglist = (ListView) findViewById(R.id.dialoglist);
//        settingStr = EntryConditionActivity.listStr;
        type = getIntent().getBundleExtra("BUNDLE").getInt("TYPE");
        mCondition = (Condition) getIntent().getBundleExtra("BUNDLE").getSerializable("CONDITION");
        ruleconditions = (Ruleconditions) getIntent().getBundleExtra("BUNDLE").getSerializable("RULECONDITIONS");
        // 0是定时 1是亮度 2是活动，3是声音，4是温度 5是离家，6是回家，7是气体 8是大于，小于，等于 ，9是温度
        dialogConditionListAdapter = new DialogListAdapter(DialogActivity.this.getApplicationContext(), ruleconditions, type);
        dialoglist.setAdapter(dialogConditionListAdapter);

        dialoglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (type) {// 0是定时 1是亮度 2是活动，3是声音，4是温度 5是离家，6是回家，7是气体 8是大于，小于，等于 ，9是温度
                    case 1:
                        mCondition.attribute = "brightness";
                        mCondition.rightValue = (position + 1) + "";
                        mCondition.operator = "=";
                        mCondition.ruleconditionname = "brightness";
                        mCondition.status = 0;


                        NewRuleItemInfo mNewRuleItemInfo = new NewRuleItemInfo();
                        mNewRuleItemInfo.setmCondition(mCondition);
                        AddControlRuleActivity.mNewRuleConditionInfoList.add(mNewRuleItemInfo);

                        ((App) getApplicationContext()).finishActivity();
                        break;
                    case 2:
                        mCondition.attribute = "activities";
                        mCondition.rightValue = ruleconditions.data.get(0).getActivities().get(position).value + "";
                        mCondition.operator = "=";
                        mCondition.ruleconditionname = ruleconditions.data.get(0).getActivities().get(position).name;
                        mCondition.status = 0;
                        mCondition.conditionType=2;


                        NewRuleItemInfo mNewRuleItemInfo1 = new NewRuleItemInfo();
                        mNewRuleItemInfo1.setmCondition(mCondition);
                        AddControlRuleActivity.mNewRuleConditionInfoList.add(mNewRuleItemInfo1);
                        ((App) getApplicationContext()).finishActivity();
                        break;
                    case 3:
                        mCondition.attribute = "voice";
                        mCondition.rightValue = ruleconditions.data.get(0).getVoice().get(position).value + "";
                        mCondition.operator = "=";
                        mCondition.ruleconditionname = ruleconditions.data.get(0).getVoice().get(position).name;
                        mCondition.status = 0;
                        mCondition.conditionType=3;


                        NewRuleItemInfo mNewRuleItemInfo2 = new NewRuleItemInfo();
                        mNewRuleItemInfo2.setmCondition(mCondition);
                        AddControlRuleActivity.mNewRuleConditionInfoList.add(mNewRuleItemInfo2);

                        ((App) getApplicationContext()).finishActivity();
                        break;
                    case 5:
                        mCondition.attribute = "leavehome";
                        mCondition.rightValue = ruleconditions.data.get(0).getLeavehome().get(position).value + "";
                        mCondition.operator = "=";
                        mCondition.ruleconditionname = ruleconditions.data.get(0).getLeavehome().get(position).name;
                        mCondition.status = 0;


                        NewRuleItemInfo mNewRuleItemInfo3 = new NewRuleItemInfo();
                        mNewRuleItemInfo3.setmCondition(mCondition);
                        AddControlRuleActivity.mNewRuleConditionInfoList.add(mNewRuleItemInfo3);
                        ((App) getApplicationContext()).finishActivity();
                        break;
                    case 6:
                        mCondition.attribute = "gohome";
                        mCondition.rightValue = ruleconditions.data.get(0).getGohome().get(position).value + "";
                        mCondition.operator = "=";
                        mCondition.ruleconditionname = ruleconditions.data.get(0).getGohome().get(position).name;
                        mCondition.status = 0;


                        NewRuleItemInfo mNewRuleItemInfo4 = new NewRuleItemInfo();
                        mNewRuleItemInfo4.setmCondition(mCondition);
                        AddControlRuleActivity.mNewRuleConditionInfoList.add(mNewRuleItemInfo4);

                        ((App) getApplicationContext()).finishActivity();
                        break;
                    case 7:
                        mCondition.attribute = "gas";
                        mCondition.rightValue = ruleconditions.data.get(0).getGas().get(position).value + "";
                        mCondition.operator = "=";
                        mCondition.ruleconditionname = ruleconditions.data.get(0).getGas().get(position).name;
                        mCondition.status = 0;
                        mCondition.conditionType=7;


                        NewRuleItemInfo mNewRuleItemInfo5 = new NewRuleItemInfo();
                        mNewRuleItemInfo5.setmCondition(mCondition);
                        AddControlRuleActivity.mNewRuleConditionInfoList.add(mNewRuleItemInfo5);


                        ((App) getApplicationContext()).finishActivity();
                        break;
                    case 8:
                        mCondition.attribute = "temperature";
//                        if (position == 0) {
//                            mCondition.operator = getString(R.string.higer);
//                        } else if (position == 1) {
//                            mCondition.operator = getString(R.string.equal);
//                        } else if (position == 2) {
//                            mCondition.operator = getString(R.string.lower);
//                        }
                        mCondition.conditionType = 8;
                        if (position == 0) {
                            mCondition.operator = ">";
                        } else if (position == 1) {
                            mCondition.operator = "=";
                        } else if (position == 2) {
                            mCondition.operator = "<";
                        }
                        mCondition.ruleconditionname = ruleconditions.data.get(0).getTemperature().get(position).name;
                        mCondition.status = 0;
                        Intent intent = new Intent();
                        intent.putExtra("MCONDITION", mCondition);
                        setResult(31, intent);
                        finish();
                        break;
                }

            }
        });
        dialogConditionListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }
}
