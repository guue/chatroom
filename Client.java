package chatRoom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 1. @Author: Himit_ZH
 2. @Date: 2020/6/4 14:55
 3. @Description: 聊天室客户端GUI
 */
public class Client {

    public JFrame frame;
    //头部参数
    public JTextField host_textfield;
    public JTextField port_textfield;
    public JTextField name_textfield;
    public JButton exitButton;
    public JButton head_connect;
    //底部参数
    public JTextField text_field;
    public JButton foot_send;
    public JButton foot_sysClear;
    public JButton foot_userClear;
    public JButton foot_sendFile;

    //右边参数
    public JLabel users_label;
    public JList<String> userlist;
    public DefaultListModel<String> users_model;

    //左边参数
    public JScrollPane sysTextScrollPane;
    public JTextPane sysMsgArea;
    public JScrollBar sysVertical;

    //中间参数
    public JScrollPane userTextScrollPane;
    public JTextPane userMsgArea;
    public JScrollBar userVertical;

    private String title;


    public Client(String title) {
        this.title = title;
    }




    public void init() {




        frame = new JFrame(this.title);
        JPanel panel = new JPanel();        /*主要的panel，上层放置连接区，下层放置消息区，中间是消息面板，左边是系统消息，右边是当前room的用户列表*/
        JPanel toppanel = new JPanel();    /*上层panel，用于放置连接区域相关的组件*/
        JPanel bottompanel = new JPanel();    /*下层panel，用于放置发送信息区域的组件*/
        JPanel centerpanel = new JPanel();    /*中间panel，用于放置聊天信息*/
        JPanel leftpanel = new JPanel();    /*左边panel，用于放置房间列表和加入按钮*/
        JPanel rightpanel = new JPanel();   /*右边panel，用于放置房间内人的列表*/

        /*顶层的布局，分中间，东南西北五个部分*/
        BorderLayout layout = new BorderLayout();

        /*格子布局，主要用来设置西、东、南三个部分的布局*/
        GridBagLayout gridBagLayout = new GridBagLayout();

        /*主要设置北部的布局*/
        FlowLayout flowLayout = new FlowLayout();

        /*设置初始窗口的一些性质*/
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.setContentPane(panel);
        frame.setLayout(layout);

        /*设置各个部分的panel的布局和大小*/
        toppanel.setLayout(flowLayout);
        bottompanel.setLayout(gridBagLayout);
        leftpanel.setLayout(gridBagLayout);
        centerpanel.setLayout(gridBagLayout);
        rightpanel.setLayout(gridBagLayout);

        //设置面板大小
        leftpanel.setPreferredSize(new Dimension(200, 0));
        rightpanel.setPreferredSize(new Dimension(155, 0));
        bottompanel.setPreferredSize(new Dimension(0, 40));

        //进度条布局
/*        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);*/


        //头部布局
        host_textfield = new JTextField();
        port_textfield = new JTextField();
        name_textfield = new JTextField();
        exitButton = new JButton("退出");
        host_textfield.setPreferredSize(new Dimension(100, 25));
        port_textfield.setPreferredSize(new Dimension(70, 25));
        name_textfield.setPreferredSize(new Dimension(150, 25));

        JLabel host_label = new JLabel("服务器IP:");
        JLabel port_label = new JLabel("端口:");
        JLabel name_label = new JLabel("昵称:");

        head_connect = new JButton("连接");


        toppanel.add(host_label);
        toppanel.add(host_textfield);
        toppanel.add(port_label);
        toppanel.add(port_textfield);
        toppanel.add(name_label);
        toppanel.add(name_textfield);
        toppanel.add(head_connect);
        toppanel.add(exitButton);

        //底部布局
        foot_send = new JButton("发送");
        foot_sendFile = new JButton("发送文件");
        foot_sysClear = new JButton("清空系统消息");
        foot_sysClear.setPreferredSize(new Dimension(193, 0));
        foot_userClear = new JButton("清空聊天消息");
        foot_userClear.setPreferredSize(new Dimension(128, 0));
        text_field = new JTextField();
//        receiverTextField = new JTextField();
//        receiverTextField.setPreferredSize(new Dimension(80, 25));
        String name = JOptionPane.showInputDialog("请输入聊天所用昵称：");
        if (name != null && !name.isEmpty()) {
            name_textfield.setText(name);
        }

        bottompanel.add(foot_sysClear, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), 0, 0));
        bottompanel.add(text_field, new GridBagConstraints(1, 0, 1, 1, 100, 100,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), 0, 0));
        bottompanel.add(foot_send, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), 0, 0));
        bottompanel.add(foot_userClear, new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), 0, 0));
        bottompanel.add(foot_sendFile,new GridBagConstraints(4,0,1,1,1.0,1.0,
                GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets(1,1,1,1),0,0));

        //左边布局
        JLabel sysMsg_label = new JLabel("系统消息：");
        sysMsgArea = new JTextPane();
        sysMsgArea.setEditable(false);
        sysTextScrollPane = new JScrollPane();
        sysTextScrollPane.setViewportView(sysMsgArea);
        sysVertical = new JScrollBar(JScrollBar.VERTICAL);
        sysVertical.setAutoscrolls(true);
        sysTextScrollPane.setVerticalScrollBar(sysVertical);
        leftpanel.add(sysMsg_label, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        leftpanel.add(sysTextScrollPane, new GridBagConstraints(0, 1, 1, 1, 100, 100,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        //右边布局
        users_model = new DefaultListModel<>();
        userlist = new JList<String>(users_model);
        JScrollPane userListPane = new JScrollPane(userlist);
        users_label = new JLabel("聊天室内人数:0");


        rightpanel.add(users_label, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));


        rightpanel.add(userListPane, new GridBagConstraints(0, 1, 1, 1, 100, 100,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        //中间布局
        JLabel userMsg_label = new JLabel("聊天窗:");
        userMsgArea = new JTextPane();
        userMsgArea.setEditable(false);
        userTextScrollPane = new JScrollPane();
        userTextScrollPane.setViewportView(userMsgArea);
        userVertical = new JScrollBar(JScrollBar.VERTICAL);
        userVertical.setAutoscrolls(true);
        userTextScrollPane.setVerticalScrollBar(userVertical);

        centerpanel.add(userMsg_label, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        centerpanel.add(userTextScrollPane, new GridBagConstraints(0, 1, 1, 1, 100, 100,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        /*设置顶层布局*/
        panel.add(toppanel, "North");
        panel.add(bottompanel, "South");
        panel.add(leftpanel, "West");
        panel.add(rightpanel, "East");
        panel.add(centerpanel, "Center");


        //将按钮事件全部注册到监听器
        ClientButtonEventListener clientButtonEventListener = new ClientButtonEventListener(this);
        //连接服务器
        head_connect.addActionListener(clientButtonEventListener);
        foot_send.addActionListener(clientButtonEventListener);
        foot_sysClear.addActionListener(clientButtonEventListener);
        foot_userClear.addActionListener(clientButtonEventListener);
        foot_sendFile.addActionListener(clientButtonEventListener);
        exitButton.addActionListener(clientButtonEventListener);


        //窗口关闭事件
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(frame, "确定关闭聊天室界面?", "提示",
                        JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    if (e.getWindow() == frame) {
                        frame.dispose();
                        System.exit(0);
                    }
                }
            }
        });




        //窗口显示
        frame.setVisible(true);


    }
    public synchronized void updateUserList(String userName) {
        users_model.addElement(userName);
        userlist.revalidate();
        userlist.repaint();
        users_label.setText("当前连接用户:"+users_model.size());
    }

    public void removeUserFromList(String name) throws Exception {
        users_model.removeElement(name);
        userlist.revalidate();
        userlist.repaint();
        users_label.setText("当前连接用户:"+users_model.size());
    }

    public void removeAllUserFromList() throws Exception {
        users_model.removeAllElements();
        userlist.revalidate();
        userlist.repaint();
        users_label.setText("当前连接用户:"+users_model.size());
    }



    public static void main(String[] args) throws InterruptedException {
        Client client = new Client("聊天室");
        client.init();



    }
}





