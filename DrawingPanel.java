import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;


public class DrawingPanel extends JPanel implements Serializable {

    int NODE_WIDTH = 5;
    int NODE_HEIGHT = 5;
    static class Line implements Serializable{
        /*  array of class<lines>
            to store lines by recoding it's two nodes
         */
        int x1, y1, x2, y2;
        public Line(int x1, int y1, int x2, int y2){
            this.x1 = x1;   // node 1
            this.y1 = y1;
            this.x2 = x2;   // node 2
            this.y2 = y2;
        }
    }
    static class Node implements Serializable{

        int x, y;
        public Node(int x, int y){
            this.x = x;
            this.y = y;

        }
    }

    static class Dyn_Line implements Serializable{      // save the line by saving its two node index

        int nodeIndex1, nodeIndex2;
        public Dyn_Line(int nodeIndex1, int nodeIndex2){
            this.nodeIndex1 = nodeIndex1;
            this.nodeIndex2 = nodeIndex2;

        }
    }
    ArrayList<Line> lines = new ArrayList<Line>();
    ArrayList<Dyn_Line> dyn_lines = new ArrayList<Dyn_Line>();
    ArrayList<Node> nodes = new ArrayList<Node>();

    Node initialNode = null, targetNode = null;
    boolean initPointDragging = true, lineDrawPermission = false, bufferLine=false;
    int xBuff1, xBuff2, yBuff1, yBuff2, nodeBuffer1, nodeBuffer2;
    String actionSelected = "add";
    public DrawingPanel() {

        addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                        if (e.getButton() == MouseEvent.BUTTON1) {

                            super.mouseClicked(e);
                            ////////////////////////////////////////////////////////////////////////////////////////////////
                            // deleting a node with all its line when clicked
                            if (Objects.equals(actionSelected, "delete")) {
//
                                int x = e.getX();     // initial points
                                int y = e.getY();
                                Node targetNodeDelete = inNode(x,y);
                                if (targetNodeDelete != null) {
                                    System.out.println("hi1");
                                    removeNode(targetNodeDelete);
                                    System.out.println("hi2");

                                }

                            }
                            if (Objects.equals(actionSelected, "add")) {     // insert a node

                                int x = e.getX();     // node points
                                int y = e.getY();
                                addNode(x, y);
                                repaint();
                            }
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        super.mousePressed(e);
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                        initPointDragging = true;
                        if (e.getButton() == MouseEvent.BUTTON1) {

                            if (Objects.equals(actionSelected, "add")) {
                                lineDrawPermission = false;
                                initialNode = inNode(xBuff2, yBuff2);
                                if (initialNode != null) {          // edge ended on a node
                                    nodeBuffer2 = nodes.indexOf(initialNode);
                                    System.out.println("in node[end]");
                                    addLine(xBuff1, yBuff1, xBuff2, yBuff2);
                                    addDynLine(nodeBuffer1, nodeBuffer2);

                                }
                                else {
                                    System.out.println("not in node[end]");
                                    bufferLine = false;

                                }
                                xBuff1 = xBuff2 = yBuff1 = yBuff2 = 0;

                            }
                            else if (Objects.equals(actionSelected, "move")){

                                if (targetNode!=null){
                                    int x = e.getX();
                                    int y = e.getY();
                                    targetNode.x = x;
                                    targetNode.y = y;
                                    targetNode = null;
                                }
                            }
                            repaint();
                        }

                    }
                }
        );

        addMouseMotionListener(
                new MouseMotionAdapter() {
                    @Override
                    public void mouseDragged(MouseEvent e) {

                        // only if it's a left click
                        if (e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK) {

                            if (Objects.equals(actionSelected, "add")) {
                                if (initPointDragging) {         // initial point where it drags
                                    initPointDragging = false;
                                    int x = e.getX();
                                    int y = e.getY();

                                    System.out.println("inNode(x,y) : " + inNode(x, y));
                                    initialNode = inNode(x, y);
                                    if (initialNode != null) {
                                        System.out.println("in node");
                                        lineDrawPermission = true;
                                        xBuff1 = initialNode.x;
                                        yBuff1 = initialNode.y;
                                        nodeBuffer1 = nodes.indexOf(initialNode);
                                        System.out.println("nodeBuffer1: " + nodeBuffer1);
                                    }

                                }

                                else {
                                    if (lineDrawPermission) {
                                        if (Objects.equals(actionSelected, "add")) {
                                            xBuff2 = e.getX();
                                            yBuff2 = e.getY();
                                            bufferLine = true;
                                            repaint();
                                        }
                                    }
                                }
                            }

                            else if (Objects.equals(actionSelected, "move")){
                                if (initPointDragging) {         // initial point where it drags
                                    initPointDragging = false;
                                    int x = e.getX();
                                    int y = e.getY();

                                    System.out.println("initial in moving node");

                                    targetNode = inNode(x, y);
                                    if (targetNode != null) {
                                        System.out.println("moving the node...");

                                    }
                                }

                                else{
                                    if (targetNode != null) {
                                        int x = e.getX();
                                        int y = e.getY();
                                        targetNode.x = x;
                                        targetNode.y = y;
                                    }
                                    repaint();

                                }
                            }

                        }
                    }
                }
        );
    }

    public void addNode(int x, int y){
        Node newNode = new Node(x, y);
        nodes.add(newNode);
    }

    public void addLine(int x1, int y1, int x2, int y2){
        Line newLine = new Line(x1, y1, x2, y2);
        lines.add(newLine);
    }

    public void addDynLine(int nodeIndex1, int nodeIndex2){
        Dyn_Line newLine = new Dyn_Line(nodeIndex1, nodeIndex2);
        dyn_lines.add(newLine);
    }

    public void removeNode(Node node){
        int nodeIndex = nodes.indexOf(node);
        nodes.remove(nodeIndex);

        Iterator<Dyn_Line> dyn_line_itr = dyn_lines.iterator();
        while(dyn_line_itr.hasNext()){

            Dyn_Line line = dyn_line_itr.next();
            System.out.println("checking line " + line.nodeIndex1 + " -> " + line.nodeIndex2);
            if(line.nodeIndex1 == nodeIndex || line.nodeIndex2 == nodeIndex)
                dyn_line_itr.remove();

            else if(line.nodeIndex1 > nodeIndex || line.nodeIndex2 > nodeIndex) {

                if (line.nodeIndex1 > nodeIndex)
                    line.nodeIndex1--;

                if (line.nodeIndex2 > nodeIndex)
                    line.nodeIndex2--;
            }
        }
//        System.out.println("new length: " + dyn_lines.size());
        repaint();
    }

    public void updateClass(ArrayList<ArrayList> data){

        this.dyn_lines = data.get(1);
        this.nodes = data.get(0);
        repaint();
    }

    public ArrayList getClassInfo(){
        ArrayList<ArrayList> data = new ArrayList<>();
        data.add(nodes);
        data.add(dyn_lines);
        return data;

    }

    public Node inNode(int x, int y){
        int NODE_ERROR_THRESHOLD = 8;
        for (Node node: nodes){
            if (node.x - NODE_ERROR_THRESHOLD <= x && x <= node.x + NODE_ERROR_THRESHOLD) {        // check x value
                if (node.y - NODE_ERROR_THRESHOLD <= y && y <= node.y + NODE_ERROR_THRESHOLD)   // check y value
                    return node;
            }
        }
        return null;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;

        for (Node node: nodes) {
            g.drawOval(node.x - NODE_WIDTH / 2, node.y - NODE_HEIGHT / 2, NODE_WIDTH, NODE_HEIGHT);
            g.fillOval(node.x - NODE_WIDTH / 2, node.y - NODE_HEIGHT / 2, NODE_WIDTH, NODE_HEIGHT);
        }
        for (Dyn_Line line: dyn_lines){
            Node node1 = nodes.get(line.nodeIndex1);
            Node node2 = nodes.get(line.nodeIndex2);
            Line2D newLine = new Line2D.Double(node1.x,node1.y,node2.x,node2.y);
            g2d.draw(newLine);

            AffineTransform tx = new AffineTransform();
            Polygon arrowHead = new Polygon();
            arrowHead.addPoint( 0,5);
            arrowHead.addPoint( -5, -5);
            arrowHead.addPoint( 5,-5);

            tx.setToIdentity();
            double angle = Math.atan2(node2.y-node1.y, node2.x-node1.x);
            tx.translate(node2.x, node2.y);
            tx.rotate((angle-Math.PI/2d));

            Graphics2D g2d2 = (Graphics2D) g2d.create();
            g2d2.setTransform(tx);
            g2d2.fill(arrowHead);
            g2d2.dispose();
        }

        // to buffer draw the current drawing line (if exists)
        if (Objects.equals(actionSelected, "add") && bufferLine) {
            Line2D line = new Line2D.Double(xBuff1, yBuff1, xBuff2, yBuff2);
            g2d.draw(line);
            AffineTransform tx = new AffineTransform();
            Polygon arrowHead = new Polygon();
            arrowHead.addPoint( 0,5);
            arrowHead.addPoint( -5, -5);
            arrowHead.addPoint( 5,-5);

            tx.setToIdentity();
            double angle = Math.atan2(yBuff2-yBuff1, xBuff2-xBuff1);
            tx.translate(xBuff2, yBuff2);
            tx.rotate((angle-Math.PI/2d));

            Graphics2D g2d2 = (Graphics2D) g2d.create();
            g2d2.setTransform(tx);
            g2d2.fill(arrowHead);
            g2d2.dispose();
            bufferLine = false;
        }

    }
}
