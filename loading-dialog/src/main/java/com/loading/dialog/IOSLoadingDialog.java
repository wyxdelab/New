package com.loading.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.lang.reflect.Field;

/**
 * Author: Jett
 * Date: 2020-04-17 15:11
 * Email: hydznsq@163.com
 * Des: 精仿iOS加载框
 */
public class IOSLoadingDialog extends DialogFragment
        implements DialogInterface.OnKeyListener {

    /**
     * 默认点击外面无效
     */
    private boolean onTouchOutside = false;

    /**
     * 加载框提示信息 设置默认
     */
    private String hintMsg = "正在加载";
    private TextView hintTextView;

    /**
     * 设置是否允许点击外面取消
     *
     * @param onTouchOutside
     * @return
     */
    public IOSLoadingDialog setOnTouchOutside(boolean onTouchOutside) {
        this.onTouchOutside = onTouchOutside;
        return this;
    }

    /**
     * 设置加载框提示信息
     *
     * @param hintMsg
     */
    public IOSLoadingDialog setHintMsg(String hintMsg) {
        if (!hintMsg.isEmpty()) {
            this.hintMsg = hintMsg;
            if (hintTextView != null) {
                hintTextView.setText(hintMsg);
            }
        }
        return this;
    }

    /**
     * 利用反射区调用DialogFragment的commitAllowingStateLoss()方法
     *
     * @param manager
     * @param tag
     */
    public void showAllowingStateLoss(FragmentManager manager, String tag) {
        try {
            Field dismissed = DialogFragment.class.getDeclaredField("mDismissed");
            dismissed.setAccessible(true);
            dismissed.set(this, false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            Field shown = DialogFragment.class.getDeclaredField("mShownByMe");
            shown.setAccessible(true);
            shown.set(this, true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        // 设置背景透明
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // 去掉标题
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(onTouchOutside);
        View loadingView = inflater.inflate(R.layout.ios_dialog_loading, container);
        hintTextView = loadingView.findViewById(R.id.tv_ios_loading_dialog_hint);
        hintTextView.setText(hintMsg);
        //不响应返回键
        dialog.setOnKeyListener(this);
        return loadingView;
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
    }

}

