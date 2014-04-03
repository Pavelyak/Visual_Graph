import java.util.*;

/**
 * Created by Ринат on 07.03.14.
 * Updated by P.Yak. on 28.03.14
 */

public class Node {

    private static int  nodesCount=0; // Счетчик числа вершин
    public int id; // номер вершины
    ArrayList<Node> listIn; // динамический массив ссылок на входящие вершин.
    ArrayList<Node> listOut; // динамический массив ссылок на исходящие вершины.

    //конструктор
    public Node(){
        this.id = nodesCount;
        nodesCount++;
        listIn = new ArrayList<Node>();
        listOut = new ArrayList<Node>();
    }


    public void printLinks(){                                       // печатает id всех входящих и выходящих вершины
      for (int i = 0; i < listIn.size(); i++ ){                      // проходим по всем элементам массива входящих вершин
          System.out.println(id + " in = " + listIn.get(i).id);
      }
      for (int i = 0; i < listOut.size(); i++ ){
          System.out.println(id + " out = " + listOut.get(i).id);
      }
    }

    public void addOut (Node NodeFinish){          // функция, добавляющая связи между двумя вершинами
        listOut.add(NodeFinish);                   // добавляем out вершину NodeFinish  в массив искомого Node
        NodeFinish.listIn.add(this);               // заносим искомую вершину в in массив конечной вершины NodeFinish
    }
}
