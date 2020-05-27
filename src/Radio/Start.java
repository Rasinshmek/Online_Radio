/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Radio;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;

/**
 *
 * @author alpha
 */
public class Start extends JFrame {

    JButton start, stop, custom, start_stream, offmc;
    JLabel l1, lsong, check_connection;
    JComboBox box;
    JTextField ip;
    Handler handler = new Handler();
    Thread1[] array = new Thread1[1];
    Thread2[] array2 = new Thread2[1];
    Thread_Server[] array3 = new Thread_Server[1];
    ArrayList<String> items_song = new ArrayList<String>();
    ArrayList<String> items_URL = new ArrayList<String>();
    String[] mas = new String[1];
    int counter = 0;
    int i = 0;

    public Start(String s) throws IOException, Exception {
        super(s);
        refreshs();
        Thread3 t3 = new Thread3();
        t3.start();
        box = new JComboBox(new DefaultComboBoxModel(items_URL.toArray()));
        setLayout(new BorderLayout());//Создание слоя для катинки
        setContentPane(new JLabel(new ImageIcon("Paw.gif")));//Добавление картинки на фон главного окна
        setLayout(new FlowLayout());//Создание слоя для всего кроме картинки      
        start = new JButton("Старт");
        stop = new JButton("Стоп");
        offmc = new JButton("Выключить микрофон");
        custom = new JButton("Подключится к серверу");
        start_stream = new JButton("Начать трансляцию");
        lsong = new JLabel();
        ip = new JTextField();
        check_connection = new JLabel("Состояние");
        l1 = new JLabel("<html>"
                + "<div style='text-align: center;'style=color:#FFFFFF>"
                + "<strong>Курсовой проект на тему:</strong>"
                + "<br/>"
                + "<br/>Разработка клиент-серверного приложения "
                + "<br/>для воспроизведения и передачи голоса."
                + "<br/>"
                + "<br/>"
                + "<br/>"
                + "<br/>"
                + "<br/>"
                + "<br/>"
                + "<br/>"
                + "<br/>"
                + "<br/>"
                + "<br/><div style='text-align: right;'>Выполнил:"
                + "<br/>Студент группы 3ИТ-5"
                + "<br/>Лушаков Иван Витальевич</div>"
                + "</div>"
                + "</html>");

        box.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        check_connection.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));//Установка шрифтов 
        l1.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));//Установка шрифтов  
        lsong.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));//Установка шрифтов 
        custom.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));//Установка шрифтов   
        start.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));//Установка шрифтов  
        stop.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));//Установка шрифтов  
        ip.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));//Установка шрифтов  
        offmc.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));//Установка шрифтов  
        start_stream.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));//Установка шрифтов       
        box.addActionListener(handler);//Добавление слушателей
        start_stream.addActionListener(handler);//Добавление слушателей
        offmc.addActionListener(handler);//Добавление слушателей
        start.addActionListener(handler);//Добавление слушателей
        stop.addActionListener(handler);//Добавление слушателей
        custom.addActionListener(handler);//Добавление слушателей
        ip.setPreferredSize(new Dimension(450, 30));

        add(l1);//Добавление объектов на форму  
        add(box);
        add(stop);
        add(start);
        add(start_stream);
        add(lsong);
        add(custom);
        add(ip);
        add(offmc);
        add(check_connection);
    }

    public void refreshs() {
        URL oURL;
        URLConnection oConnection;
        BufferedReader oReader;
        String sLine;
        StringBuilder sbResponse;
        String sResponse = null;

        try {
            oURL = new URL("http://air.radiorecord.ru:805/status.xsl");
            oConnection = oURL.openConnection();
            oReader = new BufferedReader(new InputStreamReader(oConnection.getInputStream()));
            sbResponse = new StringBuilder();
            while ((sLine = oReader.readLine()) != null) {
                sbResponse.append(sLine);
            }
            sResponse = sbResponse.toString();//HTML строка для парсинга 
            String[] step1 = sResponse.split("Current Song:</td><td class=\"streamdata\">");
            int ii = 0;
            for (int i = 1; i < step1.length; i++) {
                String[] step2 = step1[i].split("</td>");
                String[] step3 = step1[i - 1].split("Mount Point /");
                String[] step4 = step3[1].split("</h3>");
                String[] step5 = step4[0].split("_");
                if (step5[1].equals("aac")) {

                } else {
                    items_URL.add(ii, "http://air.radiorecord.ru:805/" + step4[0]);
                    if (step2[0].equals("Unknown") || step2[0].equals(" - ") || step2[0].equals("")) {
                        items_song.add(ii, "Неизвестно");
                    } else {

                        items_song.add(ii, step2[0]);
                    }
                    ii++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class Handler implements ActionListener {//Класс слушателя

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == box) {
                try {
                    array[i].stop();
                    array[i] = null;

                } catch (Exception EX) {
                }

                array[i] = new Thread1();
                array[i].setURL((String) box.getSelectedItem());
                array[i].start();
                lsong.setText("<html><div style=color:#ff0000>" + items_song.get(box.getSelectedIndex()) + "</div></html>");
            }

            if (e.getSource() == stop) {
                try {
                    array[i].stop();
                    array[i] = null;

                } catch (Exception EX) {
                }
                try {
                    array2[i].socket_close();
                    Thread.sleep(30);
                    array2[i].stop();
                    array2[i] = null;
                    check_connection.setText("Отключен");
                } catch (Exception EX) {
                }

            }

            if (e.getSource() == start) {
                try {
                    array[i].stop();
                    array[i] = null;

                } catch (Exception EX) {
                }

                array[i] = new Thread1();
                array[i].setURL((String) box.getSelectedItem());
                array[i].start();
                lsong.setText("<html><div style=color:#ff0000>" + items_song.get(box.getSelectedIndex()) + "</div></html>");

            }
            if (e.getSource() == start_stream) {
                try {
                    array[i].stop();
                    array[i] = null;
                    array2[i].stop();
                    array2[i] = null;

                } catch (Exception EX) {
                }

                array3[i] = new Thread_Server();
                array3[i].start();

            }
            if (e.getSource() == offmc) {
                if (counter == 1) {
                    try {
                        array3[i].offmc(1);
                        counter--;
                        offmc.setText("Выключить микрофон");
                    } catch (Exception EX) {
                    }

                } else {
                    try {
                        array3[i].offmc(0);
                        counter++;
                        offmc.setText("Включить микрофон");
                    } catch (Exception EX) {
                    }

                }

            }
            if (e.getSource() == custom) {
                try {
                    array2[i].stop();
                    array2[i] = null;

                } catch (Exception EX) {
                }
                array2[i] = new Thread2();
                array2[i].setip(ip.getText());
                array2[i].start();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (array2[i].check_connection() == 1) {
                    check_connection.setText("Подключено");
                }

            }

        }
    }

    class Thread3 extends Thread {

        public void refresh() {
            URL oURL;
            URLConnection oConnection = null;
            BufferedReader oReader = null;
            String sLine = null;
            StringBuilder sbResponse = null;
            String sResponse = null;

            try {
                oURL = new URL("http://air.radiorecord.ru:805/status.xsl");
                oConnection = oURL.openConnection();
                oReader = new BufferedReader(new InputStreamReader(oConnection.getInputStream()));
                sbResponse = new StringBuilder();
                while ((sLine = oReader.readLine()) != null) {
                    sbResponse.append(sLine);
                }
                sResponse = sbResponse.toString();//HTML строка для парсинга 
                String[] step1 = sResponse.split("Current Song:</td><td class=\"streamdata\">");
                int ii = 0;
                for (int i = 1; i < step1.length; i++) {
                    String[] step2 = step1[i].split("</td>");
                    String[] step3 = step1[i - 1].split("Mount Point /");
                    String[] step4 = step3[1].split("</h3>");
                    String[] step5 = step4[0].split("_");
                    if (step5[1].equals("aac")) {
                        i--;
                    } else {
                        items_URL.set(ii, "http://air.radiorecord.ru:805/" + step4[0]);
                        if (step2[0].equals("Unknown") || step2[0].equals(" - ") || step2[0].equals("")) {
                            items_song.set(ii, "Неизвестно");
                        } else {

                            items_song.set(ii, step2[0]);
                        }
                        ii++;
                    }
                }
            } catch (IOException e) {
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep(4000);
                    refresh();
                    lsong.setText("<html><div style=color:#ff0000>" + items_song.get(box.getSelectedIndex()) + "</div></html>");
                }
            } catch (InterruptedException ex) {
            }

        }
    }
}
