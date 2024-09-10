package burp;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
public class Config {
    //插件名称
    private static final String EXTENDER_NAME = "Start4burp";
    //插件版本
    private static final String EXTENDER_VERSION = "1.1";
    private static String REQUST_URL;
    private static String REQUST_DOAMIN = "";
    private static String REQUST_ROOT_DOMAIN = "";

    private static String REQUST_URl_DIR_2 = "";
    private static String REQUST_ROOT_URl = "";


    private static String REQUST_PORT = "";

    //环境路径
    private static String RunName = "python";

    //插件文件路径
    private static String PluginPath = "plugins.py";

    //插件选项
    private static String CMD_OPTIONS_COMMAND = "";

    private static String REQUST_FILE_PATH = "";

    //是否注入
    private static boolean IS_INJECT = false;


    public static Map<String,Object> obj = new HashMap<>();
    public static String getExtenderName() {
        return EXTENDER_NAME;
    }

    public static String getExtenderVersion() {
        return EXTENDER_VERSION;
    }

    public static Yaml yaml = new Yaml();

    //配置文件目录
//    public static String Start4BurpConfigPath = String.format("%s/.config/start4burp", System.getProperty("user.home"));

    public static String Start4BurpConfigPath = BurpExtender.callbacks.getExtensionFilename().substring(0, (BurpExtender.callbacks.getExtensionFilename().lastIndexOf(File.separator))) + File.separator;

    //配置文件路径
    public static String ConfigPath =  String.format("%s/%s", Start4BurpConfigPath, "Config.yml");

    public static String getRunName() {
        return Config.RunName;
    }

    public static void setRunName(String plugins_name, String runname,Map<String, String> single_data) {
        save_plugin(plugins_name,runname,single_data.get("file"),single_data.get("parameters"));
//        BurpExtender.callbacks.saveExtensionSetting("PYTHON_NAME", String.valueOf(pythonName));
        Config.RunName = runname;
    }


    public static void save_plugin(String plugins_name, String plugins_mode, String plugins_file, String plugins_parameters) {
        List<Map<String,Object>> listmenu = (List<Map<String, Object>>) obj.get("plugins");
        for (Map<String, Object> data : listmenu) {
            Map<String,String> temp_data = (Map<String, String>) data.get("plugin");
            if(Objects.equals(temp_data.get("name"), plugins_name))
            {
                temp_data.put("name",plugins_name);
                temp_data.put("mode",plugins_mode);
                temp_data.put("file",plugins_file);
                temp_data.put("parameters",plugins_parameters);
                Map<String,Object>  temp_plug = new HashMap<>();
                temp_plug.put("plugin",temp_data);
                int index = listmenu.indexOf(data);
                listmenu.remove(data);
                listmenu.add(index,temp_plug);
                obj.put("plugins",listmenu);
                break;
            }
        }
        try {
            Writer ws = new OutputStreamWriter(new FileOutputStream(ConfigPath), StandardCharsets.UTF_8);
            yaml.dump(obj, ws);
            ws.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getPluginsPath() {
        return Config.PluginPath;
    }

    public static void setPluginPath(String plugins_name, String pluginsPath,Map<String, String> single_data) {
        save_plugin(plugins_name,single_data.get("mode"),pluginsPath,single_data.get("parameters"));
//        BurpExtender.callbacks.saveExtensionSetting("SQLMAP_PATH", String.valueOf(pluginsPath));
        Config.PluginPath = pluginsPath;
    }



    public static String getCmdOptionsCommand() {
        return Config.CMD_OPTIONS_COMMAND;
    }

    public static void setCmdOptionsCommand(String pluginsPath, String CmdOptionsCommand,Map<String, String> single_data) {

        save_plugin(pluginsPath,single_data.get("mode"),single_data.get("file"),CmdOptionsCommand);
//        BurpExtender.callbacks.saveExtensionSetting("SQLMAP_OPTIONS_COMMAND", String.valueOf(sqlmapOptionsCommand));
        Config.CMD_OPTIONS_COMMAND = CmdOptionsCommand;
    }

    public static boolean isIsInject() {
        return IS_INJECT;
    }

    public static void setIsInject(boolean isInject) {
        IS_INJECT = isInject;
    }
    public static String getRequstFilePath() {
        return REQUST_FILE_PATH;
    }

    public static void setRequstFilePath(String requstFilePath) {
        REQUST_FILE_PATH = requstFilePath;
    }

    //数据包请求url
    public static void setRequstUrl(String requstUrl) {
        REQUST_URL = requstUrl;
    }
    public static String getRequstUrl() {
        return REQUST_URL;
    }


    //数据包请求
    public static void setRequstDomain(String requstDomain) {
        REQUST_DOAMIN = requstDomain;
    }
    public static String getRequstDomain() {
        return REQUST_DOAMIN;
    }


    public static void setRequstRootDomain(String requstRootDomain) {
        REQUST_ROOT_DOMAIN = requstRootDomain;
    }
    public static String getRequstRootDomain() {
        return REQUST_ROOT_DOMAIN;
    }


    public static void setRequstUrlDir2(String requstUrlDir2) {
        REQUST_URl_DIR_2 = requstUrlDir2;
    }
    public static String getRequstUrlDir2() {
        return REQUST_URl_DIR_2;
    }
    public static void setRequstRootUrl(String requstrooturl) {
        REQUST_ROOT_URl = requstrooturl;
    }
    public static String getRequstRootUrl() {
        return REQUST_ROOT_URl;
    }



    public static void setRequstPort(String requstPort) {
        REQUST_PORT = requstPort;
    }
    public static String getRequstPort() {
        return REQUST_PORT;
    }
    public static Map<String, String> getPoc_Data(String plugins_name) {
        List<Map<String,Object>> listmenu = (List<Map<String, Object>>) obj.get("plugins");
        for (Map<String, Object> data : listmenu){
            Map<String,String> temp_data = (Map<String, String>) data.get("plugin");
            if(Objects.equals(temp_data.get("name"), plugins_name))
            {
                return temp_data;
            }
        }
        return null;
    }



//    {{url}} {{file}} {{domain}} {{root_main}}   {{urldir2}}   {{port}}

    public static Map<String, Object> LoadMenu() throws IOException {
        // 构造函数，初始化配置
        File Start4BurpPathFile = new File(Start4BurpConfigPath);
        if (!(Start4BurpPathFile.exists() && Start4BurpPathFile.isDirectory())) {
            boolean a = Start4BurpPathFile.mkdirs();
        }

        File settingPathFile = new File(ConfigPath);
        if (!(settingPathFile.exists())) {
            ArrayList<Map>  plugins_list  =  new ArrayList<>();
            Map single_plugins = new HashMap<>();
            single_plugins.put("name","sqlmap");
            single_plugins.put("mode","python3");
            single_plugins.put("file","sqlmap.py");
            single_plugins.put("parameters","-r {{file}}");
            Map<String,Object>  temp_plug = new HashMap<>();
            temp_plug.put("plugin",single_plugins);
            plugins_list.add(temp_plug);
            obj.put("plugins",plugins_list);
            Writer ws = new OutputStreamWriter(new FileOutputStream(ConfigPath), StandardCharsets.UTF_8);
            yaml.dump(obj, ws);
            ws.close();
            return obj;
        }else {
            InputStream inorder = new FileInputStream(ConfigPath);
            obj = yaml.load(inorder);
            inorder.close();
            return obj;
        }
    }
    public static void main(String[] args) throws IOException {
        Config.LoadMenu();
    }
}
