package com.musician.musician;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Note extends Button {

    private String name;
    private int levelOfOctavia;
    private int colPosition;
    private int rowPosition;

    public Note(String name, int levelOfOctavia, int colPosition, int rowPosition) {
        this.name = name;
        this.levelOfOctavia = levelOfOctavia;
        this.colPosition = colPosition;
        this.rowPosition = rowPosition;
        setText(name);
        makingNoteClickable();

        // it shows something ugly...
        setTooltip(
                new Tooltip(this.getName() + " " + this.getLevelOfOctavia())
                );

    }

    public void makingNoteClickable()
    {
        this.setOnAction(event -> {
            Object node = event.getSource(); //returns the object that generated the event
            Note note = (Note) node;
            note.setStyle("-fx-background-color: white");
            System.out.println("Note name: " + note.getText() + " and level of octavia: " + note.getLevelOfOctavia());
        });
    }

    // make a method which shows all notes from one octava in other colour when the button is clicked

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevelOfOctavia() {
        return levelOfOctavia;
    }

    public void setLevelOfOctavia(int levelOfOctavia) {
        this.levelOfOctavia = levelOfOctavia;
    }

    public Map<Integer, Integer> getPositionOfNote()
    {
        Map <Integer, Integer> positionOfNotes = new HashMap<>();
        positionOfNotes.put(this.rowPosition, this.colPosition);

        return positionOfNotes;
    }


}
