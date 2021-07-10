package sample;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import sample.Place;

public class DescribedPlace extends Place {
    private String beskrivning;
    private String namn;

    public DescribedPlace(double x, double y, int nummer, String namn, String beskrivning, String category) {
        super(x, y, nummer, category, namn);
        this.beskrivning = beskrivning;
        this.namn = namn;
        this.beskrivning=beskrivning;
    }
    public String getBeskrivning(){
        return beskrivning;
    }

    public String toString() {
        return namn +": "+getX()+", "+getY()+"\n"+getBeskrivning();
    }
}
    class DescAlert extends Alert {
        private TextField field;
        private TextField field1;
        DescAlert(){
            super(AlertType.CONFIRMATION);
            GridPane grid = new GridPane();
            Label lab = new Label("namn: ");
            field = new TextField();
            Label lab1 = new Label("Beskrivning");
            field1 = new TextField();
            grid.addRow(0, lab, field);
            grid.addRow(1, lab1, field1);
            grid.setHgap(14);
            setHeaderText(null);
            setTitle("Nytt namn");
            getDialogPane().setContent(grid);
        }
        public String getNamn(){
            return field.getText();
        }
        public String getBeskrivning(){
            return field1.getText();
        }

    }





