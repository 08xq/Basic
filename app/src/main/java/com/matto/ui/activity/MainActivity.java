package com.matto.ui.activity;


import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.common.model.control.LogicProxy;
import com.common.view.ui.BaseActivity;
import com.matto.R;
import com.matto.model.MainLogic;
import com.matto.ui.fragment.DiscoveryFragment;
import com.matto.ui.fragment.HomeFragment;
import com.matto.ui.fragment.ShowMeFragment;
import com.matto.ui.view.MainView;

import butterknife.OnClick;


public class MainActivity extends BaseActivity implements MainView {


    public static void start(Activity context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        context.finish();
    }

    MainLogic mainLogic;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onInitView() {
        if (mainLogic == null)
            mainLogic = LogicProxy.getInstance().getBindViewProxy(MainLogic.class, this);
        switchHome();
    }

    @Override
    public void switchHome() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, new HomeFragment()).commit();
    }

    @Override
    public void switchDiscovery() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, new DiscoveryFragment()).commit();
    }

    @Override
    public void switchShomeMe() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, new ShowMeFragment()).commit();
    }


    @OnClick({R.id.navigation_selection, R.id.navigation_discovery, R.id.navigation_about})
    void onClick(View view) {
        mainLogic.switchNavigation(view.getId());
    }
}
