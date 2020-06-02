/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package npLog.nopointer.core;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 日志输出管理工具
 */
public class NpLog {

    public static final String npBleTag = "npLogTag";

    //log日志目录
    private static String mLogDir = "npLog";

    //log日志文件名
    private static String mLogFileName = "log";


    //日志文件的最大大小（以M为单位），最大不能超过5M
    private static float logFileMaxSizeByM = 2;

    //是否显示当前日志的大小
    private static boolean enableShowCurrentLogFileSize = false;

    private static String appVersionName = "";
    private static String appVersionCode = "";

    /**
     * log日志目录
     */
    public static void setLogDir(String mLogDir) {
        NpLog.mLogDir = mLogDir;
    }

    /**
     * log日志文件名
     */
    public static void setLogFileName(String mLogFileName) {
        NpLog.mLogFileName = mLogFileName;
    }

    public static void setEnableShowCurrentLogFileSize(boolean enableShowCurrentLogFileSize) {
        NpLog.enableShowCurrentLogFileSize = enableShowCurrentLogFileSize;
    }

    /**
     * 日志文件的最大大小（以M为单位），最大不能超过5M
     */
    public static void setLogFileMaxSizeByM(float logFileMaxSizeByM) {
        NpLog.logFileMaxSizeByM = logFileMaxSizeByM;
    }

    /**
     * 初始化日志管理
     *
     * @param logDir      日志的文件夹
     * @param logFileName 日志文件,不需要带后缀名称
     */
    public static void initLog(String logDir, String logFileName, Context context) {
        mLogDir = logDir;
        mLogFileName = logFileName;
        if (context != null) {
            appVersionName = PhoneInfoUtils.getVersionName(context);
            appVersionCode = PhoneInfoUtils.getVersionCode(context) + "";
        }
        initDirAndFileName();
        Log.e("npLogTag", "初始化log管理器" + logDir + "/" + logFileName);

    }

    /**
     * 初始化日志管理 默认文件夹 NpLog ，默认文件名log
     */
    public static void initLog() {
        initLog(null, null, null);
    }


    public static File getBleLogFileDir() {
        initDirAndFileName();
        File appDir = new File(Environment.getExternalStorageDirectory(), getFilePath());
        return appDir;
    }


    private NpLog() {
    }

    public static boolean allowD = true;
    public static boolean allowE = true;
    public static boolean allowI = true;
    public static boolean allowV = true;
    public static boolean allowW = true;
    public static boolean allowWtf = true;

    //是否允许保存,默认允许
    public static boolean allowSave = true;

    private static SimpleDateFormat smp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");


    public static void e(String content) {
        if (allowE) {
            Log.e(npBleTag, content);
        }
    }

    public static void eAndSave(String content) {
        e(content);
        save(content);
    }

    /**
     * 保存日志
     *
     * @param content
     */
    public static void save(String content) {
        if (!allowSave) {
            return;
        }
        String dateTime = smp.format(new Date());
        try {
            writeFile(dateTime + "  " + content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void w(String content) {
        if (!allowW) return;
        Log.w(npBleTag, content);
    }

    public static void wAndSave(String content) {
        w(content);
        save(content);
    }

    public static void i(String content) {
        if (!allowW) return;
        Log.i(npBleTag, content);
    }

    public static void iAndSave(String content) {
        i(content);
        save(content);
    }

    public static void d(String content) {
        if (!allowD) return;
        Log.d(npBleTag, content);
    }

    public static void dAndSave(String content) {
        d(content);
        save(content);
    }

    //记录日志
    public synchronized static void writeFile(String strLine) {
        // 首先创建文件夹
        File appDir = new File(Environment.getExternalStorageDirectory(), mLogDir);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String fileName = mLogFileName + ".txt";
        File file = new File(appDir, fileName);
        if (!file.exists()) {
            //文件第一次创建的时候,追加一些额外信息，比如app版本和手机型号等等
            BufferedWriter fileOutputStream = null;
            try {
                fileOutputStream = new BufferedWriter(new FileWriter(file, true));
                fileOutputStream.write(gteAppInfo());
                fileOutputStream.newLine();
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (enableShowCurrentLogFileSize) {
                e("size:" + file.length());
            }
            if (file.length() > logFileMaxSizeByM * 1024 * 1024) {
                clearLogFile();
                writeFile(strLine);
                return;
            }
        }
        //追加文件写的内容
        try {
            BufferedWriter fileOutputStream = new BufferedWriter(new FileWriter(file, true));
            fileOutputStream.write(strLine);
            fileOutputStream.newLine();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除日志文件
     */
    public synchronized static void clearLogFile() {
        File file = new File(Environment.getExternalStorageDirectory(), getFilePath());
        if (file.exists()) {
            e("成功删除文件" + file.getAbsolutePath());
            file.delete();
        }
    }

    /**
     * 向src文件添加header
     *
     * @param content
     * @param srcPath
     * @throws Exception
     */
    private static void appendFileHeader(String content, String srcPath) throws Exception {
        RandomAccessFile src = new RandomAccessFile(srcPath, "rw");
        int srcLength = (int) src.length();
        byte[] buff = new byte[srcLength];
        src.read(buff, 0, srcLength);
        src.seek(0);
        byte[] header = content.getBytes("utf-8");
        src.write(header);
        src.seek(header.length);
        src.write(buff);
        src.close();
    }


    //获取app的版本 和手机信息
    private static String gteAppInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("手机品牌:" + PhoneInfoUtils.getDeviceBrand()).append("\n");
        stringBuilder.append("手机型号:" + PhoneInfoUtils.getSystemModel()).append("\n");
        stringBuilder.append("安卓版本:" + PhoneInfoUtils.getSystemVersion()).append("\n");
        stringBuilder.append("语言环境:" + PhoneInfoUtils.getSystemLanguage()).append("\n");
        if (!TextUtils.isEmpty(appVersionName)) {
            stringBuilder.append("AppVersionName:" + appVersionName).append("\n");
        }
        if (!TextUtils.isEmpty(appVersionCode)) {
            stringBuilder.append("AppVersionCode:" + appVersionCode).append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * 获取日志文件的路径
     *
     * @return
     */
    private static String getFilePath() {
        initDirAndFileName();
        return mLogDir + "/" + mLogFileName + ".txt";
    }

    /**
     * 初始化日志文件夹和名称
     */
    private static void initDirAndFileName() {
        if (TextUtils.isEmpty(mLogDir)) {
            mLogDir = "npLog";
        }
        if (TextUtils.isEmpty(mLogFileName)) {
            mLogFileName = "log";
        }
        if (logFileMaxSizeByM <= 0) {
            logFileMaxSizeByM = 2;
        }
        if (logFileMaxSizeByM >= 5) {
            logFileMaxSizeByM = 5;
        }
    }

}
