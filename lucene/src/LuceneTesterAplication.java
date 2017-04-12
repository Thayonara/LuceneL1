import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.lucene.queryParser.ParseException;

import javax.swing.*;
import javax.xml.ws.handler.Handler;
import java.io.IOException;

/**
 * Created by thayo on 12/04/2017.
 */
public class LuceneTesterAplication extends Application implements EventHandler {
    Button button;
    TextField consultTextF;
    TextField consultTextF1;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Consulta Lucene");
        button = new Button();
        consultTextF = new TextField();
        consultTextF1 = new TextField();
        button.setText("Consultar");

        button.setOnAction(this);

        HBox hb = new HBox();
        hb.getChildren().add(consultTextF);
        hb.getChildren().add(consultTextF1);
        hb.getChildren().add(button);

        hb.setSpacing(10);


        Scene scene = new Scene(hb, 500, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void handle(Event event) {
        if(event.getSource() == button){
            String consT = consultTextF.getText();
            String base = consultTextF1.getText();

             LuceneTester tester;

            try{
                tester = new LuceneTester();
                consultTextF.setText("");
                consultTextF1.setText("");
                System.out.println(base);


                if(base.equals("base 1")){
                    tester.createIndex("original");
                    tester.searcher("original");
                    System.out.println("base 1");

                }
                if(base.equals("base 2")){
                    tester.createIndex("withStopWord");
                    tester.searcher(tester.tokenizeStop(consT));
                    System.out.println("base 2");

                }
                if(base.equals("base 3")){
                    tester.createIndex("withStemming");
                    tester.searcher(tester.tokenizeStem(consT));
                    System.out.println("base 3");

                } if(base.equals("base 4")){
                    tester.createIndex("withStopStem");
                    tester.searcher(tester.tokenizeStopStem(consT));
                    System.out.println("base 4");

                }

            } catch (IOException i){
                i.printStackTrace();
            } catch (ParseException r){
                r.printStackTrace();
            }

        }

    }
}
