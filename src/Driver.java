import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.lang.*;
import java.io.*;
import java.util.Scanner;


/**
 * Created by Павел Яковлев and modified by Ринат Даутов on 14.03.14 till the end.
 * Класс, контролирующий всю внутренню деятельность системы. То бишь main
 */
public class Driver {

    private GraphWriter graphWriter; // класс, занимающийся отрисовкой графа

    public  Driver (GGraph graph, Ant[] AntColony) {
        try {
            // Make look and feel, system specific
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        int fringe = 40; // размер рамки

        // создаем graphWriter
        graphWriter = new GraphWriter(800, 600, fringe, graph, AntColony);

        // Panel  - в ней будет отрисовываться сам граф
        JPanel graphPanel = new JPanel(new BorderLayout());
        graphPanel.add(graphWriter, BorderLayout.CENTER);

        // оконное приложение, с которым происходит работа
        AppWindow appwin = new AppWindow();                // создание окна
        appwin.setSize(800, 600);                          // установка размера, названия и добавления панели отрисовки
        appwin.setTitle("An Ant visualization");
        appwin.add(graphPanel, BorderLayout.CENTER);
        appwin.setVisible(true);                           // делаем видимым
    }

    public static void main(String [ ] args){
        Ant[] AntColony;                                   // инициализация колонии муравьев.

        //GGraph testGGraph = new GGraph(5, 20);           // создание тестового графа
        GGraph testGGraph = GGraph.myread();               // тестовый граф считанный из файла
        testGGraph.adjust();                               // пружины
        AntColony = new Ant[3];                            // содание колонии

        System.out.println("граф создан");                 // сервисная информация

        Unconscious Un1 = new Unconscious(testGGraph,AntColony);    // создание бессознательного
        Un1.start();                                      // старт потока
        new Driver(testGGraph, AntColony);                // визуализация тест. графа


    }
}