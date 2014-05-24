

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;

/**
 * Created by Ринат on 14.03.14.
 * Appwindow - окно, с которым происходит работа.
 */
public class AppWindow extends JFrame implements ActionListener, PropertyChangeListener {

    private GraphWriter graphWriter;        // Canvas, в котором  отрисовывается граф.
    private GGraph graph;                   // Экземпляр графа
    private Ant[] antColony;                // Экземпляр колонии
    private JPanel mainPanel;                // Панель в которой размещаются все остальные компоненты
    private JPanel buttonsPanel;             // Панель кнопок
    private JPanel textPanel;               // Панель текстовых полей
    private JButton paintBtn;                // Кнопка отрисовки
    private JButton algoritmBtn;             // Кнопка запуска алгоритма
    private JButton fileBtn;                 // Кнопка выбора файла
    private TaskSprings taskSprings;        // Задание - отрисовать пружины. Должно выполняться в отдельном потоке.
    private JProgressBar progressBar;       // Прогресс в работе
    private TextField iTextField;
    private int iterationsLimit;


    // Конструктор базового окна.
    public AppWindow(GGraph graph, Ant[] antColony) {

        // Инициализация окна,  переменных.
        super("An Ant visualization");
        this.graph = graph;
        this.antColony = antColony;
        this.setSize(900, 700);                          // установка размера

        //Создание главной панели
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());         // Установка менеджера компоновки.

        createButtons();
        createTextFields();
        //ProgressBar
        progressBar = new JProgressBar();

        mainPanel.add(progressBar, BorderLayout.SOUTH);
        mainPanel.add(buttonsPanel, BorderLayout.NORTH);   // Добавляем кнопочную панель вверху окна
        mainPanel.add(textPanel, BorderLayout.EAST);       // На западе

        // финальные шаги
        add(mainPanel);                                            // Добавление панели к окну
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);   // Выход при нажатии на крестик
        this.setVisible(true);                                     // Установка окна видимым для пользователя
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
            /*Toolkit
           .getDefaultToolkit()
           .beep();*/
            setCursor(null);                         //turn off the wait cursor
            progressBar.setVisible(false);
        }
    }

    // Функция, реализующая действия при нажатии на кнопку
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == paintBtn) {
            paintBtnProcessing();
        }
        else if (ae.getSource() == algoritmBtn) {
            algortmBtnProcessing();
        }
        else if (ae.getSource() == fileBtn) {
            fileBtnProcessing();
        }
        else if (ae.getSource() == iTextField) {
            iTextFieldProcessing();
        }
    }

    // Функция, получающая на вход прогресс длительной задачи
    // Устанавливает процент выполнения данной задачи на прогрессбаре
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
        }
    }


    //Функция, реализующая отрисовку графа при нажатии на кнопуу
    public void paintGraph(GGraph graph, Ant[] AntColony ) {
        // создаем graphWriter - Canvas на котором происходит отрисовка
        int fringe = 40;                                      // размер рамки
        graphWriter = new GraphWriter(800, 600, fringe,       // Размер области рисования.
                                      graph, AntColony);      // Экземпляры класса графа и муравьиной колонии

        // Добавление поля для отрисовки graphwriter (Canvas).
        mainPanel.add(graphWriter, BorderLayout.CENTER);      // Добавляем в центр панели
        mainPanel.revalidate();                               // Обновление панели
    }


    // ButtonProcessing функции
    private void algortmBtnProcessing() {
        progressBar.setIndeterminate(false);
        progressBar.setStringPainted(true);
        progressBar.setMinimum(0);
        progressBar.setMaximum(iterationsLimit);
        progressBar.setVisible(true);

        Unconscious un1 = new Unconscious(graph, antColony);         // создание бессознательного
        un1.start();                                                 // старт потока
        un1.addPropertyChangeListener(this);
        //new Thread(graphWriter).start();                           // Отрисовка муравьев
    }

    private void fileBtnProcessing() {
        JFileChooser fileopen = new JFileChooser();

        int ret = fileopen.showDialog(null, "Открыть файл");
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileopen.getSelectedFile();
            graph = GGraph.myread(file);
        }
    }

    private void paintBtnProcessing() {
        paintBtn.setVisible(false);                      // Исчезновение кнопки
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        progressBar.setIndeterminate(true);              // Бесконечный ПрогрессБар

        //Выполнение задания - отрисовки
        taskSprings = new TaskSprings();
        taskSprings.execute();
    }

    private void iTextFieldProcessing() {
        // Перевод строки из поля в число
        String strInt = iTextField.getText();
        try {
            iterationsLimit = Integer.parseInt(strInt);
            Unconscious.setIterationsLimit(iterationsLimit);  // Установка макс. лимита
            iTextField.setEnabled(false);
        } catch (NumberFormatException e) {
            System.err.println("Неверный формат строки!");
        }
    }


    // Create функции
    private void createTextFields() {
        //Текстовые поля
        textPanel = new JPanel(new GridLayout(20, 1));     // 20 строк, 1 столбец в менеджере компоновки
        Label lblIteration = new Label("Iteration");
        iTextField = new TextField();
        iTextField.addActionListener(this);
        textPanel.add(lblIteration);
        textPanel.add(iTextField);
    }
    private void createButtons() {
        // создание кнопочной панели
        buttonsPanel = new JPanel(new FlowLayout());
        // Создание paintBtn - кнопка отрисовки
        paintBtn = new JButton("Начать отрисовку");
        buttonsPanel.add(paintBtn);
        paintBtn.addActionListener(this);                // Добавляем слушателя к кнопке, который следит за нажатием.
        paintBtn.setFocusable(false);                    // На кнопку не наведен фокус.

        //Кнопка алгоритма
        algoritmBtn = new JButton("Алгоритм");
        buttonsPanel.add(algoritmBtn);
        algoritmBtn.addActionListener(this);
        algoritmBtn.setFocusable(false);

        //Кнопка выбора файла
        fileBtn = new JButton("Выбрать файл");
        buttonsPanel.add(fileBtn);
        fileBtn.addActionListener(this);
        fileBtn.setFocusable(false);
    }
}

