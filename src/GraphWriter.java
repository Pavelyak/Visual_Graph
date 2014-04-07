/**
 * Created by Ринат on 07.04.14.
 */

import visualGraph.src.Arrow2D;
import visualGraph.src.GraphNode;


import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Enumeration;
import java.util.logging.Logger;

public class GraphWriter extends JComponent {
    Graphics2D g2;
    int width;
    int height;
    int fringe;
    GGraph graph;
    public final int nodeDiameter = 10; // диаметр вершинки

    public GraphWriter(int width, int height, int fringe, GGraph graph){
        this.width =  width - (2*fringe);      // размер области рисования
        this.height = height - 3*fringe;       // размер области рисования
        this.fringe = fringe;
        this.graph = graph;
    }



    // отрисовка
    public void paint(Graphics g) {
        //Logger.global.info("@ => paint in dGraphWriter");
        this.g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,                          // хз, что это
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.white);                                // отрисовка области белым цветом
        g2.fillRect(fringe, fringe, width, height);

        for (int id = 0; id <= graph.getNodesCount() - 1; id++){ // пробегаемся по массиву вершин графа
            GNode gNode = graph.getGNode(id);                    // и отрисовываем их
            int x = gNode.getX();
            int y = gNode.getY();

            // отрисовка ребер
            g2.setColor(Color.black);
            for (int i = 0; i < gNode.listOut.size(); i++){
                GNode gNodeFinish = gNode.listOut.get(i);
                int xFin = gNodeFinish.getX();
                int yFin = gNodeFinish.getY();
                g2.drawLine(x + nodeDiameter/2, y + nodeDiameter/2, xFin + nodeDiameter/2, yFin + nodeDiameter/2);
            }

            // отрисовка вершин
            g2.setColor(Color.red);
            g2.fill(new Ellipse2D.Double(x, y, nodeDiameter, nodeDiameter));

        }
    }
        /*for (GraphNode node : nodes) {
            double posX = getXPos(node.getX());
            double posY = getYPos(node.getY());
            for (Enumeration<GraphNode> e = node.getNeighbours();
                 e.hasMoreElements();) {
                GraphNode neighbour = e.nextElement();
                g2.setColor(Color.black);
                Arrow2D arrow = new Arrow2D(posX, posY,
                        getXPos(neighbour.getX()),
                        getYPos(neighbour.getY()),
                        nodeDiamter/2);
                arrow.draw(g2);
            }
            g2.setColor(Color.green); // Node color

            posX -= nodeDiamter/2;
            posY -= nodeDiamter/2;
            g2.fill(new Ellipse2D.Double(posX, posY, nodeDiamter, nodeDiamter));*/

    //g2.setColor(Color.black); // Label color
    //g2.drawString(node.getName(), (int) posX, (int) posY);// + " at (" + (int) posX + ", " + (int) posY + ")"

    //update(g2);


}
