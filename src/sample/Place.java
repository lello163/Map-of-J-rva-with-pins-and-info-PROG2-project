package sample;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

abstract class Place extends Polygon {
    private boolean covered = true;
    private boolean marked = false;
    private static boolean covered1 = true;
    private double y;
    private double x;
    private int nmbr = 0;
    private String category;
    private String namn;

    public Place(double x, double y, int nmbr, String category, String namn) {
        super(x, y, x - 15, y - 15, x + 15, y - 15);
        this.nmbr = nmbr;
        paintCovered();
        this.x = x;
        this.y = y;
        this.category = category;
        this.namn = namn;

    }
    public String getNamn(){
        return namn;
    }

    public String getCategory(){
        return category;
    }


    public boolean isMarked() {
        return marked;
    }

    public void markPlace() {

        this.marked = true;
        setFill(Color.YELLOW);
        setVisible(true);
    }

    public void unMark() {
        //avmarkera
        paintCovered();
        setStroke(null);
        this.marked = false;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
        System.out.println("HIT IGEN");
    }


    //	public static boolean getCovered(){
//		return covered1;
//	}
    public int getNmbr() {
        return nmbr;
    }
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    public Position getPosition(){
        return new Position(x, y);
    }

    public void paintCovered() {

        if (nmbr == 1)
            setFill(Color.RED);
        else if (nmbr == 2)
            setFill(Color.BLUE);
        else if (nmbr == 3)
            setFill(Color.GREEN);
        else if (nmbr == 0)
            setFill(Color.BLACK);



    }


}

