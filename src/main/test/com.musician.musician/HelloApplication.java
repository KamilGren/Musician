import com.musician.musician.HelloApplication;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloWorldTest
{

    @Test
    public void itsAeolianScale_showsGoodNotes() {

        HelloApplication helloApplication = new HelloApplication();
        List<String> listOfNotes = helloApplication.createGuitarKey("C", 1);
        List <String> listOfCorrectNotes = new ArrayList<String>() {
            {
                add("C");
                add("D");
                add("D#");
                add("F");
                add("G");
                add("G#");
                add("A#");
                add("C");
            }
        };

        assertEquals(listOfNotes, listOfCorrectNotes);
    }
    @Test
    public void itsLocrianScale_showsGoodNotes() {

        HelloApplication helloApplication = new HelloApplication();

        List<String> listOfNotes = helloApplication.createGuitarKey("C", 2);
        List <String> listOfCorrectNotes = new ArrayList<String>() {
            {
                add("C");
                add("C#");
                add("D#");
                add("F");
                add("F#");
                add("G#");
                add("A#");
                add("C");
            }
        };

        assertEquals(listOfNotes, listOfCorrectNotes);
    }
    @Test
    public void itsIonianScale_showsGoodNotes() {

        HelloApplication helloApplication = new HelloApplication();

        List<String> listOfNotes = helloApplication.createGuitarKey("C", 3);
        List <String> listOfCorrectNotes = new ArrayList<String>() {
            {
                add("C");
                add("D");
                add("E");
                add("F");
                add("G");
                add("A");
                add("B");
                add("C");
            }
        };

        assertEquals(listOfNotes, listOfCorrectNotes);
    }
}