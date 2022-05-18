import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;


public class Main{

    public static void writeToFile (File path, ArrayList data)
    {
        try(ObjectOutputStream write= new ObjectOutputStream (new FileOutputStream(path)))
        {
            write.writeObject(data);
            System.out.println("Done writing to file");
        }
        catch(NotSerializableException nse)
        {
            System.out.println("Error writing in file " + nse);

        }
        catch(IOException eio)
        {
            System.out.println("Error writing in file " + eio);
        }
    }

    public static ArrayList readFromFile(File path)
    {
        ArrayList data = null;

        try(ObjectInputStream inFile = new ObjectInputStream(new FileInputStream(path)))
        {
            data = (ArrayList) inFile.readObject();
            System.out.println("Done reading from file");
            return data;
        }
        catch(ClassNotFoundException | IOException cnfe)
        {
            System.out.println("Error writing in file " + cnfe);

        }
        return data;
    }

    public static void main(String[] args) throws FileNotFoundException {


        //#############################################################################################################
        // CREATING FRAME AND PANELS
        JFrame mainF = new JFrame("Graph drawer");
        final DrawingPanel[] drawingP = {new DrawingPanel()};

        Border border = new LineBorder(Color.ORANGE, 4, true);
        drawingP[0].setBorder(border);

        JPanel optionP = new JPanel();
        JRadioButton addRB = new JRadioButton("add node", true);
        JRadioButton moveRB = new JRadioButton("move node");
        JRadioButton deleteRB = new JRadioButton("delete node");
        ButtonGroup jRadioGroup = new ButtonGroup( );

        JLabel instructionHelper = new JLabel("Add nodes by clicking, add edge by pressing and dragging your mouse");        // provides instruction when selecting buttons

        JButton saveToFile = new JButton("save to file");
        JButton readFromFile = new JButton("read from file");
        JButton reset = new JButton("reset");

        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainF.remove(drawingP[0]);
                drawingP[0] = new DrawingPanel();
                mainF.add(drawingP[0]);
                drawingP[0].setBounds(0,0,1000,600);
                drawingP[0].setBorder(border);
                drawingP[0].addMouseListener(new PopClickListener(drawingP[0], addRB, moveRB, deleteRB, instructionHelper));      // for the right click menu
                SwingUtilities.updateComponentTreeUI(mainF);

            }
        });
        saveToFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Specify a file to save");
                fileChooser.setSelectedFile(new File("SavedDrawing.chen"));
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                FileNameExtensionFilter filter = new FileNameExtensionFilter("chen files (*.chen)", "chen");
                fileChooser.setFileFilter(filter);
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    fileToSave = new File(fileToSave , "SavedDrawing.chen");
                    ArrayList data = drawingP[0].getClassInfo();
                    writeToFile(fileToSave, data);
                }

            }
        });
        readFromFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File selectedFile;
                JFileChooser chooser = new JFileChooser();
                chooser.setSelectedFile(new File("SavedDrawing.chen"));
                FileNameExtensionFilter filter = new FileNameExtensionFilter("chen files (*.chen)", "chen");
                chooser.setFileFilter(filter);
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    selectedFile = chooser.getSelectedFile();
                    ArrayList data = readFromFile(selectedFile);
                    drawingP[0].updateClass(data);
                }

            }
        });
        jRadioGroup.add(addRB);
        jRadioGroup.add(moveRB);
        jRadioGroup.add(deleteRB);

        mainF.add(drawingP[0]);
        mainF.add(optionP);

        optionP.add(addRB);
        optionP.add(moveRB);
        optionP.add(deleteRB);
        optionP.add(instructionHelper);
        optionP.add(saveToFile);
        optionP.add(readFromFile);
        optionP.add(reset);
        drawingP[0].addMouseListener(new PopClickListener(drawingP[0], addRB, moveRB, deleteRB, instructionHelper));      // for the right click menu

//        drawingP.addMouseListener(new PopClickListener(drawingP, addRB, moveRB, deleteRB, instructionHelper));      // for the right click menu
        //#############################################################################################################

//         adding action listener to JRadio button
        addRB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingP[0].actionSelected = "add";
                instructionHelper.setText("Add nodes by clicking, add edge by pressing and dragging your mouse");
            }
        });
        moveRB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingP[0].actionSelected = "move";
                instructionHelper.setText("Move a node by pressing on the line and dragging your mouse");

            }
        });
        deleteRB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingP[0].actionSelected = "delete";
                instructionHelper.setText("Delete a node by clicking on the line **All the edges connected with the node will be gone");

            }
        });

        //#############################################################################################################
        // LAYOUT
        mainF.setLayout(null);
        optionP.setLayout(null);
        mainF.setSize(1015, 850);
        drawingP[0].setBounds(0,0,1000,600);
        optionP.setBounds(0,600,900,300);

        addRB.setBounds(50,10,100,50);
        moveRB.setBounds(50,50,200,50);
        deleteRB.setBounds(50,90,200,50);
        instructionHelper.setBounds(50,150,1000,50);

        saveToFile.setBounds(700, 20, 150,30);
        readFromFile.setBounds(700, 70, 150,30);
        reset.setBounds(700, 120, 150,30);

        mainF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainF.setVisible(true);

    }
}
