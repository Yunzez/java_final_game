package com.finalproject.game.views;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.finalproject.game.FinalProjectGame;
import com.finalproject.game.components.GameButton;

/**
 * show() Method: This method is called when the screen becomes the current
 * screen for a Game. It's usually used for setting up the stage, loading
 * assets, or initializing variables. It's called only once unless you set the
 * screen again.
 */

public class WelcomeScreen implements Screen {
    private FinalProjectGame game;
    OrthographicCamera camera;
    Stage stage;

    public WelcomeScreen(FinalProjectGame finalProjectGame) {
        this.game = finalProjectGame;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        // Initialize your assets and layout here

        // Create buttons using GameButton class
        String[] buttonLabels = { "Start", "Settings", "Quit" };
        float y = 300; // Initial y-coordinate
        for (String label : buttonLabels) {
            final String finalLabel = label; // Create a final local variable
            TextButton button = GameButton.createButton(label, game.font);
            button.setPosition(camera.viewportWidth / 2 - button.getWidth() / 2, y);
            y -= 80; // Move down for the next button
            button.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("Clicked: " + finalLabel);
                    if ("Start".equals(finalLabel)) {
                        game.setScreen(new GameScreen(game));
                        dispose();
                    }

                    if ("Quit".equals(finalLabel)) {
                        Gdx.app.exit();
                    }

                    if("Settings".equals(finalLabel)){
                        game.setScreen(new SettingScreen(game));
                        dispose();
                    }
                }
            });
            stage.addActor(button);
        }

    }

    @Override
    public void render(float delta) {

        // Clear the screen
        ScreenUtils.clear(0, 0, 0.2f, 1);

        // Update the camera and set the projection matrix
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        // Begin the batch
        game.batch.begin();

        // Draw your text here
        GlyphLayout layout = new GlyphLayout();
        layout.setText(game.font, "Welcome to The world of dingzhen");

        float objectWidth = camera.viewportWidth;
        float objectHeight = camera.viewportHeight;

        float centerX = (objectWidth - layout.width) / 2;
        float textY = (objectHeight + layout.height) / 2 + (objectHeight * 0.1f);

        game.font.draw(game.batch, layout, centerX, textY);

        game.batch.end();

        // Update and draw the stage
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void pause() {
        // Pause your screen here
    }

    @Override
    public void resume() {
        // Resume your screen here
    }

    @Override
    public void hide() {
        // Hide your screen here
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.update();
        stage.getViewport().update(width, height, true);
    }

    // ... (implement other Screen methods but leave them empty for now)
}
