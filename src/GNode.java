import java.awt.*;
import java.util.Random;
/**
 * Created by Павел Яковлев on 28.03.14.
 * Класс, создающий вершинки, подготовленные для отрисовки на графе. Задаются координаты вершинок
 */

public class GNode extends Node {
    private int x; //координата х точки
    private int y; // координата у точки

    public GNode(){
        super();
        Random rand = new Random();
        this.x = rand.nextInt(800); // подумать над связью  с окнами, рисуемыми в GUI
        this.y = rand.nextInt(600); //подумать аналогично
    }

    public void draw(){
//Ринат, написи отрисовку этой штуки в окнище

    }
    public void print(){
        System.out.println("My ID  = " + id);
        System.out.println("X = " + x);
        System.out.println("Y = " + y);

    }

    public void paint(Graphics g){
        g. fillOval(this.x, this.y, 4, 4);
    }
}
