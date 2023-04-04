package burp;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Starter implements Runnable {

    @Override
    public void run() {
        try {

//            {{url}} {{file}} {{domain}} {{root_main}}   Start4burpdir2}}   {{port}}
            String command = String.format("%s \"%s\"  %s",Config.getRunName(),Config.getPluginsPath(),Config.getCmdOptionsCommand());
            command = command.replace("{{url}}",Config.getRequstUrl());
            command = command.replace("{{file}}","\""+Config.getRequstFilePath()+"\"");
            command = command.replace("{{domain}}",Config.getRequstDomain());
            command = command.replace("{{root_main}}",Config.getRequstRootDomain());
            command = command.replace("{{urldir2}}",Config.getRequstUrlDir2());
            command = command.replace("{{port}}",Config.getRequstPort());
            List<String> cmds = new ArrayList<>();
            int osType = Util.getOSType();
            if(osType == Util.OS_WIN){
                cmds.add("cmd.exe");
                cmds.add("/c");
                cmds.add("start");
                String batFilePath = Util.makeBatFile("start4burp.bat",command);
                if(!batFilePath.equals("Fail")){
                    cmds.add(batFilePath);
                }else{
                    String eMsg = "make start4burp.bat fail!";
                    JOptionPane.showMessageDialog(null,eMsg,"Start4burp alert",JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }else if(osType == Util.OS_MAC){
                String optionCommand = Config.getCmdOptionsCommand();
                //将参数数中的"转译为\"
                optionCommand = optionCommand.replace("\"","\\\"");
                command = String.format("%s \\\"%s\\\"  %s",Config.getRunName(),Config.getPluginsPath(),Config.getCmdOptionsCommand());
                command = command.replace("{{url}}",Config.getRequstUrl());
                command = command.replace("{{file}}","\\\""+Config.getRequstFilePath()+"\\\"");
                command = command.replace("{{domain}}",Config.getRequstDomain());
                command = command.replace("{{root_main}}",Config.getRequstRootDomain());
                command = command.replace("{{urldir2}}",Config.getRequstUrlDir2());
                command = command.replace("{{port}}",Config.getRequstPort());
                cmds.add("osascript");
                cmds.add("-e");
                String cmd = "tell application \"Terminal\" \n" +
                        "        activate\n" +
                        "        do script \"%s\"\n" +
                        "end tell";
                cmds.add(String.format(cmd,command));
                //BurpExtender.stdout.println(String.format(cmd,command));
            }else if(osType == Util.OS_LINUX){
                cmds.add("/bin/sh");
                cmds.add("-c");
                cmds.add("gnome-terminal");
                Util.setSysClipboardText(command);
                JOptionPane.showMessageDialog(null,"The command has been copied to the clipboard. Please paste it into Terminal for execution","Start4burp alert",JOptionPane.OK_OPTION);
            }else{
                cmds.add("/bin/bash");
                cmds.add("-c");
                cmds.add(command);
            }

            ProcessBuilder processBuilder = new ProcessBuilder(cmds);
            Process process = processBuilder.start();
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            BufferedReader input = new BufferedReader (ir);
            String line;
            while ((line = input.readLine()) != null) {
                BurpExtender.stdout.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            BurpExtender.stderr.println("[*]" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new Thread(new Starter()).start();
        Properties properties = System.getProperties();
        System.out.println(properties.get("java.io.tmpdir"));
    }
}
