package com.rainbow.smartring.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.TintTypedArray;

import com.google.android.material.appbar.MaterialToolbar;
import com.rainbow.smartring.R;

/**
 * SuperSchedule
 * Created By Rainbow on 2020/5/8.
 */
public class TitleToolbar extends RelativeLayout {

    private TextView mTitle;
    private View mRootView;
    private MaterialToolbar mToolbar;

    public TitleToolbar(Context context) {
        this(context, null);
    }

    public TitleToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.toolbarStyle);
    }

    public TitleToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        mRootView = View.inflate(context, R.layout.title_toolbar, this);
        mToolbar = mRootView.findViewById(R.id.my_toolbar);
        mTitle = mRootView.findViewById(R.id.tv_toolbar_title);

        TintTypedArray array = TintTypedArray.obtainStyledAttributes(
                context, attrs, R.styleable.TitleToolbar, defStyleAttr, 0);

        String title = array.getString(R.styleable.TitleToolbar_title);
        int color = array.getColor(R.styleable.TitleToolbar_titleColor, Color.BLACK);
        int menuId = array.getResourceId(R.styleable.TitleToolbar_menu, 0);
        int navigationIconId = array.getResourceId(R.styleable.TitleToolbar_navigationIcon, 0);

        mToolbar.setTitle("");
        mTitle.setText(title);
        mTitle.setTextColor(color);
        if (menuId != 0) {
            // 当使用menu时，TitleToolbar不作为ActionBar使用，故inflateMenu会失效
            // setSupportActionBar(mToolbar.getToolbar());
            mToolbar.inflateMenu(menuId);
        }
        if (navigationIconId != 0) {
            mToolbar.setNavigationIcon(navigationIconId);
        }
        array.recycle();
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setTitleColor(@ColorInt int color) {
        mTitle.setTextColor(color);
    }

    public void setOnMenuItemClickListener(MaterialToolbar.OnMenuItemClickListener listener) {
        mToolbar.setOnMenuItemClickListener(listener);
    }

    public void setNavigationOnClickListener(OnClickListener listener) {
        mToolbar.setNavigationOnClickListener(listener);
    }

    public MaterialToolbar getToolbar() {
        return mToolbar;
    }
}
