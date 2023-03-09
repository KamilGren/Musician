//package com.musician.musician;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Octavia {
//
//    private int levelOfOctavia;
//    private List<Note> notesInOctavia = new ArrayList<>();
//    private final String [] allNotes  = {"E", "F", "F#", "G", "G#", "A", "A#", "B", "C", "C#", "D", "D#"};
//
//    public Octavia(int levelOfOctavia) {
//
//        this.levelOfOctavia = levelOfOctavia;
//        notesInOctavia = createOctavia(levelOfOctavia);
//    }
//
//    public int getLevelOfOctavia() {
//        return levelOfOctavia;
//    }
//
//    public void setLevelOfOctavia(int levelOfOctavia) {
//        this.levelOfOctavia = levelOfOctavia;
//    }
//
//    public List<Note> createOctavia(int levelOfOctavia) {
//
//        List<Note> notesInOctavia = new ArrayList<>();
//
//        for(int i = 0; i < 12; i++)
//        {
//            Note note = new Note(allNotes[i], levelOfOctavia);
//            notesInOctavia.add(note);
//        }
//        return notesInOctavia;
//    }
//
//    public List<Note> getNotesInOctavia() {
//        return notesInOctavia;
//    }
//
//    // musze byc w stanie pobrac poziom oktawy i literke
//    // czyli robimy cos jak mape,
//}
