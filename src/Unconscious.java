/**
 * Created by Павел Яковлев on 15.04.14.
 * бессознательное муравьиной колонии, отвечающее за поиск эйлерова пути на графе.
 * реализует испарение со временем феромонов из вершинок с помощью потоков исполнения.
 */
public class Unconscious extends Thread {
    GGraph Graph;
   Unconscious(GGraph Graph){
   this.Graph = Graph;
   }
    @Override
    // реализуем бессознательное как поток исполнения.
    public void run() {
        while(true){
            try{
            evaporateNodes(Graph);
            sleep(1000);		//Приостанавливает поток на 1 секунду
        }catch(InterruptedException e){}
      }
    }

    //функция, реализующая испарение феромонов во всех точках графа.
    public void evaporateNodes(GGraph Graph){
        for (int i = 0; i <=Graph.getgNodesArraySize(); i++ ){
            Graph.getGNode(i).evaporatePheromon();
        }
    }
}
