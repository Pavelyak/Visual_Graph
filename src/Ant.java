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
    private final double alpha = 1,          // указываем параметры алгоритма. Альфа - феромоны
                         beta  = 0.2,        // Бета - длина пути
                         pheromoneMax = 20;  // регулируемый параметр для феромонов.
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


    /* Конструктор муравья, в котором прописываем муравья,
    который ищет замкнутый маршрут. В случае, если замкну-
    тый маршрут не найден, муравей объявляется failed,
    если муравей нашел замкнутый маршрут  - finished    */

    public Ant(GGraph graph){
        this.graph = graph;
        route = new ArrayList<Integer>();
        edgesVisited = new ArrayList<Edge>();
        startNode = startNodeGenerator(graph);          //выбираем начальную точку
        currentNode = startNode;
        previousNode = startNode;           // для визуализации, ибо необходимо правильно расставлять стрелки
        finished = false;                   // устанавливаем флаг того, что муравейка не финишировал.
        failed = false;                     // презумция невиновности. Муравей считается незафейлившимся в начале.
        alive = true;
    }

    /*override
    * Реализуем собственно модель поведения муравья.
    * */

     public void run(){
         while(!finished && !failed){             // бесконечный цикл
            chooseNextNode();               // обхода вершин
            visitNextNode();                // и оставление феромонов в них
            writePheromon();                // запоминает, куда должен оставить феромоны
             if(finished){
                     alive = false;
                     while(true){
                         try{
                             sleep(1);
                         }
                         catch (InterruptedException e){}
                     }

                 }
             }
         }


    /* функция выбора точки старта муравья
    * Здесь стартовая точка выбирается абсолютно случайно
    * из множества всех вершин графа  */
    public GNode startNodeGenerator(GGraph graph){
        // Создаем генератор случайных чисел
        Random randGenerator = new Random();
        // генерируется случайное число в интервале от 0 до числа вершин на графе
        int intStartNode = randGenerator.nextInt(graph.getgNodesArraySize());

        //стартовой точкой устанавливается точка с номером, сгенерированным выше
        startNode = graph.getGNode(intStartNode);

        return startNode;
    }

    /* функция выбора следующей вершинки для движения муравья     */
    public void chooseNextNode(){

        ArrayList<Integer> allowedWay;              // массив с ID разрешенных вершинок для муравья
        allowedWay = new ArrayList<Integer>();
        ArrayList<Double> probablities;             // массив, в котором считаются вероятности
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
                        inRoute = true;
                        break;
                    }
                    else j++;
                }
            }
            if (inRoute == false){
                allowedWay.add(currentNodeID);
            }
        }

        //Проверяем, можно ли вообще куда - то пойти.
        if(allowedWay.size() == 0){
            failed = true;
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
                    probablities.add(probablities.get(i) + (Math.pow(currentEdge.getPheromonLevel(), alpha)/fullprobablity)
                                                         * (Math.pow(currentEdge.getWeight() , beta)));
                    }
            }
            // генератор случайных чисел
            Random rand = new Random();
            // сгенерировали случайное вещественное число от 0 до 1
            double numero = rand.nextFloat();

            // Теперь по сгенерированному числу, смотрит, куда же пойдет муравей.
            for(int i = 0; i <= allowedWay.size() - 1 ; i++){
                if( (numero >= probablities.get(i)) && (numero < probablities.get(i+1)) ){
                    // если выбранная точка попадает на начальную,
                    // а заданный процент точек еще не пройден.
                    if(currentNode.getConnectingEdge(allowedWay.get(i)).getFinishGNode().getId() == startNode.getId()
                                                    && route.size() < nodesPercentToFinish*graph.getNodesCount() -1){
                        //если кроме начальной точки есть еще какая - то,
                        // то пробуем еще раз найти путь
                        if(allowedWay.size() > 1){
                            chooseNextNode();
                        }

                        else{
                            alive = false;
                            this.stop();
                        }
                    }
                    // если прошел заданный процент пути пути
                    // и пришел в начальную точку
                    else if(currentNode.getConnectingEdge(allowedWay.get(i)).getFinishGNode().getId() == startNode.getId()
                         && route.size() >= nodesPercentToFinish*graph.getNodesCount() -1){

                        finished = true;
                        nextEdge = currentNode.getConnectingEdge(allowedWay.get(i));
                    }
                    // или просто идет дальше
                    else {
                        nextEdge = currentNode.getConnectingEdge(allowedWay.get(i));
                        allowedWay.clear();
                        probablities.clear();
                    }
                }
            }
        }
    }



    /* Функция посещения следуюющей вершины.
       Муравей запоминает здесь свой путь, а также его длину
    */
    public void visitNextNode(){
        previousNode = currentNode;
        if (nextEdge != null){
            currentNode = nextEdge.getFinishGNode();
            route.add(currentNode.getId());
            routeLength += nextEdge.getWeight();
        }
    }

    // Функция запоминания, куда надо оставлять феромоны
    public void writePheromon(){ edgesVisited.add(nextEdge); }

    // Функция оставления феромона на ребре
    public void putPheromones(){
        for(int i = 0; i< edgesVisited.size() ; i++){
            edgesVisited.get(i).setPheromonLevel((float)(pheromoneMax*edgesVisited.size()-pheromoneMax/routeLength));
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

    // Функция, возвращающая длину пройденного пути
    public synchronized long getRouteLength(){ return routeLength;}

    // Функция, сообщающая, жив ли еще муравей
    public boolean amAlive(){ return alive;}

    // Функция, возвращающая ID стартовой точки пути
    public synchronized int getStartNodeID() {return startNode.getId();}

    // Функция, сообщающая, успешный ли муравей.
    public boolean isSuccessful(){return finished;}
}

