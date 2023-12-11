package chatRoom;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

import static chatRoom.Msg2Panel.insertMessage;

public class ClinentReader extends Thread {
    private Socket socket;
    public Client client;
    public DataOutputStream filewriter;
    public ClinentReader(Client client,Socket socket) {
        this.socket=socket;
        this.client = client;
    }

    @Override
    public void run(){
        try {
            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            int length = -1;
            byte[] buffer = new byte[4096];
            long totalBytesRead = 0;
            while (true) {
                try {

                    String Msg = dis.readUTF();
                    System.out.println(Msg);
                    int index1 = Msg.indexOf(",");
                    int index2 = Msg.indexOf("/");
//                    System.out.println(index1);
//                    System.out.println("index2 = " + index2);
                    if(index1!=-1 && index2!=-1) {
                        String cmd = Msg.substring(0, index1);
                        String name = Msg.substring(index1 + 1, index2);
                        String msg = Msg.substring(index2 + 1);
                        switch (cmd) {
                            case "sys":
                                insertMessage(client.sysMsgArea, "系统消息:", msg, true);
                                break;
                            case "msg":
                                insertMessage(client.userMsgArea, name + ":", msg, false);
                                if(!client.users_model.contains(name)){
                                    client.updateUserList(name);
                                }
                                break;
                            case "user":{
                                client.updateUserList(name);
                                break;
                            }
                            case "exit":{
                                insertMessage(client.sysMsgArea, "系统消息:", msg, true);
                                client.removeUserFromList(name);
                                break;
                            }
                            case "File": {
                                String fileName = dis.readUTF();
                                long fileSize = dis.readLong();
//                                System.out.println(fileName);
//                                System.out.println(fileSize);
                                JFileChooser fileChooser = new JFileChooser();
                                fileChooser.setDialogTitle("保存文件");
                                fileChooser.setSelectedFile(new File(fileName));
                                int userSelection = fileChooser.showSaveDialog(client.frame);
                                if (userSelection == JFileChooser.APPROVE_OPTION) {
                                    File fileToSave = fileChooser.getSelectedFile();
                                    receiveFile(dis,fileToSave, fileSize);
                                }

                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println("error:"+e);
                    client.removeAllUserFromList();
                    client.head_connect.setText("连接");
                    client.port_textfield.setEditable(true);
                    client.name_textfield.setEditable(true);
                    client.host_textfield.setEditable(true);
                    client.head_connect.setEnabled(true);
                    socket.shutdownOutput();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void receiveFile(DataInputStream dis, File file, long fileSize) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            long totalBytesRead = 0;

            while (totalBytesRead < fileSize) {
                bytesRead = dis.read(buffer, 0, (int) Math.min(buffer.length, fileSize - totalBytesRead));
                bos.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
            }
        }
    }

}
