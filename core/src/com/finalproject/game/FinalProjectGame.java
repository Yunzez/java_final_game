package com.finalproject.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.finalproject.game.models.FontGenerator;
import com.finalproject.game.views.WelcomeScreen;
import com.badlogic.gdx.audio.Music;

public class FinalProjectGame extends Game {
	public SpriteBatch batch;
	public Skin skin;
	Texture img;
	public BitmapFont font;
	Music gameMusic;
	private boolean musicEnabled = true;


	@Override
	public void create () {
		// skin = new Skin(Gdx.files.internal("uiskin.json"));
		skin = new Skin();
		batch = new SpriteBatch();
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("bgm.mp3")); // Replace with your music file path
        gameMusic.setLooping(true); // Music will loop continuously
        gameMusic.play();

		FontGenerator fontGenerator = new FontGenerator("fonts/Retro_Gaming.ttf");
        this.font = fontGenerator.generate(18, Color.WHITE, 0.2f, Color.DARK_GRAY);
        fontGenerator.dispose();
		this.setScreen(new WelcomeScreen(this));

	}

	public boolean isMusicEnabled() {
        return musicEnabled;
    }

	public void toggleMusic(boolean enable) {
        musicEnabled = enable;
        if (gameMusic != null) {
            if (enable) {
                gameMusic.play();
            } else {
                gameMusic.pause();
            }
        }
    }

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		gameMusic.dispose();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
}
