/**
 * Created by Павел Яковлев on 28.03.14.
 * реализован 09.04.14
 * Класс, реализующий муравья, совершающего обход по графу, оставляющего феромоны в пройденных точках.
 * Часть реализации муравьиного алгоритма поиска суперстроки.
 */
import java.util.ArrayList;
import java.util.Random;


public class Ant extends Thread {
    GGraph graph;
    private final double alpha = 0.5,        // указываем параметры алгоритма.
                         beta  = 0.5;


    private long routeLength = 0;

    private GNode startNode,                 // самая начальная точка
                  currentNode,               // текущее местонахождение муравья
                  previousNode;              // предыдущее местонахождения муравья

    private Edge nextEdge;                   // ребро, выбранное муравьем для ходьбы

    private boolean finished;                // флаг, который устанавливается, когда муравей вернулся в исходную точку

    ArrayList<Integer> route;                /* массив, в котором муравей сохраняет(ID вершин) пройденный путь
                                             он же табу - лист, куда ходить больше нельзя( в него не записана
                                             начальная точка */


    /*конструктор муравья, в котором прописываем муравья,
    который бегает бесконечно долго и оставляет феромоны*/

    public Ant(GGraph graph){
        this.graph = graph;
        route = new ArrayList<Integer>();
        startNode = startNodeGenerator(graph);          //выбираем начальную точку
        currentNode = startNode;
        previousNode = startNode;           // для визуализации, ибо необходимо правильно расставлять стрелки
        finished = false;                   // устанавливаем флаг того, что муравейка не финишировал.
    }

    /*override
    * Реализуем собственно модель поведения муравья.
    * Следует здесь ПЕРЕПИСАТЬ ВСЕ МОЗГИ ТРУДЯЖКИ*/

     public void run(){
        while(route.size()!=10){                        // бесконечный цикл
            chooseNextNode();               // обхода вершин
            visitNextNode();                // и оставление феромонов в них
            putPheromon();
            /*try{
                sleep(10);
            }
            catch (InterruptedException e){}*/

        }
    }

    /* функция выбора точки старта муравья
    * Здесь стартовая точка выбирается абсолютно случайно
    * из множества всех вершин графа на 28 апреля 2014 года */
    public GNode startNodeGenerator(GGraph graph){
        //реализация обертки для генератора случайных чисел
        Random rand = new Random();
        // генерируется случайное число в интервале от 0 до числа вершин на графе
        int intStartNode = rand.nextInt(graph.getgNodesArraySize());

        //стартовой точкой устанавливается точка с номером, сгенерированным выше
        startNode = graph.getGNode(intStartNode);
        System.out.print("Starting from NODE " + startNode.getId());

        return startNode;
    }

    /* функция выбора следующей вершинки для движения муравья
    * ТРЕБУЕТ КАЧЕСТВЕННО НОВОГО ОСМЫСЛЕНИЯ НА ОСНОВЕ СТОХАСТИЧЕСКОЙ МОДЕЛИ */
    public void chooseNextNode(){

        ArrayList<Integer> allowedWay;              // массив с ID разрешенных вершинок для муравья
        allowedWay = new ArrayList<Integer>();
        ArrayList<Double> probablities;
        probablities = new ArrayList<Double>();
        probablities.add(0.0);                      // первый элемент в массиве - нуль (так проще реализовать)
        double fullprobablity;


        /* Указываем муравью, куда он может пойти из текущей вершины
        * пройденные ранее вершины запрещаются.
        */
        System.out.println("currentNode.listOut.size " + currentNode.listOut.size());
        for(int i = 0; i <= currentNode.listOut.size()-1 ; i++ ){
            System.out.println("starting calcs");
            System.out.println("route.size " + route.size());
            boolean inRoute;
            int j = 0;
            inRoute = false;
            if (route.size() !=0 && currentNode.listOut.size() != 0 ){
                while (j != route.size() -1  ) {
                    if(currentNode.listOut.get(i).getFinishGNode().getId() == route.get(j)){
                        System.out.println("HUI");
                        inRoute = true;
                        j = route.size()-1 ;
                    }
                    else j++;
                }
            }
            System.out.println(inRoute);
            if (inRoute == false){
                allowedWay.add(currentNode.listOut.get(i).getFinishGNode().getId());
                System.out.println("Добавлено в Маршрут!!!");
            }

        }

        // теперь считаем знаменатель формулы вероятности пойти в каждую из точек
        fullprobablity = 0.0;
        for(int i = 0; i <= allowedWay.size() - 1 ; i++ ){
            fullprobablity += Math.pow(currentNode.getConnectingEdge(allowedWay.get(i)).getPheromonLevel() , alpha);
        }
        System.out.println("fullprobablity" + fullprobablity);

        //считаем вероятность пойти в каждую вершинку ВЫРАВНИВАЕТСЯ К ЕДИНИЦЕ
        for(int i = 0; i <= allowedWay.size() - 1; i++ ){
            if (fullprobablity != 0){
                probablities.add( probablities.get(i) + (Math.pow(currentNode.getConnectingEdge(allowedWay.get(i)).getPheromonLevel(), alpha)/ fullprobablity));
                System.out.println("probablity " + i +" " + probablities.get(i+1) );
            }
        }

        Random rand = new Random();      // генератор случайных чисел
        double numero = rand.nextFloat(); // сгенерировали случайное вещественное число от 0 до 1
        System.out.println("Random Number generated was" + numero);

        for(int i = 0; i <= allowedWay.size() - 1 ; i++){
            if( (numero >= probablities.get(i)) && (numero < probablities.get(i+1)) ){
                nextEdge = currentNode.getConnectingEdge(allowedWay.get(i));
                System.out.println("Выбрал путь!");
                allowedWay.clear();
                probablities.clear();


            }
        }



        /*Random rand = new Random();
        try{
            int intNextEdge = rand.nextInt(currentNode.listOut.size()); //выбираем одну из вершин, куда далее пойдет муравей
            nextEdge = currentNode.listOut.get(intNextEdge);
        }
        catch (IllegalArgumentException e){
            nextEdge = null;
            this.stop();}*/
    }



    // Функция посещения следуюющей вершины
    public void visitNextNode(){
        previousNode = currentNode;
        if (nextEdge != null){
            currentNode = nextEdge.getFinishGNode();
            System.out.print("I`m in this NODE" + currentNode.getId());
            System.out.println("Поменяно");
            route.add(currentNode.getId());
            routeLength += nextEdge.getEdgeWeight();
        }


    }

    // Функция оставления феромона на ребре
    public void putPheromon(){
        nextEdge.setPheromonLevel();
        System.out.println("Оставил феромон!");
    }

    // функции получения координат муравья
    public int getX() {
        return currentNode.getX();
    }
    public int getY() {
        return currentNode.getY();
    }
    public int getPreviousX(){
        return previousNode.getX();
    }
    public int getPreviousY(){
        return previousNode.getY();
    }
    public Edge getVisitedEdge(){
        return nextEdge;
    }
    public long getRouteLength(){ return routeLength;}
}

