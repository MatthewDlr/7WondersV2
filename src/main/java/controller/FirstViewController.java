package controller;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class FirstViewController implements Initializable {

    @FXML
    private MediaView fxmlIntroVideoFrame, fxmlVideoEffectFrame;

    @FXML
    private ImageView MainTitleFrame, singlePlayerButton, multiPlayerButton, singlePlayerSetupFrame;
    private MediaPlayer introVideo, backgroundVideoMedia, accessDeniedSoundEffect, mainThemeAudio;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        SetupAllComponents();

        introVideo.play();
        mainThemeAudio.play();
        mainThemeAudio.setVolume(0.5);

        introVideo.setOnEndOfMedia(() -> {
            fxmlVideoEffectFrame.setMediaPlayer(backgroundVideoMedia);
            backgroundVideoMedia.play();
            introVideo.stop();
            fxmlIntroVideoFrame.setVisible(false);

            ShowComponentsWithTransition();

        });
    }

    private void SetupAllComponents() {
        File introVideoFile = new File("src/main/resources/videos/7WondersIntro.mp4");
        File titleVideoFile = new File("src/main/resources/videos/MainScreen.mp4");
        File musicThemeFile = new File("src/main/resources/musics/7Wonders - Main Theme.mp3");
        File accessdeniedSoundFile = new File("src/main/resources/musics/AccessDenied.mp3");

        Media introVideoMedia = new Media(introVideoFile.toURI().toString());
        Media titleVideoMedia = new Media(titleVideoFile.toURI().toString());
        Media musicThemeMedia = new Media(musicThemeFile.toURI().toString());
        Media accessdeniedSoundMedia = new Media(accessdeniedSoundFile.toURI().toString());

        introVideo = new MediaPlayer(introVideoMedia);
        backgroundVideoMedia = new MediaPlayer(titleVideoMedia);
        mainThemeAudio = new MediaPlayer(musicThemeMedia);
        accessDeniedSoundEffect = new MediaPlayer(accessdeniedSoundMedia);

        fxmlIntroVideoFrame.setMediaPlayer(introVideo);
        backgroundVideoMedia.setAutoPlay(true);
        backgroundVideoMedia.setCycleCount(MediaPlayer.INDEFINITE);
        mainThemeAudio.setAutoPlay(true);

        SetButtonScale(singlePlayerButton);
        SetButtonScale(multiPlayerButton);
    }

    private void SetButtonScale(ImageView button) {
        button.setScaleX(1.3);
        button.setScaleY(1.3);
    }


    private void ShowComponentsWithTransition() {
        FadeTransition fadeTransition1 = new FadeTransition(Duration.millis(750), MainTitleFrame);
        fadeTransition1.setFromValue(0);
        fadeTransition1.setToValue(1);
        fadeTransition1.setInterpolator(Interpolator.EASE_OUT);
        fadeTransition1.play();

        fadeTransition1.setOnFinished((event -> {
            FadeTransition fadeTransition2 = new FadeTransition(Duration.millis(250), singlePlayerButton);
            fadeTransition2.setFromValue(0);
            fadeTransition2.setToValue(1);
            fadeTransition2.setInterpolator(Interpolator.EASE_OUT);

            FadeTransition fadeTransition3 = new FadeTransition(Duration.millis(250), multiPlayerButton);
            fadeTransition3.setFromValue(0);
            fadeTransition3.setToValue(1);
            fadeTransition3.setInterpolator(Interpolator.EASE_OUT);

            ParallelTransition parallelTransition = new ParallelTransition(fadeTransition2, fadeTransition3);
            parallelTransition.play();
        }));
    }

    @FXML
    protected void MultiPlayerButtonClicked() {
        PlayAccessDeniedSoundEffect();
        RotateTransition(multiPlayerButton, 500, 2, 10, true);
    }

    private void PlayAccessDeniedSoundEffect() {
        accessDeniedSoundEffect.play();
        accessDeniedSoundEffect.setOnEndOfMedia(() -> {
            accessDeniedSoundEffect.stop();
            accessDeniedSoundEffect.seek(Duration.ZERO);
        });
    }

    private void RotateTransition(ImageView imageView, int duration, int cycleCount, int byAngle, boolean autoReverse) {
        imageView.setRotate(0);
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(duration), imageView);
        rotateTransition.setByAngle(byAngle);
        rotateTransition.setCycleCount(cycleCount);
        rotateTransition.setAutoReverse(autoReverse);
        rotateTransition.setInterpolator(Interpolator.EASE_BOTH);
        rotateTransition.play();
    }

    @FXML
    protected void MouseIsOverSinglePayerButton() {
        SetButtonScale(singlePlayerButton);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), singlePlayerButton);
        scaleTransition.setInterpolator(Interpolator.EASE_OUT);
        scaleTransition.setByX(0.15);
        scaleTransition.setByY(0.15);
        scaleTransition.play();
    }

    @FXML
    protected void MouseLeaveSinglePayerButton() {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), singlePlayerButton);
        scaleTransition.setInterpolator(Interpolator.EASE_OUT);
        scaleTransition.setByX(-0.15);
        scaleTransition.setByY(-0.15);
        scaleTransition.play();
    }

    @FXML
    public void SinglePlayerButtonClicked() {

        singlePlayerSetupFrame.setVisible(true);

        ParallelTransitionWithTranslateAndFade(singlePlayerSetupFrame, 1000, -160, 0, 1, 1250);

    }

    private void ParallelTransitionWithTranslateAndFade(ImageView imageView, double translateFromY, double translateToY, double fadeFromValue, double fadeToValue, int duration) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(duration), imageView);
        translateTransition.setFromY(translateFromY);
        translateTransition.setToY(translateToY);
        translateTransition.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(duration), imageView);
        fadeTransition.setFromValue(fadeFromValue);
        fadeTransition.setToValue(fadeToValue);
        fadeTransition.setInterpolator(Interpolator.EASE_OUT);

        ParallelTransition parallelTransition = new ParallelTransition(translateTransition, fadeTransition);
        parallelTransition.play();
        parallelTransition.setOnFinished(event -> {
            try {
                SwitchToGameConfigScene();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void SwitchToGameConfigScene() throws IOException {
        Parent singlePlayerSetup = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/controller/LocalGameSetup.fxml")));
        Scene scene = fxmlVideoEffectFrame.getScene();
        scene.setRoot(singlePlayerSetup);
    }
}