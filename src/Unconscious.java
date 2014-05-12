import java.util.ArrayList;

/**
 * Created by Павел Яковлев on 15.04.14.
 * бессознательное муравьиной колонии, отвечающее за поиск эйлерова пути на графе.
 * реализует испарение со временем феромонов из вершинок с помощью потоков исполнения.
 */
public class Unconscious extends Thread {

    GGraph graph;
    Ant[] AntColony;
    private static int iterationsNum;
    private final int  iterationsLimit = 10;
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


        for (int i = 0; i < AntColony.length; i++ ){        // инициализация муравьев.
            System.out.println("вход в цикл создания муравьев");
            AntColony[i] = new Ant(graph);
            AntColony[i].start();
            System.out.println("Муравей создан" + i);
        }

        while(checkAnts() != colonySize){} //ждем, пока все завершат работу

        System.out.println("Все муравьи завершили работу");

        // ищем текущий  оптимальный и неоптимальный маршрут

        long max = AntColony[0].getRouteLength();
        long min = AntColony[0].getRouteLength();
        maxRoute =  AntColony[0].route;
        minRoute =  AntColony[0].route;

        for(int i = 0; i < AntColony.length; i ++){
            if(AntColony[i].getRouteLength() > max){
                max = AntColony[i].getRouteLength();
                maxRoute =  AntColony[i].route;
            }
            if(AntColony[i].getRouteLength() < min){
                min = AntColony[i].getRouteLength();
                minRoute =  AntColony[i].route;
            }
        }

        System.out.println("Все муравьи завершили работу");
        System.out.print("Максимальный путь " + maxRoute);
        System.out.print("Минимальный  путь " + minRoute);

        // ОТЛАДКА
        for (int i = 0; i < AntColony.length; i++){
            System.out.println("I am " + i +"Ant and my route is" + AntColony[i].getRouteLength());
            System.out.println("I am ALIVE Потоком? "+AntColony[i].isAlive());
            System.out.println("I am ALIVE флагом? "+AntColony[i].amAlive());
        }

        //останавливаем потоки.
        for (int i = 0; i < AntColony.length; i++){
            AntColony[i].stop();
        }

        System.out.println("Закончилась итерация " + iterationsNum);

    }


}

