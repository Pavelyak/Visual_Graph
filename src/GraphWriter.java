/**
 * Created by Ринат on 07.04.14.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Enumeration;
import java.util.logging.Logger;
import java.math.*;

public class GraphWriter extends Canvas implements Runnable {
    Graphics2D g2;
    int width;
    int height;
    int fringe;
    GGraph graph;                           // Ggraph, который надо отрисовывать
    private Ant[] AntColony;                // Массив -  колония муравьев для отрисовки
    public final int nodeDiameter = 14;     // диаметр вершинки

    // конструктор. В параметры передаем размеры, граф, колонию которых необходимо отрисовать
    public GraphWriter(int width, int height, int fringe,      // ширина, высота, размер рамки
                       GGraph graph, Ant[] AntColony ){        // конкретные экземпляры графа и колонии муравьев
        this.width =  width - (2*fringe);                      // размер области рисования
        this.height = height - 3*fringe;                       // размер области рисования
        this.fringe = fringe;
        this.graph = graph;
        this.AntColony = AntColony;
    }



    // отрисовка
    public void paint(Graphics g) {
        this.g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.white);                                // отрисовка области белым цветом
        g2.fillRect(fringe, fringe, width, height);

        for (int id = 0; id <= graph.getNodesCount() - 1; id++){ // пробегаемся по массиву вершин графа
            GNode gNode = graph.getGNode(id);                    // и отрисовываем их
            int x = gNode.getX();
            int y = gNode.getY();
            paintgNode(x, y);                         // отрисовка вершин
            paintArrows(gNode, x, y);                 // отрисовка стрелок исходящих из данной вершины
        }
    }

    // функция, делающая перерисовку муравьев
    public void update(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        double phlev;
        int antDiameter = 10;
        // отрисовка муравьев
        for (int i = 0; i < AntColony.length; i++){
            int posX1 = AntColony[i].getPreviousX();             // старые и новые координаты муравьев
            int posY1 = AntColony[i].getPreviousY();
            int posX2 = AntColony[i].getX();
            int posY2 = AntColony[i].getY();

            //закраска старых положений муравьев
            g2.setColor(Color.red);
            g2.fill(new Ellipse2D.Double(posX1 - antDiameter/2,
                                         posY1 - antDiameter/2,
                                         antDiameter,
                                         antDiameter));
            //отрисовкка нового положения муравьев
            g2.setColor(Color.black);
            g2.fill(new Ellipse2D.Double(posX2 - antDiameter/2,
                                         posY2 - antDiameter/2,
                                         antDiameter,
                                         antDiameter));
            paintPheromoneArrow(i, posX1, posY1, posX2, posY2);
        }
    }

    private void paintArrows(GNode gNode, int x, int y) {
        g2.setColor(Color.black);
        for (int i = 0; i < gNode.listOut.size(); i++){
            GNode gNodeFinish = gNode.listOut.get(i).getFinishGNode();
            int xFin = gNodeFinish.getX();
            int yFin = gNodeFinish.getY();
            Arrow2D arrow = new Arrow2D(x, y, xFin, yFin, nodeDiameter/2);
            arrow.draw(g2);
        }
    }
    private void paintgNode(int x, int y){
        g2.setColor(Color.red);
        g2.fill(new Ellipse2D.Double(x - nodeDiameter/2,
                                     y - nodeDiameter/2,
                                     nodeDiameter,
                                     nodeDiameter));
    }


    private void paintPheromoneArrow(int i, int posX1, int posY1, int posX2, int posY2) {
        double  phlev;
        if(AntColony[i].getVisitedEdge() != null ){
            phlev = Math.log((AntColony[i].getVisitedEdge().getPheromonLevel()) + 1);
            g2.setStroke(new BasicStroke(0.2f + (float)phlev));
        }
        else {
            g2.setStroke(new BasicStroke(0.2f));
            g2.drawLine(posX1, posY1, posX2, posY2);
        }
    }

    @Override
    public void run() {
        while (true) {
            repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {}
        }
    }
}
