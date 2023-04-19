package client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientMain extends Application {
    public static void main(String[] args) {
        //launch(args);
        //Application.launch(Main.class, args);
        try (Socket socket = new Socket("localhost", 5000)) {
            BufferedReader input = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);
            String userInput;
            String response;
            String clientName = "empty";
            ClientThread clientThread = new ClientThread(socket);
            clientThread.start();

            do {
                if (clientName.equals("empty")) {
                    System.out.println("Enter your name");
                    userInput = scanner.nextLine();
                    clientName = userInput;
                    output.println(userInput);
                    if (userInput.equals("exit")) {
                        break;
                    }
                } else {
                    String message = ("(" + clientName + ")" + " message ");
                    System.out.println(message);
                    userInput = scanner.nextLine();
                    output.println(message + " " + userInput);
                    if (userInput.equals("exit")) {
                        break;
                    }
                }
            } while (!userInput.equals("exit"));
        } catch (Exception e) {
            System.out.println("Exception in client main" + e.getStackTrace());
        }
    }

    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        HBox header= new HBox();
        Text title = new Text("ITMO Chat");
        title.setTextAlignment(TextAlignment.CENTER);
        header.getChildren().add(title);
        VBox userList = new VBox();
        Border border = new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        userList.setBorder(border);
        //toolbar.setBorder(border);
        Text textField = new Text("Bömi");

        userList.getChildren().addAll(textField);
        userList.setPrefWidth(200);


        VBox chat = new VBox();
        ScrollPane scrollPane = new ScrollPane(chat);
        scrollPane.vvalueProperty().bind(chat.heightProperty());


        HBox input = new HBox();
        input.setBorder(border);
        input.setPrefWidth(1000);
        TextField userInput = new TextField();
        userInput.setPrefWidth(800);
        userInput.maxWidth(800);

        input.getChildren().add(userInput);
        Button submit = new Button("Submit");
        input.getChildren().add(submit);
        root.setTop(header);
        root.setRight(userList);
        root.setBottom(input);
        root.setCenter(scrollPane);
        //root.setCenter(chat);
        root.setStyle("-fx-font-family: " +
                "'Lucida Sans Unicode'");
        root.setStyle("-fx-font-size: 18");
        Scene scene = new Scene(root, 1000,700);
        String username = "bömi";
        //AtomicReference<String> message = null;
        submit.setOnAction(e -> {
            if (userInput.getText().equals("")) {
                System.out.println("No input");
                userInput.requestFocus();
            } else {
                //message.set(userInput.getText());
                System.out.println(userInput.getText());
                String message = userInput.getText();
                chat.getChildren().add(new Text(username + " : " + message));
                userInput.setText("");
                userInput.requestFocus();
            }
        });

        userInput.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                if (userInput.getText().equals("")) {
                    System.out.println("No input");
                    userInput.requestFocus();
                } else {
                    System.out.println(userInput.getText());
                    //message.set(userInput.getText());
                    String message = userInput.getText();
                    chat.getChildren().add(new Text(username + " : " + message));
                    userInput.setText("");
                    userInput.requestFocus();
                }
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}