package com.sun.toy.fam;

import android.support.v7.app.AppCompatActivity;

import com.sun.toy.fam.interfaces.IInitializer;

/**
 * Created by 1100160 on 2017. 3. 15..
 */

public class BaseActivity extends AppCompatActivity implements IInitializer {


    protected void initialize() {
        initData();
        initView();
        initControl();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initControl() {

    }
}
