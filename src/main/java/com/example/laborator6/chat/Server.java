package com.example.laborator6.chat;

import com.example.laborator6.database.ConnectionInfoDataBaseRepository;
import com.example.laborator6.database.MessageDataBaseRepository;
import com.example.laborator6.domain.ConnectionInfo;
import com.example.laborator6.domain.Message;

import java.io.*;
import java.net.*;
import java.util.List;

public class Server implements Runnable {

    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(1234)) {

            Socket clientSocket;
            InputStream inputStream;
            DataInputStream dataInputStream;

            ConnectionInfoDataBaseRepository connectionInfoDataBaseRepository = new ConnectionInfoDataBaseRepository();
            MessageDataBaseRepository messageDataBaseRepository = new MessageDataBaseRepository();

            while (true) {

                clientSocket = server.accept();

                inputStream = clientSocket.getInputStream();
                dataInputStream = new DataInputStream(inputStream);

                String message = dataInputStream.readUTF();
                long idFriend = dataInputStream.readLong();
                long idUser = dataInputStream.readLong();

                System.out.println(message);
                System.out.println("Friend: " + idFriend);
                System.out.println("User: " + idUser);

                List<ConnectionInfo> connectionInformation = connectionInfoDataBaseRepository.getConnectionInfo();

                messageDataBaseRepository.saveMessage(new Message(message, idUser, idFriend));

                SendToClient(message, idUser, idFriend, connectionInformation);


            }
        } catch (IOException e) {
            System.out.println("Exception");
        }
    }

    private void SendToClient(String message, long idUser, long idFriend, List<ConnectionInfo> connectionInformation) throws IOException {

        InetAddress IP_User = null;
        InetAddress IP_Friend = null;

        for(ConnectionInfo info : connectionInformation){
            if(info.getIdUser().equals(idUser)){
                IP_User = InetAddress.getByName(info.getIP());
            }
            if(info.getIdUser().equals(idFriend)){
                IP_Friend = InetAddress.getByName(info.getIP());
            }
        }

        boolean sameConnection = IP_User != null && IP_User.equals(IP_Friend);

        System.out.println("Send to client");

        System.out.println(IP_Friend);

        try (Socket client = new Socket()) {


            client.connect(new InetSocketAddress(IP_Friend, (int) (idFriend + 5000)));
            OutputStream outputStream = client.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            dataOutputStream.writeUTF(message);
            dataOutputStream.writeBoolean(sameConnection);

        }
        catch(IOException e){
            System.out.println("Connection cannot be established");
        }
    }

}
