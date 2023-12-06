package com.finalproject.game.views;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.finalproject.game.FinalProjectGame;
import com.finalproject.game.components.GameButton;
import com.finalproject.game.models.FontGenerator;

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
    private Texture background;
    private BitmapFont headerFont;
    private Skin skin;

    public WelcomeScreen(FinalProjectGame finalProjectGame) {
        this.game = finalProjectGame;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        stage = new Stage(new ScreenViewport());
        FontGenerator fontGenerator = new FontGenerator("fonts/Crang.ttf");
        Color color = Color.valueOf("EB5E28FF");

        this.headerFont = fontGenerator.generate(25, Color.WHITE, 2f, new Color(color));
        fontGenerator.dispose();

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        String[] buttonLabels = { "Start", "Settings", "Quit" };
        background = new Texture(Gdx.files.internal("backgrounds/welcomBackground.png"));

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
                        game.setScreen(new ChooseCharacterScreen(game));
                        dispose();
                    }

                    if ("Quit".equals(finalLabel)) {
                        Gdx.app.exit();
                    }

                    if ("Settings".equals(finalLabel)) {
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

        // Update the camera and set the projection matrix
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        // Begin the batch
        game.batch.begin();

        // Clear the screen
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the background
        game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw your text here

        Label titleLabel = new Label("Welcome to The Meme Village!", new Label.LabelStyle(headerFont, Color.WHITE));

        titleLabel.setFontScale(1.2f);

        float objectWidth = camera.viewportWidth;
        float objectHeight = camera.viewportHeight;

        float centerX = (objectWidth - titleLabel.getWidth()) / 2;
        float textY = (objectHeight + titleLabel.getHeight()) / 2 + (objectHeight * 0.1f);

        titleLabel.setPosition(centerX, textY);
        stage.addActor(titleLabel);
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
