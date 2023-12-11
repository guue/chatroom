package chatRoom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


/**
 * @brief 初始化连接 服务端的gui界面
 */
public class Sever {
    public  List<Socket> onlineSockets = new ArrayList<>();
    public  HashMap<Socket,String> user_socket = new HashMap<>();
    /*GUI参数*/
    public JFrame frame;

    public JTextField port_textfield;
    public JTextField name_textfield;
    public JButton connectButton;



    public JScrollPane sysTextScroll;
    public JScrollBar sysVertical;
    public  JTextPane sysTextArea;

    public DefaultListModel<String> users_model;
    public JList<String> userlist;
    public JScrollPane userListPane;
    public JButton kick;
    public JLabel user;

    public JTextPane userTextArea;
    public JScrollPane userTextScroll;
    public JScrollBar userVertical;







    public String title;
    private JProgressBar progressBar;

    /**
     * @MethodName GUI初始化
     */
    public void initview(){




        JPanel panel = new JPanel();//主面板
        JPanel topPanel = new JPanel(); //上面是连接按钮区域
//        JPanel bottomPanel = new JPanel();//下面发送信息区域
        JPanel centerpanel = new JPanel();    /*中间panel，用于放置聊天信息*/
        JPanel leftpanel = new JPanel();    /*左边panel，用于放置房间列表和加入按钮*/
        JPanel rightpanel = new JPanel();   /*右边panel，用于放置房间内人的列表*/

        /*底层布局，分中间，上下左右五个部分*/
        BorderLayout layout = new BorderLayout();
        /*格子布局，主要用来设置左、右、下三个部分的布局 1*3 */
        GridBagLayout gridBagLayout = new GridBagLayout();
        /*主要设置上部的布局 	组件按照加入的先后顺序按照设置的对齐方式从左向右排列，一行排满到下一行开始继续排列*/
        FlowLayout flowLayout = new FlowLayout();


        //窗口设置在屏幕中央
        frame = new JFrame(this.title);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(new ImageIcon("D:\\Java practice\\NetworkChatProject\\icon.jpg").getImage());

        frame.setSize(900,600);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(panel);
        frame.setLayout(layout);



        topPanel.setLayout(flowLayout);

        leftpanel.setLayout(gridBagLayout);
        centerpanel.setLayout(gridBagLayout);
        rightpanel.setLayout(gridBagLayout);

        leftpanel.setPreferredSize(new Dimension(200,0));
        centerpanel.setPreferredSize(new Dimension(550,0));
        rightpanel.setPreferredSize(new Dimension(155,0));


        /*------------------------------------------------头部----------------------------------------*/
        JLabel port_label = new JLabel("端口号");
        JLabel name_label = new JLabel("管理员");
        port_textfield = new JTextField();         //端口号
        port_textfield.setPreferredSize(new Dimension(70,25));
        name_textfield = new JTextField();        //管理员
        name_textfield.setPreferredSize(new Dimension(150,25));

        String name = JOptionPane.showInputDialog("请输入本聊天室管理员昵称：");
        if (name != null && !name.isEmpty()) {
            name_textfield.setText(name);
        }
        name_textfield.setEditable(false);
        name_textfield.setBackground(Color.LIGHT_GRAY);
        connectButton = new JButton("连接");

        topPanel.add(port_label);
        topPanel.add(port_textfield);
        topPanel.add(name_label);
        topPanel.add(name_textfield);
        topPanel.add(connectButton);

        /*-------------------------------------Left----------------------------------------------------*/
        /**
         * @brief 系统信息显示区域：如 xxx进入了聊天 服务器已关闭
         */
        JLabel sysMsg_label = new JLabel("系统日志：");
        sysTextArea = new JTextPane();
        sysTextArea.setEditable(false);

        sysTextScroll = new JScrollPane();//设置滚动面板
        sysTextScroll.setViewportView(sysTextArea);//滚动面板内容

        sysVertical = new JScrollBar(JScrollBar.VERTICAL);//滚动条
        sysVertical.setAutoscrolls(true);
        sysTextScroll.setVerticalScrollBar(sysVertical);//设置滚动条

        leftpanel.add(sysMsg_label,new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        leftpanel.add(sysTextScroll, new GridBagConstraints(0, 1, 1, 1, 100, 100,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));


        /*-------------------------------------Right----------------------------------------------------*/

        user = new JLabel("当前连接用户:0");
        kick = new JButton("踢出");
        //管理列表的数据模型 向列表模型中添加、删除元素
        users_model = new DefaultListModel<>();
        userlist = new JList<String>(users_model);
        userListPane = new JScrollPane(userlist);
        rightpanel.add(user, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        rightpanel.add(kick, new GridBagConstraints(0, 1, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        rightpanel.add(userListPane, new GridBagConstraints(0, 2, 1, 1, 100, 100,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        /*-------------------------------------Center---------------------------------------------------*/
        JLabel userMsg_label = new JLabel("聊天室:");
        userTextArea = new JTextPane();
        userTextArea.setEditable(false);

        userTextScroll = new JScrollPane();
        userTextScroll.setViewportView(userTextArea);

        userVertical = new JScrollBar(JScrollBar.VERTICAL);
        userVertical.setAutoscrolls(true);
        userTextScroll.setVerticalScrollBar(userVertical);

        centerpanel.add(userMsg_label, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        centerpanel.add(userTextScroll, new GridBagConstraints(0, 1, 1, 1, 100, 100,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));




        panel.add(topPanel, "North");
//        panel.add(bottomPanel,"South");
        panel.add(leftpanel,"West");
        panel.add(rightpanel,"East");
        panel.add(centerpanel,"Center");

        SeverButtonEventListener severButtonEventListener = new SeverButtonEventListener(this);
        connectButton.addActionListener(severButtonEventListener);
        kick.addActionListener(severButtonEventListener);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirmDialog = JOptionPane.showConfirmDialog(frame,"确定关闭？","提示",
                        JOptionPane.YES_NO_OPTION);
                if(confirmDialog == JOptionPane.YES_OPTION){

                    frame.dispose();
                    System.exit(0);

                }
            }
        });
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setVisible(true);
        frame.repaint();



    }






    public synchronized void updateUserList(String userName) {
        users_model.addElement(userName);
        userlist.revalidate();
        userlist.repaint();
        user.setText("当前连接用户:"+users_model.size());
    }


    public void removeUserFromList(String userName) throws Exception {
        users_model.removeElement(userName);
        userlist.revalidate();
        userlist.repaint();
        user.setText("当前连接用户:"+users_model.size());
    }

    public void updateProgressBar(int value) {
        progressBar.setValue(value);
    }

    public Socket findSocketByUsername(String username) {
        Set<Map.Entry<Socket, String>> set = user_socket.entrySet();
        Iterator<Map.Entry<Socket, String>> iterator = set.iterator();

        while (iterator.hasNext()) {
            Map.Entry<Socket, String> entry = iterator.next();
            if (entry.getValue().equals(username)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public Sever(String title) throws IOException {
        this.title = title;
    }


    public static void main(String[] args) throws IOException {
        Sever sever = new Sever("聊天室服务端");
        sever.initview();
    }



}
