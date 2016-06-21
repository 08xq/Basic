package com.zhoujinlong.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.android.core.control.Toast;
import com.android.core.model.Canceller;
import com.android.core.ui.BaseFragment;
import com.android.core.control.XRecyclerViewHelper;
import com.android.core.widget.CustomViewpager;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhoujinlong.R;
import com.zhoujinlong.ui.adapter.CustomViewPageAdapter;
import com.zhoujinlong.ui.adapter.HomeRecyclerAdapter;
import com.android.core.adapter.RecyclerAdapter;
import com.zhoujinlong.model.bean.Classify;
import com.zhoujinlong.presenter.MainLogic;
import com.zhoujinlong.presenter.base.CommonView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author: 蜡笔小新
 * @date: 2016-05-31 10:51
 * @GitHub: https://github.com/meikoz
 */
public class HomeFragment extends BaseFragment implements CommonView<Classify>, XRecyclerView.LoadingListener {

    @Bind(R.id.recly_view)
    XRecyclerView mRecyclerView;

    private List<Classify.TngouEntity> classifys = new ArrayList<>();
    private CustomViewpager mViewpage;
    private MainLogic mHomeLogic;
    private RecyclerAdapter recyclerAdapter;
    private int page = 1;
    private Canceller canceller;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_compete;
    }

    @Override
    protected void onInitView() {
    }

    @Override
    protected void onInitData() {
        mHomeLogic = getLogicImpl(MainLogic.class, this);
    }

    // 广告数据
    public static List<Integer> getAdData() {
        List<Integer> adList = new ArrayList<>();
        adList.add(R.drawable.abc_adv_1);
        adList.add(R.drawable.abc_adv_2);
        adList.add(R.drawable.abc_adv_3);
        return adList;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        XRecyclerViewHelper.init().setLinearLayout(getActivity(), mRecyclerView);

        View header = View.inflate(getActivity(), R.layout.abc_viewpager_view, null);
        mViewpage = (CustomViewpager) header.findViewById(R.id.viewpager);
        mRecyclerView.addHeaderView(header);

        CustomViewPageAdapter adapter = new CustomViewPageAdapter(getActivity(), getAdData());
        mViewpage.updateIndicatorView(getAdData().size(), 0);
        mViewpage.setAdapter(adapter);
        mViewpage.startScorll();

        recyclerAdapter = new HomeRecyclerAdapter(getActivity(), R.layout.item_compete_classitfy, classifys);
        mRecyclerView.setAdapter(recyclerAdapter);
        mRecyclerView.setLoadingListener(this);
        showLoadingView();
        onRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(canceller != null) {
            canceller.Cancel();
        }
        ButterKnife.unbind(this);
    }

    @Override
    public void onLoadComplete() {
        canceller = null;
        Toast.show("请求成功");
        //加载完成需要做的操作
        hideLoadingView();
    }

    /**
     * 网络加载成功后显示数据
     */
    @Override
    public void onShowListData(Classify listData, boolean isMore) {
        canceller = null;
        if (listData.isStatus()) {
            if (!isMore)
                classifys.clear();
            classifys.addAll(listData.getTngou());
            recyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                canceller = mHomeLogic.onLoadRemoteData(false, 1);
                mRecyclerView.refreshComplete();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
                canceller = mHomeLogic.onLoadRemoteData(true, page);
                mRecyclerView.loadMoreComplete();
            }
        }, 2000);
    }
}
