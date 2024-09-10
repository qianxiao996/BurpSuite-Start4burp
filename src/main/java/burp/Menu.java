package burp;

import org.yaml.snakeyaml.Yaml;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import static burp.BurpExtender.helpers;
import static burp.BurpExtender.stdout;


public class Menu implements IContextMenuFactory {

    @Override
    public List<JMenuItem> createMenuItems(final IContextMenuInvocation invocation) {
        List<JMenuItem> list = new ArrayList<JMenuItem>();
        Map<String,Object>  all_data;
        List<Map<String, Object>> listmenu;
        try {
            all_data = Config.LoadMenu();
            listmenu = (List<Map<String, Object>>) all_data.get("plugins");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for(Map<String, Object> data    :    listmenu)
        {
            final Map<String, String>[] single_data = new Map[]{(Map<String, String>) data.get("plugin")};
            System.out.println(single_data[0].get("name"));
            JMenuItem jMenuItem = new JMenuItem(single_data[0].get("name"));
            list.add(jMenuItem);
            jMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Map<String, String> ddd = Config.getPoc_Data(single_data[0].get("name"));
                    ConfigDlg cfd = new ConfigDlg(ddd);
                    cfd.show();

                    if(Config.isIsInject()) {
                        IHttpRequestResponse[] messages = invocation.getSelectedMessages();
                        byte[] req = messages[0].getRequest();
                        IHttpService httpService = messages[0].getHttpService();
                        String host = httpService.getHost();
                        int port = httpService.getPort();
                        URL httpurl =helpers.analyzeRequest(messages[0]).getUrl();
//                        String url = httpService.getProtocol()+"://"+httpService.getHost()+":"+httpService.getPort();
                        Config.setRequstDomain(host);
                        Config.setRequstUrl(httpService +"/");
                        //二级目录
                        String urldir_2;
                        if((httpurl.getPath()).split("/").length>=2)
                        {
                            urldir_2 = httpService +"/"+((httpurl.getPath()).split("/")[1]+"/").replace("//","/");
                        }
                        else{
                            urldir_2 = httpService +"/";

                        }
                        String root_doamin ;
                        if (host.split("\\.").length>2)
                        {
                            String[] host_domain =   host.split("\\.");
                            root_doamin = host_domain[host_domain.length-2]+"."+host.split("\\.")[host_domain.length-1];
                        }
                        else
                        {
                            root_doamin = host;
                        }
                        Config.setRequstDomain(host);
                        Config.setRequstRootDomain(root_doamin);
                        Config.setRequstUrlDir2(urldir_2);
                        Config.setRequstPort(String.valueOf(port));
//                        System.out.println(Config.getCmdOptionsCommand());
                        if((Config.getCmdOptionsCommand()).contains("{{file}}"))
                        {
                            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                            String data = df.format(new Date());
                            host = host.replace(".", "_");
                            String requstFilename = String.format("%s_%s_%s.req", host, port, data);
                            String reqFilePath = Util.getTempReqName(requstFilename);
                            Util.writeFile(req, reqFilePath);
                        }
                        new Thread(new Starter()).start();
                        Config.setIsInject(false);
                    }
                }
            });
        }
        return list;
    }
}
