
/* ребро - основное отличие от просто выходящей вершины в том,
 * что кол-во феромона откладывается на ребре, феромон зависит
 * от i и j вершины
 * Created by Ринат on 07.04.14.
 * modified by Павел on 28.04.14.
 */

class Edge {

    private static int  edgesCount = 0;
    private int edgeWeight;
    public int edgeID;

    private GNode startGNode,
                  finishGNode;

    private volatile float pheromonLevel = 10;

    // Кфонструктор ребра
    Edge (GNode finishGNode){
        this.edgeID = edgesCount;
        edgesCount++;
        this.finishGNode = finishGNode;
        edgeWeight = 1;
    }

    public int getEdgeWeight(){
        return edgeWeight;
    }

    public synchronized void setPheromonLevel(float pheromoneUp){
        pheromonLevel += pheromoneUp;
    }

    public void evaporatePheromon(){
        pheromonLevel = pheromonLevel * (float)0.95 ; //испарение на 5%
    }

    public float getPheromonLevel(){
        return pheromonLevel;
    }

    public GNode getFinishGNode(){
        return finishGNode;
    }

    public int getEdgeID() { return edgeID; }
}

