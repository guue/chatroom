package chatRoom;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static chatRoom.IP.*;
import static chatRoom.Msg2Panel.insertMessage;

public class SeverButtonEventListener implements ActionListener {

    public Sever server;
    SeverThread severThread;

    public SeverButtonEventListener(Sever server) {
        this.server = server;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();

        if (source == server.connectButton) {
            handleConnectButtonClick();

        } else if (source == server.kick) {
            try {
                handleKickButtonClick();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void handleConnectButtonClick() {
        String strport;
        int port;
        strport = server.port_textfield.getText();
        if (!ipCheckPort(strport))
            JOptionPane.showMessageDialog(server.frame, "Invalid port number", "Error", JOptionPane.ERROR_MESSAGE);
        port = Integer.parseInt(strport);

        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                insertMessage(server.sysTextArea, "系统消息:", "服务器已经启动，监听端口：" + port, true);

                server.connectButton.setEnabled(false);
                server.connectButton.setText("已连接");

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    severThread = new SeverThread(clientSocket, server);
                    severThread.start();

                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(server.frame, "Failed to start the server on port " + port,
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }).start();
    }


/*    private void handleKickButtonClick() throws IOException {
        String name = server.userlist.getSelectedValue();
        severThread.handleSysMsg(name + "被踢出了群聊");
        Socket key = null;
        Set<Map.Entry<Socket, String>> set = server.user_socket.entrySet();// entrySet()方法就是把map中的每个键值对变成对应成Set集合中的一个对象.
        // set对象中的内容如下:[3=c, 2=b, 1=a, 5=e, 4=c]
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry<Socket, String> entry = (Map.Entry<Socket, String>) it.next();
            // entry中的内容就是set集合中的每个对象(map集合中的一个键值对)3=c....
            // Map.Entry就是一种类型,专值map中的一个键值对组成的对象.
            if (entry.getValue().equals(name)) {
                key = entry.getKey();

            }

        }
//        System.out.println(key);
        key.close();
    }*/
    private void handleKickButtonClick() throws IOException {
        String name = server.userlist.getSelectedValue(); // 获取选中的用户名
        if(name!=null) {
            severThread.handleuserremove(name + "被踢出了群聊",name); // 向其他用户发送系统消息

            // 使用封装的方法来查找对应的 Socket
            Socket userSocket = server.findSocketByUsername(name);

            if (userSocket != null && !userSocket.isClosed()) {
                try {
                    userSocket.close(); // 关闭找到的 Socket
                } catch (IOException e) {
                    e.printStackTrace(); // 处理异常
                }
            }
        }
    }

}