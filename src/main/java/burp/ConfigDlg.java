package burp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 配置窗口类，负责显示配置窗口，处理窗口消息
 */
public class ConfigDlg extends JDialog {
    private final Map<String, String> single_data;
    private final JPanel mainPanel = new JPanel();

    private final JLabel lbRunName =  new JLabel("Run  Name:");
    private final JTextField tfRunName = new JTextField(30);
    private final JLabel lbPluginPath = new JLabel("Plugin path:");
    private final JTextField tfPluginPath = new JTextField(30);
    private final JButton btnBrowse = new JButton("Browse");
    private final JLabel lbCmdOption = new JLabel("Cmd option:");
    private final JTextField tfCmdOption = new JTextField(30);
    private final JLabel lbPrompt = new JLabel("Prompt:");

    private final JButton btnOK = new JButton("OK");
    private final JButton btnCancel = new JButton("Cancel");


    public ConfigDlg(Map<String, String> data) {
        single_data = data;
        initGUI();
        initEvent();
        initValue();
        this.setTitle("Start4burp config");
    }


    /**
     * 初始化UI
     */
    private void initGUI(){
        JLabel lbRunNameHelp = new JLabel("?");
        lbRunNameHelp.setToolTipText("eg: python,java,cmd.exe,/bin/bash ...");
        JLabel lbCmdOptionHelp = new JLabel("?");
        lbCmdOptionHelp.setToolTipText("eg: -u {{url}} -f {{file}} -d {{domain}} {{Maindomain}} ");
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.add(lbRunName,new GBC(0,0,2,1).setFill(GBC.BOTH).setInsets(10,10,2,0));
        mainPanel.add(tfRunName, new GBC(1,0,3,1).setFill(GBC.BOTH).setInsets(10,0,2,10));
        mainPanel.add(lbRunNameHelp,new GBC(4,0,6,1).setFill(GBC.BOTH).setInsets(10,0,2,10));
        mainPanel.add(lbPluginPath,new GBC(0,1,2,1).setFill(GBC.BOTH).setInsets(10,10,2,0));
        mainPanel.add(tfPluginPath,new GBC(1,1,3,1).setFill(GBC.BOTH).setInsets(10,0,2,10));
        mainPanel.add(btnBrowse,new GBC(4,1,1,1).setFill(GBC.BOTH).setInsets(10,0,2,10));
        mainPanel.add(lbCmdOption,new GBC(0,2,2,1).setFill(GBC.BOTH).setInsets(10,10,2,0));
        mainPanel.add(tfCmdOption,new GBC(1,2,3,1).setFill(GBC.BOTH).setInsets(10,0,2,10));
        mainPanel.add(lbCmdOptionHelp,new GBC(4,2,1,1).setFill(GBC.BOTH).setInsets(10,0,2,10));
        mainPanel.add(btnOK,new GBC(0,3,1,1).setFill(GBC.BOTH).setInsets(10,10,10,0));
        mainPanel.add(btnCancel,new GBC(1,3,1,1).setFill(GBC.BOTH).setInsets(10,0,10,10));

        if(Util.getOSType() == Util.OS_LINUX){
            lbPrompt.setText("Notice: The command will be copied to the clipboard. Paste it into Terminal!");
            mainPanel.add(lbPrompt,new GBC(2,3,1,1).setFill(GBC.BOTH).setInsets(10,0,2,10));
        }else if(Util.getOSType() == Util.OS_MAC){
            lbPrompt.setText("Notice: Please ensure that Terminal is in running state!");
            mainPanel.add(lbPrompt,new GBC(2,3,1,1).setFill(GBC.BOTH).setInsets(10,0,2,10));
        }
        lbPrompt.setForeground(new Color(0,0,255));

        this.setModal(true);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.add(mainPanel);
        //使配置窗口自动适应控件大小，防止部分控件无法显示
        this.pack();
        //居中显示配置窗口
        Dimension screensize=Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(screensize.width/2-this.getWidth()/2,screensize.height/2-this.getHeight()/2,this.getWidth(),this.getHeight());
        BurpExtender.callbacks.customizeUiComponent(this);
    }


    /**
     * 初始化事件
     */
    private void initEvent(){

        btnBrowse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);//设置只能选择目录
                int returnVal = chooser.showOpenDialog(ConfigDlg.this);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    String selectPath =chooser.getSelectedFile().getPath() ;
                    tfPluginPath.setText(selectPath);
                    chooser.hide();
                }
            }
        });


        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Config.setIsInject(true);
                Config.setRunName(String.valueOf(single_data.get("name")),tfRunName.getText().trim(),single_data);
                Config.setPluginPath(String.valueOf(single_data.get("name")),tfPluginPath.getText().trim(),single_data);
                Config.setCmdOptionsCommand(String.valueOf(single_data.get("name")),tfCmdOption.getText().trim(),single_data);
                ConfigDlg.this.dispose();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Config.setIsInject(false);
                ConfigDlg.this.dispose();
            }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                Config.setIsInject(false);
            }
        });

    }


    /**
     * 为控件赋值
     */
    public void initValue(){
        tfRunName.setText(String.valueOf(single_data.get("mode")));
        //BurpExtender.stderr.println("Python name:"+Config.getPythonName());
        tfPluginPath.setText(String.valueOf(single_data.get("file")));
        tfCmdOption.setText(String.valueOf(single_data.get("parameters")));
    }
}