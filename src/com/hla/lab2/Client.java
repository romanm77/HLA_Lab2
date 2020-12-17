package com.hla.lab2;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Client {

    public static void main(String[] args) throws IOException {
        try {
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName("localhost");

            byte[] sendingDataBuffer = new byte[100000];
            byte[] receivingDataBuffer = new byte[100000];
            byte[] receivedData2 = new byte[100000];

            String sentence = "Hello! I'm a client.";
            sendingDataBuffer = sentence.getBytes();

            DatagramPacket sendingPacket = new DatagramPacket(sendingDataBuffer, sendingDataBuffer.length,
                    IPAddress,3000);

            int i = 0;
            int bt = 3000;
            long length = receivingDataBuffer.length;

            while (length > bt) {
                clientSocket.send(sendingPacket);
                DatagramPacket receivingPacket = new DatagramPacket(receivingDataBuffer, bt);
                clientSocket.receive(receivingPacket);

                byte[] receivedData = receivingPacket.getData();
                System.arraycopy(receivedData, 0, receivedData2, bt * i, bt);

                length = length - bt;
                i = i + 1;
                }

            File soundFile = new File("src/com/hla/lab2/sound5.wav");
            FileOutputStream fos = new FileOutputStream(soundFile);
            fos.write(receivedData2);
            fos.close();

            AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.setFramePosition(0);
            clip.start();
            Thread.sleep(clip.getMicrosecondLength() / 1000);
            clip.stop();
            clip.close();
            System.out.println("Sound has been played!");
            clientSocket.close();
        }
        catch(SocketException | UnsupportedAudioFileException | LineUnavailableException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
