/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package psp_14;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JOptionPane;

/**
 *
 * @author alpha
 */
class Thread2 extends Thread {

    String ip = "";
    int check_connection = 0;

    public void setip(String ip) {
        this.ip = ip;
    }
    Socket s;

    public void socket_close() throws IOException {
        s.close();
    }

    public int check_connection() {
        return check_connection;
    }

    public void play() {
        AudioFormat format = new AudioFormat(48000.0f, 16, 2, true, false);
        SourceDataLine speakers;

        try {

            s = new Socket(ip, 8888);
            InputStream is = s.getInputStream();
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            speakers.open(format);
            speakers.start();
            int numBytesRead;
            byte[] data = new byte[2048000];
            check_connection = 1;
            while (true) {
                numBytesRead = is.read(data);
                speakers.write(data, 0, numBytesRead);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Что то пошло не так  ͡๏̯͡๏");
        }
    }

    @Override
    public void run() {
        play();
    }
}
