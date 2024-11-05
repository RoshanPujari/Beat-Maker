package beatbox;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.sound.midi.*;
import static javax.sound.midi.ShortMessage.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class BeatBox {

    private ArrayList<JCheckBox> checkBoxList;
    private Sequencer sequencer;
    private Sequence sequence;
    private Track track;
    private JFrame frame;
    private JPanel checkPane;

    String[] instrumentName = {"Bass Drum", "Closed Hi-Hat",
        "Open Hi-Hat", "Acoustic Snare", "Crash Cymbal", "Hand Clap",
        "High Tom", "Hi Bongo", "Maracas", "Whistle", "Low Conga",
        "Cowbell", "Vibraslap", "Low-mid Tom", "High Agogo",
        "Open Hi Conga"};

    int[] instrument = {35, 42, 46, 38, 49, 39, 50, 60, 70, 72, 64, 56, 58, 47, 67, 63};

    public static void main(String[] args) {
        new BeatBox().buildGUI();
    }

    private void buildGUI() {
        //TODO: create the main frame and main panel
        //main frame to be created
        frame = new JFrame("BeatBeater");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // creating layout for the panel
        BorderLayout borderLayout = new BorderLayout();
        JPanel background = new JPanel(borderLayout);
        background.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //TODO: create menu bar for File Menu conatining save and load
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save"); // save menu item
        saveItem.addActionListener(e -> saveDialogBox());
        JMenuItem loadItem = new JMenuItem("Load"); // load menu item
        loadItem.addActionListener(e -> openDialogBox());

        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        menuBar.add(fileMenu);

        //TODO: create the container where buttons will reside
        //Box container to store all the buttons in boxlayout horizontally
        Box buttonBox = new Box(BoxLayout.Y_AXIS);
        buttonBox.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        //creating all the buttons we need
        JButton start = new JButton("Play");
        start.addActionListener(e -> {
            createTracks();
            startSequencer();
        });
        buttonBox.add(start);

        JButton stop = new JButton("Stop");
        stop.addActionListener(e -> sequencer.stop());
        buttonBox.add(stop);

        JButton upTempo = new JButton("Tempo up");
        upTempo.addActionListener(e -> changeTempo(1.03f));
        buttonBox.add(upTempo);

        JButton downTempo = new JButton("Temp down");
        downTempo.addActionListener(e -> changeTempo(0.97f));
        buttonBox.add(downTempo);

        //TODO: create the container where instrument labels will reside
        //Box container to store all the names of the instrument
        Box nameBox = new Box(BoxLayout.Y_AXIS);
        nameBox.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 10));
        for (int i = 0; i < 16; i++) {
            JLabel label = new JLabel(instrumentName[i]);
            label.setBorder(BorderFactory.createEmptyBorder(4, 1, 4, 1));
            nameBox.add(label);
        }

        //TODO: create and setup container for checkboxes
        //creating checkbox arraylist to store all the CheckBox components
        checkBoxList = new ArrayList<>();
        //Layout for arranging checkboxes
        GridLayout grid = new GridLayout(16, 16);
        grid.setHgap(6);
        grid.setVgap(1);
        // another panel that contains all the checkboxes
        checkPane = new JPanel(grid);

        for (int i = 0; i < 256; i++) {
            JCheckBox checkBox = new JCheckBox();
            checkBox.addActionListener(e -> createTrackAndResume());
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
        frame.getContentPane().add(menuBar, BorderLayout.NORTH);

        // midi sound setup
        setUpMidi();

        //TODO: setting all the frames properties
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    // as the name suggests
    private void setUpMidi() {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequence = new Sequence(Sequence.PPQ, 4);
            track = sequence.createTrack();
            sequencer.setTempoInBPM(120);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // creates track
    private void createTracks() {
        int[] trackList;

        //delete the previous track
        sequence.deleteTrack(track);
        track = sequence.createTrack();

        for (int i = 0; i < 16; i++) {
            trackList = new int[16];
            int key = instrument[i];

            for (int j = 0; j < 16; j++) {
                JCheckBox ck = checkBoxList.get(j + 16 * i);
                if (ck.isSelected()) {
                    trackList[j] = key;
                } else {
                    trackList[j] = 0;
                }
            }
            makeTrack(trackList);
            track.add(makeEvent(CONTROL_CHANGE, 1, 127, 0, 16));
        }
        track.add(makeEvent(PROGRAM_CHANGE, 9, 1, 0, 15));
    }

    // starts the sequencer
    private void startSequencer() {
        try {
            sequencer.setSequence(sequence);
            sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
            sequencer.setTempoInBPM(120);
            sequencer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // this one is for the checkbox to update the track and keep playing
    private void createTrackAndResume() {
        if (sequencer.isRunning()) {
            sequencer.stop();
            createTracks();
            startSequencer();
        }
    }

    private void makeTrack(int[] list) {
        for (int i = 0; i < 16; i++) {
            int key = list[i];

            if (key != 0) {
                track.add(makeEvent(NOTE_ON, 9, key, 100, i));
                track.add(makeEvent(NOTE_OFF, 9, key, 100, i + 1));
            }
        }
    }

    private void changeTempo(float tempoMultiplier) {
        float tempoFactor = sequencer.getTempoFactor();
        sequencer.setTempoFactor(tempoFactor * tempoMultiplier);
    }

    public static MidiEvent makeEvent(int command, int channel, int data1, int data2, int tick) {
        MidiEvent me;
        try {
            ShortMessage shmsg = new ShortMessage(command, channel, data1, data2);
            me = new MidiEvent(shmsg, tick);

        } catch (InvalidMidiDataException e) {
            throw new RuntimeException(e);
        }
        return me;

    }

    private void writeFile(String name) {
        boolean[] checkBoxStats = new boolean[256];

        for (int i = 0; i < 256; i++) {
            JCheckBox checkBox = checkBoxList.get(i);
            if (checkBox.isSelected()) {
                checkBoxStats[i] = true;
            }
        }

        // TWR in action
        try (ObjectOutputStream savedCheckBoxStats = new ObjectOutputStream(new FileOutputStream(name))) {
            savedCheckBoxStats.writeObject(checkBoxStats);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void loadFile(String name) {
        boolean[] checkBoxStats = null;

        try (ObjectInputStream loadCheckBoxStat = new ObjectInputStream(new FileInputStream(name))) {
            checkBoxStats = (boolean[]) loadCheckBoxStat.readObject();

        } catch (ClassNotFoundException | IOException e) { // interesting...
            e.printStackTrace();
        }

        for (int i = 0; i < 256; i++) {
            checkBoxList.get(i).setSelected(checkBoxStats[i]);
        }

        sequencer.stop();

    }

    private void saveDialogBox() {
        String fileName = null;
        JFileChooser fileChooser = new JFileChooser(new File(""));
        FileFilter fileFilter = new FileNameExtensionFilter("ser", "ser");
        fileChooser.setFileFilter(fileFilter);
        int i = fileChooser.showSaveDialog(frame);

        if (i == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            fileName = selectedFile.getAbsolutePath(); //getName();
            writeFile(fileName);
        }

    }

    private void openDialogBox() {
        String fileName = null;
        JFileChooser fileChooser = new JFileChooser(new File(""));
        FileFilter fileFilter = new FileNameExtensionFilter("ser", "ser");
        fileChooser.setFileFilter(fileFilter);
        int i = fileChooser.showOpenDialog(frame);

        if (i == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            fileName = selectedFile.getAbsolutePath(); //getName();
            loadFile(fileName);
        }

    }

//    class TrackEnd implements ControllerEventListener{
//        @Override
//        public void controlChange(ShortMessage event) {
//            createTrackAndStart();
//            System.out.println("End");
//        }
//    }
}
