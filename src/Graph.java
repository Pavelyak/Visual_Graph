import java.util.Random;
/**
 * Created by Павел Яковлев on 14.03.14.
 * попытаемся создать тестовый рандомный граф, а позже, визуализировать его
 */
public class Graph{

    // объявляем конструктор, создающий тестовый граф.
    public Graph(int NodesSum, int LinksSum ){

        NodesArray = new Node[NodesSum];
        this.NodesSum = NodesSum;
        this.LinksSum = LinksSum;

        for(int id = 0; id <= (NodesSum -1); id++){              //создаем заданное количество тестовых вершин
            NodesArray[id] = new Node();
        }
        for(int i = 1; i<= LinksSum;i++){                   //присваиваем случайные связи вершинам
            Random rand = new Random();
            int k = rand.nextInt(NodesSum);
            int j = rand.nextInt(NodesSum);
            NodesArray[k].addOut(NodesArray[j]);

        }
    }
    public void printGraphStat(){

        System.out.println("Graph has " + NodesSum + " nodes and " +  LinksSum +" links");
        System.out.println("Statistics for each node provided below");
        for(int i = 0; i<=(NodesSum -1); i++)
            NodesArray[i].printLinks();

    }
    Node[] NodesArray;            //создаем массив вершинок
    public static int NodesSum;
    public static int LinksSum;
}

