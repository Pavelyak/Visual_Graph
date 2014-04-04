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
        addWindowListener(new MyWindowAdapter());    // регистрация слушателей
    }

    public static void main(String args[]){
        AppWindow appwin = new AppWindow();       // создаем окно, устанавливаем имя, размер и делаем видимым.
        appwin.setSize(800,600);
        appwin.setTitle("An Ant visualization");
        appwin.setVisible(true);

        GNode gNode = new GNode();
    }



    class MyWindowAdapter extends WindowAdapter {    // закрытие окна при нажатии на крестик
        public void windowClosing(WindowEvent we) {
            System.exit(0);
        }
    }
}

