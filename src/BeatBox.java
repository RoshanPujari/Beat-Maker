import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Track;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BeatBox {
    private ArrayList<JCheckBox> checkBoxList;
    private Sequencer sequencer;
    private Sequence sequence;
    private Track track;
    private JPanel checkPane;

    String[] instrumentName = {"Bass Drum", "Closed Hi-Hat",
            "Open Hi-Hat","Acoustic Snare", "Crash Cymbal", "Hand Clap",
            "High Tom", "Hi Bongo", "Maracas", "Whistle", "Low Conga",
            "Cowbell", "Vibraslap", "Low-mid Tom", "High Agogo",
            "Open Hi Conga"};

    int[] instrument = {35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63};

    public static void main(String[] args) {
        new BeatBox().buildGUI();
    }

    private void buildGUI() {
        //TODO: create the main frame and main panel
        //main frame to be created
        JFrame frame = new JFrame("MeatBeater");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // creating layout for the panel
        BorderLayout borderLayout = new BorderLayout();
        JPanel background = new JPanel(borderLayout);
        background.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        //TODO: create the container where buttons will reside
        //Box container to store all the buttons in boxlayout horizontally
        Box buttonBox = new Box(BoxLayout.Y_AXIS);
        buttonBox.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
        //creating all the buttons we need
        JButton start = new JButton("Play");
        start.addActionListener(e -> createTrackAndStart());
        buttonBox.add(start);

        JButton stop = new JButton("Stop");
        start.addActionListener(e -> sequencer.stop());
        buttonBox.add(stop);

        JButton upTempo = new JButton("Tempo up");
        start.addActionListener(e -> changeTempo());
        buttonBox.add(upTempo);

        JButton downTempo = new JButton("Temp down");
        start.addActionListener(e -> changeTempo());
        buttonBox.add(downTempo);

        //TODO: create the container where instrument labels will reside
        //Box container to store all the names of the instrument
        Box nameBox = new Box(BoxLayout.Y_AXIS);
        nameBox.setBorder(BorderFactory.createEmptyBorder(0,5,0,10));
        for(int i = 0; i < 16; i++){
            JLabel label = new JLabel(instrumentName[i]);
            label.setBorder(BorderFactory.createEmptyBorder(4,1,4,1));
            nameBox.add(label);
        }

        //TODO: create and setup container for checkboxes
        //creating checkbox arraylist to store all the CheckBox components
        checkBoxList = new ArrayList<>();
        //Layout for arranging checkboxes
        GridLayout grid = new GridLayout(16,16);
        grid.setHgap(6);
        grid.setVgap(1);
        // another panel that contains all the checkboxes
        checkPane = new JPanel(grid);

        for(int i = 0; i < 256; i++) {
           JCheckBox checkBox = new JCheckBox();
           checkBox.setSelected(false);
           checkPane.add(checkBox);
           checkBoxList.add(checkBox);

        }

        //TODO: Add Everything
        //add instrument name Box container to the panel
        background.add(nameBox, BorderLayout.WEST);
        //add checkbox panel to the panel
        background.add(checkPane, BorderLayout.CENTER);
        //add buttons Box container to the panel
        background.add(buttonBox, BorderLayout.EAST);

        //add the panel to the frame's content pane
        frame.getContentPane().add(background);

        //TODO: setting all the frames properties
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    private void createTrackAndStart() {

    }

    private void changeTempo() {

    }

}
