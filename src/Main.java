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
public class Main {

    public static void main(String [] args){
        final Ant[] AntColony;                                   // инициализация колонии муравьев.
        final GGraph testGGraph ;                                // тестовый граф считанный из файла
        AntColony = new Ant[15];                                 // содание колонии

        // Создание окна отдельным потоком
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AppWindow(null, AntColony);
            }
        });
    }
    // Функция запускающая бессознательное муравьев
    public static void unconsciousStart(GGraph testGGraph, Ant[] AntColony){
        Unconscious Un1 = new Unconscious(testGGraph, AntColony);    // создание бессознательного
        Un1.start();                                                 // старт потока
    }
}