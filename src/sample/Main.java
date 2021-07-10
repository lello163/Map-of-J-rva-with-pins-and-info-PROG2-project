package sample;

import java.io.*;
import java.util.*;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
    private Map<Position, Place> placesByPosition = new HashMap<>();
    private Map<String, List<Place>> placesByName = new HashMap<>();
    private ObservableList<String> platser = FXCollections.observableArrayList("Bus", "Underground", "Train");
    private ListView<String> li = new ListView<>(platser);
    private Map<String, String> kategorier = null;
    private HashMap<String, List<Place>> catPlaces = new HashMap<>();
    private ArrayList<Place> markedPlaces = new ArrayList<>();
    private RadioButton namedBtn;
    private RadioButton descBtn;
    private Stage primaryStage;
    private ImageView imageView = new ImageView();
    private Pane root;
    private TextField field;
    private String fed;
    private Scene scene;
    private Button newBtn;
    private Button removeBtn;
    private boolean nummer = true;
    private ClickHandler positionHandler = new ClickHandler();
    private Color farg;
    //private Place tri;
    private boolean changed = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        BorderPane border = new BorderPane();
        HBox horizont = new HBox();
        VBox verticalRadio = new VBox();
        VBox cateVbox = new VBox(new Label("Categories"));
        root = new Pane();
        StackPane stack = new StackPane();

        horizont.setSpacing(15);
        horizont.setAlignment(Pos.CENTER);
        FlowPane flow = new FlowPane();

        VBox v = new VBox();
        MenuBar bar = new MenuBar();
        v.getChildren().add(bar);
        Menu filemenu = new Menu("File");
        bar.getMenus().add(filemenu);
        MenuItem item3 = new MenuItem("Save");
        filemenu.getItems().add(item3);
        item3.setOnAction(new SaveHandler());
        MenuItem item1 = new MenuItem(("Load Map"));
        filemenu.getItems().add(item1);
        item1.setOnAction(new OpenHandler());
        MenuItem item2 = new MenuItem(("Load Places"));
        filemenu.getItems().add(item2);
        item2.setOnAction(new LoadPlaces());
        MenuItem item4 = new MenuItem(("Exit"));
        filemenu.getItems().add(item4);
        item4.setOnAction(e -> primaryStage.fireEvent(
                new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST)));
        v.getChildren().add(horizont);

        imageView = new ImageView();
        field = new TextField("Search");
        newBtn = new Button("New");
        newBtn.setOnAction(new PositionHandler());
        Button searchBtn = new Button("Search");
        searchBtn.setOnAction(new SearchHandler());
        Button hideBtn = new Button("Hide");
        hideBtn.setOnAction(new HideHandler());
        removeBtn = new Button("Remove");
        removeBtn.setOnAction(new removeHandler());
        Button coorBtn = new Button("Coordinates");
        coorBtn.setOnAction(new Coordinats());
        Button hideCat = new Button("Hide Category");
        hideCat.setOnAction(new HideCategory());
        namedBtn = new RadioButton("Named");
        descBtn = new RadioButton("Described");
        namedBtn.setSelected(true);
        ToggleGroup toggle = new ToggleGroup();
        namedBtn.setToggleGroup(toggle);
        descBtn.setToggleGroup(toggle);

        verticalRadio.getChildren().addAll(namedBtn, descBtn);
        horizont.getChildren().addAll(newBtn, verticalRadio, field, searchBtn, hideBtn, removeBtn, coorBtn);
        horizont.setAlignment(Pos.CENTER);

        ScrollPane scroll = new ScrollPane();
        scroll.setContent(imageView);
        scroll.setFitToHeight(false);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        root.getChildren().add(scroll);

        stack.getChildren().add(hideCat);
        li.setPrefSize(200, 125);
        li.setOnMouseClicked(new ShowCategoryPlace());
        cateVbox.getChildren().addAll(li, stack);
        cateVbox.setAlignment(Pos.CENTER);

        border.setRight(cateVbox);
        border.setTop(v);
        border.setCenter(root);
        scene = new Scene(border);
        primaryStage.setScene(scene);
        primaryStage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, new ExitHandler());
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    class PositionHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            root.addEventHandler(MouseEvent.MOUSE_CLICKED, positionHandler);
            scene.setCursor(Cursor.CROSSHAIR);
            newBtn.setDisable(true);

        }
    }


    class MarkHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {

            Place tri = (Place) event.getSource();

            MouseButton button = event.getButton();
            if (button == MouseButton.SECONDARY) {
                // info alert

                if (tri instanceof NamedPlace) {
                    Alert a = new Alert(Alert.AlertType.INFORMATION, tri.toString());
                    a.showAndWait();

                } else if (tri instanceof DescribedPlace) {
                    Alert a = new Alert(Alert.AlertType.INFORMATION, tri.toString());
                    a.showAndWait();
                }

            } else if (!tri.isMarked()) {

                tri.setFill(Color.YELLOW);
                tri.setMarked(true);
                markedPlaces.add(tri);

            } else {
                tri.setMarked(false);
                tri.paintCovered();
                markedPlaces.remove(tri);
            }
        }
    }

    class ClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            double x = event.getX();
            double y = event.getY();

            String cat = "None";
            farg = (null);
            int nmbr = 0;
            fed = li.getSelectionModel().getSelectedItem();

            if (fed == null) {
                nmbr = 0;
                li.getSelectionModel().clearSelection();

            } else {
                switch (fed) {
                    case "Bus":
                        nmbr = 1;
                        li.getSelectionModel().clearSelection();
                        cat = "Bus";
                        break;
                    case "Underground":
                        nmbr = 2;
                        li.getSelectionModel().clearSelection();
                        cat = "Underground";
                        break;
                    case "Train":
                        nmbr = 3;
                        li.getSelectionModel().clearSelection();
                        cat = "Train";
                        break;

                }
            }
            Place tri = null;
            try {
                if (namedBtn.isSelected()) {
                    NamedAlert alert = new NamedAlert();
                    Optional<ButtonType> res = alert.showAndWait();
                    if (res.isPresent() && res.get() == ButtonType.OK) {
                        String namn = alert.getNamn();
                        if (namn.trim().isEmpty()) {
                            Alert msg = new Alert(Alert.AlertType.ERROR, "Tomt namn!");
                            msg.setHeaderText(null);
                            msg.showAndWait();
                            return;
                        } else if (res.isPresent() && res.get() == ButtonType.CANCEL) {
                            event.consume();
                        } else {
                            tri = new NamedPlace(x, y, nmbr, namn, cat);
                            Position p = new Position(x, y);
                            addStruc(tri);
                        }
                    }

                } else if (descBtn.isSelected()) {

                    DescAlert desc = new DescAlert();
                    Optional<ButtonType> res = desc.showAndWait();
                    if (res.isPresent() && res.get() == ButtonType.OK) {
                        String namn = desc.getNamn();
                        String beskrivning = desc.getBeskrivning();
                        if (namn.trim().isEmpty() || beskrivning.trim().isEmpty()) {
                            Alert msg = new Alert(Alert.AlertType.ERROR, "Tomt namn eller beskrivning!");
                            msg.setHeaderText(null);
                            msg.showAndWait();
                            return;
                        } else if (res.isPresent() && res.get() == ButtonType.CANCEL) {
                            event.consume();
                        }
                        tri = new DescribedPlace(x, y, nmbr, namn, beskrivning, cat);
                        Position p = new Position(x, y);
                        addStruc(tri);



                        }
                }
                tri.setOnMouseClicked(new MarkHandler());
                root.getChildren().add(tri);
                changed = true;
                root.removeEventHandler(MouseEvent.MOUSE_CLICKED, positionHandler);
                scene.setCursor(Cursor.DEFAULT);
                newBtn.setDisable(false);
            } catch (NumberFormatException e) {
                Alert msg = new Alert(Alert.AlertType.ERROR);
                msg.setContentText("Fel inmatning!");
                msg.setHeaderText(null);
                msg.showAndWait();
            } catch (NullPointerException ne) {
                System.out.println("Fångade en nullpointer");
            } catch (IllegalArgumentException se) {
                System.out.println("Fångade en Illegalargument");
            }

        }

    }
    public void addStruc(Place p){
        String cat = p.getCategory();
        String namn = p.getNamn();
        if (placesByPosition.containsKey(p)){
            new Alert(Alert.AlertType.ERROR);
            return;
        }
        placesByPosition.put(new Position(p.getX(), p.getY()), p);
        if (catPlaces.containsKey(cat)) {
            catPlaces.get(cat).add(p);
        } else {
            catPlaces.put(cat, new ArrayList<>());
            catPlaces.get(cat).add(p);
        }

        if (placesByName.containsKey(namn)) {
            placesByName.get(namn).add(p);
        } else {
            placesByName.put(namn, new ArrayList<>());
            placesByName.get(namn).add(p);
        }
    }

    class ShowCategoryPlace implements EventHandler<MouseEvent> {
        public void handle(MouseEvent event) {
            String selected = li.getSelectionModel().getSelectedItem();
            if (selected != null && selected.equals("Bus")) {
                if (catPlaces.containsKey("Bus")) {
                    for (Place p : catPlaces.get("Bus"))
                        p.setVisible(true);
                }
            }
            if (selected != null && selected.equals("Train")) {
                if (catPlaces.containsKey("Train")) {
                    for (Place p : catPlaces.get("Train"))
                        p.setVisible(true);
                }
            }
            if (selected != null && selected.equals("Underground")) {
                if (catPlaces.containsKey("Underground")) {
                    for (Place p : catPlaces.get("Underground"))
                        p.setVisible(true);
                }
            }
        }
    }

    class HideCategory implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
            String selected = li.getSelectionModel().getSelectedItem();
            if (selected != null && selected.equals("Bus")) {
                if (catPlaces.containsKey("Bus")) {
                    for (Place p : catPlaces.get("Bus"))
                        p.setVisible(false);
                }
            }
            if (selected != null && selected.equals("Underground")) {
                if (catPlaces.containsKey("Underground")) {
                    for (Place u : catPlaces.get("Underground"))
                        u.setVisible(false);
                }
            }
            if (selected != null && selected.equals("Train")) {
                if (catPlaces.containsKey("Train")) {
                    for (Place u : catPlaces.get("Train"))
                        u.setVisible(false);
                }
            }
        }
    }

    class SearchHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            String word = field.getText();
            for (Place p : markedPlaces) {
                p.unMark();
            }
            markedPlaces.clear();
            try {
                if(placesByName.containsKey(word))

                if (word.isEmpty())
                    word = li.getSelectionModel().getSelectedItem();
                else
                    li.getSelectionModel().select((word));
                if (word == null || placesByName == null)
                    return;
                List<Place> fed = placesByName.get(word);

                //markedPlaces.add(tri); Glöm inte att ta bort undre markedplaces ifall det buggar
                for (Place p : fed) {
                    p.setMarked(true);
                    p.setFill(Color.YELLOW);
                    p.markPlace();
                    markedPlaces.add(p);
                }
            } catch (NullPointerException e) {
                if (placesByName.get(word) == null) {
                    new Alert(Alert.AlertType.ERROR, "Finns inte något som heter så").showAndWait();
                }
            }
        }
    }
    class removeHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            root.getChildren().removeAll(markedPlaces);
            for(Place m: markedPlaces){
                catPlaces.get(m.getCategory()).remove(m);
                placesByPosition.remove(m.getPosition());
                placesByName.get(m.getNamn()).remove(m);
            }
            markedPlaces.clear();

            changed = true;
        }
    }

    class HideHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            for (Place p : markedPlaces) {
                p.setVisible(false);
                p.unMark();
            }
            markedPlaces.clear();

        }
    }

    class Coordinats implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {



            try {
                CoorAlert cor = new CoorAlert();
                Optional<ButtonType> coralert = cor.showAndWait();
                Position p = new Position(cor.getsX(), cor.getsY());
                if (placesByPosition.containsKey(p)) {
                    if (coralert.isPresent() && coralert.get() == ButtonType.OK) {
                        for (Place pl : markedPlaces) {
                            pl.unMark();
                        }
                        markedPlaces.clear();

                        if (placesByPosition.containsKey(p)) {
                            placesByPosition.get(p).markPlace();
                            markedPlaces.add(placesByPosition.get(p));
                            System.out.println("HIT");
                        }
                    }
                }
                     else {
                        new Alert(Alert.AlertType.ERROR, "Finns ingen position här").showAndWait();
                    }
            } catch (NumberFormatException ev) {
                Alert n = new Alert(Alert.AlertType.ERROR, "Fel, måste innehålla siffror");
                n.showAndWait();
            }
        }
    }
    class ExitHandler implements EventHandler<WindowEvent> {
        public void handle(WindowEvent event) {
            if (changed) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Osparat. Avsluta ändå?");
                Optional<ButtonType> res = alert.showAndWait();
                if (res.isPresent() && res.get() == ButtonType.CANCEL)
                    event.consume();
            }
        }
    }

    class OpenHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            FileChooser fileChooser = new FileChooser();
            if (changed) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Osparat, Avsluta ändå?");
                Optional<ButtonType> res = alert.showAndWait();
                if (res.isPresent() && res.get() == ButtonType.CANCEL) {
                    event.consume();
                    return;
                }
            }
            fileChooser.setTitle("Välj bildfil");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Bildfiler",
                            "*.jpg", "*.png"),
                    new FileChooser.ExtensionFilter("Alla filer", "*.*"));
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                for(Place m: placesByPosition.values()){
                    root.getChildren().remove(m);
                }
                placesByName.clear();
                placesByPosition.clear();
                markedPlaces.clear();

                String name = file.getAbsolutePath();
                Image image = new Image("file:" + name);
                imageView.setImage(image);
                primaryStage.sizeToScene();

                //  while (root.getChildren().contains(tri)){
                ArrayList<Node> tabort = new ArrayList<>();
                for(Node p: root.getChildren())
                    if(p instanceof Place){
                        tabort.add(p);
                    }
                root.getChildren().removeAll(tabort);
            }
        }
    }
    class LoadPlaces implements EventHandler<ActionEvent>{
        public void handle(ActionEvent e){
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Välj platsfiler");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Alla filer", "*.*")
            );
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                String name = file.getAbsolutePath();
                for(Place m: placesByPosition.values()){
                    root.getChildren().remove(m);
                }
                placesByName.clear();
                placesByPosition.clear();
                markedPlaces.clear();
                try{
                    FileReader in = new FileReader(name);
                    BufferedReader br = new BufferedReader(in);
                    String line;
                    while ((line=br.readLine()) != null){
                        String[] tokens = line.split(",");
                        if(tokens.length >= 5) {
                            String type = tokens[0];
                            String category = tokens[1];
                            double x = Double.parseDouble(tokens[2]);
                            double y = Double.parseDouble(tokens[3]);
                            String namn = tokens[4];
                            int nmbr;
                            switch (category) {
                                case "Bus":
                                    nmbr = 1;
                                    break;
                                case "Underground":
                                    nmbr = 2;
                                    break;
                                case "Train":
                                    nmbr = 3;
                                    break;
                                default:
                                    nmbr = 0;
                            }
                            Place p;
                            if (type.equals("Named")) {
                                p = new NamedPlace(x, y, nmbr, namn, category);
                            } else {
                                String desc = tokens[5];
                                p = new DescribedPlace(x, y, nmbr, namn, desc, category);
                            }
                            addStruc(p);
                            p.setOnMouseClicked(new MarkHandler());
                            root.getChildren().add(p);
                        }
                    }
                    br.close();
                    in.close();
                }catch(FileNotFoundException error){
                    new Alert(Alert.AlertType.ERROR, "Fel!").showAndWait();
                }catch(IOException event){
                    new Alert(Alert.AlertType.ERROR, "Fel!").showAndWait();
                }
            }
        }
    }

    class SaveHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Välj platsfiler");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Alla filer", "*.*")
                );
                File file = fileChooser.showSaveDialog(primaryStage);
                if(file == null){
                    return;
                }

                FileWriter text = new FileWriter(file, false);
                PrintWriter out = new PrintWriter(text);
                for (Place p : placesByPosition.values())
                    if (p instanceof NamedPlace) {
                        out.println("Named," + p.getCategory() + "," + p.getX() + "," + p.getY() + "," + p.getNamn());
                    } else if (p instanceof DescribedPlace) {
                        out.println("Described," + p.getCategory() + "," + p.getX() + ',' + p.getY() + ',' + p.getNamn() + "," + ((DescribedPlace) p).getBeskrivning());
                    }
                System.out.println("test");
                out.println();
                changed = false;
                out.close();
            } catch (FileNotFoundException fnfe) {
                System.err.println("Filen går ej att skriva!");
            } catch (IOException ioe) {
                System.err.println("Fel har inträffat!");
            }
        }
    }
}