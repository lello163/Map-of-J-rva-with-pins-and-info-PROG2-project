package sample;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
public class Position{
    private double  xCoord,yCoord;

    public Position(double xCoord, double yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;

    }
    public double getXCoord() {
        return xCoord;
    }
    public double getYCoord() {
        return yCoord;
    }

    public boolean equals(Object other) {
        if (other instanceof Position) {
            Position pos = (Position) other;
            return xCoord == pos.xCoord && yCoord == pos.yCoord;
        }
        else
            return false;
    }

    public int hashCode() {
        return (int) xCoord + (int) yCoord;
    }
}


