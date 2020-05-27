/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Radio;

/**
 *
 * @author alpha
 */
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Thread_Server extends Thread {

    static List<Sender> senderList = new ArrayList<Sender>();
    static MicrophoneReader mr;
    static volatile Integer sendersCreated = 0;
    static volatile Integer numBytesRead;
    static volatile Integer senderNotReady = 0;
    static volatile byte[] data;
    static volatile byte[] data2;
    static final Object monitor = new Object();
    static int param = 1;

    @Override
    public void run() {
        main();
    }

    protected void offmc(int param) {
        this.param = param;

    }

    protected void main() {
        try {
            mr = new MicrophoneReader();
            mr.start();
            ServerSocket ss = new ServerSocket(8888);
            Scanner sc = new Scanner(System.in);
            //while (!sc.next().equals("quit")) {
            while (true) {
                Socket s = ss.accept();
                Sender sndr = new Sender(s);
                senderList.add(sndr);
                sndr.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mr.setFinishFlag();

            for (Sender sndr : senderList) {
                sndr.setFinishFlag();
            }

        }
    }

    static class Sender extends Thread {

        Socket s;
        volatile boolean finishFlag;
        int position;
        int senderNumber;

        public Sender(Socket s) {
            this.s = s;
            finishFlag = false;
            System.out.print("Sender started: #");
            senderNumber = ++sendersCreated;
            System.out.println(senderNumber);
        }

        public void setFinishFlag() {
            finishFlag = true;
        }

        public void run() {
            try {
                OutputStream os = s.getOutputStream();

                while (!finishFlag) {
                    synchronized (monitor) {
                        senderNotReady++;

                        monitor.wait();
                        if (param == 0) {
                            os.write(data2, 0, 0);
                        } else {
                            os.write(data, 0, numBytesRead);
                        }

                        os.flush();

                        senderNotReady--;
                    }
                    Thread.sleep(10);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class MicrophoneReader extends Thread {

        volatile boolean finishFlag;

        AudioFormat format = new AudioFormat(48000.0f, 16, 2, true, false);
        //AudioFormat format = new AudioFormat(8000.0f, 8, 1, true, false);
        int CHUNK_SIZE = 102400;
        TargetDataLine mixer;

        public MicrophoneReader() {
            finishFlag = false;
            System.out.println("Microphone reader started");
        }

        public void setFinishFlag() {
            finishFlag = true;
        }

        public void run() {
            try {
                mixer = AudioSystem.getTargetDataLine(format);

                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                mixer = (TargetDataLine) AudioSystem.getLine(info);
                mixer.open(format);

                data = new byte[CHUNK_SIZE];
                mixer.start();

                while (!finishFlag) {
                    synchronized (monitor) {
                        if (senderNotReady == sendersCreated) {
                            monitor.notifyAll();
                            continue;
                        }

                        numBytesRead = mixer.read(data, 0, CHUNK_SIZE);
                    }

                    Thread.sleep(10);
                }
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            } catch (InterruptedException ex) {
                Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
