import java.util.Random;

/**
 * Created by Killon on 04.04.14.
 */
public class GGraph {
    GNode[] gNodesArray;            // массив вершин
    public static int nodesCount;
    public static int linksCount;


    public GGraph(int nodesCount, int linksCount ){

        gNodesArray = new GNode[nodesCount];
        this.nodesCount = nodesCount;
        this.linksCount = linksCount;

        for(int id = 0; id <= (nodesCount -1); id++){              //создаем заданное количество тестовых вершин
            gNodesArray[id] = new GNode();
        }
        for(int i = 1; i <= linksCount;i++){                   //присваиваем случайные связи вершинам
            Random rand = new Random();
            int k = rand.nextInt(nodesCount);
            int j = rand.nextInt(nodesCount);
            gNodesArray[k].addOut(gNodesArray[j]);

        }
    }
    public void printGraphStat(){

        System.out.println("Graph has " + nodesCount + " nodes and " +  linksCount +" links");
        System.out.println("Statistics for each node provided below");
        for(int i = 0; i<=(nodesCount -1); i++)
            gNodesArray[i].printLinks();

    }



    /*for(int i = 0 ; i <= gNodesArray.length -1 ; i++){
            gNodesArray[i] = new GNode();
            gNodesArray[i].addOut(gNodesArray[0]);
            gNodesArray[i].print();
            gNodesArray[i].printLinks();*/
}
