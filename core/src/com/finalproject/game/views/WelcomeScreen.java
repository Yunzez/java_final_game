package com.finalproject.game.views;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.finalproject.game.FinalProjectGame;

public class WelcomeScreen implements Screen {
    private FinalProjectGame game;
    OrthographicCamera camera;

    public WelcomeScreen(FinalProjectGame finalProjectGame) {
        this.game = finalProjectGame;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void show() {
        // Initialize your assets and layout here
    }

    @Override
    public void render(float delta) {
        // Draw your screen here
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Welcome to Drop!!! ", 100, 150);
        game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
        // To switch to the GameScreen
        game.setScreen(new GameScreen(game));
    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here
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
        // Dispose your assets here
    }

    // ... (implement other Screen methods but leave them empty for now)
}
