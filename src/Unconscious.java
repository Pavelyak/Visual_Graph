import java.io.*;
import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.beans.*;


/**
 * Created by Павел Яковлев on 15.04.14.
 * бессознательное муравьиной колонии, отвечающее за поиск эйлерова пути на графе.
 * реализует испарение со временем феромонов из вершинок с помощью потоков исполнения.
 */
public class Unconscious extends Thread {

    GGraph graph;                               // ссылка на граф
    Ant[] AntColony;                            // ссылка на колонию муравьев
    private static int iterationsNum;           // текущая итерация
    public static int  iterationsLimit = 500;   // предел итераций по умолчанию
    private int colonySize                      // кол-во муравьев в колонии
              , stoppedAnts                     // кол-во умерших муравьев
              , workingAnts                     // кол=во работающих муравьев
              ;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this); // поддержка слушателей

    Hashtable<Integer, String> GeneticCode;     // Хэш - таблица с генетическим кодом

    String[][] dataMax;
    String[][] dataMin;
    String[][] dataAvg;

    Integer[][][] overlaps;

    String Maximums = "C:\\Users\\Killon\\Desktop\\Проект\\Statistics\\Maximums.txt"
         , Minimums = "C:\\Users\\Killon\\Desktop\\Проект\\Statistics\\Minimums.txt"
         , Average = "C:\\Users\\Killon\\Desktop\\Проект\\Statistics\\Averages.txt"
         , source0 = "Iteration"
         , source1 = " Maximum Length"
         , source2 = " Minimum Length"
         , source3 = " Average Length";

    // Конструктор бессознательного
    Unconscious(GGraph graph, Ant[] AntColony ){
        this.graph = graph;
        this.AntColony = AntColony;
        colonySize = AntColony.length ;
        iterationsNum = 0;
        // считываем генетический код из файла
        GeneticCode = readCode();
        // Записываем шапки двухмерных массивов статистики
        overlaps = readOverlap();
        dataMax = new String[iterationsLimit+1][2];
        dataMax[0][0] = source0;
        dataMax[0][1] = source1;
        dataMin = new String[iterationsLimit+1][2];
        dataMin[0][0] = source0;
        dataMin[0][1] = source2;
        dataAvg = new String[iterationsLimit+1][2];
        dataAvg[0][0] = source0;
        dataAvg[0][1] = source3;
    }

    @Override
    // реализуем бессознательное как поток исполнения.
    public void run() {
        while(iterationsNum != iterationsLimit){
            try{
                iterationsNum++;
                setProgress(iterationsNum);
                makeIteration();
                evaporateEdges(graph);
                sleep(1);		//Приостанавливает поток на 1 миллисекунду
        }
            catch(InterruptedException e){}
        }
        writeStatistics();
    }

    //функция, реализующая испарение феромонов во всех точках графа.
    public void evaporateEdges(GGraph graph){
        for (int i = 0; i <graph.getgNodesArraySize(); i++ ){
            GNode gNode = graph.getGNode(i);
            for (int j = 0; j < gNode.listOut.size(); j++){
                gNode.listOut.get(j).evaporatePheromon() ;
            }
        }
    }

    //Функция, печатающая статистику по феромонам на ребрах в консоль
    public void printPheromons(){
        for (int i = 0; i <= graph.getgNodesArraySize(); i++ ){
            GNode gNode = graph.getGNode(i);
            for (int j = 0; j < gNode.listOut.size(); j++)
            System.out.println("Edge" + i + "pheromone level is" + gNode.listOut.get(j).getPheromonLevel()) ;
        }
    }

    /* Функция, проверяющая количество работающих в данный момент муравьев в колонии.
       Возвращает число остановившихся муравьев.
      */
    public int checkAnts(){
        stoppedAnts = 0;
        workingAnts = 0;
        for (int i = 0; i < AntColony.length; i++){
            if(AntColony[i].amAlive()){
                workingAnts++;
            }
            else{
                stoppedAnts++;
            }
        }
        return stoppedAnts;

    }

    public void makeIteration(){


        // инициализация муравьев.

        for (int i = 0; i < AntColony.length; i++ ){
            System.out.println("вход в цикл создания муравьев");
            AntColony[i] = new Ant(graph);
            AntColony[i].start();
            System.out.println("Муравей создан" + i);
        }

        //ждем, пока все завершат работу
        while(checkAnts() != colonySize){}

        System.out.println("Все муравьи завершили работу");

        //печатаем все успешные замкнутые маршруты в файлики.

        try{
            BufferedWriter writer;
            writer = new BufferedWriter(new FileWriter("C:\\Users\\Killon\\Desktop\\Проект\\Lalka.txt",true));
            //запись успешных путей муравеек
            String source = " I`m ant ";
            String source2 = " successful\n";
            String source3 = new java.util.Date().toString ();


            writer.newLine();
            writer.write("Experiment at " + source3 );
            writer.newLine();

            for(int i = 0; i < AntColony.length; i ++){
                if(AntColony[i].isSuccessful()){
                    AntColony[i].route.add(0, AntColony[i].getStartNodeID()); // добавляем стартовую точку маршрута.
                    writer.write(source + i + source2);
                    writer.newLine();
                    writer.write(AntColony[i].route.toString());
                    writer.newLine();
                    writer.write(GeneticCode.get(AntColony[i].route.get(0)));
                    for(int j=0; j< AntColony[i].route.size()-1; j++ ){

                        int currentNode = AntColony[i].route.get(j);
                        int nextNode = AntColony[i].route.get(j+1);

                        String s = GeneticCode.get(nextNode);
                        char[] buf = new char[10];
                        s.getChars(overlaps[currentNode][nextNode][0]+1,s.length(),buf,0);
                        writer.write(buf);
                    }

                    writer.flush();
                    System.out.println("Записано в файл");
                }
            }

        }
        catch (IOException e) {
            System.out.println("IOEXCEPTION occured");
        }

        // Секция аналитики.
        // Смотрим Максимальный, минимальный и средний путь колонии.
        long maxRouteLength = 1;
        long minRouteLength = AntColony[0].getRouteLength();
        float avgRouteLength = 0;
        float avgTemp = 0;
        int successfulAnts = 0 ;
        long max = 0;
        long min = 0;

        for(int i = 0; i < AntColony.length; i ++){
                        // Находим максимальную и минимальную длину
            if(AntColony[i].isSuccessful() && AntColony[i].getRouteLength() + 1 > max){
                maxRouteLength = AntColony[i].getRouteLength()+1; //+1 потому что начальной точки в массиве Route нет
            }
            if(AntColony[i].isSuccessful() && AntColony[i].getRouteLength() +1 < min){
                minRouteLength = AntColony[i].getRouteLength() + 1 ;
                }
            //Считаем среднюю длину пути
            if(AntColony[i].isSuccessful()){
                avgTemp+=AntColony[i].getRouteLength();
                successfulAnts++;
                }
        }

        try{
            avgRouteLength = avgTemp / (float)successfulAnts;
        }
        catch (ArithmeticException e){
            avgRouteLength = 0;
        }
        dataMax[iterationsNum][0]=String.valueOf(iterationsNum);
        dataMax[iterationsNum][1]=String.valueOf(maxRouteLength);

        dataMin[iterationsNum][0]=String.valueOf(iterationsNum);
        dataMin[iterationsNum][1]=String.valueOf(minRouteLength);

        dataAvg[iterationsNum][0]=String.valueOf(iterationsNum);
        dataAvg[iterationsNum][1]=String.valueOf(avgRouteLength);



        // Оставляем феромоны.
        // И одновременно останавливаем потоки исполнения муравьев.
        for (int i = 0; i < AntColony.length; i++){
            if(AntColony[i].isSuccessful()){
                AntColony[i].putPheromones();
            }
            AntColony[i].stop();
        }

        System.out.println("Закончилась итерация " + iterationsNum);

    }

    public static Hashtable readCode(){
        try {
            // ссылка на файл где будет лежать генетический код
            File file = new File("C:\\Users\\Killon\\Desktop\\code.txt");
            FileReader fr = new FileReader(file);                         // класс FileReader для считывания данных
            Scanner sc = new Scanner(file);                               // класс Scanner - для удобства считывания
            int sizeOfHash = sc.nextInt();   // считывает  целое число с потока ввода и сохраняем в переменную
            Hashtable<Integer,String> geneticCode = new Hashtable(sizeOfHash);    // создаем хэш - таблицу

            while (sc.hasNext() ) {                                    // добавляем ребро с весом к графу
                int i = sc.nextInt();
                String j = sc.nextLine();
                geneticCode.put(i, j);
            }
            System.out.println("Я вышел");
            fr.close();
            return geneticCode;
        } catch (IOException e) {
            System.out.println("I/O error" + e);                          // ошибка на наличие файла
            return null;
        }
    }

    public static Integer[][][] readOverlap(){
        try {
            // ссылка на файл где будет лежать перекрытия генетического кода
            File file = new File("C:\\Users\\Killon\\Documents\\genome0overlaps.txt");
            FileReader fr = new FileReader(file);                         // класс FileReader для считывания данных
            Scanner sc = new Scanner(file);                               // класс Scanner - для удобства считывания
            int sizeOfLaps = sc.nextInt();   // считывает  целое число с потока ввода и сохраняем в переменную
            Integer[][][] overlap = new Integer[sizeOfLaps][sizeOfLaps][1];

            // заполняю массив overlap
            while (sc.hasNext() ) {

                int i = sc.nextInt();
                int j = sc.nextInt();
                int k = sc.nextInt();

                overlap[i][j][0] = k;

            }
            fr.close();
            return overlap;
        }
        catch (IOException e) {
            System.out.println("I/O error" + e);                          // ошибка на наличие файла
            return null;
        }
    }


    public void writeStatistics(){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(Maximums,true));
            for (int i=0;i < iterationsLimit;++i){
                for (int j=0;j<2;++j){
                    writer.write(dataMax[i][j] + " ");
                    writer.flush();
                }
                writer.newLine();
            }
            writer.close();

            writer = new BufferedWriter(new FileWriter(Minimums,true));
            for (int i=0; i< iterationsLimit; ++i){
                for (int j=0;j<2 ;++j){
                    writer.write(dataMin[i][j] + " ");
                    writer.flush();
                }
                writer.newLine();
            }
            writer.close();
            writer = new BufferedWriter(new FileWriter(Average,true));
            for (int i=0;i<iterationsLimit;++i){
                for (int j=0;j<2;++j){
                    writer.write(dataAvg[i][j] + " ");
                    writer.flush();
                }
                writer.newLine();
            }
            writer.close();

        }
        catch (IOException e) {
            System.out.println("IOEXCEPTION occured");
        }
    }
    public static void setIterationsLimit(int iterationsLimit) {
        Unconscious.iterationsLimit = iterationsLimit;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void setProgress(int progress){
        pcs.firePropertyChange("progress", 0, progress);
    }

}



