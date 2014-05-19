import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.event.*;

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

    // Конструктор базового окна.
    public AppWindow(GGraph graph, Ant[] antColony) {
        // Инициализация окна,  переменных.
        super("An Ant visualization");
        this.graph = graph;
        this.antColony = antColony;

        addWindowListener(new MyWindowAdapter());        // регистрация оконного слушателя, нужный при закрытии окна.
        this.setSize(800, 600);                          // установка размера

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
        mainPanel.add(buttonsPanel, BorderLayout.NORTH); // Добавляем кнопочную панель вверху окна.

        // финальные шаги
        add(mainPanel);                                  // Добавление панели к окну
        this.setVisible(true);                           // установка окна видимым для пользователя
    }

    class MyWindowAdapter extends WindowAdapter {    // класс, реализующий пустой интерфейс слушателя окна
        public void windowClosing(WindowEvent we) {  // реализация выхода, при нажатии на крестик
            System.exit(0);
        }
    }

    public void actionPerformed(ActionEvent ae) {    // Функция, реализующая действия при нажатии на кнопку
        String str = ae.getActionCommand();
        if (str.equals("Начать отрисовку")) {
            paintGraph(graph, antColony);            // Отображение графа на экране
            paintBtn.setVisible(false);              // Исчезновение кнопки
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

