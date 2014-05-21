import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Павел  on 04.04.14.
 * Created by Ринат on 07.04.14.
 * алгорим пружинок Created by Манэ on 09.05.14
 */
public class GGraph {
    private GNode[] gNodesArray;                                //  массив вершин
    private  int nodesCount;
    private  int linksCount;


    // параметры для подсчета энергии//
    private double n;
    private double a;
    private double m;
    private double b;
    private double c;

    //Края для графа*/
    private int right_edge;
    private int left_edge;
    private int upper_edge;
    private int down_edge;

    public GGraph(int nodesCount, int linksCount ){

        gNodesArray = new GNode[nodesCount];
        this.nodesCount = nodesCount;
        this.linksCount = linksCount;

        n = a = m = b = c = 1;
        n = 100000000;
        a = 1;
        m = 1;
        b = 1;
        c = 1000000000;

        left_edge = 0;
        right_edge = 800;
        upper_edge = 0;
        down_edge = 600;


        for(int id = 0; id <= (nodesCount - 1); id++){        //создаем заданное количество тестовых вершин
            Random rand = new Random();
            int x = 40 + rand.nextInt(800 - 2*40 - 10);       // подумать над связью  с окнами, рисуемыми в GUI
            int y = 40 + rand.nextInt(600 - 3*40 - 10);       // от fringe до writer.height - nodeDiameter
            gNodesArray[id] = new GNode(x , y);
        }
        for(int i = 1; i <= linksCount; i++){                 //присваиваем случайные связи вершинам
            Random rand = new Random();
            int k = rand.nextInt(nodesCount);
            int j = rand.nextInt(nodesCount);
            gNodesArray[k].addOut(gNodesArray[j]);
        }
    }


    // конструктор графа.
    public GGraph (int nodesCount) {
        gNodesArray = new GNode[nodesCount];
        this.nodesCount = nodesCount;

        n = a = m = b = c = 1;
        n = 100000000;
        a = 1;
        m = 1;
        b = 1;
        c = 1000000000;

        left_edge = 0;
        right_edge = 800;
        upper_edge = 0;
        down_edge = 600;

        for(int id = 0; id <= (nodesCount - 1); id++){        //создаем заданное количество  вершин
            Random rand = new Random();
            int x = 40 + rand.nextInt(800 - 2*40 - 10);       //подумать над связью  с окнами, рисуемыми в GUI
            int y = 40 + rand.nextInt(600 - 3*40 - 10);       //от fringe до writer.height - nodeDiameter
            gNodesArray[id] = new GNode(x , y);
        }
    }

    // функция, читающая файл, на выходе получаем граф. Координаты точек вершин расставляются случ. образом.
    public static GGraph myread(File file){
        try {
            FileReader fr = new FileReader(file);                         // класс FileReader для считывания данных
            Scanner sc = new Scanner(file);                               // класс Scanner - для удобства считывания            if (sc.hasNextInt()) {                // возвращает истинну если с потока ввода можно считать целое число
            int sizeOfGraph = sc.nextInt();   // считывает  целое число с потока ввода и сохраняем в переменнуюSystem.out.println(sizeOfGraph);
            GGraph gGraph = new GGraph(sizeOfGraph);                  // создаем граф с заданным размером

            while (sc.hasNext() ) {                                    // добавляем ребро с весом к графу
                int i = sc.nextInt();
                int j = sc.nextInt();
                double weight = sc.nextDouble();
                gGraph.addEdge(i, j, weight);
            }
            System.out.println("Я вышел");
            fr.close();
            return gGraph;
        } catch (IOException e) {
            System.out.println("I/O error" + e);                          // ошибка на наличие файла
            return null;
        }
    }

    // функция, создающая связь между двумя вершинами
    public void addEdge(int i, int j, double weight){         //на входе номера вершин для соединения и вес ребра
        gNodesArray[i].addOut(gNodesArray[j]);
        gNodesArray[i].getConnectingEdge(j).setWeight(weight);
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

    private double energy_to_line(int x1, int x2){
        double energy = this.c/((x1-x2)*(x1-x2));
        return energy;
    }

    private double repulsion_energy(int x1, int y1, int x2, int y2){
        double energy = this.n/((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
        return energy;
    }

    private double attraction_energy(int x1, int y1, int x2, int y2){
        double energy = this.m * ((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        return energy;
    }

    private double energy(int i, int x, int y){
        // проверка на то, чтобы все точки лежали внутри окошка (энергии не бесконечность)


        double energy = 0;
        if (x <= this.left_edge){
            energy = Double.POSITIVE_INFINITY;
        }
        if (x >= this.right_edge){
            energy = Double.POSITIVE_INFINITY;
        }
        if (y <= this.upper_edge){
            energy = Double.POSITIVE_INFINITY;
        }
        if (y >= this.down_edge){
            energy = Double.POSITIVE_INFINITY;
        }

        energy += energy_to_line(x, left_edge);
        energy += energy_to_line(x, right_edge);
        energy += energy_to_line(y, upper_edge);
        energy += energy_to_line(y, down_edge);

        for (int j = 0; j < nodesCount; ++j){
            if (j!=i)
                energy += repulsion_energy(x,y,gNodesArray[j].getX(),gNodesArray[j].getY());

        }
        for(int j = 0; j < gNodesArray[i].listIn.size(); ++j){

            GNode node = gNodesArray[i].listIn.get(j).getFinishGNode();
            energy += attraction_energy(x,y,node.getX(),node.getY());
        }
        for(int j = 0; j < gNodesArray[i].listOut.size(); ++j){
            GNode node = gNodesArray[i].listOut.get(j).getFinishGNode();
            energy += attraction_energy(x,y,node.getX(),node.getY());
        }

        return energy;
    }
    private int find_best_local_spot(int i){
        int end = 0;
        int ret = 0;
        while (end==0){
            int choise = -1;
            double en = energy(i, gNodesArray[i].getX(), gNodesArray[i].getY());
            if (en == Double.POSITIVE_INFINITY){
                find_best_spot(i);
                return 1;
            }
            double en1 = energy(i, gNodesArray[i].getX() - 1, gNodesArray[i].getY());
            if (en1 < en){
                choise = 0;
                en = en1;
            }
            en1 = energy(i, gNodesArray[i].getX(), gNodesArray[i].getY() + 1);
            if (en1 < en){
                choise = 1;
                en = en1;
            }
            en1 = energy(i, gNodesArray[i].getX() + 1, gNodesArray[i].getY());
            if (en1 < en){
                choise = 2;
                en = en1;
            }
            en1 = energy(i, gNodesArray[i].getX(), gNodesArray[i].getY() - 1);
            if (en1 < en){
                choise = 3;
                en = en1;
            }
            switch(choise){
                case 0:
                    ret = 1;
                    gNodesArray[i].setX(gNodesArray[i].getX()-1);
                    break;
                case 1:
                    ret = 1;
                    gNodesArray[i].setY(gNodesArray[i].getY()+1);
                    break;
                case 2:
                    ret = 1;
                    gNodesArray[i].setX(gNodesArray[i].getX()+1);
                    break;
                case 3:
                    ret = 1;
                    gNodesArray[i].setY(gNodesArray[i].getY()-1);
                    break;
                default:
                    end = 1;
            }
        }
        return ret;
    }
    private int local_adjust_internal(){
        for(int i = 0; i < nodesCount; ++i){
            if(find_best_local_spot(i)!=0)
                return 1;
        }
        return 0;
    }
    private void local_adjust(){
        int moved = 0;
        do {
            moved = local_adjust_internal();
        } while (moved!=0);

    }
    private int find_best_spot(int i){
        int min_x = 0;
        int min_y = 0;
        int ret = 0;
        double en = energy(i, gNodesArray[i].getX(), gNodesArray[i].getY());
        for (int x = left_edge; x < right_edge; ++x) {
            for (int y = upper_edge; y < down_edge; ++y) {
                double en1 = energy(i, x, y);
                if (en1 < en){
                    min_x = x;
                    min_y = y;
                    en = en1;
                    ret = 1;
                }
            }
        }
        if(ret == 1){
            gNodesArray[i].setX(min_x);
            gNodesArray[i].setY(min_y);
        }
        return ret;
    }

    private int global_adjust() {
        local_adjust();
        for (int i = 0; i < nodesCount; ++i){
            if (find_best_spot(i)!=0)
                return 1;
        }
        return 0;
    }

    public void adjust() {
        // ф-ия, которая расставляет вершины на графе:
        // двигаем каждую вершину, ищем наименьшую энергию
        // до тех пор пока ф-ия поиска наилучшего положения больше не двигает точки (возвращает 0)

        // энергия:
        // от краев: 1/r, бесконечность если за края уходит
        // не связанные вершины: n/(r^a)
        // связанные вершины: m*(r^b)
        // m=n=1, a=b=1;
        // сумма этих энергий дает энергию вершины

        int moved = 0;
        do {
            moved = global_adjust();
        } while (moved != 0);

    }
}
