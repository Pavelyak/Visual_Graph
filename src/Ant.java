/**
 * Created by Павел Яковлев on 28.03.14.
 * реализован 09.04.14
 * Класс, реализующий муравья, совершающего обход по графу, оставляющего феромоны в пройденных точках.
 * Часть реализации муравьиного алгоритма поиска суперстроки.
 */
import java.util.Random;

public class Ant extends Thread {

    private GNode currentNode ;             //текущее местонахождение муравья
    private GNode nextNode;                 // следующее
    private GNode startNode;                // самая начальная точка



    //конструктор муравья, в котором прописываем муравья,
    //который бегает бесконечно долго и оставляет феромоны
    public Ant(GGraph graph){
        startNodeGenerator(graph);          //выбираем начальную точку
        currentNode = startNode;
        putPheromon();                      // оставляем феромончик
    }

    //override
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

    //функция выбора следующей вершинки для движения муравья
    public void chooseNextNode(){
        Random rand = new Random();
        try{
            int intNextNode = rand.nextInt(currentNode.listOut.size()); //выбираем одну из вершин, куда далее пойдет муравей
            nextNode = currentNode.listOut.get(intNextNode);
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
        currentNode = nextNode;
    }

    //функция, оставление феромона
    public void putPheromon(){
        currentNode.setPheromonLevel();
    }
}

