package com.hla.lab2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server {

    public static void main(String[] args) throws IOException {
        try {
            DatagramSocket serverSocket = new DatagramSocket(3000);
            File soundFile = new File("src/com/hla/lab2/sound1.wav");
            byte[] receivingDataBuffer = new byte[64000];
            byte[] sendingDataBuffer = new byte[64000];

            DatagramPacket inputPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);
            System.out.println("Waiting for client connection...");

            serverSocket.receive(inputPacket);
            String receivedData = new String(inputPacket.getData());
            System.out.println("Customer message: "+receivedData);

            FileInputStream fis = new FileInputStream(soundFile);
            fis.read(sendingDataBuffer);

            InetAddress senderAddress = inputPacket.getAddress();
            int senderPort = inputPacket.getPort();

            DatagramPacket outputPacket = new DatagramPacket(sendingDataBuffer, sendingDataBuffer.length,
                    senderAddress,senderPort);

            serverSocket.send(outputPacket);
            serverSocket.close();
        }
        catch (SocketException e){
            e.printStackTrace();
        }
    }
}
