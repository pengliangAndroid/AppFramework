package com.wstro.app.common.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.View;

public class DialogUtil {

    private static ProgressDialog progressDialog;

    public static AlertDialog.Builder dialogBuilder(Context context, String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
        if (msg != null) {
            builder.setMessage(msg);
        }
        if (title != null) {
            builder.setTitle(title);
        }
        return builder;
    }

    public static AlertDialog.Builder dialogBuilder(Context context, String title, String msg, int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_LIGHT);
        if (msg != null) {
            builder.setMessage(Html.fromHtml(msg));
        }
        if (title != null) {
            builder.setTitle(title);
        }
        return builder;
    }


    public static AlertDialog.Builder dialogBuilder(Context context, int title, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_LIGHT);
        if (view != null) {
            builder.setView(view);
        }
        if (title > 0) {
            builder.setTitle(title);
        }
        return builder;
    }

    public static AlertDialog.Builder dialogBuilder(Context context, int titleResId, int msgResId) {
        String title = titleResId > 0 ? context.getResources().getString(titleResId) : null;
        String msg = msgResId > 0 ? context.getResources().getString(msgResId) : null;
        return dialogBuilder(context, title, msg);
    }

    public static Dialog showTips(Context context, String title, String des) {
        return showTips(context, title, des, null, null);
    }

    public static Dialog showTips(Context context, int title, int des) {
        return showTips(context, context.getString(title), context.getString(des));
    }

    public static Dialog showTips(Context context, int title, int des, int btn, DialogInterface.OnDismissListener dismissListener) {
        return showTips(context, context.getString(title), context.getString(des), context.getString(btn), dismissListener);
    }

    public static Dialog showTips(Context context, String title, String des, String btn, DialogInterface.OnDismissListener dismissListener) {
        AlertDialog.Builder builder = dialogBuilder(context, title, des);
        builder.setCancelable(true);
        builder.setPositiveButton(btn, null);
        Dialog dialog = builder.show();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnDismissListener(dismissListener);
        return dialog;
    }

    public static void showProgressDialog(Context context,String message, boolean cancelable) {
        progressDialog = ProgressDialog.show(context,"",message);
        progressDialog.setCancelable(cancelable);
    }

    public static void stopProgressDialog() {
        if(progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}