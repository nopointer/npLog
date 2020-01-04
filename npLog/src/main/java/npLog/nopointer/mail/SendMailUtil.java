package npLog.nopointer.mail;

import android.text.TextUtils;

import java.io.File;

public class SendMailUtil {
    //qq
    private static String HOST = "smtp.qq.com";
    private static String PORT = "587";
    private static String FROM_ADD = "";
    //现在QQ邮箱不是密码 而是临时密令
    private static String FROM_PSW = "";

//    //163
//    private static final String HOST = "smtp.163.com";
//    private static final String PORT = "465"; //或者465  994
//    private static final String FROM_ADD = "teprinciple@163.com";
//    private static final String FROM_PSW = "teprinciple163";
////    private static final String TO_ADD = "2584770373@qq.com";


    public static String getHOST() {
        return HOST;
    }

    public static void setHOST(String HOST) {
        SendMailUtil.HOST = HOST;
    }

    public static String getPORT() {
        return PORT;
    }

    public static void setPORT(String PORT) {
        SendMailUtil.PORT = PORT;
    }

    public static String getFromAdd() {
        return FROM_ADD;
    }

    public static void setFromAdd(String fromAdd) {
        FROM_ADD = fromAdd;
    }

    public static String getFromPsw() {
        return FROM_PSW;
    }

    public static void setFromPsw(String fromPsw) {
        FROM_PSW = fromPsw;
    }

    public static void send(final File file, String toAdd, String appName, String userContact, final SendMailCallback sendMailCallback) {
        final MailInfo mailInfo = createMail(toAdd, appName, userContact);
        final MailSender sms = new MailSender();
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = false;
                if (file != null && file.length() > 0) {
                    result = sms.sendFileMail(mailInfo, file);
                } else {
                    result = sms.sendTextMail(mailInfo);
                }
                if (sendMailCallback != null) {
                    sendMailCallback.onSend(result);
                }
            }
        }).start();
    }

    public static void send(String toAdd, String appName, String userContact) {
        final MailInfo mailInfo = createMail(toAdd, appName, userContact);
        final MailSender sms = new MailSender();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sms.sendTextMail(mailInfo);
            }
        }).start();
    }

    private static MailInfo createMail(String toAdd, String appName, String userContact) {
        final MailInfo mailInfo = new MailInfo();
        mailInfo.setMailServerHost(HOST);
        mailInfo.setMailServerPort(PORT);
        mailInfo.setValidate(true);
        mailInfo.setUserName(FROM_ADD); // 你的邮箱地址
        mailInfo.setPassword(FROM_PSW);// 您的邮箱密码
        mailInfo.setFromAddress(FROM_ADD); // 发送的邮箱
        mailInfo.setToAddress(toAdd); // 发到哪个邮件去
        mailInfo.setSubject(appName + "App 日志反馈"); // 邮件主题
        if (TextUtils.isEmpty(userContact)) {
            userContact = "";
        }
        mailInfo.setContent("您好，这是我的联系方式" + userContact + "附件里面是日志文件！！！"); // 邮件文本
        return mailInfo;
    }


    public interface SendMailCallback {
        void onSend(boolean isSuccess);
    }
}