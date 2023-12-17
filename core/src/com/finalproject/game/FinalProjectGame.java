package com.finalproject.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.finalproject.game.models.FontGenerator;
import com.finalproject.game.models.GameCharacter;
import com.finalproject.game.views.WelcomeScreen;
import com.badlogic.gdx.audio.Music;

public class FinalProjectGame extends Game {
	public SpriteBatch batch;
	public Skin skin;
	Texture img;
	public BitmapFont font;
	Music gameMusic;
	public Boolean showTutorial = true;
	private boolean musicEnabled = true;
	private ArrayList<GameCharacter> characterList = new ArrayList<GameCharacter>();

	@Override
	public void create() {
		// skin = new Skin(Gdx.files.internal("uiskin.json"));
		skin = new Skin();
		batch = new SpriteBatch();
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("bgm.mp3")); // Replace with your music file path
		gameMusic.setLooping(true); // Music will loop continuously
		// gameMusic.play();
		loadCharactersFromJson();
		FontGenerator fontGenerator = new FontGenerator("fonts/Retro_Gaming.ttf");
		this.font = fontGenerator.generate(18, Color.WHITE, 0.2f, Color.DARK_GRAY);
		fontGenerator.dispose();
		this.setScreen(new WelcomeScreen(this));

	}

	public void refreshCharacterList() {
		characterList.clear();
		loadCharactersFromJson();
	}



	private void loadCharactersFromJson() {
		JsonReader jsonReader = new JsonReader();
		JsonValue base = jsonReader.parse(Gdx.files.internal("document/characters.json"));

		for (JsonValue characterValue : base.iterator()) {
			int health = characterValue.getInt("health");
			int strength = characterValue.getInt("strength");
			int defense = characterValue.getInt("defense");
			int speed = characterValue.getInt("speed");
			int level = characterValue.getInt("level");
			int monsterKilled = 0;
			String name = characterValue.getString("name");
			String imagePath = characterValue.getString("imagePath");
			String lostImagePath = characterValue.getString("lostImagePath");
			String description = characterValue.getString("description");
			GameCharacter character = new GameCharacter(health, strength, defense, speed, level, monsterKilled, name,
					imagePath, lostImagePath);
			character.setDescription(description);
			characterList.add(character);
		}

		// Now characterList contains all characters loaded from JSON
	}

	public ArrayList<GameCharacter> getCharacterList() {
		return characterList;
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

	public void toggleTutorial(boolean showTutorial) {
		this.showTutorial = showTutorial;
	}

	public boolean isTutorialEnabled() {
		return showTutorial;
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		batch.dispose();
		gameMusic.dispose();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
}
