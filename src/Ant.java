/**
 * Created by Павел Яковлев on 28.03.14.
 * реализован 09.04.14
 * Класс, реализующий муравья, совершающего обход по графу, оставляющего феромоны в пройденных точках.
 * Часть реализации муравьиного алгоритма поиска суперстроки.
 */
import java.util.ArrayList;
import java.util.Random;

public class Ant extends Thread {


    private GNode startNode;                 // самая начальная точка
    private GNode currentNode ;              // текущее местонахождение муравья
    private GNode previousNode;

    private Edge nextEdge;                   // ребро, выбранное муравьем для ходьбы

    private boolean finished;                // флаг, который устанавливается, когда муравей вернулся в исходную точку

    ArrayList<GNode> route;                  // массив, в котором муравей сохраняет пройденный путь


    /*конструктор муравья, в котором прописываем муравья,
    который бегает бесконечно долго и оставляет феромоны*/

    public Ant(GGraph graph){
        route = new ArrayList<GNode>();
        startNodeGenerator(graph);          //выбираем начальную точку
        currentNode = startNode;
        route.add(startNode);               // записываем стартовую точку в маршрут
        previousNode = startNode;           // для визуализации, ибо необходимо правильно расставлять стрелки
        finished = false;                   // устанавливаем флаг того, что муравейка не финишировал.
/*
ПЕРЕПИСАТЬ КОНСТРУКЦИЮ ОСТАВЛЕНИЯ ФЕРОМОНОВ

        putPheromon();                      // оставляем феромончик
*/
    }

    /*override
    * Реализуем собственно модель поведения муравья.
    * Следует здесь ПЕРЕПИСАТЬ ВСЕ МОЗГИ ТРУДЯЖКИ*/

     public void run(){
        while(true){                        // бесконечный цикл
            chooseNextNode();               // обхода вершин
            visitNextNode();                // и оставление феромонов в них
            putPheromon();
            try{
                sleep(100);
            }
            catch (InterruptedException e){}

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

        return startNode;
    }

    /* функция выбора следующей вершинки для движения муравья
    * ТРЕБУЕТ КАЧЕСТВЕННО НОВОГО ОСМЫСЛЕНИЯ НА ОСНОВЕ СТОХАСТИЧЕСКОЙ МОДЕЛИ */
    public void chooseNextNode(){
        Random rand = new Random();
        try{
            int intNextEdge = rand.nextInt(currentNode.listOut.size()); //выбираем одну из вершин, куда далее пойдет муравей
            nextEdge = currentNode.listOut.get(intNextEdge);

        }
        catch (IllegalArgumentException e){
            nextEdge = null;
            this.stop();}
    }



    // Функция посещения следуюющей вершины
    public void visitNextNode(){
        previousNode = currentNode;
        if (nextEdge != null){
            currentNode = nextEdge.getFinishGNode();
        }
        route.add(currentNode);
    }

    // Функция оставления феромона на ребре
    public void putPheromon(){
        nextEdge.setPheromonLevel();
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
}

