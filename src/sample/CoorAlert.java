package sample;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class CoorAlert extends Alert {
    private TextField texX;
    private TextField texY;

    public CoorAlert() {
        super(AlertType.CONFIRMATION);

        GridPane grid = new GridPane();
        Label x = new Label("X: ");
        Label y = new Label(("Y: "));
        texX = new TextField();
        texY = new TextField();
        grid.addRow(0, x, texX);
        grid.addRow(1, y, texY);
        grid.setVgap(10);
        grid.setHgap(10);
        getDialogPane().setContent(grid);
        setHeaderText(null);
        setTitle("Input Coordinates: ");

    }

    public double getsX() {
        return Double.parseDouble(texX.getText());
    }

    public double getsY() {
        return Double.parseDouble(texY.getText());
    }

}
