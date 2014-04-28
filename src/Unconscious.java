/**
 * Created by Павел Яковлев on 15.04.14.
 * бессознательное муравьиной колонии, отвечающее за поиск эйлерова пути на графе.
 * реализует испарение со временем феромонов из вершинок с помощью потоков исполнения.
 */
public class Unconscious extends Thread {

   GGraph graph;
   Unconscious(GGraph graph){
   this.graph = graph;
   }
    @Override
    // реализуем бессознательное как поток исполнения.
    public void run() {
        while(true){
            try{
            evaporateEdges(graph);
            printPheromons();
            sleep(1000);		//Приостанавливает поток на 1 секунду
        }catch(InterruptedException e){}
      }
    }

    //функция, реализующая испарение феромонов во всех точках графа.
    public void evaporateEdges(GGraph graph){
        for (int i = 0; i <=graph.getgNodesArraySize(); i++ ){
            GNode gNode = graph.getGNode(i);
            for (int j = 0; j < gNode.listOut.size(); j++)
            gNode.listOut.get(j).evaporatePheromon() ;
        }
    }

    public void printPheromons(){
        for (int i = 0; i <= graph.getgNodesArraySize(); i++ ){
            GNode gNode = graph.getGNode(i);
            for (int j = 0; j < gNode.listOut.size(); j++)
            System.out.println("Edge" + i + "pheromone level is" + gNode.listOut.get(j).getPheromonLevel()) ;
        }
    }
}
