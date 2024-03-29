package com.musician.musician;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.AudioSystem;

public class GuitarString {

    private String name;
    private final static String [] allNotes  = {"E", "F", "F#", "G", "G#", "A", "A#", "B", "C", "C#", "D", "D#"};
    private static List<String> listOfStringNotes = Arrays.stream(allNotes).toList();

    private List<String> listOfThisStringNotes;
    private List<Note> listOfNotes;

    public GuitarString(String name) {
        this.name = name;
        this.listOfNotes = createStringUpdate(name);
        this.listOfThisStringNotes = changesNotesToString(listOfNotes);
    }

    public List<String> changesNotesToString(List<Note> listOfNotes) {
        List<String> listOfStringNotes = new ArrayList<>();

        for(Note note : listOfNotes) {
            listOfStringNotes.add(note.getName());
        }
        return listOfStringNotes;
    }

    public List<String> getListOfThisStringNotes() {
        return listOfThisStringNotes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static List<String> getListOfStringNotes() {
        return listOfStringNotes;
    }

    public static List<String> getAllNotesFromChoosenNote(String note) {

        List<String> listOfNotesFromChoosenNote = new ArrayList<String>();
        int index = listOfStringNotes.indexOf(note);

        listOfNotesFromChoosenNote.addAll(listOfStringNotes.subList(index, 12));
        listOfNotesFromChoosenNote.addAll(listOfStringNotes.subList(0, index));

        return listOfNotesFromChoosenNote;
    }

    public static void setListOfStringNotes(List<String> listOfStringNotes) {
        GuitarString.listOfStringNotes = listOfStringNotes;
    }

    public List<Note> getListOfNotes() {
        return listOfNotes;
    }

    public void setListOfNotes(List<Note> listOfNotes) {
        this.listOfNotes = listOfNotes;
    }

    public void convertStringtoNote(List<String> listOfNotes) {

        for(String noteName : listOfNotes) {

        }

    }

    // create new guitar string
    //this is new method of creating guitar string, im working on it
    public static List<String> createGuitarString(String nameOfString)
    {

        // all 12 notes in string from E to D#
        List<String> notes = getListOfStringNotes();

        // new list for all guitar notes from one guitar string
        List<String> allGuitarStringNotes = new ArrayList<>();

        // we have 20 notes on one string
        int numberOfNotesInFretboard = 20;

        // and 12 sounds - notes in one octava like from E4 to E3
        int numberOfNotesInOneOctavia = 12;

        // nameOfString - which is only note and number like E4 :)
        String index = Character.toString(nameOfString.charAt(0)); // first character of nameOfString - its note like E,A,D,G...
        int octava = Character.getNumericValue(nameOfString.charAt(1)); // level of octava

        // index of start note, im using my list where E is first so for A its 5 - EFF#GG#A
        int startNoteIndex = notes.indexOf(index);

        allGuitarStringNotes.addAll(notes.subList(startNoteIndex,numberOfNotesInOneOctavia));

        int notesToFullFretboard = numberOfNotesInFretboard - startNoteIndex;

            if(notesToFullFretboard < 12) {
                allGuitarStringNotes.addAll(notes);
            }
            else {
                notesToFullFretboard = notesToFullFretboard - 12;
                allGuitarStringNotes.addAll(notes);
                allGuitarStringNotes.addAll(notes.subList(0, notesToFullFretboard));
            }

        System.out.println(allGuitarStringNotes);

            return allGuitarStringNotes;
    }

    public static List <Note> createStringUpdate(String nameOfString) 
    {

        int sizeOfOctavia = 12; // size of all notes in one octava
        List<Note> listOfStringNotes = new ArrayList<>(); // list of notes in string which we are making
        String[] allNotes = getAllNotes(); // table of strings with all notes from guitar
        List<String> listOfNotes = Arrays.stream(allNotes).toList(); // passing this notes to list
        int indexOfStartNote;
        String index = Character.toString(nameOfString.charAt(0)); // first character of nameOfString - its note like E,A,D,G...
        int octava = Character.getNumericValue(nameOfString.charAt(1)); // like 4 3 or 2
        indexOfStartNote = listOfNotes.indexOf(index);
        String startNote = listOfNotes.get(indexOfStartNote); // finding this note in list and create String with it
        int numberOfNotesInOneOctava = sizeOfOctavia - listOfNotes.indexOf(index); // number of notes start from first column to last note (its D#=11)
        // so in A string it will be 12-5 = 7, 7 notes in this octava = A,A#,B,C,C#,D,D#

        String [] arrayOfGuitarString = {"E", "A", "D", "G", "B", "E"}; // string with his number of presence on fretboard like E = 0, A = 1...
        List <String> arrayofGuitarStrings = Arrays.stream(arrayOfGuitarString).toList();
        // now we need to iterate until we find last note in list
        // after that we continue from E to end of list
        // and we are reapiting this with E to having full guitarString which has 21 notes on it in 3 oraz 4 octavas
        // one loop creates notes in one octava, another loop in another octava

        // do until size of one string is 21


        // ucinanie stringa i wklejanie powtarzalnego stringa dopoki nie zrobi full gryfu a potem jedynie trzeba dodac oktawy
        // czyli najpierw zrob stringa wszystkich nut, a potem stworz z tego nuty
        // chodzi o to ze oktawy sie roznia... wiec przypal, w roznych miejscach rozne oktawy od roznych miejsc sie zaczynaja i koncza
        // ale koncza zawsze na E... to prawda akurat
        // hm.. do zeszytu!

        switch (startNote) {
            case "G", "E" -> {
                Note note;
                for (int i = 0; i < 20; i++) {
                    // so create number of notes with specific octava which is another for each string in guitar
                    if (listOfStringNotes.size() < numberOfNotesInOneOctava)
                    {
                        if(octava == 4) {  // only when we deal with E which is on 0 string and 5 string (1 and 6 string on guitar)
                            note = new Note(listOfNotes.get(indexOfStartNote), octava, i, 0);
                        }
                        else {
                            note = new Note(listOfNotes.get(indexOfStartNote), octava, i, 5);
                        }
                        listOfStringNotes.add(note);
                        indexOfStartNote++; // index of start note from first note on string starts to increase (A=5.. A#=6.. B)
                    }
                    if (listOfStringNotes.size() >= numberOfNotesInOneOctava && listOfStringNotes.size() < 22) // only in G
                    {
                        if (indexOfStartNote >= 12)
                        {
                            indexOfStartNote = 0;

                            if(octava == 4)
                            {
                                note = new Note(listOfNotes.get(indexOfStartNote), octava-1, i, 0);
                            }
                            else
                            {
                                note = new Note(listOfNotes.get(indexOfStartNote), octava-1, i, 5);
                            }
                            listOfStringNotes.add(note);
                            indexOfStartNote++;
                        }
                        else
                        {
                            if(octava == 4)
                            {
                                note = new Note(listOfNotes.get(indexOfStartNote), octava-1, i, 0);
                            }
                            else
                            {
                                note = new Note(listOfNotes.get(indexOfStartNote), octava-1, i, 5);
                            }
                            listOfStringNotes.add(note);
                            indexOfStartNote++;
                        }
                    }
                }
            }
            case  "A", "D", "B" -> {

                Note note;
                for (int i = 0; i < 19; i++)
                {
                    //  create number of notes with specific octava which can be another for each string in guitar
                    if (listOfStringNotes.size() <= numberOfNotesInOneOctava)
                    {
                        if (indexOfStartNote >= 12) {   // index of start note from first note on string starts to increase (A=5.. A#=6.. B)
                            indexOfStartNote = 0;
                            note = new Note(listOfNotes.get(indexOfStartNote), octava, i, arrayofGuitarStrings.indexOf(startNote));
                            listOfStringNotes.add(note);
                            indexOfStartNote++;
                        }
                        else {
                            note = new Note(listOfNotes.get(indexOfStartNote), octava, i, arrayofGuitarStrings.indexOf(startNote));
                            listOfStringNotes.add(note);
                            indexOfStartNote++;
                        }
                    }
                    if (listOfStringNotes.size() >= numberOfNotesInOneOctava && listOfStringNotes.size() <= (numberOfNotesInOneOctava + sizeOfOctavia - 1) && listOfStringNotes.size() < 22) {
                        if (indexOfStartNote >= 12) {
                            indexOfStartNote = 0;
                            note = new Note(listOfNotes.get(indexOfStartNote), octava - 1, i, arrayofGuitarStrings.indexOf(startNote));
                            listOfStringNotes.add(note);
                            indexOfStartNote++;
                        }
                        else {
                            note = new Note(listOfNotes.get(indexOfStartNote), octava - 1, i, arrayofGuitarStrings.indexOf(startNote));
                            listOfStringNotes.add(note);
                            indexOfStartNote++;
                        }
                    }
                    if (listOfStringNotes.size() >= numberOfNotesInOneOctava + 12 && listOfStringNotes.size() >= (numberOfNotesInOneOctava + sizeOfOctavia - 1) && listOfStringNotes.size() < 22) {

                        if (indexOfStartNote >= 12) {
                            indexOfStartNote = 0;
                            note = new Note(listOfNotes.get(indexOfStartNote), octava - 2, i, arrayofGuitarStrings.indexOf(startNote));
                            listOfStringNotes.add(note);
                            indexOfStartNote++;
                        } else {
                            note = new Note(listOfNotes.get(indexOfStartNote), octava - 2, i, arrayofGuitarStrings.indexOf(startNote));
                            listOfStringNotes.add(note);
                            indexOfStartNote++;

                        }
                    }
                }
            }
        }
            return listOfStringNotes;
    }

    public void showNotesFromOneOctavia(int levelOfOctavia)
    {
        boolean flag = false;

        for(int i = 0; i < 12; i++)
        {
            if(listOfNotes.get(i).getLevelOfOctavia() == levelOfOctavia && !flag)
            {
                listOfNotes.get(i).setStyle("-fx-background-color: orange");
                flag = true;
            }
            else if (flag) {
                listOfNotes.get(i).setStyle("-fx-background-color: orange");
            }
        }
    }

    public static String[] getAllNotes() {
        return allNotes;
    }
}
