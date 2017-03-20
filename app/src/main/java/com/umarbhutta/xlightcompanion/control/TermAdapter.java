package com.umarbhutta.xlightcompanion.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.R;

import java.util.List;

/**
 * Created by Administrator on 2017/3/20.
 */

public class TermAdapter extends BaseAdapter {

    private Context mActivity;
    private List<String> strList;

    public TermAdapter(Context activity, List<String> strList) {
        this.mActivity = activity;
        this.strList = strList;
    }

    @Override
    public int getCount() {
        return strList.size();
    }

    @Override
    public Object getItem(int position) {
        return strList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.term_list_item, parent, false);
            //通过上面layout得到的view来获取里面的具体控件
            holder.tv_term = (TextView) convertView.findViewById(R.id.tv_term);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_term.setText(strList.get(position));
        return convertView;
    }

    class ViewHolder {
        private TextView tv_term;
    }
}
