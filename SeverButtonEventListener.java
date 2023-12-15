package chatRoom;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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