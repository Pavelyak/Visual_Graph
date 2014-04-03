import java.awt.*;
import java.util.Random;
/**
 * Created by Павел Яковлев on 28.03.14.
 * Класс, создающий вершинки, подготовленные для отрисовки на графе. Задаются координаты вершинок
 */

public class GNode extends Node {
    private int x; //координата х точки
    private int y; // координата у точки

    public GNode(Graphics2D g){
        super();
        Random rand = new Random();
        this.x = rand.nextInt(1000); // подумать над связью  с окнами, рисуемыми в GUI
        this.y = rand.nextInt(1000); //подумать аналогично
    }

    public void draw(){
//Ринат, написи отрисовку этой штуки в окнище


    }
}
