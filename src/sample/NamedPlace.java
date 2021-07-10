package sample;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;


public class NamedPlace extends Place {
    private String namn;
    public NamedPlace(double x, double y, int nummer, String namn, String category) {
        super(x, y, nummer, category, namn);
        this.namn = namn;
    }
    public String toString() {
        return namn +": "+getX()+", "+getY();
    }
}
class NamedAlert extends Alert{
    private TextField field;
    NamedAlert(){
        super(AlertType.CONFIRMATION);
        GridPane grid = new GridPane();
        Label lab = new Label("namn: ");
        field = new TextField();
        grid.addRow(0, lab, field);
        grid.setHgap(14);
        setHeaderText(null);
        setTitle("Nytt namn");
        getDialogPane().setContent(grid);
    }
    public String getNamn(){
        return field.getText();
    }

    }
