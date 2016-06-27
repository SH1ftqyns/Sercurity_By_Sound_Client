package com.example.bl_uestc.sercurity_bysound.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;

/**
 * Created by lcc_luffy on 2016/1/28.
 */
public abstract class BaseFragment extends Fragment {
    protected View rootView;

    protected Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(context).inflate(getLayoutId(), null);
        ButterKnife.bind(this, rootView);
        initialize(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        onCreateView();
        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }

    public abstract int getLayoutId();

    public void initialize(@Nullable Bundle savedInstanceState) {

    }

    public void onCreateView() {

    }

    private Toast toast;

    public void toast(CharSequence msg) {
        if (toast == null)
            toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG);
        toast.setText(msg);
        toast.show();
    }
}
