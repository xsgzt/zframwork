package com.ztyb.framework.widget.sidebarsearchview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ztyb.framework.R;
import com.ztyb.framework.recycleview.WrapEmptyRecyclerView;

/**
 * 仿微信联系人侧边栏搜索列表
 */
public class SidebarSearchView extends FrameLayout {
    private Context mContext;
    private View mView;
    private RecyclerView mWrapEmptyRecyclerView;
    private SideBarView sideBarView;
    private TextView textView;
    private RecyclerView.Adapter mAdapter;

    public SidebarSearchView(@NonNull Context context) {
        this(context, null);
    }

    public SidebarSearchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SidebarSearchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initLayout();

    }

    private void initLayout() {
        mView = LayoutInflater.from(mContext).inflate(R.layout.sidebarserchview_layout, this);

        mWrapEmptyRecyclerView = getView(R.id.wer_list);
        mWrapEmptyRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        sideBarView = getView(R.id.sidebarview);

        textView = getView(R.id.tv_show_text);

        sideBarView.setOnTouchListener(new SideBarView.OnTouchListener() {
            @Override
            public void touchText(String s, int visibility) {
                textView.setVisibility(visibility);
                textView.setText(s);
            }
        });

    }


    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter != null) {
            mAdapter = adapter;
            mWrapEmptyRecyclerView.setAdapter(adapter);
        }
    }

    public void notifyDataSetChanged() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }

    }


    public <T extends View> T getView(int viewId) {
        T t = null;
        if (mView != null) {
            t = mView.findViewById(viewId);

        }
        return t;
    }


}
