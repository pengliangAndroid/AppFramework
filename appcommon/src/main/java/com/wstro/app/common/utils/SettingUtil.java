package com.wstro.app.common.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.wstro.app.common.CommonConstants;
import com.wstro.app.common.model.Font;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent Woo
 * Date: 2016/1/5
 * Time: 11:23
 */
public class SettingUtil {


    /**
     * WIFI下加载大图
     */
    public static boolean getOnlyWifiLoadImg(Context ctx) {
        return (boolean) SPUtils.get(ctx, CommonConstants.ONLY_WIFI_LOAD_IMG, false);
    }

    public static void setOnlyWifiLoadImg(Context ctx, boolean isOn) {
        SPUtils.put(ctx, CommonConstants.ONLY_WIFI_LOAD_IMG, isOn);
    }

    /**
     * 设置字体
     */
    public static List<Font> getFontList(Context ctx) {
        List<Font> list = new ArrayList<>();
        try {
            String[] files = ctx.getResources().getAssets().list("fonts");
            for (String name : files) {
                Font font = new Font();
                font.name = name.substring(0, name.lastIndexOf("."));
                font.path = "fonts/" + name;
                list.add(font);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String getCurrentFont(Context ctx) {
        return (String) SPUtils.get(ctx, CommonConstants.CURRENT_FONT_PATH, "");
    }

    /**
     * 设置字号
     */
    public static int getCurrentFontSize(Context ctx) {
        return (int) SPUtils.get(ctx, CommonConstants.CURRENT_FONT_SIZE, 0);
    }

    public static void setCurrentFontSize(Context ctx, int size) {
        SPUtils.put(ctx, CommonConstants.CURRENT_FONT_SIZE, size);
    }

//    public static int getThemeId(String theme) {
//        Field f;
//        try {
//            f = R.style.class.getField(theme);
//            return f.getInt(null);
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
//
//    public static int getAttrId(String attr) {
//        Field f;
//        try {
//            f = R.attr.class.getField(attr);
//            return f.getInt(null);
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }

    /**
     * 清理缓存
     */
    public static void countDirSizeTask(final File dir, final CountDirSizeListener listener) {
        new Thread() {
            public void run() {
                final long result = getDirSize(dir);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(result);
                    }
                });
            }
        }.start();
    }

    public static void clearAppCache(final File dir, final ClearCacheListener listener) {
        new Thread() {
            public void run() {
                clearCache(dir);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult();
                    }
                });
            }
        }.start();
    }

    public interface ClearCacheListener {
        void onResult();
    }

    public interface CountDirSizeListener {
        void onResult(long result);
    }

    private static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }

    public static String formatFileSize(long fileS) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }

        if (fileSizeString.startsWith(".")) {
            return "0B";
        }
        return fileSizeString;
    }

    private static void clearCache(File file) {
        clearFile(file);
    }

    private static void clearFile(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                clearFile(child);
            }
        } else {
            file.delete();
        }
    }

    private static int clearFolder(File dir, long curTime) {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            for (File child : dir.listFiles()) {
                if (child.isDirectory()) {
                    deletedFiles += clearFolder(child, curTime);
                }
                if (child.lastModified() < curTime) {
                    if (child.delete()) {
                        deletedFiles++;
                    }
                }
            }
        }
        return deletedFiles;
    }


}
