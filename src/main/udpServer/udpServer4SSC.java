package main.udpServer;

/*
      This file is part of Joystick2SSC.

      Copyright (C) 2019  Gregor Schlechtriem

      This program is free software: you can redistribute it and/or modify
      it under the terms of the GNU General Public License as published by
      the Free Software Foundation, either version 3 of the License, or
      (at your option) any later version.

      This program is distributed in the hope that it will be useful,
      but WITHOUT ANY WARRANTY; without even the implied warranty of
      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
      GNU General Public License for more details.

      You should have received a copy of the GNU General Public License
      along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import main.Main;

import java.io.*;
import java.net.*;
import java.util.*;

import static java.lang.System.out;

public class udpServer4SSC {

    private static int rxPort = 12000;
    private static int txPort = 12001;
    private static String msgResponse = "";

    public static void connect2wLAN() throws SocketException, InterruptedException {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets)) {
            if ((netint.getName().contains("wlan")) && netint.isUp()){
                if (Main.Debug.ON) {
                    displayInterfaceInformation(netint);
                }
            }
        }
        udpCommandwithResponse("?", "");
    }

    private static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
        out.printf("Display name: %s\n", netint.getDisplayName());
        out.printf("Name: %s\n", netint.getName());
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            out.printf("InetAddress: %s\n", inetAddress);
        }
        out.print("is up: " + netint.isUp());
        out.print("\nis loopback: " + netint.isLoopback());
        out.print("\nis virtual: " + netint.isVirtual());
        out.print("\nis point to point: " + netint.isPointToPoint());
        out.print("\nsupports multicast: " + netint.supportsMulticast() + "\n");

    }

    static void udpCommandwithResponse(String sCommand, String sResponse) throws InterruptedException {
        Thread udpSender = new Thread (new UdpSender(sCommand));
        udpSender.start();
        Thread udpReceiver = new Thread (new UdpReceiver());
        udpReceiver.start();
        while (udpReceiver.isAlive()) {
            Thread.sleep(2);
        }
        if (Main.Debug.ON) {
            out.print("\nReceived from PiKoder: " + msgResponse + "\n");
        }
    }

    public static class UdpSender implements Runnable {
        /**
         * The command to be sent.
         */
        private final String sCommand;
        UdpSender(String sCommand) { this.sCommand = sCommand; }

        @Override
        public void run() {
            try (DatagramSocket serverSocket = new DatagramSocket(txPort)) {
                DatagramPacket datagramPacket = new DatagramPacket(
                    sCommand.getBytes(),
                    sCommand.length(),
                    InetAddress.getByName("192.168.4.1"),
                    // InetAddress.getByName("255.255.255.255"), // use for broadcast
                    txPort);
                serverSocket.send(datagramPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class UdpReceiver implements Runnable {


        UdpReceiver() {
        }

        @Override
        public void run() {
            /*
              Bind the client socket to the port on which you expect to
              read incoming messages
             */
            try (DatagramSocket clientSocket = new DatagramSocket(rxPort)) {
                /*
                  Create a byte array buffer to store incoming data. If the message length
                  exceeds the length of your buffer, then the message will be truncated. To avoid this,
                  you can simply instantiate the buffer with the maximum UDP packet size, which
                  is 65506. However, we will receive only short messages from the receiver
                 */
                byte[] buffer = new byte[100];
                // Set a timeout of 500 ms for the client.
                clientSocket.setSoTimeout(500);
                DatagramPacket datagramPacket = new DatagramPacket(buffer, 0, buffer.length);
                /*
                   The receive method will wait for 500 ms for data.
                   After that, the client will throw a timeout exception.
                  */
                clientSocket.receive(datagramPacket);
                StringBuilder receivedMessage = new StringBuilder (new String (datagramPacket.getData()));
                /*
                  The following loop will build the message if sent in single tokens.
                 */
                if (receivedMessage.charAt(1) == 0) {
                    int eomDetected = 3;
                    int i = 0;
                    while ((eomDetected != 0)) {
                        datagramPacket = new DatagramPacket(buffer, 0, buffer.length);
                        clientSocket.receive(datagramPacket);
                        String receivedToken = new String(datagramPacket.getData());
                        if (receivedToken.charAt(0) < ' ') eomDetected = eomDetected - 1;
                        else {
                            receivedMessage.setCharAt(i,receivedToken.charAt(0));
                            i += 1;
                        }
                    }
                }
                msgResponse = new String(receivedMessage);
            } catch (SocketException e) {
                e.printStackTrace();
                msgResponse = "";
            } catch (IOException e) {
                System.out.println("Timeout. Client is closing.");
                msgResponse = "";
            }
        }

    }
}


