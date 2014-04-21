/**
 * Created by Павел Яковлев on 28.03.14.
 * реализован 09.04.14
 * Класс, реализующий муравья, совершающего обход по графу, оставляющего феромоны в пройденных точках.
 * Часть реализации муравьиного алгоритма поиска суперстроки.
 */
import java.util.Random;

public class Ant extends Thread {

    private GNode currentNode ;             //текущее местонахождение муравья
    //private GNode nextNode;                 // следующее
    private GNode startNode;                // самая начальная точка
    private Edge visitedEdge;
    private Edge nextEdge;                  // ребро, выбранное муравьем для ходьбы
    private GNode previousNode;


    //конструктор муравья, в котором прописываем муравья,
    //который бегает бесконечно долго и оставляет феромоны
    public Ant(GGraph graph){
        startNodeGenerator(graph);          //выбираем начальную точку
        currentNode = startNode;
        previousNode = startNode;
/*
        putPheromon();                      // оставляем феромончик
*/
    }

    //override
    public void run(){
        while(true){                        // бесконечный цикл
            chooseNextNode();               // обхода вершин
            visitNextNode();                // и оставление феромонов в них
            putPheromon();
            try{
                sleep(1000);
            }
            catch (InterruptedException e){}

        }
    }

    //функция выбора следующей вершинки для движения муравья
    public void chooseNextNode(){
        Random rand = new Random();
        try{
            int intNextNode = rand.nextInt(currentNode.listOut.size()); //выбираем одну из вершин, куда далее пойдет муравей
            nextEdge = currentNode.listOut.get(intNextNode);
        }
        catch (IllegalArgumentException e){ this.stop();}
    }
    //функция выбора точки старта муравья
    public GNode startNodeGenerator(GGraph graph){
        Random rand = new Random();
        int intStartNode = rand.nextInt(graph.getgNodesArraySize());
        startNode = graph.getGNode(intStartNode);
        return startNode;
    }

    //функция посещения следуюющей вершины
    public void visitNextNode(){
        previousNode = currentNode;
        currentNode = nextEdge.getFinishGNode();
    }

    //функция, оставление феромона
    public void putPheromon(){
        nextEdge.setPheromonLevel();
    }

    // функция получения координат муравья
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

