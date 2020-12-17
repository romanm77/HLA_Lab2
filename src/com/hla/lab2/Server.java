package com.hla.lab2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {

    public static void main(String[] args) throws IOException {
        try {
            DatagramSocket serverSocket = new DatagramSocket(3000);
            File soundFile = new File("src/com/hla/lab2/sound1.wav");
            byte[] receivingDataBuffer = new byte[100000];
            byte[] sendingDataBuffer = new byte[100000];

            DatagramPacket inputPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);
            System.out.println("Waiting for client connection...");

            ExecutorService executorService = Executors.newFixedThreadPool(10);

            AtomicBoolean needPrint = new AtomicBoolean(true);

            while (true) {

                serverSocket.receive(inputPacket);

                executorService.submit(() -> {

                    String receivedData = new String(inputPacket.getData());

                    if (needPrint.get()) {
                        System.out.println("Customer message: " + receivedData);
                    }
                    needPrint.set(false);

                    try {
                        FileInputStream fis = new FileInputStream(soundFile);
                        fis.read(sendingDataBuffer);

                        InetAddress senderAddress = inputPacket.getAddress();
                        int senderPort = inputPacket.getPort();

                        long length = soundFile.length();
                        int bt = 3000;

                        while (length > 0) {
                            DatagramPacket outputPacket = new DatagramPacket(sendingDataBuffer, bt,
                                    senderAddress, senderPort);

                            serverSocket.send(outputPacket);

                            System.arraycopy(sendingDataBuffer, bt, sendingDataBuffer, 0, sendingDataBuffer.length - bt);
                            length = length - bt;
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            }
        }
        catch (SocketException e){
            e.printStackTrace();
        }
    }
}
