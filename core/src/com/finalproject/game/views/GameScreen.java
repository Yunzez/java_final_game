package com.finalproject.game.views;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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

    // New variables for health, attack, and defense
    int health = 100;
    int attack = 50;
    int defense = 40;

    Label healthLabel;
    Label attackLabel;
    Label defenseLabel;

    boolean isPaused = false;

    private Texture statusBarBackground;

    final float menuHeight = 80;
    final float gameWidth = Gdx.graphics.getWidth();
    final float gameHeight = Gdx.graphics.getHeight() - menuHeight;

    private Stage settingsStage;
    private boolean isSettingsOpen = false;

    public GameScreen(FinalProjectGame game) {
        characterX = 0;
        characterY = 0;
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        characterTexture = new Texture(Gdx.files.internal("charactors/xiaochuan.png"));
        statusBarBackground = new Texture(Gdx.files.internal("backgrounds/statusbarbg.png"));

        settingsStage = new Stage(new ScreenViewport());

    }

    @Override
    public void show() {
        // Create labels
        healthLabel = new Label("Health: " + health, new Label.LabelStyle(game.font, Color.WHITE));
        attackLabel = new Label("Attack: " + attack, new Label.LabelStyle(game.font, Color.WHITE));
        defenseLabel = new Label("Defense: " + defense, new Label.LabelStyle(game.font, Color.WHITE));

        // Create and add the Inventory button
        TextButton inventoryButton = GameButton.createButton("Inventory", game.font);
        inventoryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isPaused = true;
                // Open inventory here
            }
        });

        TextButton pauseButton = GameButton.createButton("Pause", game.font);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isPaused = true;
            }
        });

        TextButton settingsButton = GameButton.createButton("Settings", game.font);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Settings button clicked"); // Debugging line
                isSettingsOpen = !isSettingsOpen;
                if (isSettingsOpen) {
                    Gdx.input.setInputProcessor(settingsStage);
                } else {
                    Gdx.input.setInputProcessor(stage);
                }
            }
        });

        Table statusBarTable = new Table();
        statusBarTable.top().left();
        statusBarTable.setBackground(new Image(statusBarBackground).getDrawable());
        statusBarTable.add(healthLabel).pad(11);
        statusBarTable.add(attackLabel).pad(11);
        statusBarTable.add(defenseLabel).pad(11);

        statusBarTable.setBounds(0, Gdx.graphics.getHeight() - 80, Gdx.graphics.getWidth(), 80);

        Table buttonTable = new Table();
        buttonTable.add(inventoryButton).pad(11);
        buttonTable.add(settingsButton).pad(11);
        statusBarTable.add(buttonTable).expandX().right();

        Table settingsTable = new Table();

        settingsTable.setFillParent(true); // Makes table take up the whole stage
        settingsTable.center();

        TextButton quitButton = GameButton.createButton("Quit game", game.font);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        TextButton backToMainButton = GameButton.createButton("Main menu", game.font);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new WelcomeScreen(game));
            }
        });

        TextButton resumeButton = GameButton.createButton("Resume", game.font);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isSettingsOpen = false;
                Gdx.input.setInputProcessor(stage);
            }
        });

        settingsTable.add(quitButton).pad(10).row();
        settingsTable.add(resumeButton).pad(10).row();
        settingsTable.add(backToMainButton).pad(10);
        settingsStage.addActor(settingsTable);

        stage.addActor(statusBarTable);

    }

    @Override
    public void render(float delta) {
        if (isPaused) {
            return;
        }

        ScreenUtils.clear(0, 0, 0, 0);

        if (isSettingsOpen) {
            settingsStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
            settingsStage.draw();
        } else {
            handleMoveCharactor(delta);
            camera.update();
            game.batch.setProjectionMatrix(camera.combined);
            game.batch.begin();
            game.batch.draw(characterTexture, characterX, characterY);
            game.batch.end();

            stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
            stage.draw();
        }
    }

    public void handleMoveCharactor(float delta) {
        float potentialX = characterX;
        float potentialY = characterY;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            potentialX -= speedX * gameWidth * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            potentialX += speedX * gameWidth * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            potentialY += speedY * gameHeight * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            potentialY -= speedY * gameHeight * delta;
        }

        // Boundary checks
        if (potentialX >= 0 && potentialX <= gameWidth - characterTexture.getWidth()) {
            characterX = potentialX;
        }
        if (potentialY >= 0 && potentialY <= gameHeight - characterTexture.getHeight()) {
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
