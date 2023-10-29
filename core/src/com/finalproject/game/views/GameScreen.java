package com.finalproject.game.views;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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

public class GameScreen implements Screen {
    private FinalProjectGame game;
    OrthographicCamera camera;
    Stage stage;
    private Texture characterTexture;
    float characterX, characterY; // character position
    float speedX = 0.12f; // speed as a percentage of screen width
    float speedY = 0.15f; // speed as a percentage of screen height
    

    public GameScreen(FinalProjectGame game) {
        characterX = 0;
        characterY = 0;
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        characterTexture = new Texture(Gdx.files.internal("charactors/xiaochuan.png"));
    }

    @Override
    public void show() {
        // Initialize your game assets and layout here
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
        // Draw your game here
        // Clear the screen
        ScreenUtils.clear(0, 0, 0, 0);


        handleMoveCharactor(delta);
        // Update the camera and set the projection matrix
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        // Begin the batch
        game.batch.begin();

        game.batch.draw(characterTexture, characterX, characterY);

        game.batch.end();

        // Update and draw the stage
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }
    public void handleMoveCharactor(float delta) {
        float potentialX = characterX;
        float potentialY = characterY;
    
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            potentialX -= speedX * Gdx.graphics.getWidth() * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            potentialX += speedX * Gdx.graphics.getWidth() * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            potentialY += speedY * Gdx.graphics.getHeight() * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            potentialY -= speedY * Gdx.graphics.getHeight() * delta;
        }
    
        // Boundary checks
        if (potentialX >= 0 && potentialX <= Gdx.graphics.getWidth() - characterTexture.getWidth()) {
            characterX = potentialX;
        }
        if (potentialY >= 0 && potentialY <= Gdx.graphics.getHeight() - characterTexture.getHeight()) {
            characterY = potentialY;
        }
    }
    

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.update();
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // Pause your game here
    }

    @Override
    public void resume() {
        // Resume your game here
    }

    @Override
    public void hide() {
        // Hide your game here
    }

    @Override
    public void dispose() {
        // Dispose your assets here
    }

    // ... (implement other Screen methods but leave them empty for now)
}
