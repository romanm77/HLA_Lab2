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

            byte[] sendingDataBuffer = new byte[64000];
            byte[] receivingDataBuffer = new byte[64000];

            String sentence = "Hello! I'm a client.";
            sendingDataBuffer = sentence.getBytes();

            DatagramPacket sendingPacket = new DatagramPacket(sendingDataBuffer, sendingDataBuffer.length,
                    IPAddress,3000);

            clientSocket.send(sendingPacket);

            DatagramPacket receivingPacket = new DatagramPacket(receivingDataBuffer,receivingDataBuffer.length);
            clientSocket.receive(receivingPacket);

            byte[] receivedData = receivingPacket.getData();
            File soundFile = new File("src/com/hla/lab2/sound5.wav");
            FileOutputStream fos = new FileOutputStream(soundFile);
            fos.write(receivedData);
            fos.close();

            AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.setFramePosition(0);
            clip.start();
            Thread.sleep(clip.getMicrosecondLength()/1000);
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
