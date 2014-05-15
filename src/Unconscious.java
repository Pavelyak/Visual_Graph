import java.io.*;
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
    private final int  iterationsLimit = 1;
    private int colonySize ;
    private int stoppedAnts;
    private int workingAnts;
    private boolean iterationFinished = false;
    ArrayList<Integer> maxRoute;
    ArrayList<Integer> minRoute;

    Unconscious(GGraph graph, Ant[] AntColony ){
        this.graph = graph;
        this.AntColony = AntColony;
        colonySize = AntColony.length ;
        iterationsNum = 0;
    }
    @Override
    // реализуем бессознательное как поток исполнения.
    public void run() {
        while(iterationsNum != iterationsLimit){
            try{
                iterationsNum++;
                makeIteration();

            /*for (int i = 0; i < AntColony.length; i++){
                System.out.println("I am " + i +"Ant and my route is" + AntColony[i].getRouteLength());
                System.out.println("I am ALIVE? "+AntColony[i].isAlive());*/

            sleep(1000);		//Приостанавливает поток на 1 секунду
        }catch(InterruptedException e){}
      }
    }

    //функция, реализующая испарение феромонов во всех точках графа.
    public void evaporateEdges(GGraph graph){
        for (int i = 0; i <=graph.getgNodesArraySize(); i++ ){
            GNode gNode = graph.getGNode(i);
            for (int j = 0; j < gNode.listOut.size(); j++)
            gNode.listOut.get(j).evaporatePheromon() ;
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
        Hashtable GeneticCode = readCode();
        System.out.println(GeneticCode);


        for (int i = 0; i < AntColony.length; i++ ){        // инициализация муравьев.
            System.out.println("вход в цикл создания муравьев");
            AntColony[i] = new Ant(graph);
            AntColony[i].start();
            System.out.println("Муравей создан" + i);
        }

        while(checkAnts() != colonySize){} //ждем, пока все завершат работу

        System.out.println("Все муравьи завершили работу");



        //печатаем все успешные замкнутые маршруты.

        try{
            BufferedWriter writer;
            writer = new BufferedWriter(new FileWriter("C:\\Users\\Killon\\Desktop\\Проект\\Testingcolony.txt",true));
            //запись успешных путей муравеек
            String source = "Я муравей ";
            String source2 = " успешный и мой маршрут\n";
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
                    // невыровненный вывод кода.
                    for (int j = 0; j < AntColony[i].route.size(); j++){
                        String buf = new String();
                        System.out.println(GeneticCode.get(AntColony[i].route.get(j)).toString());
                        buf = GeneticCode.get(AntColony[i].route.get(j)).toString();
                        writer.write(buf);
                    }
                    writer.flush();
                    System.out.println("Записано в файл");
                }
            }

        }
        catch (IOException e) {
            System.out.println("IOEXEPTION occured");
        }

        for(int i = 0; i < AntColony.length; i ++){
            if(AntColony[i].isSuccessful()){
                System.out.println("Я муравей " + i + "успешный и мой маршрут");
                System.out.println(AntColony[i].route);
            }
            else{
                System.out.println("Я муравей " + i + "фейловый");
            }
        }




        //останавливаем потоки.
        for (int i = 0; i < AntColony.length; i++){
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
