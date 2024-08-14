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
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 日志输出管理工具
 */
public class NpLog {


    public static final int LEVEL_V = 0;
    public static final int LEVEL_D = 1;
    public static final int LEVEL_I = 2;
    public static final int LEVEL_W = 3;
    public static final int LEVEL_E = 4;

    private static final String npBleTag = "npLogTag";

    private static Context mContext = null;

    //log日志目录
    private static String mLogDir = "npLog";

    //log日志文件名
    private static String mLogFileName = "log";

    //日志文件的最大大小（以M为单位），最大不能超过5M
    private static float logFileMaxSizeByM = 2;

    //是否强制使用超过最大的记录，就是不设置上限，也就是1T的大小
    private static boolean isForceOutOfMaxSize = false;

    //是否显示当前日志的大小
    private static boolean enableShowCurrentLogFileSize = false;

    //app的版本号和版本名称
    private static String appVersionName = "";
    private static String appVersionCode = "";

    //是否显示调用路径和行号
    public static boolean allowShowCallPathAndLineNumber = true;

    public static boolean allowSaveCallPathAndLineNumber = true;

    //是否允许打印日志，默认允许
    public static boolean allowLog = true;

    //是否允许保存,默认允许
    public static boolean allowSave = true;

    //日志级别
    private static int logLevel = LEVEL_E;


    public static void setLogLevel(int logLevel) {
        NpLog.logLevel = logLevel;
    }

    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

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

    public static void setIsForceOutOfMaxSize(boolean isForceOutOfMaxSize) {
        NpLog.isForceOutOfMaxSize = isForceOutOfMaxSize;
    }

    /**
     * 初始化日志管理
     *
     * @param logDir      日志的文件夹
     * @param logFileName 日志文件,不需要带后缀名称
     */
    public static void initLog(String logDir, String logFileName, Context context) {
        mContext = context;
        mLogDir = logDir;
        mLogFileName = logFileName;
        if (context != null) {
            appVersionName = PhoneInfoUtils.getVersionName(context);
            appVersionCode = PhoneInfoUtils.getVersionCode(context) + "";
        }
        initDirAndFileName();
        Log.e("npLogTag", "初始化log管理器" + mLogDir + "/" + mLogFileName);

    }


    public static File getLogFileDir() {
        return getLogFile().getParentFile();
    }

    public static File getLogFile() {
        initDirAndFileName();
        File logParentDir = getLogParentDir();
        if (logParentDir != null) {
            return new File(logParentDir, getFilePath());
        } else {
            return null;
        }
    }


    private NpLog() {
    }


    private static SimpleDateFormat smp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);


    /**
     * 打印日志
     *
     * @param content
     */
    public static void log(String content) {
        if (allowLog) {
            if (allowShowCallPathAndLineNumber) {
                StackTraceElement caller = getCallerStackTraceElement();
                content = "[" + getCallPathAndLineNumber(caller) + "]：" + content;
            }
            switch (logLevel) {
                case LEVEL_V:
                    Log.v(npBleTag, content);
                    break;
                case LEVEL_D:
                    Log.d(npBleTag, content);
                    break;
                case LEVEL_I:
                    Log.i(npBleTag, content);
                    break;
                case LEVEL_W:
                    Log.w(npBleTag, content);
                    break;
                case LEVEL_E:
                    Log.e(npBleTag, content);
                    break;
            }

        }
    }

    public static void logAndSave(String content) {
        if (allowSaveCallPathAndLineNumber) {
            StackTraceElement caller = getCallerStackTraceElement();
            content = "[" + getCallPathAndLineNumber(caller) + "]：" + content;
        }
        if (allowLog) {
            Log.e(npBleTag, content);
        }
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
        if (allowSaveCallPathAndLineNumber) {
            StackTraceElement caller = getCallerStackTraceElement();
            content = "[" + getCallPathAndLineNumber(caller) + "]：" + content;
        }
        try {
            writeFile(dateTime + "  " + content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //记录日志
    public synchronized static void writeFile(final String strLine) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // 首先创建文件夹
                    File appDir = new File(getLogParentDir(), mLogDir);
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
                            log("size:" + file.length());
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 删除日志文件
     */
    public synchronized static void clearLogFile() {
        File file = new File(getLogParentDir(), getFilePath());
        if (file.exists()) {
            log("成功删除文件" + file.getAbsolutePath());
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

        if (!isForceOutOfMaxSize) {
            if (logFileMaxSizeByM >= 5) {
                logFileMaxSizeByM = 5;
            }
        } else {
            logFileMaxSizeByM = 1024 * 1024 * 1024;
        }
    }

    /**
     * 获取调用路径和行号
     *
     * @return
     */
    private static String getCallPathAndLineNumber(StackTraceElement caller) {
        String result = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        result = String.format(result, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        return result;
    }


    static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

    static File getLogParentDir() {
        return mContext.getExternalFilesDir(null);
    }
}
