package com.finalproject.game.views;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class GameScreen implements Screen {
    private Game game;

    public GameScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Initialize your game assets and layout here
    }

    @Override
    public void render(float delta) {
        // Draw your game here
    }

    @Override
    public void resize(int width, int height) {
        // Resize your game here
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
