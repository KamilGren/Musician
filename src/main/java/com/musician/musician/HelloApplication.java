package com.musician.musician;

import javafx.application.Application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
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

    List <Note> notesFromChord = new ArrayList<>();

    @Override
    public void start(Stage stage) throws IOException
    {

        vBox.getChildren().addAll(root, hBox);
        hBox.setPadding(new Insets(15, 12, 15, 12));
        hBox.setSpacing(10);

        createFretBoard();
        buildMenuOptions();
        fillGuitarNeck();


        HashMap<Integer, List<String>> chords = showHarmonicChords(createGuitarKey("A", 1));

//        for(Map.Entry<Integer, List<String>> set : chords.entrySet()) {
//            System.out.println(set.getValue() + " = " + set.getKey());
//            System.out.println();
//        }
//


        for(int i = 0; i <= 12; i++)
        {
            ColumnConstraints column = new ColumnConstraints();
            RowConstraints row = new RowConstraints();

            root.getColumnConstraints().add(column);
            root.getRowConstraints().add(row);

            column.setPrefWidth(100);
            row.setPrefHeight(100);
        }

        Scene scene = new Scene(vBox, 1695, 670);

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

        // all what we want to know to show typed scale on fretboard

        Label lChooseScale = new Label ("Choose your scale: ");
        cNameOfScale1 = new ComboBox<Object>(FXCollections.observableArrayList(GuitarString.getListOfStringNotes()));
        cNameOfScale2 = new ComboBox<>(FXCollections.observableArrayList(typeOfScales));
        AtomicBoolean flag1 = new AtomicBoolean(false);
        AtomicBoolean flag2 = new AtomicBoolean(false);

        cNameOfScale1.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            clearFretboard();
            flag1.set(true);

            if(!newValue.toString().isEmpty() && flag2.get()) {
                showGuitarKey(createGuitarKey((String)cNameOfScale1.getValue(), Integer.parseInt(String.valueOf(cNameOfScale2.getValue().charAt(0)))));
                changeTextInTextArea((String)cNameOfScale1.getValue() + String.valueOf(cNameOfScale2.getValue().charAt(0)));
            }
        });
        cNameOfScale2.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            clearFretboard();
            flag2.set(true);
            if(flag1.get() && flag2.get()) {

                // we are creating here and sending name of scale to textarea, i want to create and send this created scale to textarea method
                showGuitarKey(createGuitarKey((String)cNameOfScale1.getValue(), Integer.parseInt(String.valueOf(cNameOfScale2.getValue().charAt(0)))));
                changeTextInTextArea((String)cNameOfScale1.getValue() + String.valueOf(cNameOfScale2.getValue().charAt(0)));
            }
        });

        TextArea scaleHarmonicInformations = new TextArea();
        scaleHarmonicInformations.setMinSize(100, 100);

        hBox.getChildren().addAll(lChooseChord, cNameOfChord1, cNameOfChord2, cNameOfChord3, lChooseScale, cNameOfScale1, cNameOfScale2, scaleHarmonicInformations);
    }

    // we are here, stop 13:06
    public static void changeTextInTextArea(String scale) {

        List <String> textToShow = new ArrayList<>();

    }

    // we are working on ready scale and we create most of chords from pattern (222)
    public static HashMap<Integer, List<String>> showHarmonicChords(List<String> someGuitarKey)
    {
        HashMap<Integer, List<String>> chords = new HashMap<>();

        // distance between notes to create chord
        int maj[]  = {4,3,4};
        int min[] = {3,4,3};
        int sept[] = {4,3,3};
        int halfd[] = {3,3,4};

        int index = 0;


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
            chords.put(j, chord);
        }

        List<String> chordNames = new ArrayList<>();
        for(Map.Entry<Integer, List<String>> set : chords.entrySet()) {

            List<String> notes = GuitarString.getAllNotesFromChoosenNote(set.getValue().get(0));
            System.out.println(set);

            if (set.getValue().get(1) == notes.get(3) && set.getValue().get(2) == notes.get(7)) {
                System.out.println("minor");
                chordNames.add(notes.get(0) + "minor7");
            }
            if (set.getValue().get(1) == notes.get(4) && set.getValue().get(3) == notes.get(10)) {
                chordNames.add(notes.get(0) + "major7");

            }
            if (set.getValue().get(2) == notes.get(7) && set.getValue().get(3) == notes.get(9)) {
                chordNames.add(notes.get(0) + "7");

            }
            if (set.getValue().get(1) == notes.get(3) && set.getValue().get(2) == notes.get(6)) {
                System.out.println("Halfd");
                chordNames.add(notes.get(0) + "7b5");

            }

        }

        // if z 1 do 2 .. 3


        return chords;
    }




    public static List<String> createListOfNoteFromFirstNote(String startNote) { // sorting all guitar notes in one string from startNote like G, G#, A#...

        int index = GuitarString.getListOfStringNotes().indexOf(startNote);
        List <String> stringNotes = GuitarString.getListOfStringNotes();
        List<String> allNotesFromStartNote = new ArrayList<>(stringNotes.subList(index, stringNotes.size()));
        allNotesFromStartNote.addAll(stringNotes.subList(0, index));

        return allNotesFromStartNote;
    }

    public String createChordName()
    {
        return cNameOfChord1.getValue() + cNameOfChord2.getValue() + cNameOfChord3.getValue();
    }

    public  List<String> createGuitarKey(String startNote, int level)
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

        // altered scale
        // altered start from dominant (7 notes from first note) even its note not from scale!
        // we are taking dominant note - in a natural moll its E
        // we are adding one note to it - now its F
        // and we are going to create f-moll melodic on 7 position
        // f-g-a#-b-c-d-e and add last on first position - e-f-g-a#-b-c-d

        // okey, so give me in DEFGABC#D ok later


        List<String> guitarKeyNotes = new ArrayList<>();
        List<String> listOfNotes = createListOfNoteFromFirstNote(startNote);
        String pattern = guitarKey.get(level);

        for(int i = 0; i <= pattern.length(); i++) // do until pattern is end
        {

            if (index <= 11) { // max number in scale its 11 (start from 0) - its always 12 notes
                guitarKeyNotes.add(listOfNotes.get(index)); // we are receiving note name from following index
                index += Integer.parseInt(String.valueOf(pattern.charAt(i))); // we are adding numbers (1 or 2) from pattern to obtain higher index number in every loop rep
            }
            else {
                guitarKeyNotes.add(startNote);
            }
        }

        return guitarKeyNotes;
    }

    public void showGuitarKey(List<String> guitarKey)
    {
        ObservableList<Node> notesOnBoard = root.getChildren(); // all notes from board

        for (Node node : notesOnBoard) {
            for (String s : guitarKey) {
                if (node instanceof Note && ((Note) node).getName().equals(s)) {

                    if (((Note) node).getName().equals(guitarKey.get(0))) {
                        node.setStyle("-fx-background-color: yellow");
                    } else
                        node.setStyle("-fx-background-color: red");
                }
            }
        }
    }

    public void fillGuitarNeck()
    {
        GuitarString guitarStringG = new GuitarString("G3");
        GuitarString guitarStringB = new GuitarString("B3");
        GuitarString guitarStringE2 = new GuitarString("E2");
        GuitarString guitarStringE4 = new GuitarString("E4");
        GuitarString guitarStringA = new GuitarString("A4");
        GuitarString guitarStringD = new GuitarString("D4");

        for(int i = 0; i <= 20; i++)
        {
            root.add(guitarStringE4.getListOfNotes().get(i), i, 5);
            root.add(guitarStringA.getListOfNotes().get(i), i, 4);
            root.add(guitarStringD.getListOfNotes().get(i), i, 3);
            root.add(guitarStringG.getListOfNotes().get(i), i, 2);
            root.add(guitarStringB.getListOfNotes().get(i), i, 1);
            root.add(guitarStringE2.getListOfNotes().get(i), i, 0);
        }
       // System.out.println("Poziom oktawy dla A" + guitarStringB3.getListOfNotes().get(9).getLevelOfOctavia() + "nazwa nuty: " + guitarStringB3.getListOfNotes().get(9).getName());

    }

    public void findNotesInOneOctavia(int levelOfOctavia, String backgroundColor) {

        ObservableList<Node> childrens = root.getChildren();

        for(Node node : childrens)
        {
            Note note = (Note) node;
            if(note.getLevelOfOctavia() == levelOfOctavia)
            {
                note.setStyle("-fx-background-color: " + backgroundColor);
            }
        }
    }

    // it will work for more than 4 notes bcs it takes more then one actava
    public List<String> addNotes (int a, int b, int c, int d) {

        List<String> notes = new ArrayList<>();
        List <String> allNotes = GuitarString.getListOfStringNotes();

        notes.add(allNotes.get(a));

        if(b >= 12) // if index of second note is higher then array length
        {
            b -= 12;
        }
        notes.add(allNotes.get(b));

        if (c >= 12)
        {
            c -= 12;
        }
        notes.add(allNotes.get(c));

        ///
        if(d >= 12)
        {
            d -= 12;
            notes.add(allNotes.get(d));
        }
        if (d != 0 && d < 12) {
            notes.add(allNotes.get(d));
        }

        return notes;
    }

    public void createFretBoard() {

        GuitarString guitarStringG = new GuitarString("G3");
        GuitarString guitarStringB = new GuitarString("B3");
        GuitarString guitarStringE2 = new GuitarString("E2");
        GuitarString guitarStringE4 = new GuitarString("E4");
        GuitarString guitarStringA = new GuitarString("A4");
        GuitarString guitarStringD = new GuitarString("D4");

    }
    public void clearFretboard()
    {
        ObservableList<Node> childrens = root.getChildren();

        for(Node node : childrens )
        {
            node.setStyle("-fx-background-color: brown");
        }
    }

    // creating chords on fretboard
    public void createChord(String nameOfChord)
    {

        // name of chord can be X Maj - dur  its 7
        // X m - moll

        ObservableList<Node> childrens = root.getChildren();
        List <String> notesInChord = new ArrayList<>();
        List <String> allNotes = GuitarString.getListOfStringNotes();
        String startNote;

        if(nameOfChord.contains("#"))
        {
            startNote = String.valueOf(nameOfChord.subSequence(0,2));
            System.out.println(startNote);
        }
        else
            startNote = String.valueOf(nameOfChord.charAt(0));


        int startNoteIndex = allNotes.indexOf(startNote);
        int chordSize;
        // its easy
        // find first note and 4... 7... in dur
        // find first note and 3 and 7 in moll


        // major chords
        if (nameOfChord.contains("Major") && !nameOfChord.contains("7"))
        {
            notesInChord.addAll(addNotes(startNoteIndex, startNoteIndex+4,startNoteIndex+7, 0));
        }
        else if (nameOfChord.contains("Major7"))
        {
            notesInChord.addAll(addNotes(startNoteIndex, startNoteIndex+4,startNoteIndex+7, startNoteIndex+11));
        }

        // minor chords
        else if ((nameOfChord.contains("Minor")) && !nameOfChord.contains("7"))
        {
            notesInChord.addAll(addNotes(startNoteIndex, startNoteIndex+3, startNoteIndex+7, 0));
        }
        else if (nameOfChord.contains("Minor7"))
        {
            notesInChord.addAll(addNotes(startNoteIndex, startNoteIndex+3, startNoteIndex+7, startNoteIndex+10));
        }

        // diminished chords
        else if (nameOfChord.contains("Diminished"))
        {
            notesInChord.addAll(addNotes(startNoteIndex, startNoteIndex+3, startNoteIndex+6, 0));
        }
        else if (nameOfChord.contains("Diminished7"))
        {
            notesInChord.addAll(addNotes(startNoteIndex, startNoteIndex+3, startNoteIndex+7, startNoteIndex+9));
        }

        // half-dimnished chords
        else if (nameOfChord.contains("Half-diminished7"))
        {
            notesInChord.addAll(addNotes(startNoteIndex, startNoteIndex+3, startNoteIndex+6, startNoteIndex+10));
        }
        // dominant chords
        else if (nameOfChord.contains("Dominant7"))
        {
            notesInChord.addAll(addNotes(startNoteIndex, startNoteIndex+3, startNoteIndex+7, startNoteIndex+10));
        }
        else
            System.out.println("We dont have this chord!");


        if (nameOfChord.contains("7")) {
            chordSize = 4;
        }
        else
            chordSize = 3;

        for(Node node : childrens )
        {
            for(int i = 0; i < chordSize; i++)
            {
                if(((Note) node).getName().equals(notesInChord.get(i))) {
                    node.setStyle("-fx-background-color: pink");
                }
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}

