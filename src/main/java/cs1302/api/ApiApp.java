package cs1302.api;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Random;
import java.lang.Integer;
import java.io.FileInputStream;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.scene.control.TextArea;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * REPLACE WITH NON-SHOUTING DESCRIPTION OF YOUR APP.
 */
public class ApiApp extends Application {

    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    /**
    private static class bdlResponse {
        static bdlResult[] results;
    }
    */

    /**
     * Initializes the String variables that represent the team data.
     */
    private static class BdlResult {
        String abbreviation;
        String conference;
        String division;
        String name;
        String city;
    }

    /**
     * Initializes the array which is parsed through to get to the image urls.

    private static class SerpResponse {
        static SerpResult[] image_urls;
    }
    */

    /**
     * Initializes the Knowledge object that is used to parse through the JSON..
     */
    private static class SerpResult {
        Knowledge knowledge_graph;
    }

    /**
     * Initializes the class Knowledge from which the data is drawn.
     */
    private static class Knowledge {
        String website;
        String title;
        String description;
        String founded;

    }

    Stage stage;
    VBox root;
    VBox header;

    String searchText = new String("enter any value between 1 and 30");
    Text instruction = new Text("type a team ID the search box, then hit the button");
    String url;
    String errorString;
    private boolean getTeams;
    Button button = new Button("Search team!");
    TextField searchField = new TextField();
    Text bodyText = new Text("");
    static Text bodyText2 = new Text("");
    String f = "Team name: ";
    String ci = "City: ";
    String d = "Division: ";
    String c = "Conference: ";
    String a = "Abbreviation: ";
    static String add = "Additional Info: ";
    String web = "Website: ";
    String found = "Founded: ";
    String desc = "Description: ";
    String fn = "Title: ";

    //    static WebView webView = new WebView();
    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        stage = null;
        root = new VBox();
        header = new VBox();


    } // ApiApp

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        // initialize the array with default image
        BorderPane borderPane = new BorderPane();
        ToolBar toolBar = new ToolBar();
        Text search = new Text("Search: ");

        bodyText.setText(f + "\n" + ci + "\n" + c + "\n" + d + "\n" + a + "\n\n");
        bodyText.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        bodyText2.setText(add + "\n" + web + "\n" + found + "\n" + fn);
        bodyText2.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        //webView.getEngine().load("https://nba.com/hawks");
        searchField.setText(searchText);
        searchField.setPrefWidth(300);
        toolBar.getItems().addAll(search, searchField, button);
        header.getChildren().addAll(toolBar, instruction);
        borderPane.setTop(header);
        root.getChildren().addAll(bodyText, bodyText2);
        borderPane.setCenter(root);
        EventHandler<ActionEvent> buttonHandler = (ActionEvent gtb) -> {
            this.handle(gtb);
        };
        button.setOnAction(buttonHandler);

        Scene scene = new Scene(borderPane);
        stage.setTitle("NBA Teams!");
        stage.setMaxWidth(1280);
        stage.setMaxHeight(720);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
        Platform.runLater(() -> stage.setResizable(false));
    } // start

    /**
     * The code that is run when the "Search teams!" button is pressed.
     *
     * @param event the setOnAction quality
     */

    public void handle (ActionEvent event) {
        button.setDisable(true);
        instruction.setText("Data received!");
        int urlParameter = 0;
        searchText = searchField.getText();
        urlParameter = Integer.parseInt(searchText);
        getTeams(urlParameter);
        button.setDisable(false);
    }


    /**
     * Sends a request to the BallDontLie API to gather data on NBA teams,
     * then sends the data gathered to the SerpStack API to generate additional info about
     * the teams.
     *
     * @param id is the team ID used as the query for the first API
     * @return getTeams is true if the method has run appropriately
     */

    public Boolean getTeams(int id) {
        if (id > 0) {
            try {
                url = "https://www.balldontlie.io/api/v1/teams/" + id;
                System.out.println(url);
            } catch (Exception e) {
                System.out.println("input is invalid");
            }
        }

        try {
            if (id == 0 || id > 30) {
                errorString = "only 30 teams in the NBA. Please enter a value 1-30";
                throw new IllegalArgumentException(errorString);
            }
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();
            HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
            BdlResult ballDontLie = GSON.fromJson(response.body(), (java.lang.reflect.Type)
                BdlResult.class);
            String abb = ballDontLie.abbreviation;
            String conf = ballDontLie.conference;
            String div = ballDontLie.division;
            String fna = ballDontLie.name;
            String city = ballDontLie.city;
            bodyText.setText(f + fna + "\n" + ci + city + "\n" + c + conf + "\n"
                + d + div + "\n" + a + abb + "\n\n");
            getTeams = true;
            String name = ballDontLie.city + "+" + ballDontLie.name;
            System.out.println(name);
            if (getTeams == true) {
                url = "http://api.serpstack.com/search?access_key=7f0f18cea16d95bef0bd09eb75e31251&query=" + name;
                System.out.println(url);
            }
            HttpRequest request2 = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();
            HttpResponse<String> response2 = HTTP_CLIENT.send(request2, BodyHandlers.ofString());
            SerpResult serp = GSON.fromJson(response2.body(), SerpResult.class);
            System.out.println(serp.knowledge_graph.website);
            updateSerpResponse(serp);

        } catch (IllegalArgumentException | IOException | URISyntaxException
            | InterruptedException e) {
            instruction.setText("Last attempt to get team failed...");
            Alert a = new Alert(AlertType.NONE);
            a.setContentText(errorString);
            a.setTitle("Error");
            a.setAlertType(AlertType.ERROR);
            a.showAndWait();

            return getTeams;
        }
        return getTeams;
    }

    /**
     * Parses through the serpResponse JSON to find the image urls.
     *
     * @param serp is the JSON object
     */
    private  void updateSerpResponse(SerpResult serp) {
        String w = serp.knowledge_graph.website;
        String de = serp.knowledge_graph.description;
        String fo = serp.knowledge_graph.founded;
        String ti = serp.knowledge_graph.title;

        if (w == null) {
            w = "N/A";
        }

        if (fo == null) {
            w = "N/A";
        }

        if (ti == null) {
            w = "N/A";
        }
        bodyText2.setText(add + "\n" + web + w + "\n" + found + fo + "\n" + fn + ti);

        //webView.getEngine().load(website);
    }
} // ApiApp
