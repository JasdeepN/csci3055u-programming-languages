package ActorUI;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.AnchorPane;
import javafx.geometry.Insets;
import javafx.scene.Node;


public class PopupMsg {
  PopupMsg(String title, String msg) {
    final Stage primaryStage = new Stage();
    primaryStage.setTitle(title);

    final Popup popup = new Popup();
    popup.setX(300);
    popup.setY(200);

    final Label msgLabel = new Label(msg);

    AnchorPane root = new AnchorPane();

    Button closeBtn = new Button("Dismiss");

    HBox hbox = new HBox(closeBtn);

    root.getChildren().addAll(hbox);

    AnchorPane.setRightAnchor(hbox, 10d);
    AnchorPane.setBottomAnchor(hbox, 10d);

    Scene scene = new Scene(root, 300, 200);
    primaryStage.setScene(scene);
    primaryStage.show();

    closeBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent event) {
        // hides newly created window
        ((Node)(event.getSource())).getScene().getWindow().hide();
      }
    });


// HBox layout = new HBox(10);
// layout.setStyle("-fx-background-color: cornsilk; -fx-padding: 10;");
// primaryStage.setScene(new Scene(layout, 200, 150));
// primaryStage.show();
  }
}
