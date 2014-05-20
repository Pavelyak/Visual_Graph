import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
/**
 * Created by Павел Яковлев on 28.03.14.
 * Класс, создающий вершинки, подготовленные для отрисовки на графе. Задаются координаты вершинок
 */

public class GNode  {
    private static int  nodesCount = 0; // Счетчик числа вершин
    public int id; // номер вершины
    private int x; //координата х вершины
    private int y; // координата у вершины
    private volatile float pheromonLevel = 0;
    ArrayList<Edge> listIn; // динамический массив ссылок на входящие вершин.
    ArrayList<Edge> listOut; // динамический массив ссылок на исходящие вершины.

    //конструктор
    public GNode(int x, int y){
        this.id = nodesCount;
        nodesCount++;
        listIn = new ArrayList<Edge>();
        listOut = new ArrayList<Edge>();
        this.x = x;
        this.y = y;
    }
    // возвращают координаты GNode
    public int getX(){
        return this.x;
    }

    public synchronized void setX(int x){
        this.x = x;
    }

    public int getY(){
        return this.y;
    }

    public synchronized void setY(int y){
        this.y = y;
    }


    public int getId() {return id;}


    /*public void printLinks(){                                  // печатает id всех входящих и выходящих вершины
        for (int i = 0; i < listIn.size(); i++ ){
            System.out.println(id + " in = " + listIn.get(i).id);
        }
        for (int i = 0; i < listOut.size(); i++ ){
            System.out.println(id + " out = " + listOut.get(i).id);
        }
    }*/

    public void addOut (GNode gNodeFinish){          // функция, добавляющая связи между двумя вершинами
        listOut.add(new Edge (gNodeFinish));         // добавляем out вершину NodeFinish  в массив искомого Node
        gNodeFinish.listIn.add(new Edge(this));      // заносим искомую вершину в in массив конечной вершины NodeFinish
    }

    // пишет статистику, которая нам не особо нужна сейчас
    public void print(){
        System.out.println("My ID  = " + id);
        System.out.println("X = " + x);
        System.out.println("Y = " + y);
    }

    //функция, возвращающая стрелочку, соединяющую данну вершинку с указанной ID
    public Edge getConnectingEdge(int id){
        int i = 0;
        while (this.listOut.get(i).getFinishGNode().getId() != id){
            i++;
        }
        return this.listOut.get(i);
    }
}

