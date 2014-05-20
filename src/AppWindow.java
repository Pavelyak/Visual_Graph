

import javafx.scene.control.ProgressBar;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.event.*;
import java.io.*;

/**
 * Created by Ринат on 14.03.14.
 * Appwindow - окно, с которым происходит работа.
 */
public class AppWindow extends JFrame implements ActionListener  {

    private GraphWriter graphWriter;        // Canvas, в котором  отрисовывается граф.
    private GGraph graph;                   // Экземпляр графа
    private Ant[] antColony;                // Экземпляр колонии
    public JPanel mainPanel;                // Панель в которой размещаются все остальные компоненты
    public JButton paintBtn;                // Кнопка отрисовки
    private TaskSprings taskSprings;
    private JProgressBar progressBar;
    private TextField textIteration;
    private JPanel textPanel;

    // Конструктор базового окна.
    public AppWindow(GGraph graph, Ant[] antColony) {
        // Инициализация окна,  переменных.
        super("An Ant visualization");
        this.graph = graph;
        this.antColony = antColony;

        addWindowListener(new MyWindowAdapter());        // регистрация оконного слушателя, нужный при закрытии окна.
        this.setSize(900, 700);                          // установка размера

        //Создание главной панели
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());         // Установка менеджера компоновки.

        // создание кнопочной панели
        JPanel buttonsPanel = new JPanel(new FlowLayout());

        // Создание paintBtn - кнопка отрисовки
        paintBtn = new JButton("Начать отрисовку");
        buttonsPanel.add(paintBtn);
        paintBtn.addActionListener(this);                // Добавляем слушателя к кнопке, который следит за нажатием.
        paintBtn.setFocusable(false);                    // На кнопку не наведен фокус.

        //Кнопка алгоритма
        JButton btn = new JButton("Алгоритм");
        buttonsPanel.add(btn);
        btn.addActionListener(this);
        btn.setFocusable(false);

        //Кнопка выбора файла
        JButton btnFile = new JButton("Выбрать файл");
        buttonsPanel.add(btnFile);
        btnFile.addActionListener(this);
        btnFile.setFocusable(false);

        //ProgressBar
        progressBar = new JProgressBar();
        mainPanel.add(progressBar, BorderLayout.SOUTH);

        mainPanel.add(buttonsPanel, BorderLayout.NORTH); // Добавляем кнопочную панель вверху окна

        //Текстовые поля
        textPanel = new JPanel(new GridLayout(20, 1));     // 20 строк, 1 столбец в менеджере компоновки
        Label lblIteration = new Label("Iteration");
        textIteration = new TextField();
        textIteration.addActionListener(this);
        textPanel.add(lblIteration);
        textPanel.add(textIteration);

        mainPanel.add(textPanel, BorderLayout.EAST);       // На западе

        // финальные шаги
        add(mainPanel);                                  // Добавление панели к окну
        this.setVisible(true);                           // установка окна видимым для пользователя
    }

    class MyWindowAdapter extends WindowAdapter {    // класс, реализующий пустой интерфейс слушателя окна
        public void windowClosing(WindowEvent we) {  // реализация выхода, при нажатии на крестик
            System.exit(0);
        }
    }
    // Класс, выполняющий задание - расположение вершин. Все делается в отдельном потоке.
    class TaskSprings extends SwingWorker<Void, Void> {
        @Override
        public Void doInBackground() {
            graph.adjust();
            paintGraph(graph, antColony);
            return null;
        }
        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            setCursor(null);                         //turn off the wait cursor
            progressBar.setVisible(false);
        }
    }

    public void actionPerformed(ActionEvent ae) {    // Функция, реализующая действия при нажатии на кнопку
        String str = ae.getActionCommand();
        if (str.equals("Начать отрисовку")) {
            paintBtn.setVisible(false);                      // Исчезновение кнопки
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            progressBar.setIndeterminate(true);              // Бесконечный ПрогрессБар

            //Выполнение задания - отрисовки
            taskSprings = new TaskSprings();
            taskSprings.execute();
        }
        else if (str.equals("Алгоритм")) {

            progressBar.setStringPainted(true);
            progressBar.setMinimum(0);
            progressBar.setMaximum(100);
            Main.unconsciousStart(graph, antColony);  // Старт муравьев
            // Запуск отрисовки муравьев отдельным потоком
            // чтобы программа реагировала на действие пользователя в GUI
            Thread t = new Thread(graphWriter);
            t.start();
        }
        else if (str.equals("Выбрать файл")) {
            JFileChooser fileopen = new JFileChooser();
            int ret = fileopen.showDialog(null, "Открыть файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileopen.getSelectedFile();
                graph = GGraph.myread(file);
            }
        }
        else if (ae.getSource() == textIteration){
            // Перевод строки из поля в число
            String strInt = textIteration.getText();
            try {
                int iteration = Integer.parseInt(strInt);
                Unconscious.setIterationsLimit(iteration);  // Установка макс. лимита
                textIteration.setEnabled(false);
            } catch (NumberFormatException e) {
                System.err.println("Неверный формат строки!");
            }
        }
    }

    //Функция, реализующая отрисовку графа при нажатии на кнопуу
    public void paintGraph(GGraph graph, Ant[] AntColony ) {

        // создаем graphWriter - Canvas на котором происходит отрисовка
        int fringe = 40;                                      // размер рамки
        graphWriter = new GraphWriter(800, 600, fringe,       // Размер области рисования. Он пока не масшабируется
                                      graph, AntColony);      // Экземпляры класса графа и муравьиной колонии

        // Добавление поля для отрисовки graphwriter (Canvas).
        mainPanel.add(graphWriter, BorderLayout.CENTER);      // Добавляем в центр панели
        mainPanel.revalidate();                               // Обновление панели
    }
}

