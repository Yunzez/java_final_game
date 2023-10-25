package com.finalproject.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.finalproject.game.views.WelcomeScreen;

public class FinalProjectGame extends Game {
	public SpriteBatch batch;
	public Skin skin;
	Texture img;
	public BitmapFont font;


	@Override
	public void create () {
		// skin = new Skin(Gdx.files.internal("uiskin.json"));
		skin = new Skin();
		batch = new SpriteBatch();
		font = new BitmapFont();
		this.setScreen(new WelcomeScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
}
