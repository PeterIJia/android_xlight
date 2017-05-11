package com.umarbhutta.xlightcompanion.control.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.R;

import java.util.List;

/**
 * Created by Administrator on 2017/3/5.
 */

public class ControlResultListAdapter extends BaseAdapter {

    private Context mActivity;
    private List<Integer> mIntList;
    private List<String> mSettingStr;
    private LayoutInflater inflater;//这个一定要懂它的用法及作用

    public ControlResultListAdapter(Context activity, List<String> settingStr, List<Integer> intList) {
        this.mActivity = activity;
        this.mIntList = intList;
        this.mSettingStr = settingStr;
        this.inflater = LayoutInflater.from(mActivity);
    }

    @Override
    public int getCount() {
        return mSettingStr.size();
    }

    @Override
    public Object getItem(int position) {
        return mSettingStr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.control_result_list_item, parent, false);
            //通过上面layout得到的view来获取里面的具体控件
            holder.tv_setting_name = (TextView) convertView.findViewById(R.id.tv_setting_name);
            holder.iv_lift_img = (ImageView) convertView.findViewById(R.id.iv_lift_img);
            holder.ll_item = (LinearLayout) convertView.findViewById(R.id.ll_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_setting_name.setText(mSettingStr.get(position));
        holder.iv_lift_img.setImageResource(mIntList.get(position));

        //如果设置了回调，就设置点击事件
        if (mOnItemClickListener != null) {
            holder.ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        private TextView tv_setting_name;
        private ImageView iv_lift_img;
        private LinearLayout ll_item;
    }

    /**
     * ItemClick的回调接口
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

}
