package chatRoom;

import java.io.*;
import java.net.Socket;
import static chatRoom.Msg2Panel.*;


/**
 * @brief 子线程  负责数据的接收 转发
 */
public class SeverThread extends Thread {

    public Socket clientSocket;
    public Sever server;
    public String Msg;


    public SeverThread(Socket clientSocket, Sever server) {
        this.clientSocket = clientSocket;
        this.server = server;


    }

    @Override
    public void run() {
        try {
            InputStream is = clientSocket.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            server.onlineSockets.add(clientSocket);

            while (true) {
                try {

                    Msg = dis.readUTF();

                    int index = Msg.indexOf(",");
                    if (index!=-1) {
                        String cmd = Msg.substring(0, index);
                        String msg = Msg.substring(index + 1);
                        switch (cmd) {
                            case "user": {
                                server.user_socket.put(clientSocket, msg);
                                handleNewUser(msg);

                                break;
                            }
                            case "msg": {
                                insertMessage(server.userTextArea, server.user_socket.get(clientSocket) + ":", msg, false);
                                break;
                            }
                            case "File": {

                                String sendName = dis.readUTF();
                                String receiverName = dis.readUTF();

                                String fileName = dis.readUTF(); // 读取文件名
                                long fileSize = dis.readLong(); // 读取文件大小
                                System.out.println(fileName);
                                System.out.println(fileSize);
                                String filemsg = sendName + "向" + receiverName + "发送了文件：" + fileName;
                                handleSysMsg(filemsg);
                                File file = new File(fileName);

                                Socket socket = server.findSocketByUsername(receiverName);
                                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());


                                dos.writeUTF("File," + receiverName + "/ni");
                                dos.writeUTF(fileName);
                                dos.writeLong(fileSize);
                                sendFileToClient(socket, file);

                                break;

                            }

                        }
                        if (!cmd.equals("File")) {
                            sendall(cmd + "," + server.user_socket.get(clientSocket) + "/" + msg);
                        }
                    }


                } catch (IOException e) {
                    System.out.println("error:"+e);
                    String name = server.user_socket.get(clientSocket);
                    if (name != null) {
                        server.removeUserFromList(name);
                        try {
                            handleuserremove(name + "退出了群聊",name);
                        }catch (Exception f){
                            f.printStackTrace();
                        }
                        server.onlineSockets.remove(clientSocket);
                        server.user_socket.remove(clientSocket);

                        dis.close();
                        clientSocket.close();


                    }



                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void handleNewUser(String userName) throws IOException {

        handleSysMsg(userName+"加入了聊天");
        server.updateUserList(userName);

    }
    public  void handleuserremove(String namemsg,String name) throws IOException {

        insertMessage(server.sysTextArea,"系统消息:",namemsg,true);
        sendall("exit,"+name+"/"+namemsg);

    }
    public  void handleSysMsg(String msg) throws IOException {
        insertMessage(server.sysTextArea,"系统消息:",msg,true);
        sendall("sys,"+server.user_socket.get(clientSocket)+"/"+msg);

    }

    public  void sendall(String msg) throws IOException {
        for (Socket socket : server.onlineSockets) {
            OutputStream os = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeUTF(msg);
            dos.flush();

        }


    }
    private void sendFileToClient(Socket clientSocket,File fileToSend) throws IOException {
        FileInputStream fis = new FileInputStream(fileToSend);
        BufferedOutputStream bos = new BufferedOutputStream(clientSocket.getOutputStream());
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
            bos.write(buffer, 0, bytesRead);
            bos.flush();
        }
    }
}


