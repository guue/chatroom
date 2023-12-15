package chatRoom;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.Objects;


import static chatRoom.IP.*;

public class ClientButtonEventListener implements ActionListener {
    public Client client;
    public DataOutputStream dos;
    public Socket charClient;

    public ClientButtonEventListener(Client client) {
        this.client = client;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();
        if (source == client.head_connect) {
            handleClientConnectButtonClick();
        } else if (source == client.foot_send) {
            try {
                handleCilentSendMsgClick();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }else if (source == client.foot_sysClear){
            client.sysMsgArea.setText("");

        } else if (source == client.foot_userClear) {
            client.userMsgArea.setText("");
        }
        else if(source == client.foot_sendFile){
            try {
                handleClientSendFileClick();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else if (source == client.exitButton){
            try {
                handleClientExitClick();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }


    private void handleClientExitClick() throws IOException {
        if(Objects.equals(client.head_connect.getText(), "已连接")){
            charClient.close();
        }else{
            JOptionPane.showMessageDialog(client.frame, "还没有连接服务器", "错误", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void exitCurrentChatRoom() {
        client.frame.dispose();
    }
    private void joinOtherChatRoom() {
        Client client = new Client("聊天室");
        client.init();
    }


    private void handleCilentSendMsgClick() throws IOException {
        try {
            String Msg = client.text_field.getText();

            sendMsg("msg,", Msg);
            client.text_field.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(client.frame, "还没有连接服务器", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleClientConnectButtonClick() {
        String port;
        String ip;

        ip = client.host_textfield.getText();
        port = client.port_textfield.getText();
        if (!ipCheckHost(ip))
            JOptionPane.showMessageDialog(client.frame, "Invalid IP address", "Error", JOptionPane.ERROR_MESSAGE);

        if (!ipCheckPort(port))
            JOptionPane.showMessageDialog(client.frame, "Invalid Port", "Error", JOptionPane.ERROR_MESSAGE);
        new Thread(() -> {
            try {
                charClient = new Socket(ip, Integer.parseInt(port));
                dos= new DataOutputStream(charClient.getOutputStream());
                new ClinentReader(client, charClient).start();
                client.head_connect.setText("已连接");
                client.port_textfield.setEditable(false);
                client.name_textfield.setEditable(false);
                client.host_textfield.setEditable(false);
                client.head_connect.setEnabled(false);
                String name = client.name_textfield.getText();
                sendMsg("user,", name);




            } catch (IOException e) {
                JOptionPane.showMessageDialog(client.frame, "连接服务器失败！", "错误", JOptionPane.ERROR_MESSAGE);

            }
        }).start();

    }
    private void handleClientSendFileClick() throws IOException {
        String port_File;
        String ip_File;
        String name_Accept;
        String name_Send;
        if (Objects.equals(client.head_connect.getText(), "已连接")) {
            name_Send = client.name_textfield.getText();
            name_Accept = JOptionPane.showInputDialog("请输入要发送的人");
            ip_File = client.host_textfield.getText();
            port_File = client.port_textfield.getText();
            if (!Objects.equals(name_Accept, null)) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("选择文件");
                int result = fileChooser.showOpenDialog(client.frame);
                if (result == JFileChooser.APPROVE_OPTION) {

                    File selectedFile = fileChooser.getSelectedFile();
                    new Thread(() -> {
                        try {
                            Socket socket = new Socket(ip_File, Integer.parseInt(port_File));
                            FileInputStream fis = new FileInputStream(selectedFile);
                            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                            BufferedOutputStream bos = new BufferedOutputStream(dos);
                            dos.writeUTF("File," + "匿名" + "/" + "文件");
                            dos.writeUTF(name_Send);
                            dos.writeUTF(name_Accept);
                            dos.writeUTF(selectedFile.getName());
                            dos.writeLong(selectedFile.length());

                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            long totalBytesRead = 0;


                            while ((bytesRead = fis.read(buffer)) != -1) {
                                bos.write(buffer, 0, bytesRead);
                                totalBytesRead += bytesRead;

                            }
                        } catch (IOException e) {
                            System.out.println("error:" + e);

                        }
                    }).start();
                    System.out.println("选择的文件路径是：" + ((File) selectedFile).getAbsolutePath());
                }
            } else {
                JOptionPane.showMessageDialog(client.frame, "请输入要发送的人",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(client.frame, "请先连接",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public  void sendMsg(String cmd, String msg) throws IOException {
        dos.writeUTF(cmd + msg);
        dos.flush();

    }
}