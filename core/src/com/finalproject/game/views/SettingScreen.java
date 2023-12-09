package com.finalproject.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.finalproject.game.FinalProjectGame;
import com.finalproject.game.components.GameButton;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SettingScreen implements Screen {
    private FinalProjectGame game;
    private OrthographicCamera camera;
    private Stage stage;

    public SettingScreen(FinalProjectGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load or create a Skin for the checkbox
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json")); // Adjust the path to your skin file

        // Create a checkbox for music toggle
        final CheckBox musicCheckbox = new CheckBox(" Music", skin);
        // Center the checkbox
        float checkBoxWidth = musicCheckbox.getWidth();
        float checkBoxHeight = musicCheckbox.getHeight();
        float centerX = (stage.getWidth() - checkBoxWidth) / 2;
        float centerY = (stage.getHeight() - checkBoxHeight) / 2 + 50;
        musicCheckbox.setPosition(centerX, centerY);

        musicCheckbox.setChecked(game.isMusicEnabled()); // Set initial state based on game setting

        musicCheckbox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean isMusicOn = musicCheckbox.isChecked();
                game.toggleMusic(isMusicOn);
            }
        });

         // Create a checkbox for music toggle
        final CheckBox tutorialCheckbox = new CheckBox(" show tutorial", skin);
        // Center the checkbox

        tutorialCheckbox.setPosition(centerX, centerY-50);

        tutorialCheckbox.setChecked(game.isTutorialEnabled()); // Set initial state based on game setting

         tutorialCheckbox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean isTutOn = tutorialCheckbox.isChecked();
                game.toggleTutorial(isTutOn);
            }
        });

        stage.addActor(musicCheckbox);
        stage.addActor(tutorialCheckbox);
    }

    @Override
    public void show() {
        TextButton backButton = GameButton.createButton("Back", game.font);
        backButton.setPosition(50, 50); // Set the position where you want the button to appear
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new WelcomeScreen(game));
            }
        });
        stage.addActor(backButton);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        // Add any other rendering here
        game.batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.update();
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // Implement if needed
    }

    @Override
    public void resume() {
        // Implement if needed
    }

    @Override
    public void hide() {
        // Implement if needed
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
