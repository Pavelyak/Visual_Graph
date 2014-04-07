import javax.swing.*;
import java.awt.*;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.event.*;

/**
 * Created by Ринат on 14.03.14.
 */
public class AppWindow extends Frame {

    public AppWindow(){
        addWindowListener(new MyWindowAdapter());    // регистрация оконного слушателя
    }

    class MyWindowAdapter extends WindowAdapter {    // закрытие окна при нажатии на крестик
        public void windowClosing(WindowEvent we) {
            System.exit(0);
        }
    }
}

