import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//#############################################################################################################
// pop up menu - right click
class PopClickListener extends MouseAdapter {
    DrawingPanel p;
    JRadioButton drawLine, move, remove;
    JLabel instructionHelper;
    PopClickListener(DrawingPanel p, JRadioButton drawLine, JRadioButton move, JRadioButton remove, JLabel instructionHelper){
        this.p = p;
        this.drawLine = drawLine;
        this.move = move;
        this.remove = remove;
        this.instructionHelper = instructionHelper;
    }

    public void mousePressed(MouseEvent e) {

        if(e.getButton() == MouseEvent.BUTTON3) {
            if (e.isPopupTrigger())
                doPop(e);
        }
    }

    public void mouseReleased(MouseEvent e) {

        if(e.getButton() == MouseEvent.BUTTON3) {
            if (e.isPopupTrigger())
                doPop(e);
        }
    }

    private void doPop(MouseEvent e) {
        PopUpOnNode menuOnNode = new PopUpOnNode(p, e.getX(), e.getY());
        PopUpNotLine menuNotNode = new PopUpNotLine(p, drawLine, move, remove, instructionHelper);
        DrawingPanel.Node startNode = p.inNode(e.getX(), e.getY());
        if(startNode != null){    //on Node
            menuOnNode.show(e.getComponent(), e.getX(), e.getY());
        }

        else{
            menuNotNode.show(e.getComponent(), e.getX(), e.getY());

        }

    }
}
//#############################################################################################################
// menu when right click is on node
class PopUpOnNode extends JPopupMenu {      // on line menu
    JMenuItem anItem;
    public PopUpOnNode(DrawingPanel drawingP, int x, int y) {
        anItem = new JMenuItem("remove node");
        anItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DrawingPanel.Node nodeRemove = drawingP.inNode(x, y);
                drawingP.removeNode(nodeRemove);
                repaint();

            }
        });
        add(anItem);
    }
}

//#############################################################################################################
// menu when right click is NOT on node
class PopUpNotLine extends JPopupMenu {     // not on line menu
    JMenuItem anItem,anItem2, anItem3;

    // constructor
    public PopUpNotLine(DrawingPanel drawingP,  JRadioButton drawLine, JRadioButton move, JRadioButton remove, JLabel instructionHelper){
        anItem = new JMenuItem("change selection to add node");
        anItem2 = new JMenuItem("change selection to move node");
        anItem3 = new JMenuItem("change selection to remove node");

        //#############################################################################################################
        // action for right click when it's NOT on any line
        anItem.addActionListener(new ActionListener() {     // add
            public void actionPerformed(ActionEvent e) {
                drawLine.setSelected(true);
                drawingP.actionSelected = "add";
                instructionHelper.setText("Add nodes by clicking, add edge by pressing and dragging your mouse");
            }
        });

        anItem2.addActionListener(new ActionListener() {    // move
            public void actionPerformed(ActionEvent e) {
                move.setSelected(true);
                drawingP.actionSelected = "move";
                instructionHelper.setText("Move a node by pressing on the line and dragging your mouse");
            }
        });

        anItem3.addActionListener(new ActionListener() {    // remove
            public void actionPerformed(ActionEvent e) {
                remove.setSelected(true);
                drawingP.actionSelected = "delete";
                instructionHelper.setText("Delete a node by clicking on the line **All the edges connected with the node will be gone");
            }
        });
        
        add(anItem);
        add(anItem2);
        add(anItem3);
    }
}
