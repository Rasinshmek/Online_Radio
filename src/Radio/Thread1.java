/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Radio;
import java.io.BufferedInputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.player.Player;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author alpha
 */
class Thread1 extends Thread {

    String song = "";
    public void setURL(String url) {
        if (url.trim().isEmpty()) {

        } else {
            this.song = url;
        }
    }

    public void play() {
        try {
            Player mp3player = null;
            BufferedInputStream in = null;
            in = new BufferedInputStream(new URL(song).openStream());
            mp3player = new Player(in);
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
            }
            mp3player.play();
        } catch (Exception ex) {
        }
    }

    @Override
    public void run() {
        play();
    }
}
