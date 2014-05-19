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
        //setOpaque(false);
        this.width =  width - (2*fringe);                      // размер области рисования
        this.height = height - 3*fringe;                       // размер области рисования
        this.fringe = fringe;
        this.graph = graph;
        this.AntColony = AntColony;
        //new Thread(this).start();
    }



    // отрисовка
    public void paint(Graphics g) {
        this.g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,     // хз, что это
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.white);                                // отрисовка области белым цветом
        g2.fillRect(fringe, fringe, width, height);

        for (int id = 0; id <= graph.getNodesCount() - 1; id++){ // пробегаемся по массиву вершин графа
            GNode gNode = graph.getGNode(id);                    // и отрисовываем их
            int x = gNode.getX();
            int y = gNode.getY();

            // отрисовка вершин
            g2.setColor(Color.red);
            g2.fill(new Ellipse2D.Double(x - nodeDiameter/2, y - nodeDiameter/2, nodeDiameter, nodeDiameter));

            // отрисовка стрелок
            g2.setColor(Color.black);
            for (int i = 0; i < gNode.listOut.size(); i++){
                GNode gNodeFinish = gNode.listOut.get(i).getFinishGNode();
                int xFin = gNodeFinish.getX();
                int yFin = gNodeFinish.getY();
                Arrow2D arrow = new Arrow2D(x, y, xFin, yFin, nodeDiameter/2);
                arrow.draw(g2);
            }
            // обновляем
            //update(g2);
        }
    }

    // функция, делающая перерисовку муравьев отдельным потоком
    public void update(Graphics g) {
        //Logger.global.info("@ => update in dGraphWriter");
        new Thread(this).start();
        Graphics2D g2 = (Graphics2D) g;
        double phlev;

        // отрисовка муравьев
        int antDiameter = 10;

        for (int i = 0; i < AntColony.length; i++){
            int posX1 = AntColony[i].getPreviousX();             // старые и новые координаты муравьев
            int posY1 = AntColony[i].getPreviousY();
            int posX2 = AntColony[i].getX();
            int posY2 = AntColony[i].getY();


            g2.setColor(Color.red);                              //закраска старых положений муравьев
            g2.fill(new Ellipse2D.Double(posX1 - antDiameter/2, posY1 - antDiameter/2,
                    antDiameter, antDiameter));
            g2.setColor(Color.black);                            //отрисовкка нового положения муравьев
            g2.fill(new Ellipse2D.Double(posX2 - antDiameter/2, posY2 - antDiameter/2,
                                         antDiameter, antDiameter));

            if(AntColony[i].getVisitedEdge() != null ){
                if( AntColony[i].getVisitedEdge().getPheromonLevel() != 0.0){
                    phlev = Math.log((AntColony[i].getVisitedEdge().getPheromonLevel()));
                }
                else
                    phlev = 0;

            g2.setStroke(new BasicStroke(0.2f + (float)phlev));
            }
            else {
            g2.setStroke(new BasicStroke(0.2f));
            g2.drawLine(posX1, posY1, posX2, posY2);
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            repaint();
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {}
        }
    }
}
