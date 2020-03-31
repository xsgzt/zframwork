package com.ztyb.framework.widget.tabselectview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ztyb.framework.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/9/3.
 */

public class ListAdapter extends BaseAdapter {
    private List<String> mData = new ArrayList<>();
    private Context mContext;
    public ListAdapter(Context context, List<String> data) {
        mContext = context;
        mData.add("niha");
        mData.add("niha");
        mData.add("niha");
        mData.add("niha");
        
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView inflate = (TextView) View.inflate(mContext, R.layout.tab_layout, null);
        inflate.setText(mData.get(position));
        return inflate;
    }
}
