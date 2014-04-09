/**
 * Created by Павел Яковлев on 28.03.14.
 * Класс, реализующий муравья, совершающего обход по графу, оставляющего феромоны в пройденных точках.
 * Часть реализации муравьиного алгоритма поиска суперстроки.
 */
import java.util.Random;

public class Ant {

    private GNode currentNode ; //сделать указателем
    private GNode nextNode;

    public void chooseNextNode(){
        Random rand = new Random();
        int intNextNode = rand.nextInt(currentNode.listOut.size()); //выбираем одну из вершин, куда далее пойдет муравей
        nextNode = currentNode.listOut.get(intNextNode);

    }
    public void visitNextNode(){
        currentNode = nextNode;
    }


}
