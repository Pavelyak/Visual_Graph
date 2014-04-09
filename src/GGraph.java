/**
 * Created by Ринат on 07.04.14.
 */
import java.util.Random;
/**
 * Created by Killon on 04.04.14.
 * Created by Ринат on 07.04.14.
 */
public class GGraph {
    private GNode[] gNodesArray;                                //  массив вершин
    private  int nodesCount;
    private  int linksCount;


    public GGraph(int nodesCount, int linksCount ){

        gNodesArray = new GNode[nodesCount];
        this.nodesCount = nodesCount;
        this.linksCount = linksCount;

        for(int id = 0; id <= (nodesCount - 1); id++){          //создаем заданное количество тестовых вершин
            Random rand = new Random();
            int x = 40 + rand.nextInt(800 - 2*40 - 10);                          // подумать над связью  с окнами, рисуемыми в GUI
            int y = 40 + rand.nextInt(600 - 3*40 - 10);  // от fringe до writer.height - nodeDiameter
            gNodesArray[id] = new GNode(x , y);
        }
        for(int i = 1; i <= linksCount; i++){                   //присваиваем случайные связи вершинам
            Random rand = new Random();
            int k = rand.nextInt(nodesCount);
            int j = rand.nextInt(nodesCount);
            gNodesArray[k].addOut(gNodesArray[j]);

        }
    }

    public GNode getGNode(int id){            // возвращает вершину под указанным номером.
        return this.gNodesArray[id];
    }

    public int getgNodesArraySize(){
        return gNodesArray.length;
    }

    public int getNodesCount(){
        return this.nodesCount;
    }  // возвращает число вершин в графе

    public void printGraphStat(){

        System.out.println("Graph has " + nodesCount + " nodes and " +  linksCount +" links");
        System.out.println("Statistics for each node provided below");
        for(int i = 0; i <= (nodesCount - 1); i++)
            gNodesArray[i].printLinks();

    }
}
