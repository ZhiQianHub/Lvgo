package com.bjlvgo.lvgo.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.bjlvgo.lvgo.R;
import com.bjlvgo.lvgo.base.BaseActivity;

public class MainActivity extends BaseActivity{

    private View contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView = setLvgoContent(R.layout.activity_main);
    }


}
