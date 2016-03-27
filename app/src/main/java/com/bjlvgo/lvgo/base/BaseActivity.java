package com.bjlvgo.lvgo.base;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bjlvgo.lvgo.R;

public class BaseActivity extends AppCompatActivity {

    public FrameLayout lvgoContent;
    private View lvgoBar;
    private int INIT_VIEW = 1024;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initView();
    }

    private void initView() {
        lvgoBar = findViewById(R.id.lvgo_bar);
        lvgoContent = (FrameLayout) findViewById(R.id.lvgo_content);
        setStatus();
    }


    /**
     * 设置内容栏
     * @param id
     * @return  the content of activity
     *
     */
    public View setLvgoContent(int id) {
        View view = LayoutInflater.from(this).inflate(id,null);
        if (lvgoContent.getChildCount() != 0) {
            lvgoContent.removeAllViews();
        }
        lvgoContent.addView(view);
        return view;
    }

    /**
     * 设置状态栏
     */
    private void setStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            lvgoBar.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams layoutParams = lvgoBar.getLayoutParams();
            layoutParams.height = getStatusH();
            lvgoBar.setLayoutParams(layoutParams);
        } else {
            lvgoBar.setVisibility(View.GONE);
        }
    }

    /**
     * 状态栏高度的获取
     *
     * @return
     */
    public int getStatusH() {
        int statusBarH = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusBarH = getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarH;
    }

}
