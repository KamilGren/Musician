package com.musician.musician;

import javafx.application.Application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;



// edit 23.11.2023 BorderPane

public class HelloApplication extends Application {

    public BorderPane root = new BorderPane();
    public GridPane grid = new GridPane();
    public VBox vBoxButtons = new VBox();
    public HBox hBoxCharts = new HBox();
    public VBox vBox = new VBox();
    public ComboBox<Object> cNameOfChord1 = new ComboBox<>();
    public ComboBox<String> cNameOfChord2 = new ComboBox<>();
    public ComboBox<String> cNameOfChord3 = new ComboBox<>();
    public ComboBox<Object> cNameOfScale1 = new ComboBox<>();
    public ComboBox<String> cNameOfScale2 = new ComboBox<>();
    public ComboBox<String> clevelOfOctavia = new ComboBox<>();
    public ComboBox<String> cPositionOfChord = new ComboBox<>();
    public static TextArea scaleHarmonicInformations = new TextArea();
    public HashMap<Integer, GuitarString> fretBoard = new HashMap<>();
    public HashMap<Integer, List<Note>> cagedPositionOfChord = new HashMap<>();
    public ObservableList<String> positionOfChordToDisplay = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage)
    {

        root.setCenter(grid);
        root.setBottom(vBox);

        vBoxButtons.setPadding(new Insets(15, 12, 15, 12));

        vBox.getChildren().addAll(vBoxButtons, hBoxCharts);

        fillGuitarNeck();

        // first refresh here only for add another background for some nodes - new css class doesnt work 8-)
        clearFretboard();
        buildMenuOptions();

        for(int i = 0; i <= 12; i++)
        {
            ColumnConstraints column = new ColumnConstraints();
            RowConstraints row = new RowConstraints();

            grid.getColumnConstraints().add(column);
            grid.getRowConstraints().add(row);

            column.setPrefWidth(90);
            row.setPrefHeight(90);

        }

        Scene scene = new Scene(root, 1695, 850);

        try {
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        stage.setTitle("Musician");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    public void buildMenuOptions()
    {
        // all what we need to know what type of chord client is looking for:

        HBox hboxUp = new HBox();
        HBox hboxDown = new HBox();

        String  typeOfChords[] = {"Major", "Minor", "Dominant", "Half-diminished", "Diminished"};
        String  typeOfChords2[] = {"(3 notes)", "7 (4 notes)"};
        String typeOfScales[] = {"1 - aolian scale", "2 - locrian scale", "3 - ionian / dur scale", "4 - dorian scale", "5 - phrygian scale", "6 - lydian scale", "7 - mixolydian scale", "8 - harmonic moll scale", "9 - phrygian scale"};

        String octaviaLevels[] = {"4", "3", "2", "1"};

        Label lChooseOctaviaLevel = new Label("Choose octava level: ");
        clevelOfOctavia = new ComboBox<String>(FXCollections.observableArrayList(octaviaLevels));

        Image image = new Image(getClass().getResourceAsStream("/info_icon.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        Button bInfo = new Button("Przycisk", imageView);
        bInfo.setPrefSize(20, 20);

        bInfo.setOnAction(e -> {
            Image imageOfInformations = new Image(getClass().getResourceAsStream("/informations.png"));
            ImageView imageViewOfInformations = new ImageView(imageOfInformations);
            imageViewOfInformations.setFitWidth(1024);
            imageViewOfInformations.setFitHeight(768);

            Stage stage = new Stage();
            Scene scene = new Scene(new Group(imageViewOfInformations));
            stage.setScene(scene);
            stage.show();

        });

        Label lChooseChord = new Label ("Choose your chord: ");
        cNameOfChord1 = new ComboBox<Object>(FXCollections.observableArrayList(GuitarString.getListOfStringNotes()));
        cNameOfChord2 = new ComboBox<>(FXCollections.observableArrayList(typeOfChords));
        cNameOfChord3 = new ComboBox<>(FXCollections.observableArrayList(typeOfChords2));

        // we are choose default values for ComboBoxes
        cNameOfChord1.getSelectionModel().selectFirst();
        cNameOfChord2.getSelectionModel().selectFirst();
        cNameOfChord3.getSelectionModel().selectFirst();

        // creating chords by chord name choosed by user

        cNameOfChord1.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            clearFretboard();
            String chordName = createChordName();

            if(positionOfChordToDisplay != null) {
                positionOfChordToDisplay.clear();
            }

            // in back we receive 5 position of this chord, if its major chord (without 7)
            cagedPositionOfChord = createChordsInCAGEDSystem(chordName);

            // we add it to observableArrayList
            // and to our ComboBox

            // adding chord positions from caged method to our observable list
            // using keyset to add all of keys like notes from 1 position, notes from 2 positon...
            // observable list start from 0, not from 1 so we use this loop to edit it
            for(Integer key : cagedPositionOfChord.keySet()) {
                positionOfChordToDisplay.add(key.toString());

            }

            positionOfChordToDisplay.add("All another variation of chord");

            System.out.println("Position of chord to display: " + positionOfChordToDisplay);

            // and now add this list to our combox which display it on the screen
            cPositionOfChord.setItems(positionOfChordToDisplay);
            cPositionOfChord.getSelectionModel().selectFirst();


        });
        cNameOfChord2.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            clearFretboard();
            createChord(createChordName());
        });
        cNameOfChord3.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            clearFretboard();
            createChord(createChordName());
        });

        // PROBLEM: we have the same notes, not with the correct row and col... notes od notes...
        // create action when click on combox with observable list
            cPositionOfChord.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    // selected number index
                    // this selected number is -1 then positioOfChordToDisplay
                    int selectedIndex = cPositionOfChord.getSelectionModel().getSelectedIndex();

                    clearFretboard();
                    if(selectedIndex >= 0 && selectedIndex != 6) {

                        // find way to display 1-5 position the same for all of chords
                        // position of chord to display - lista z ktorej gracz wybiera
                        // cagedpositionofchord

                        if(selectedIndex == 5) {
                            // 6 selected value on cPositionOfChord
                            // its another variations of chord
                            String chordName = createChordName();
                            createChord(chordName);
                        }
                        else {
                            List<Note> selectedPositionOfNote = cagedPositionOfChord.get(selectedIndex + 1);
                            System.out.println("selected" + selectedIndex + " " + selectedPositionOfNote);
                            // choose one of this numbers from combox to show chord on fretboard
                            displayNotesOnFretboard(selectedPositionOfNote);
                        }
                    }
                }
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
        CheckBox chShowCircleOfFifth = new CheckBox();
        Label lShowCircleOfFfith = new Label("Show Circle of Fifth?");

        AtomicBoolean flag1 = new AtomicBoolean(false);
        AtomicBoolean flag2 = new AtomicBoolean(false);
        ObservableList<String> guitarKeyToShow = FXCollections.observableArrayList(); // standard list must be final to use in lambda

        cNameOfScale1.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            clearFretboard();
            flag1.set(true);

            if(!newValue.toString().isEmpty() && flag2.get()) {

                String scaleNoteName = cNameOfScale1.getValue().toString();
                // we need type of scale in numbers choosen by user to use it in pattern to create this scale
                // its like 1 - natural moll, 2 - locrian...
                int typeOfScale = Character.getNumericValue(cNameOfScale2.getValue().charAt(0));
                guitarKeyToShow.addAll(createGuitarKey(scaleNoteName, typeOfScale));
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
                guitarKeyToShow.addAll(createGuitarKey(scaleNoteName, typeOfScale));
                showGuitarKey(guitarKeyToShow);
                changeTextInTextArea(guitarKeyToShow);

                // if name of scale, scale type and checkbox show circle of fifth is selected
            }
        });


        // first i need to assign circle first notes to type of scale because each scale has another first note starts from A.. ABCDEFG
        // so its easy to do, i can use also f.e. ascii + number of scale, at this moment i know only 7 scales
        HashMap<Integer, String> firstNotesFromCirclesOfFifth = new HashMap<>();
        firstNotesFromCirclesOfFifth.put(1, "A");
        firstNotesFromCirclesOfFifth.put(2, "B");
        firstNotesFromCirclesOfFifth.put(3, "C");
        firstNotesFromCirclesOfFifth.put(4, "D");
        firstNotesFromCirclesOfFifth.put(5, "E");
        firstNotesFromCirclesOfFifth.put(6, "F");
        firstNotesFromCirclesOfFifth.put(7, "G");

        // showing or closing chart, need to create real circle of fifth of selected scale 01.12.2023
        // need to use type of scale (3- dur 1 - moll etc)
        chShowCircleOfFifth.selectedProperty().addListener((observable, oldValue, newValue) -> {
            hBoxCharts.getChildren().clear();
            if (newValue && flag1.get() && flag2.get()) {
                int typeOfScale = Character.getNumericValue(cNameOfScale2.getValue().charAt(0));

                // create circle of fifth chart with selected scale
                createChart(createNotesForCircleOfFifth(firstNotesFromCirclesOfFifth.get(typeOfScale)), typeOfScale);
            }
        });

        scaleHarmonicInformations.setMinSize(100, 100);
        
        /// tutaj
        Label lChooseNumberOfChord = new Label("Choose position: ");

        hboxUp.getChildren().addAll(lChooseChord, cNameOfChord1, cNameOfChord2, cNameOfChord3, lChooseNumberOfChord, cPositionOfChord, bInfo);
        hboxDown.getChildren().addAll(lChooseScale, cNameOfScale1, cNameOfScale2, lShowCircleOfFfith, chShowCircleOfFifth, scaleHarmonicInformations, lChooseOctaviaLevel, clevelOfOctavia);
        vBoxButtons.getChildren().addAll(hboxUp, hboxDown);
    }

    public List<String> createNotesForCircleOfFifth(String firstNote) {

        // all guitar notes starts from firstNote
        List <String> stringNotes = GuitarString.getAllNotesFromChoosenNote(firstNote);
        List<String> circleOfFifthNotes = new ArrayList<>();
        String nextCircleOfFifthLetter;

        // repeat to have 12 notes in circle list
        while (circleOfFifthNotes.size() < 12) {

            // get next note on circle of fifth
            nextCircleOfFifthLetter = stringNotes.get(7);
            circleOfFifthNotes.add(nextCircleOfFifthLetter);
            // this makes another sort of note from this next note
            stringNotes = GuitarString.getAllNotesFromChoosenNote(nextCircleOfFifthLetter);
        }

        return circleOfFifthNotes;
    }
    public void createChart(List<String> guitarKeyNotes, int typeOfScale) {

        ObservableList<PieChart.Data> pieChartData = createChartData(guitarKeyNotes);

        PieChart chart = new PieChart(pieChartData);

        // dopisac jakiej skali jest to koło dla każdego wyboru
        chart.setTitle("Koło Kwintowe");
        // position of first is C and its on top
        chart.setStartAngle(90);
        chart.setPrefSize(300, 300);
        chart.setLabelLineLength(10);
        chart.setLabelsVisible(true);
        chart.setStyle("-fx-background-color: transparent;");


        for(int i=0; i < chart.getData().size(); i++) {

            String guitarKeyFirstNote = chart.getData().get(i).getName();
            List<String> guitarKeyfromChart = createGuitarKey(guitarKeyFirstNote, typeOfScale);

            chart.getData().get(i).getNode().setOnMouseClicked(event -> showGuitarKey(guitarKeyfromChart));
        }


        hBoxCharts.getChildren().add(chart);
    }

    // adding each note from scale to ObservableList
    private ObservableList<PieChart.Data> createChartData(List<String> guitarKeyNotes) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (String note : guitarKeyNotes) {
            pieChartData.add(new PieChart.Data(note, 1));
        }

        return pieChartData;
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
        //guitarKey.put(10, "2122221"); // melodic moll scale

       // guitarKey.put(10, "") // pentatonic moll

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
        ObservableList<Node> notesOnBoard = grid.getChildren(); // all notes from board

        // first clean fretboard from others scales, chords etc
        clearFretboard();

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
        fretBoard = createFretBoard();

        for(int i = 0; i <= 20; i++)
        {
            grid.add(fretBoard.get(1).getListOfNotes().get(i), i, 6);
            grid.add(fretBoard.get(2).getListOfNotes().get(i), i, 5);
            grid.add(fretBoard.get(3).getListOfNotes().get(i), i, 4);
            grid.add(fretBoard.get(4).getListOfNotes().get(i), i, 3);
            grid.add(fretBoard.get(5).getListOfNotes().get(i), i, 2);
            grid.add(fretBoard.get(6).getListOfNotes().get(i), i, 1);

            Label lNumberOfBar = new Label(String.valueOf(i));
            grid.add(lNumberOfBar, i, 0);
            lNumberOfBar.setStyle("-fx-font-weight: bold;");
            lNumberOfBar.setTextAlignment(TextAlignment.CENTER);


        }
        //lNumberOfBar.getStylesheets().add("guitarBar");
       // System.out.println("Poziom oktawy dla A" + guitarStringB3.getListOfNotes().get(9).getLevelOfOctavia() + "nazwa nuty: " + guitarStringB3.getListOfNotes().get(9).getName());

    }

    // we can also find which level of octavia (on guitar we have the same names of notes but we other sounds - their difference is level of octavia)
    public void findNotesInOneOctavia(int levelOfOctavia, String backgroundColor) {

        ObservableList<Node> childrens = grid.getChildren();

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
        GuitarString guitarStringA = new GuitarString("A2");
        GuitarString guitarStringD = new GuitarString("D3");

        mapOfString.put(1, guitarStringE2);
        mapOfString.put(2, guitarStringA);
        mapOfString.put(3, guitarStringD);
        mapOfString.put(4, guitarStringG);
        mapOfString.put(5, guitarStringB);
        mapOfString.put(6, guitarStringE4);

        return mapOfString;
    }


    public void clearFretboard()
    {
        ObservableList<Node> childrens = grid.getChildren();
         String harmonicButtonStyleClass = "harmonic-button";

        for(Node node : childrens )
        {
            if(!(node instanceof Label))
            node.setStyle("-fx-background-color: #D2B48C");
                if(GridPane.getColumnIndex(node) != null && (GridPane.getColumnIndex(node) == 3 || GridPane.getColumnIndex(node) == 5 || GridPane.getColumnIndex(node) == 7 || GridPane.getColumnIndex(node) == 9 || GridPane.getColumnIndex(node) == 12) && !(node instanceof Label))
                {
                    node.setStyle("-fx-background-color: #8B4513");
                }
        }
    }

    public HashMap<Integer, List<Note>> createChordsInCAGEDSystem(String chordName) {

        // 1 - dur, 2 - moll, 3 - dur7, 4 - moll7, 5 - x7
        //int chordType = 0;
        HashMap <Integer, List<Note>> listOfChords = new HashMap<>();

        // type of chord by match if it is major - names are based on note - type (major, minor etc.) and (3 note) or (4 note) formulation
        if (chordName.matches(".*[mM]ajor\\(3 notes\\)")) {

            // create 5 chords in CAGED system, strings from E low
            List<Note> chordC = createChordUsingCAGEDPattern(new String[] {"-", "2", "1", "-1", chordName, "-1"});
            List<Note> chordA = createChordUsingCAGEDPattern(new String[] {"-", "-2", "0", chordName, "0", "-2"});
            List<Note> chordG = createChordUsingCAGEDPattern(new String[] {chordName, "-1", "-3", "-3", "-3", "0"});
            List<Note> chordE = createChordUsingCAGEDPattern(new String[] {"0", "2", "2", "1", "0", chordName});
            List<Note> chordD = createChordUsingCAGEDPattern(new String[] {"-", "-", chordName, "2", "3", "2"});

            listOfChords.put(1, chordC);
            listOfChords.put(2, chordA);
            listOfChords.put(3, chordG);
            listOfChords.put(4, chordE);
            listOfChords.put(5, chordD);

        }

        return listOfChords;

    }
    // C nie działa - czyli 1 w kazdym z tych wybranych opcji
    public List<Note> createChordUsingCAGEDPattern(String [] arrayOfPatternOperations)
    {
        List<Note> newChord = new ArrayList<>();
        List <String> allNotes;
        List <String> chordNotes = new ArrayList<>();
        int tonicIndex = 0;
        String tonicNote;

        // first find tonicIndex
        for(int i = 0; i < 6; i++)
        {
                if(arrayOfPatternOperations[i].matches("[a-zA-Z].*")) {
                    tonicNote = String.valueOf(arrayOfPatternOperations[i].charAt(0));

                    if (arrayOfPatternOperations[i].matches("[a-zA-Z]#.*")) {
                        tonicNote = arrayOfPatternOperations[i].substring(0, 2);
                        System.out.println("######");
                        System.out.println(tonicNote);
                    }

                    // all notes from our tonic notes, need for create chord notes
                    allNotes = createListOfNoteFromFirstNote(tonicNote);

                    // and now add this chord major notes
                    chordNotes.add(allNotes.get(0));
                    chordNotes.add(allNotes.get(4));
                    chordNotes.add(allNotes.get(7));

                    // look for tonicNote number but only in position from 0 to 13 to not reach over the fretboard (21 position)
                    // fretboard has map with guitarstring starts from 1 not 0
                    List<String> stringNotes = fretBoard.get(i+1).getListOfThisStringNotes().subList(0, 13);
                    tonicIndex = stringNotes.indexOf(tonicNote);
                    System.out.println("Tonic index: " + tonicIndex);

                }

        }

        int patternPosition;
        // minimum position of tonic to create chord
        int minimumPosition = 0;
        int number = 0;
        Note note;

        for(int i = 0; i < 6; i++)
        {

            // In G and D its chord create from tonic and some notes are -3 from tonic note, we need to set minimum note position to display
            // working chords!
            // In C,A,E its -2

            for(int j= 0; j < 6; j++) {

                // find which pattern is it by finding its type
                if(arrayOfPatternOperations[j].matches("[a-zA-Z].*"))
                {
                    patternPosition = j;
                }
                else
                    patternPosition = -1;


                // set the limit for creating chords
                // G or D
                if(patternPosition == 0 || patternPosition == 2) {
                    System.out.println("G or D");
                    minimumPosition = 3;
                }
                // C A E
                else if (patternPosition != -1) {
                    System.out.println("C, A, E");
                    minimumPosition = 2;
                }


            // if it is matches letter it must be place of tonic, so use it
            if(arrayOfPatternOperations[i].matches("[a-zA-Z].*")) {
                newChord.add(fretBoard.get(i+1).getListOfNotes().get(tonicIndex));
            }
            // if its 0 on some string, we don't use this string in our method, its blank for us
            // here it is add or subtract to find searching note on fretboard
            // we want only numbers from -3 to 3 to add or subtract to our tonic note index
            if(arrayOfPatternOperations[i].matches("-?[0-3]") && tonicIndex >= minimumPosition) {

                number = Integer.parseInt(arrayOfPatternOperations[i]);
                note = fretBoard.get(i + 1).getListOfNotes().get(tonicIndex + number);

                    if (note.getName().equals(chordNotes.get(0)) || note.getName().equals(chordNotes.get(1)) || note.getName().equals(chordNotes.get(2))) {
                        newChord.add(fretBoard.get(i + 1).getListOfNotes().get(tonicIndex + number));
                    }
                }
                // if its some note which is not tonic and minimum position is not fullfiled
                else if (arrayOfPatternOperations[i].matches("-?[0-3]") && tonicIndex < minimumPosition) {
                    note = fretBoard.get(i + 1).getListOfNotes().get(tonicIndex + number);

                if (note.getName().equals(chordNotes.get(0)) || note.getName().equals(chordNotes.get(1)) || note.getName().equals(chordNotes.get(2))) {
                        // we need this to create some chords
                        newChord.add(fretBoard.get(i + 1).getListOfNotes().get(tonicIndex + number + 12));
                    }
                }
            }
        }
        return newChord;
    }

//    public void createChordsInCAGEDSystem() {
//
//        System.out.println(" Tworzymy na to nowa formule.");
//
//        // FIRST NOTES
//        // POSITION OF TONIC
//
//        System.out.println("C A G E D");
//        System.out.println("Dla C to jest... ");
//
//        List<Note> chordC = createChordUsingCAGEDPattern(new String[] {"-", "2", "1", "-1", chordName, "-1"});
//        List<Note> chordA = createChordUsingCAGEDPattern(new String[] {"-", "-2", "0", chordName, "0", "-2"});
//        List<Note> chordG = createChordUsingCAGEDPattern(new String[] {chordName, "-1", "-3", "-3", "-3", "0"});
//        List<Note> chordE = createChordUsingCAGEDPattern(new String[] {"0", "2", "2", "1", "0", chordName});
//        List<Note> chordD = createChordUsingCAGEDPattern(new String[] {"-", "-", chordName, "2", "3", "2"});
//
//
//    }

    public void displayNotesOnFretboard(List<Note> chordName) {
            // do until we reach the size of this list of notes

        if (chordName.size() > 0) {
            for(int i = 0; i < chordName.size(); i++)
            {
                chordName.get(i).changeColorToChordNote();
            }
        }

    }


    // creating chords on fretboard
    public void createChord(String nameOfChord) {

        // name of chord can be X Maj - dur its 7
        // X m - moll


        ObservableList<Node> childrens = grid.getChildren();

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
            for(int i = 0; i < notesInChordToZero.size(); i++)
            {
                // if name of node equals String which we have on i place in our list and its not Label
                if(!(node instanceof Label) && ((Note) node).getName().equals(notesInChordToZero.get(i)))
                {
                    ((Note) node).changeColorToChordNote();
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

    // next level - audio //

    public void createAudio() {

        double[] stringFrequencies = {82.41, 110.0, 146.83, 196.0, 246.94, 329.63}; // frequencies of each string


    }


    public static void main(String[] args) {
        launch();
    }
}

