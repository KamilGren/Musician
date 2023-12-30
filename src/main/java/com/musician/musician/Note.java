package com.musician.musician;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;

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
            note.setStyle("-fx-background-color: orange");
            System.out.println("Note name: " + note.getText() + " and level of octavia: " + note.getLevelOfOctavia());
        });
    }

    public void changeColorToChordNote() {
        this.setStyle("-fx-background-color: orange");
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

    public int getRowPosition() {
        return rowPosition;
    }

    public int getColPosition() {
        return colPosition;
    }

    public String getStringName() {

        String stringName = "";

        switch(getColPosition()) {

            case 0:
                stringName = "El";
                break;

            case 1:
                stringName = "A";
                break;

            case 2:
                stringName = "D";
                break;

            case 3:
                stringName = "G";
                break;

            case 4:
                stringName = "B";
                break;

            case 5:
                stringName = "Eh";
                break;

            default:
                System.out.println("Brak takiej struny dla tej nuty!");
        }
        
        return stringName;
    }

}
