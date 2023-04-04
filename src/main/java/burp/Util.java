package burp;


import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;

public class Util {
    public static final int OS_WIN = 1;
    public static final int OS_MAC = 2;
    public static final int OS_LINUX = 3;
    public static final int OS_UNKOWN = 4;

    public static String getOSName(){
        return System.getProperties().getProperty("os.name").toUpperCase();
    }


    public static int getOSType(){
        String OS_NAME = getOSName();
        if(OS_NAME.contains("WINDOW")){
            return OS_WIN;
        }else if(OS_NAME.contains("MAC")){
            return OS_MAC;
        }else if(OS_NAME.contains("LINUX")){
            return OS_LINUX;
        }else {
            return OS_UNKOWN;
        }
    }


    public static void writeFile(byte[] bytes,String filepath){
        try {
            //writePath 为最终文件路径名 如：D://test.txt
            FileOutputStream fos = new FileOutputStream(filepath);
            fos.write(bytes);
            fos.close();
        } catch (Exception e) {
            //e.printStackTrace();
            BurpExtender.stderr.println("[*] " + e.getMessage());
        }
    }


    public static String getTempReqName(String filename) {
        Properties properties = System.getProperties();
        String tempDir = (String) properties.get("java.io.tmpdir");
        Config.setRequstFilePath(tempDir + File.separator + filename);
        return Config.getRequstFilePath();
    }


    public static String makeBatFile(String filename,String content){
        Properties properties = System.getProperties();
        String tempDir = (String) properties.get("java.io.tmpdir");
        String batFile = (tempDir + File.separator + filename);
        String sysEncoding = System.getProperty("file.encoding");
        try {
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(batFile),sysEncoding);
            BufferedWriter writer=new BufferedWriter(write);
            writer.write(content);
            writer.close();
            return batFile;
        } catch (Exception e) {
            BurpExtender.stderr.println("[*] "+e.getMessage());
            return "Fail";
        }
    }


    public static String getBanner(){
        String bannerInfo =
                "[+] " + Config.getExtenderName() + " is loaded\n"
                        + "[+] ^_^\n"
                        + "[+]\n"
                        + "[+] ###########################################################\n"
                        + "[+]    " + Config.getExtenderName() + " v" + Config.getExtenderVersion() +"\n"
                        + "[+]    anthor: qianxiao996\n"
                        + "[+]    email:  qianxiao996@126.com\n"
                        + "[+]    github: http://github.com/qianxiao996/Start4burp\n"
                        + "[+] ###########################################################\n"
                        + "[+] Please enjoy it";
        return bannerInfo;
    }


    public static void setSysClipboardText(String str) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(str);
        clip.setContents(tText, null);
    }
}
