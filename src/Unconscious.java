import java.io.*;
import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;


/**
 * Created by Павел Яковлев on 15.04.14.
 * бессознательное муравьиной колонии, отвечающее за поиск эйлерова пути на графе.
 * реализует испарение со временем феромонов из вершинок с помощью потоков исполнения.
 */
public class Unconscious extends Thread {

    GGraph graph;
    Ant[] AntColony;
    private static int iterationsNum;
    private final int  iterationsLimit = 500;
    private int colonySize ;
    private int stoppedAnts;
    private int workingAnts;
    private boolean iterationFinished = false;
    ArrayList<Integer> maxRoute;
    ArrayList<Integer> minRoute;
    Hashtable<Integer, String> GeneticCode;
    String[][] dataMax;
    String[][] dataMin;
    String[][] dataAvg;
    String Maximums = "C:\\Users\\Killon\\Desktop\\Проект\\Statistics\\Maximums.txt";
    String Minimums = "C:\\Users\\Killon\\Desktop\\Проект\\Statistics\\Minimums.txt";
    String Average = "C:\\Users\\Killon\\Desktop\\Проект\\Statistics\\Averages.txt";
    String source0 = "Iteration";
    String source1 = " Maximum Length";
    String source2 = " Minimum Length";
    String source3 = " Average Length";

    Unconscious(GGraph graph, Ant[] AntColony ){
        this.graph = graph;
        this.AntColony = AntColony;
        colonySize = AntColony.length ;
        iterationsNum = 0;
        GeneticCode = readCode();
        dataMax = new String[iterationsLimit+1][2];
        dataMax[0][0] = source0;
        dataMax[0][1] = source1;
        dataMin = new String[iterationsLimit+1][2];
        dataMin[0][0] = source0;
        dataMin[0][1] = source1;
        dataAvg = new String[iterationsLimit+1][2];
        dataAvg[0][0] = source0;
        dataAvg[0][1] = source1;
    }

    @Override
    // реализуем бессознательное как поток исполнения.
    public void run() {
        while(iterationsNum != iterationsLimit){
            try{
                iterationsNum++;
                makeIteration();
                evaporateEdges(graph);

            /*for (int i = 0; i < AntColony.length; i++){
                System.out.println("I am " + i +"Ant and my route is" + AntColony[i].getRouteLength());
                System.out.println("I am ALIVE? "+AntColony[i].isAlive());*/

            sleep(1);		//Приостанавливает поток на 1 секунду
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

        iterationFinished = false;

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
            String source = " Я муравей ";
            String source2 = " успешный и мой маршрут\n";
            String source3 = new java.util.Date().toString ();
            String source4 = " неуспешный\n";

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
                    // невыровненный вывод кода.
                    /*for (int j = 0; j < AntColony[i].route.size(); j++){
                        String buf = new String();
                        System.out.println(GeneticCode.get(AntColony[i].route.get(j)).toString());
                        buf = GeneticCode.get(AntColony[i].route.get(j)).toString();
                        writer.write(buf);
                    }*/
                    writer.flush();
                    System.out.println("Записано в файл");
                }
            }

        }
        catch (IOException e) {
            System.out.println("IOEXCEPTION occured");
        }

        /*for(int i = 0; i < AntColony.length; i ++){
            if(AntColony[i].isSuccessful()){
                System.out.println("Я муравей " + i + "успешный и мой маршрут");
                System.out.println(AntColony[i].route);
            }
            else{
                System.out.println("Я муравей " + i + "фейловый");
            }*/
        //}

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
            File file = new File("C:\\Users\\Killon\\Desktop\\code.txt");  // ссылка на файл где будет лежать генетический код
            FileReader fr = new FileReader(file);                         // класс FileReader для считывания данных
            Scanner sc = new Scanner(file);                               // класс Scanner - для удобства считывания            if (sc.hasNextInt()) {                // возвращает истинну если с потока ввода можно считать целое число
            int sizeOfHash = sc.nextInt();   // считывает  целое число с потока ввода и сохраняем в переменнуюSystem.out.println(sizeOfGraph);
            Hashtable<Integer,String> geneticCode = new Hashtable(sizeOfHash);                // создаем граф с заданным размером

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
}



// ищем текущий  максимальный и минимальный маршрут
        /*long max = 0;
        long min = AntColony[0].getRouteLength();
        maxRoute = new ArrayList<Integer>();
        minRoute = new ArrayList<Integer>();

        //maxRoute =  AntColony[0].route;
        //minRoute =  AntColony[0].route;
        int maxStartNodeID = 0;

        for(int i = 0; i < AntColony.length; i ++){
            if(AntColony[i].getRouteLength() > max){
                max = AntColony[i].getRouteLength();
                maxRoute = AntColony[i].route;
                maxRoute.add(0,AntColony[i].getStartNodeID());
                maxStartNodeID = AntColony[i].getStartNodeID();

            }
            if(AntColony[i].getRouteLength() < min){
                min = AntColony[i].getRouteLength();
                //minRoute.add(AntColony[i].getStartNodeID());
                minRoute =  AntColony[i].route;
            }
        }

        System.out.println("Все муравьи завершили работу");
        System.out.print("Максимальный путь " + maxRoute);
        System.out.print("Стартовая точка максимального пути " + maxStartNodeID);
        System.out.print("Минимальный  путь " + minRoute);

        // ОТЛАДКА
        //for (int i = 0; i < AntColony.length; i++){
           // System.out.println("I am " + i +"Ant and my route is" + AntColony[i].getRouteLength());
           // System.out.println("I am ALIVE Потоком? "+AntColony[i].isAlive());
          //  System.out.println("I am ALIVE флагом? "+AntColony[i].amAlive());
       // }*/

