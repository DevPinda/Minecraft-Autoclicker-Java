package com.devpinda.autoclicker.minecraftautoclickerjava;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.logging.Logger;
import java.util.logging.Level;

public class AutoclickerController implements NativeKeyListener {
    @FXML
    private CheckBox leftClickCheckbox;

    @FXML
    private CheckBox rightClickCheckbox;

    @FXML
    private Slider clickRateSlider;

    @FXML
    private Label cpsLabel;

    @FXML
    private Button startButton;

    @FXML
    private Button stopButton;

    private int sliderVal = 1;
    private Robot robot;
    private volatile boolean running = false;

    public void initialize() {
        int sliderInitVal = (int) clickRateSlider.getValue();
        String sliderInitStr = Integer.toString(sliderInitVal);
        String rateLabelAppend = " cps";
        cpsLabel.setText(sliderInitStr+rateLabelAppend);

        startButton.setDisable(true);
        stopButton.setDisable(true);

        clickRateSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!clickRateSlider.isValueChanging()) {
                int snappedValue = (int) Math.round(newValue.doubleValue());
                sliderVal = snappedValue;
                clickRateSlider.setValue(snappedValue);
                cpsLabel.setText(snappedValue+rateLabelAppend);
            }
        });

        leftClickCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                rightClickCheckbox.setSelected(false);
                rightClickCheckbox.setDisable(true);
            } else {
                rightClickCheckbox.setDisable(false);
            }
            updateStartButton();
        });

        rightClickCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                leftClickCheckbox.setSelected(false);
                leftClickCheckbox.setDisable(true);
            } else {
                leftClickCheckbox.setDisable(false);
            }
            updateStartButton();
        });

        try {
            GlobalScreen.registerNativeHook();
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.SEVERE);
        } catch (NativeHookException ex) {
            System.err.println("Failed to register native hook: " + ex.getMessage());
            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(this);

        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private void updateStartButton() {
        startButton.setDisable(!leftClickCheckbox.isSelected() && !rightClickCheckbox.isSelected());
    }

    public synchronized void startClicker() {
        running = true;
        stopButton.setDisable(false);
        startButton.setDisable(true);
        leftClickCheckbox.setDisable(true);
        rightClickCheckbox.setDisable(true);
        clickRateSlider.setDisable(true);

        int delay = sliderVal == 0 ? 0 : (int) Math.round(1000.0 / sliderVal);

        new Thread(() -> {
            while (running) {
                // check which mouse buttons are selected
                if (leftClickCheckbox.isSelected()) {
                    // simulate a left mouse click
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                }
                if (rightClickCheckbox.isSelected()) {
                    // simulate a right mouse click
                    robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                }
                // delay before the next click
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public synchronized void stopClicker() {
        startButton.setDisable(false);
        stopButton.setDisable(true);
        leftClickCheckbox.setDisable(false);
        rightClickCheckbox.setDisable(false);
        clickRateSlider.setDisable(false);
        running = false;
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent event) {

    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent event) {
        if (event.getKeyCode() == NativeKeyEvent.VC_F6 && !startButton.isDisabled()) {
            startClicker();
        } else if (event.getKeyCode() == NativeKeyEvent.VC_F7 && !stopButton.isDisabled()) {
            stopClicker();
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent event) {

    }
}
