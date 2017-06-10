package holuhoev.Client;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

public class ClientGUI extends Application
{
    ClientNIO client;
    ObservableList<String> items = FXCollections.observableArrayList();
    ObservableList<String> usernamesList = FXCollections.observableArrayList();
    InetAddress adress = null;
    int port = 9999;

    @Override
    public void start(Stage primaryStage)
    {
        client = new ClientNIO(this);

        initialize();

        try
        {
            while (!client.makeConnection(adress, port))
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Неверные данные");
                alert.setContentText("Введите корректные адрес !");

                alert.showAndWait();
                initialize();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        authorize();

        Button btn = new Button();
        btn.setText("Отправить");
        btn.setDefaultButton(true);

        TextArea textArea = new TextArea();
        textArea.setPrefHeight(100);
        textArea.setPrefWidth(300);


        textArea.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent keyEvent)
            {
                if (keyEvent.getCode() == KeyCode.ENTER)
                {
                    btn.fire();
                }
            }
        });

        FlowPane textPane = new FlowPane(5, 5);
        textPane.getChildren().add(textArea);
        textPane.getChildren().add(btn);


        ListView<String> list = new ListView<>();
        list.setItems(items);
        list.setPrefWidth(300);
        list.setPrefHeight(200);
        list.setFocusTraversable(true);

        ListView<String> usernames = new ListView<>();
        usernames.setItems(usernamesList);
        usernames.setPrefWidth(100);
        usernames.setPrefHeight(200);

        FlowPane chat = new FlowPane();
        chat.getChildren().add(list);
        chat.getChildren().add(usernames);


        btn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                client.sendText(textArea.getText());
                textArea.clear();
            }
        });

        FlowPane root = new FlowPane();
        root.setOrientation(Orientation.VERTICAL);

        root.getChildren().add(chat);
        root.getChildren().add(textPane);


        Scene scene = new Scene(root, 400, 300);

        primaryStage.setTitle(client.clientName);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                client.sendDisconnectMessage();
            }
        });
    }

    public void printTextToListView(String text)
    {
        synchronized (items)
        {
            items.add(text);
        }
    }

    private void initialize()
    {

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Инициализация");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField from = new TextField();
        from.setPromptText("MacBook-Pro-Evgenij.local/127.0.0.1");
        TextField to = new TextField();
        to.setPromptText("9999");

        gridPane.add(new Label("Адрес:"), 0, 0);
        gridPane.add(from, 1, 0);
        gridPane.add(new Label("Порт:"), 2, 0);
        gridPane.add(to, 3, 0);

        dialog.getDialogPane().setContent(gridPane);

        // Request focus on the username field by default.
        Platform.runLater(() -> from.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton ->
        {
            if (dialogButton == loginButtonType)
            {
                return new Pair<>(from.getText(), to.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        try
        {
            result.ifPresent(pair ->
            {
                try
                {
                    adress = InetAddress.getByName(pair.getKey());
                } catch (UnknownHostException e)
                {
                    e.printStackTrace();
                }
                port = Integer.parseInt(pair.getValue());
            });
        } catch (Exception ex)
        {

        }
    }

    public void addUsername(String user)
    {
        synchronized (usernamesList)
        {
            usernamesList.add(user);
        }
    }

    private void authorize()
    {
        //TODO:Считать с консоли
        String username = "";

        String userlist = "nothing";

        while (userlist.equals("nothing"))
            userlist = client.getUserList();

        System.out.println("Get userlist:" + userlist);

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Авторизация");
        dialog.setContentText("Пожалуйста, введите ваш ник:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent())
        {
            username = result.get();

            while (userlist.contains(username) && !username.contains(" "))
            {
                if (username.contains(" "))
                    dialog.setHeaderText("Введите без пробелов.");
                else
                    dialog.setHeaderText("Данный ник уже используется, придумайте другой.");

                result = dialog.showAndWait();

                if (result.isPresent())
                    username = result.get();

            }
        }

        String[] users = (userlist.trim()).split(" ");

        usernamesList.clear();

        for (String us : users)
        {
            if (!us.equals(""))
                usernamesList.add(us);
        }

        client.sendConnectMessage(username);
        client.clientName = username;
    }

    @Override
    public void stop()
    {
        client.sendDisconnectMessage();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}