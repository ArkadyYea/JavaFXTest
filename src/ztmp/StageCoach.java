package ztmp;

import java.util.List;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class StageCoach extends Application {
	StringProperty title = new SimpleStringProperty();
	Text textStageX;
	Text textStageY;
	Text textStageW;
	Text textStageH;
	Text textStageF;
	CheckBox checkBoxResizable;
	CheckBox checkBoxFullScreen;
	double dragAnchorX;
	double dragAnchorY;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) {
		StageStyle stageStyle = StageStyle.DECORATED;
		List<String> unnamedParams = getParameters().getUnnamed();
		if (unnamedParams.size() > 0) {
			String stageStyleParam = unnamedParams.get(0);
			if (stageStyleParam.equalsIgnoreCase("transparent")) {
				stageStyle = StageStyle.TRANSPARENT;
			} else if (stageStyleParam.equalsIgnoreCase("undecorated")) {
				stageStyle = StageStyle.UNDECORATED;
			} else if (stageStyleParam.equalsIgnoreCase("utility")) {
				stageStyle = StageStyle.UTILITY;
			}
		}
		final Stage stageRef = stage;
		Group rootGroup;
		TextField titleTextField;
		Button toBackButton = new Button("toBack()");
		toBackButton.setOnAction(e -> stageRef.toBack());
		Button toFrontButton = new Button("toFront()");
		toFrontButton.setOnAction(e -> stageRef.toFront());
		Button closeButton = new Button("close()");
		closeButton.setOnAction(e -> stageRef.close());
		Rectangle blue = new Rectangle(250, 350, Color.SKYBLUE);
		blue.setArcHeight(50);
		blue.setArcWidth(50);
		textStageX = new Text();
		textStageX.setTextOrigin(VPos.TOP);
		textStageY = new Text();
		textStageY.setTextOrigin(VPos.TOP);
		textStageH = new Text();
		textStageH.setTextOrigin(VPos.TOP);
		textStageW = new Text();
		textStageW.setTextOrigin(VPos.TOP);
		textStageF = new Text();
		textStageF.setTextOrigin(VPos.TOP);
		checkBoxResizable = new CheckBox("resizable");
		checkBoxResizable.setDisable(stageStyle == StageStyle.TRANSPARENT || stageStyle == StageStyle.UNDECORATED);
		checkBoxFullScreen = new CheckBox("fullScreen");
		titleTextField = new TextField("Stage Coach");
		Label titleLabel = new Label("title");
		HBox titleBox = new HBox(titleLabel, titleTextField);
		VBox contentBox = new VBox(textStageX, textStageY, textStageW, textStageH, textStageF, checkBoxResizable,
				checkBoxFullScreen, titleBox, toBackButton, toFrontButton, closeButton);
		contentBox.setLayoutX(30);
		contentBox.setLayoutY(20);
		contentBox.setSpacing(10);
		rootGroup = new Group(blue, contentBox);
		Scene scene = new Scene(rootGroup, 270, 370);
		scene.setFill(Color.TRANSPARENT);
		// when mouse button is pressed, save the initial position of screen
		rootGroup.setOnMousePressed((MouseEvent me) -> {
			dragAnchorX = me.getScreenX() - stageRef.getX();
			dragAnchorY = me.getScreenY() - stageRef.getY();
		});
		// when screen is dragged, translate it accordingly
		rootGroup.setOnMouseDragged((MouseEvent me) -> {
			stageRef.setX(me.getScreenX() - dragAnchorX);
			stageRef.setY(me.getScreenY() - dragAnchorY);
		});
		textStageX.textProperty().bind(new SimpleStringProperty("x: ").concat(stageRef.xProperty().asString()));
		textStageY.textProperty().bind(new SimpleStringProperty("y: ").concat(stageRef.yProperty().asString()));
		textStageW.textProperty().bind(new SimpleStringProperty("width: ").concat(stageRef.widthProperty().asString()));
		textStageH.textProperty()
				.bind(new SimpleStringProperty("height: ").concat(stageRef.heightProperty().asString()));
		textStageF.textProperty()
				.bind(new SimpleStringProperty("focused: ").concat(stageRef.focusedProperty().asString()));
		stage.setResizable(true);
		checkBoxResizable.selectedProperty().bindBidirectional(stage.resizableProperty());
		checkBoxFullScreen.selectedProperty().addListener((ov, oldValue, newValue) -> {
			stageRef.setFullScreen(checkBoxFullScreen.selectedProperty().getValue());
		});
		title.bind(titleTextField.textProperty());
		stage.setScene(scene);
		stage.titleProperty().bind(title);
		stage.initStyle(stageStyle);
		stage.setOnCloseRequest((WindowEvent we) -> {
			System.out.println("Stage is closing");
		});
		stage.show();
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
		stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 4);
	}
}