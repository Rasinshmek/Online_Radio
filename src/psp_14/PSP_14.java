/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package psp_14;
import java.io.IOException;
import javax.swing.JFrame;

/**
 *
 * @author alpha
 */
public class PSP_14 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, Exception {
        Start f = new Start("Стартовое окно"); //Название формы        
        f.setVisible(true);//Видимость формы
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Действие при на жатии на крестик (закрытие программы)
        f.setSize(480, 720);//Размеры окна
        f.setResizable(false);//Масштабируемость
        f.setLocationRelativeTo(null);//Установка окнна пе центру 

        // TODO code application logic here
    }

}
