package org.mystic.gotshild;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Objects;

public class Controller {
    public static final int SIZE = 30;
    @FXML
    Group rootGroup;
    @FXML
    Button startTestButton;
    @FXML
    TextField userName;
    @FXML
    Label label;
    @FXML
    ImageView imageView;
    @FXML
    ImageView patternA;
    @FXML
    ImageView patternB;
    @FXML
    ImageView patternV;
    @FXML
    ImageView patternG;
    @FXML
    ImageView patternD;
    @FXML
    TextArea instructionText;
    private PrintWriter out;
    private String login;
    private int currentImage = 1;

    @FXML
    private void clearTextField() {
        userName.clear();
    }

    @FXML
    private void closeWindow() {
        if (null != out) {
            out.flush();
            out.close();
        }
        Platform.exit();
    }

    @FXML
    private void startGotshildTest() throws FileNotFoundException {
        if (!userName.getText().equalsIgnoreCase("Введите свое имя") && userName.getText().length() > 0) {
            out = new PrintWriter((userName.getText() + (System.currentTimeMillis() % 100000)) + ".txt");
            this.login = userName.getText();
            out.println(login);
            startTestButton.setDisable(true);
            userName.setVisible(false);
            setPatternsVisible(true);
            imageView.setVisible(false);
            instructionText.setVisible(false);
            int step = 1;
            goNextStep(step);
        }
    }

    private void setPatternsVisible(boolean flag) {
        patternA.setVisible(flag);
        patternB.setVisible(flag);
        patternV.setVisible(flag);
        patternG.setVisible(flag);
        patternD.setVisible(flag);
    }

    @FXML
    private void nextInstruction() {
        switch (currentImage) {
            case 1 -> {
                showNextInstruction();
                instructionText.setText("""
                         Пример корректного решения трех примеров задач!""");
            }
            case 2 -> {
                showNextInstruction();
                instructionText.setText("""
                        ВНИМАНИЕ

                        1. В каждой составной фигуре есть элемент который в точности такого же размера и положения как в примере

                        2.В каждой составной фигурет такой элемент один и только один""");
                startTestButton.setDisable(false);
            }
            default -> {
            }
        }
    }

    private void showNextInstruction() {
        currentImage++;
        final String url = Objects.requireNonNull(this.getClass().getResource("/instruction-" + currentImage + ".jpg")).toString();
        imageView.setImage(new Image(url));
    }

    private void goNextStep(int step) {
        if (step > SIZE) {
            setPatternsVisible(false);
            instructionText.setVisible(true);
            instructionText.setText("Вы успешно завершили выполнение теста! ");
            return;
        }
        final long startTime = System.currentTimeMillis();
        String url = Objects.requireNonNull(this.getClass().getResource("/tasks/" + step + ".jpg")).toString();
        ImageView task = new ImageView(new Image(url));
        task.setLayoutX(100);
        task.setLayoutY(250);
        task.setFitWidth(170);
        task.setFitHeight(136);
        task.setPreserveRatio(true);
        task.setPickOnBounds(true);
        task.setVisible(true);
        patternA.setOnMouseClicked(mouseEvent -> {
            outputData(step, "А", startTime);
            intoRecursion(step, task);
        });
        patternB.setOnMouseClicked(mouseEvent -> {
            outputData(step, "Б", startTime);
            intoRecursion(step, task);
        });
        patternV.setOnMouseClicked(mouseEvent -> {
            outputData(step, "В", startTime);
            intoRecursion(step, task);
        });
        patternG.setOnMouseClicked(mouseEvent -> {
            outputData(step, "Г", startTime);
            intoRecursion(step, task);
        });
        patternD.setOnMouseClicked(mouseEvent -> {
            outputData(step, "Д", startTime);
            intoRecursion(step, task);
        });
        rootGroup.getChildren().add(task);
    }

    private void intoRecursion(int step, ImageView task) {
        rootGroup.getChildren().remove(task);
        goNextStep(step + 1);
    }

    private void outputData(int step, String patternName, long startTime) {
        out.println(step + " \t" + patternName + " \t" + (System.currentTimeMillis() - startTime) + " \t");
    }
}
