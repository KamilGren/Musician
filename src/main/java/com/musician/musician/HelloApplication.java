package com.musician.musician;

import javafx.application.Application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class HelloApplication extends Application {

    public GridPane root = new GridPane();
    public HBox hBox = new HBox();
    public VBox vBox = new VBox();
    public ComboBox<Object> cNameOfChord1 = new ComboBox<>();
    public ComboBox<String> cNameOfChord2 = new ComboBox<String>();
    public ComboBox<String> cNameOfChord3 = new ComboBox<String>();
    public ComboBox<Object> cNameOfScale1 = new ComboBox<>();
    public ComboBox<String> cNameOfScale2 = new ComboBox<String>();
    public ComboBox<String> clevelOfOctavia = new ComboBox<String>();
    public static TextArea scaleHarmonicInformations = new TextArea();


    @Override
    public void start(Stage stage)
    {

        vBox.getChildren().addAll(root, hBox);
        hBox.setPadding(new Insets(15, 12, 15, 12));
        hBox.setSpacing(10);

        fillGuitarNeck();

        // first refresh here only for add another background for some nodes - new css class doesnt work 8-)
        clearFretboard();
        buildMenuOptions();

        for(int i = 0; i <= 12; i++)
        {
            ColumnConstraints column = new ColumnConstraints();
            RowConstraints row = new RowConstraints();

            root.getColumnConstraints().add(column);
            root.getRowConstraints().add(row);

            column.setPrefWidth(90);
            row.setPrefHeight(90);

        }

        Scene scene = new Scene(vBox, 1695, 350);

        try {
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        stage.setTitle("Musician");
        stage.setScene(scene);
        stage.show();
    }

    public void buildMenuOptions()
    {
        // all what we need to know what type of chord client is looking for:

        String  typeOfChords[] = {"Major", "Minor", "Dominant", "Half-diminished", "Diminished"};
        String  typeOfChords2[] = {"(3 notes)", "7 (4 notes)"};
        String typeOfScales[] = {"1 - aolian scale", "2 - locrian scale", "3 - ionian / dur scale", "4 - dorian scale", "5 - phrygian scale", "6 - lydian scale", "7 - mixolydian scale", "8 - harmonic moll scale", "9 - phrygian scale", "10 - melodic moll scale" };

        String octaviaLevels[] = {"4", "3", "2", "1"};

        Label lChooseOctaviaLevel = new Label("Choose octava level: ");
        clevelOfOctavia = new ComboBox<String>(FXCollections.observableArrayList(octaviaLevels));

        Label lChooseChord = new Label ("Choose your chord: ");
        cNameOfChord1 = new ComboBox<Object>(FXCollections.observableArrayList(GuitarString.getListOfStringNotes()));
        cNameOfChord2 = new ComboBox<>(FXCollections.observableArrayList(typeOfChords));
        cNameOfChord3 = new ComboBox<>(FXCollections.observableArrayList(typeOfChords2));

        // we are choose default values for ComboBoxes
        cNameOfChord1.getSelectionModel().selectFirst();
        cNameOfChord2.getSelectionModel().selectFirst();
        cNameOfChord3.getSelectionModel().selectFirst();

        cNameOfChord1.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            clearFretboard();
            createChord(createChordName());
        });
        cNameOfChord2.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            clearFretboard();
            createChord(createChordName());
        });
        cNameOfChord3.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            clearFretboard();
            createChord(createChordName());
        });

        clevelOfOctavia.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            clearFretboard();
            findNotesInOneOctavia(Integer.parseInt(newValue), "orange");
                });

        // all what we want to know to show typed scale on fretboard

        // scale is key (another name) like A moll scale...
        Label lChooseScale = new Label ("Choose your scale: ");
        cNameOfScale1 = new ComboBox<Object>(FXCollections.observableArrayList(GuitarString.getListOfStringNotes()));
        cNameOfScale2 = new ComboBox<>(FXCollections.observableArrayList(typeOfScales));
        AtomicBoolean flag1 = new AtomicBoolean(false);
        AtomicBoolean flag2 = new AtomicBoolean(false);

        cNameOfScale1.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            clearFretboard();
            flag1.set(true);

            if(!newValue.toString().isEmpty() && flag2.get()) {

                String scaleNoteName = cNameOfScale1.getValue().toString();
                // we need type of scale in numbers choosen by user to use it in pattern to create this scale
                // its like 1 - natural moll, 2 - locrian...
                int typeOfScale = Character.getNumericValue(cNameOfScale2.getValue().charAt(0));
                List <String> guitarKeyToShow = createGuitarKey(scaleNoteName, typeOfScale);
                showGuitarKey(guitarKeyToShow);
                changeTextInTextArea(guitarKeyToShow);
            }
        });
        cNameOfScale2.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            clearFretboard();
            flag2.set(true);
            if(flag1.get() && flag2.get()) {
                String scaleNoteName = cNameOfScale1.getValue().toString();
                int typeOfScale = Character.getNumericValue(cNameOfScale2.getValue().charAt(0));
                List <String> guitarKeyToShow = createGuitarKey(scaleNoteName, typeOfScale);
                showGuitarKey(guitarKeyToShow);
                changeTextInTextArea(guitarKeyToShow);
            }
        });

        scaleHarmonicInformations.setMinSize(100, 100);

        hBox.getChildren().addAll(lChooseChord, cNameOfChord1, cNameOfChord2, cNameOfChord3, lChooseScale, cNameOfScale1, cNameOfScale2, scaleHarmonicInformations, lChooseOctaviaLevel, clevelOfOctavia);
    }

    // we are here, stop 13:06 //// 20:06, 17.03.2023 wtopka tutaj string zamiast list ze skala..
    public static void changeTextInTextArea(List<String> scale) {

        // find notes (and after that, find chords) on each position in used scale
        HashMap<Integer, List<String>> harmonicChords = showHarmonicChords(scale);
        List<String> chordList = notesToAccordTranslation(harmonicChords);
        StringBuilder chordsWithScalePositions = new StringBuilder();

        for(int i=0; i < chordList.size(); i++) {
            chordsWithScalePositions.append(i+1 + " " + chordList.get(i) + "\n");
        }

        scaleHarmonicInformations.setText(chordsWithScalePositions.toString());

    }

    // we are working on ready scale and we create most of chords from pattern (222)
    public static HashMap<Integer, List<String>> showHarmonicChords(List<String> someGuitarKey)
    {
        HashMap<Integer, List<String>> chords = new HashMap<>();

        // variable which we need to iterate on list, but we adding +2
        int index = 0;

        // iterate on some guitar key and add notes to every chord (inner loop) by increasing index +2
        // this is how chords here are build - in ABCDEFG A is A C E G :)
        for (int j = 0; j < someGuitarKey.size()-1; j++) {
            index += j;
            List<String> chord = new ArrayList<>();

            for (int i = 0; i <= 3; i++)
            {
                System.out.println("Index: " + index + "j: " + j);
                if (index > someGuitarKey.size()-1) {
                    index = index - (someGuitarKey.size()-1);
                    chord.add(someGuitarKey.get(index));
                    index += 2;
                } else {
                    chord.add(someGuitarKey.get(index));
                    index += 2;
                }
            }
            index = 0;

            // so here we have all chords with numbers of being in our scale
            chords.put(j, chord);
        }
        return chords;
    }

    // use to change notes from method above to chords by adding their names to list
    public static List<String> notesToAccordTranslation(HashMap<Integer, List<String>> notesOfChords) {

        List<String> chordNames = new ArrayList<>();

        for(Map.Entry<Integer, List<String>> set : notesOfChords.entrySet())
        {
            List<String> notes = GuitarString.getAllNotesFromChoosenNote(set.getValue().get(0)); // "sorting" all notes from first note like CC#DD#...

            if (set.getValue().get(1) == notes.get(3) && set.getValue().get(2) == notes.get(7)) {
                System.out.println("minor");
                chordNames.add(notes.get(0) + "minor7");
            }
            else if (set.getValue().get(1) == notes.get(4) && set.getValue().get(3) == notes.get(11)) {
                chordNames.add(notes.get(0) + "major7");
            }
            else if (set.getValue().get(2) == notes.get(7) && set.getValue().get(3) == notes.get(10)) {
                chordNames.add(notes.get(0) + "7");
            }
            else if (set.getValue().get(1) == notes.get(3) && set.getValue().get(2) == notes.get(6)) {
                chordNames.add(notes.get(0) + "7b5");
            }
            else if (set.getValue().get(1) == notes.get(2) && set.getValue().get(2) == notes.get(5) && set.getValue().get(3) == notes.get(8)) {
                chordNames.add(notes.get(0) + "o7");
            }
        }
        return chordNames;
    }

    // creating sorted notes (sorted how it is in music) like startNote A - AA#BCC#... startNote C - CC#DD#..
    public static List<String> createListOfNoteFromFirstNote(String startNote) {

        int index = GuitarString.getListOfStringNotes().indexOf(startNote);
        List <String> stringNotes = GuitarString.getListOfStringNotes();
        List<String> allNotesFromStartNote = new ArrayList<>(stringNotes.subList(index, stringNotes.size()));
        allNotesFromStartNote.addAll(stringNotes.subList(0, index));

        return allNotesFromStartNote;
    }

    // we are using three comboxes to obtain values to create with them name of chord
    // like E - minor - 7 (4 notes), A# - major - 3 (notes)

    public String createChordName()
    {
        return cNameOfChord1.getValue() + cNameOfChord2.getValue() + cNameOfChord3.getValue();
    }

    // creating guitarKey (scale) by using pattern which looks another for each scale
    public static List<String> createGuitarKey(String startNote, int level)
    {
        int index = 0;
        HashMap<Integer, String> guitarKey = new HashMap<>();
        guitarKey.put(1, "2122122"); // aeolian scale / natural moll
        guitarKey.put(2, "1221222"); // locrian scale
        guitarKey.put(3, "2212221"); // ionian / dur
        guitarKey.put(4, "2122212"); // dorian scale
        guitarKey.put(5, "1222122"); // phrygian scale
        guitarKey.put(6, "2221221"); // lydian scale
        guitarKey.put(7, "2212212"); // mixolydian scale
        guitarKey.put(8, "2122131"); // harmonic moll scale
        guitarKey.put(9, "1312122"); // phrygian dur scale
        guitarKey.put(10, "2122221"); // melodic moll scale


        // informations for my next scales update:
        // altered scale
        // altered start from dominant (7 notes from first note) even its note not from scale!
        // we are taking dominant note - in a natural moll its E
        // we are adding one note to it - now its F
        // and we are going to create f-moll melodic on 7 position
        // f-g-a#-b-c-d-e and add last on first position - e-f-g-a#-b-c-d
        // okey, so give me in DEFGABC#D


        List<String> guitarKeyNotes = new ArrayList<>();
        List<String> listOfNotes = createListOfNoteFromFirstNote(startNote);
        String pattern = guitarKey.get(level);

        for(int i = 0; i <= pattern.length(); i++) // do until pattern is end
        {

            if (index <= 11) { // max number in scale its 11 (start from 0) - its always 12 notes
                guitarKeyNotes.add(listOfNotes.get(index)); // we are receiving note name from following index
                index += Character.getNumericValue(pattern.charAt(i)); // we are adding numbers (1 or 2) from pattern to obtain higher index number in every loop rep
            }
            else {
                guitarKeyNotes.add(startNote);
            }
        }
        return guitarKeyNotes;
    }

    // here we use ready guitar key to be display on screen - on our group of nodes - notes (on our fretboard).
    public void showGuitarKey(List<String> guitarKey)
    {
        ObservableList<Node> notesOnBoard = root.getChildren(); // all notes from board

        // of course find from all nodes (notes)
        for (Node node : notesOnBoard) {

            //  find from list of strings (notes) in guitar key
            for (String s : guitarKey) {
                // if node (note) getName equals String s
                if (node instanceof Note && ((Note) node).getName().equals(s)) {

                    if (((Note) node).getName().equals(guitarKey.get(0))) {
                        node.setStyle("-fx-background-color: orange");
                    } else
                        node.setStyle("-fx-background-color: yellow");
                }
            }
        }
    }

    // here we are filling our root by adding to each row notes from proper string (fretboard.get(x))
    public void fillGuitarNeck()
    {
        HashMap<Integer, GuitarString> fretBoard = new HashMap<>();
        fretBoard = createFretBoard();


        for(int i = 0; i <= 20; i++)
        {
            root.add(fretBoard.get(1).getListOfNotes().get(i), i, 6);
            root.add(fretBoard.get(2).getListOfNotes().get(i), i, 5);
            root.add(fretBoard.get(3).getListOfNotes().get(i), i, 4);
            root.add(fretBoard.get(4).getListOfNotes().get(i), i, 3);
            root.add(fretBoard.get(5).getListOfNotes().get(i), i, 2);
            root.add(fretBoard.get(6).getListOfNotes().get(i), i, 1);

            Label lNumberOfBar = new Label(String.valueOf(i));
            root.add(lNumberOfBar, i, 0);
            lNumberOfBar.setStyle("-fx-font-weight: bold;");
            lNumberOfBar.setTextAlignment(TextAlignment.CENTER);


        }
        //lNumberOfBar.getStylesheets().add("guitarBar");
       // System.out.println("Poziom oktawy dla A" + guitarStringB3.getListOfNotes().get(9).getLevelOfOctavia() + "nazwa nuty: " + guitarStringB3.getListOfNotes().get(9).getName());

    }

    // we can also find which level of octavia (on guitar we have the same names of notes but we other sounds - their difference is level of octavia)
    public void findNotesInOneOctavia(int levelOfOctavia, String backgroundColor) {

        ObservableList<Node> childrens = root.getChildren();

        for(Node node : childrens)
        {
            if (!(node instanceof Label)) {
                Note note = (Note) node;
                if(note.getLevelOfOctavia() == levelOfOctavia)
                {
                    note.setStyle("-fx-background-color: " + backgroundColor);
                }
            }
        }
    }

    // creating fretboard by creating guitar strings and add them to hashmap
    public HashMap<Integer, GuitarString> createFretBoard() {

        HashMap<Integer, GuitarString> mapOfString = new HashMap<>();

        GuitarString guitarStringG = new GuitarString("G3");
        GuitarString guitarStringB = new GuitarString("B3");
        GuitarString guitarStringE2 = new GuitarString("E2");
        GuitarString guitarStringE4 = new GuitarString("E4");
        GuitarString guitarStringA = new GuitarString("A4");
        GuitarString guitarStringD = new GuitarString("D4");

        mapOfString.put(1, guitarStringE4);
        mapOfString.put(2, guitarStringA);
        mapOfString.put(3, guitarStringD);
        mapOfString.put(4, guitarStringG);
        mapOfString.put(5, guitarStringB);
        mapOfString.put(6, guitarStringE2);

        System.out.println(guitarStringA.getListOfStringNotes2());

        return mapOfString;
    }


    public void clearFretboard()
    {
        ObservableList<Node> childrens = root.getChildren();
         String harmonicButtonStyleClass = "harmonic-button";

        for(Node node : childrens )
        {
            if(!(node instanceof Label))
            node.setStyle("-fx-background-color: #b2ad7f");
                if((GridPane.getColumnIndex(node) == 3 || GridPane.getColumnIndex(node) == 5 || GridPane.getColumnIndex(node) == 7 || GridPane.getColumnIndex(node) == 9 || GridPane.getColumnIndex(node) == 12) && !(node instanceof Label))
                {
                    node.setStyle("-fx-background-color: #A9A47C");
                }
        }
    }

    // creating chords on fretboard
    public void createChord(String nameOfChord) {

        // name of chord can be X Maj - dur  its 7
        // X m - moll

        ObservableList<Node> childrens = root.getChildren();

        List <String> notesInChord = new ArrayList<>();

        String startNote;

        // first find startNote
        if(nameOfChord.contains("#"))
        {
            startNote = String.valueOf(nameOfChord.subSequence(0,2));
            System.out.println(startNote);
        }
        else
            startNote = String.valueOf(nameOfChord.charAt(0));

        List <String> allNotes = createListOfNoteFromFirstNote(startNote);

        // now create from chord pattern which is used in music harmony
        // major chords
        if (nameOfChord.contains("Major") && !nameOfChord.contains("7"))
        {
            notesInChord.add(allNotes.get(0));
            notesInChord.add(allNotes.get(4));
            notesInChord.add(allNotes.get(7));
        }
        else if (nameOfChord.contains("Major7"))
        {
            notesInChord.add(allNotes.get(0));
            notesInChord.add(allNotes.get(4));
            notesInChord.add(allNotes.get(7));
            notesInChord.add(allNotes.get(11));
        }

        // minor chords
        else if ((nameOfChord.contains("Minor")) && !nameOfChord.contains("7"))
        {
            notesInChord.add(allNotes.get(0));
            notesInChord.add(allNotes.get(3));
            notesInChord.add(allNotes.get(7));
        }
        else if (nameOfChord.contains("Minor7"))
        {
            notesInChord.add(allNotes.get(0));
            notesInChord.add(allNotes.get(3));
            notesInChord.add(allNotes.get(7));
            notesInChord.add(allNotes.get(10));
        }

        // diminished chords
        else if (nameOfChord.contains("Diminished") && !nameOfChord.contains("7") && !nameOfChord.contains("-"))
        {
            notesInChord.add(allNotes.get(0));
            notesInChord.add(allNotes.get(3));
            notesInChord.add(allNotes.get(6));
        }
        else if (nameOfChord.contains("Diminished7") && !nameOfChord.contains("-"))
        {
            notesInChord.add(allNotes.get(0));
            notesInChord.add(allNotes.get(3));
            notesInChord.add(allNotes.get(7));
            notesInChord.add(allNotes.get(9));
        }

        // half-dimnished chords
        else if (nameOfChord.contains("Half-diminished7"))
        {
            notesInChord.add(allNotes.get(0));
            notesInChord.add(allNotes.get(3));
            notesInChord.add(allNotes.get(6));
            notesInChord.add(allNotes.get(10));
        }
        else if (nameOfChord.contains("Half-diminished") && !nameOfChord.contains("7"))
        {
            notesInChord.add(allNotes.get(0));
            notesInChord.add(allNotes.get(3));
            notesInChord.add(allNotes.get(6));
        }
        // dominant chords
        else if (nameOfChord.contains("Dominant7"))
        {
            notesInChord.add(allNotes.get(0));
            notesInChord.add(allNotes.get(4));
            notesInChord.add(allNotes.get(7));
            notesInChord.add(allNotes.get(10));
        }

        else
            System.out.println("We dont have this chord!");


        List<String> notesInChordToZero = new ArrayList<>();
        notesInChordToZero.addAll(notesInChord);
        int numberOfChordOccurrence = 0;


        // for all notes on fretboard
        for(Node node : childrens )
        {
            // do until we reach the size of this list of notes
            for(int i = 0; i < notesInChord.size(); i++)
            {
                // if name of node equals String which we have on i place in our list and its not Label
                if(!(node instanceof Label) && ((Note) node).getName().equals(notesInChord.get(i)))
                {
                        node.setStyle("-fx-background-color: orange");
                }
                if(notesInChordToZero.size() == 0)
                {
                    numberOfChordOccurrence++;
                    notesInChordToZero.addAll(notesInChord);
                    System.out.println(numberOfChordOccurrence);
                }

            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}

