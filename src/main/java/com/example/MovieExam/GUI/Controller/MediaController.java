package com.example.MovieExam.GUI.Controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

public class MediaController {

    @FXML
    private MediaView mediaView;

    @FXML
    private Button btnPlayPause;

    @FXML
    private Slider sldVolume;

    @FXML
    private Slider sldProgress;

    @FXML
    private Label lblCurrentTime;

    private MediaPlayer mediaPlayer;
    private Stage stage;

    @FXML
    public void initialize() {
        // Set up volume control
        sldVolume.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(newValue.doubleValue() / 100.0);
            }
        });
    }

    public void playVideo(String fileLink) {
        try {
            // Stop any currently playing video
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }

            File file = new File(fileLink);

            if (!file.exists()) {
                System.out.println("File does not exist: " + fileLink);
                return;
            }

            // Create media and player
            Media media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            // Set volume from slider
            mediaPlayer.setVolume(sldVolume.getValue() / 100.0);

            // Update time label and progress slider as video plays
            mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                updateProgress();
            });

            // Enable progress slider when media is ready
            mediaPlayer.setOnReady(() -> {
                sldProgress.setDisable(false);
                Duration total = mediaPlayer.getTotalDuration();
                sldProgress.setMax(total.toSeconds());
            });

            // Allow seeking with progress slider
            sldProgress.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (sldProgress.isValueChanging() && mediaPlayer != null) {
                    mediaPlayer.seek(Duration.seconds(newValue.doubleValue()));
                }
            });

            // Auto play
            mediaPlayer.play();
            btnPlayPause.setText("⏸");

            System.out.println("Playing: " + fileLink);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error playing video: " + e.getMessage());
        }
    }

    private void updateProgress() {
        if (mediaPlayer != null && !sldProgress.isValueChanging()) {
            Duration currentTime = mediaPlayer.getCurrentTime();

            // Update progress slider
            sldProgress.setValue(currentTime.toSeconds());

            // Update time label
            lblCurrentTime.setText(formatTime(currentTime));
        }
    }

    private String formatTime(Duration duration) {
        int minutes = (int) duration.toMinutes();
        int seconds = (int) duration.toSeconds() % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    @FXML
    public void btnPrevious(ActionEvent actionEvent) {
        // Logic to play previous video from your list
        System.out.println("Previous button clicked");
    }

    @FXML
    public void btnPlay(ActionEvent actionEvent) {
        if (mediaPlayer != null) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
                btnPlayPause.setText("▶");
            } else {
                mediaPlayer.play();
                btnPlayPause.setText("⏸");
            }
        }
    }

    @FXML
    public void btnSkip(ActionEvent actionEvent) {
        // Logic to play next video from list
        System.out.println("Skip button clicked");
    }

    @FXML
    public void btnStop(ActionEvent actionEvent) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            btnPlayPause.setText("▶");
            lblCurrentTime.setText("0:00");
            sldProgress.setValue(0);
        }
    }

    // Method to set the stage for resizing
    public void setStageForResize(Stage stage) {
        this.stage = stage;
    }
}