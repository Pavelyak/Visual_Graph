import javax.swing.*;
import java.awt.*;
import java.lang.*;


/**
 * Created by Павел Яковлев on 14.03.14.
 * Класс, контролирующий всю внутренню деятельность системы. То бишь main
 */
public class Driver {

    private GraphWriter graphWriter; // класс, занимающийся отрисовкой графа

    public  Driver (GGraph graph) {
        int fringe = 40; // размер рамки

        // создаем graphWriter
        graphWriter = new GraphWriter(800, 600, fringe, graph);

        // Panel  - в ней будет отрисовываться сам граф
        JPanel graphPanel = new JPanel(new BorderLayout());
        graphPanel.add(graphWriter, BorderLayout.CENTER);

        // оконное приложение, с которым происходит работа
        AppWindow appwin = new AppWindow();           // создание окна
        appwin.setSize(800, 600);                    // установка размера, названия и добавления панели отрисовки
        appwin.setTitle("An Ant visualization");
        appwin.add(graphPanel, BorderLayout.CENTER);
        appwin.setVisible(true);                      // делаем видимым
    }


    public static void main(String [ ] args){
        GGraph testGGraph = new GGraph(10,20); // создание тестового графа
        new Driver(testGGraph);                // визуализация тест. графа
        new Ant(testGGraph);                   // создание муравейки
        Unconscious Un1 = new Unconscious(testGGraph); //создание бессознательного
        Un1.start();  //старт потока
       }
    }

