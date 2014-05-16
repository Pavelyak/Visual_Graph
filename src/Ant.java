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
    private final double alpha = 0.9,        // указываем параметры алгоритма. Альфа - феромоны
                         beta  = 0.5,        // Бета - длина пути
                         pheromoneMax = 14;  // регулируемый параметр для феромонов.
    private final double nodesPercentToFinish = 0.0;  // сколько должен пройти муравей, чтоб завершить итерацию.


    private long routeLength = 0;

    private GNode startNode,                 // самая начальная точка
                  currentNode,               // текущее местонахождение муравья
                  previousNode;              // предыдущее местонахождения муравья

    private Edge nextEdge;                   // ребро, выбранное муравьем для ходьбы

    private boolean finished = false ;       // флаг, который устанавливается, когда муравей вернулся в исходную точку
    private boolean failed = false ;         // флаг, который устанавливается, если муравей ступил и не нашел замкнутый путь.
    private boolean alive = true;            // Живой естестенно

    ArrayList<Integer> route;                /* массив, в котором муравей сохраняет(ID вершин) пройденный путь
                                             он же табу - лист, куда ходить больше нельзя( в него не записана
                                             начальная точка */
    ArrayList<Edge> edgesVisited;            /* Массив, в котором муравей сохраняет пройденные стрелки,
                                             чтобы оставить феромоны*/


    /* конструктор муравья, в котором прописываем муравья,
    который бегает бесконечно долго и оставляет феромоны*/

    public Ant(GGraph graph){
        this.graph = graph;
        route = new ArrayList<Integer>();
        edgesVisited = new ArrayList<Edge>();
        startNode = startNodeGenerator(graph);          //выбираем начальную точку
        currentNode = startNode;
        previousNode = startNode;           // для визуализации, ибо необходимо правильно расставлять стрелки
        finished = false;                   // устанавливаем флаг того, что муравейка не финишировал.
        failed = false;                     // презумция невиновности.
        alive = true;
    }

    /*override
    * Реализуем собственно модель поведения муравья.
    * Следует здесь ПЕРЕПИСАТЬ ВСЕ МОЗГИ ТРУДЯЖКИ*/

     public void run(){
         while(!finished && !failed){             // бесконечный цикл
            chooseNextNode();               // обхода вершин
            visitNextNode();                // и оставление феромонов в них
            writePheromon();
             if(finished){
                     alive = false;
                     while(true){
                         try{
                             sleep(100);
                         }
                         catch (InterruptedException e){}
                     }

                 }
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

    /* функция выбора следующей вершинки для движения муравья     */
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

        for(int i = 0; i < currentNode.listOut.size() ; i++ ){
            boolean inRoute;
            int j = 0;
            inRoute = false;
            int currentNodeID = currentNode.listOut.get(i).getFinishGNode().getId();
            if (route.size() !=0 && currentNode.listOut.size() != 0 ){
                while (j != route.size()  ) {
                    if(currentNodeID == route.get(j)){
                        System.out.println("НЕЛЬзя!! ");
                        inRoute = true;
                        break;
                    }
                    else j++;
                }
            }
            if (inRoute == false){
                allowedWay.add(currentNodeID);
                System.out.println("Добавлено в Маршрут!!!");
            }
        }

        //Проверяем, можно ли вообще куда - то пойти.
        if(allowedWay.size() == 0){
            failed = true;
            System.out.println("ЗАФЕЙЛИЛСЯ!!!!((((((((");
            alive = false;
            this.stop();
        }


        // теперь считаем знаменатель формулы вероятности пойти в каждую из точек
        if(alive){
            fullprobablity = 0.0;

            for(int i = 0; i < allowedWay.size(); i++ ){
                Edge currentEdge = currentNode.getConnectingEdge(allowedWay.get(i));
                fullprobablity += (Math.pow(currentEdge.getPheromonLevel() , alpha))
                                *(Math.pow(currentEdge.getWeight() ,beta));
            }
            System.out.println("fullprobablity" + fullprobablity);

            //считаем вероятность пойти в каждую вершинку ВЫРАВНИВАЕТСЯ К ЕДИНИЦЕ
            if (fullprobablity != 0){
                for(int i = 0; i < allowedWay.size() ; i++ ){
                    Edge currentEdge = currentNode.getConnectingEdge(allowedWay.get(i));
                    probablities.add(probablities.get(i) + (Math.pow(currentEdge.getPheromonLevel(), alpha)/ fullprobablity)
                                                         * (Math.pow(currentEdge.getWeight() , beta)));
                    System.out.println("probablity " + i + + probablities.get(i+1) );
                }
            }

            Random rand = new Random();      // генератор случайных чисел
            double numero = rand.nextFloat(); // сгенерировали случайное вещественное число от 0 до 1
            System.out.println("Random Number generated was" + numero);

            for(int i = 0; i <= allowedWay.size() - 1 ; i++){
                if( (numero >= probablities.get(i)) && (numero < probablities.get(i+1)) ){
                    // если выбранная точка попадает на начальную, а 80% точек еще не пройдено.
                    if(currentNode.getConnectingEdge(allowedWay.get(i)).getFinishGNode().getId() == startNode.getId()
                                                    && route.size() < nodesPercentToFinish*graph.getNodesCount() -1){
                        System.out.println("РАНО ДОМОЙ ЗАХОТЕЛ !!!111");
                        //Где - то ТУТ ОШибКа
                        if(allowedWay.size() > 1){
                            chooseNextNode();
                        }
                        else{
                            System.out.println("ЗАФЕЙЛИЛСЯ!!!!((((((((");
                            alive = false;
                            this.stop();
                        }
                    }
                    // если прошел 80% пути
                    else if(currentNode.getConnectingEdge(allowedWay.get(i)).getFinishGNode().getId() == startNode.getId()
                         && route.size() >= nodesPercentToFinish*graph.getNodesCount() -1){

                        finished = true;
                        System.out.println("I HAVE FINISHED!!!");
                        nextEdge = currentNode.getConnectingEdge(allowedWay.get(i));
                    }
                    else {
                        nextEdge = currentNode.getConnectingEdge(allowedWay.get(i));
                        System.out.println("Выбрал путь!");
                        allowedWay.clear();
                        probablities.clear();
                    }
                }
                else {}
            }
        }
    }



    // Функция посещения следуюющей вершины
    public void visitNextNode(){
        previousNode = currentNode;
        if (nextEdge != null){
            currentNode = nextEdge.getFinishGNode();
            System.out.print("I`m in this NODE" + currentNode.getId());
            System.out.println("Поменяно");
            route.add(currentNode.getId());
            routeLength += nextEdge.getWeight();
            System.out.print("while visiting next NODE Starting from NODE " + startNode.getId());
        }


    }

    // Функция оставления феромона на ребре
    public void writePheromon(){
        edgesVisited.add(nextEdge);
        System.out.println("Записал,куда надо оставить феромончик!");
    }

    public void putPheromones(){
        for(int i = 0; i< edgesVisited.size() ; i++){
            edgesVisited.get(i).setPheromonLevel((float)(pheromoneMax*edgesVisited.size()-pheromoneMax/routeLength));
            System.out.println("Оставил феромоны");
        }
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

    public synchronized long getRouteLength(){ return routeLength;}

    public boolean amAlive(){ return alive;}

    public synchronized int getStartNodeID() {return startNode.getId();}

    public boolean isSuccessful(){return finished;}
}

