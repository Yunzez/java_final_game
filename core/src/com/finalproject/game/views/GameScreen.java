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
import com.badlogic.gdx.math.MathUtils;
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

import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.finalproject.game.FinalProjectGame;
import com.finalproject.game.components.GameButton;
import com.finalproject.game.components.MapGenerator;
import com.finalproject.game.models.GameCharacter;
import com.finalproject.game.models.TilePoint;
import com.finalproject.game.models.GameCharacter;

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
    private GameCharacter currentCharacter;

    private float baseSizeFactor = Gdx.graphics.getHeight() * 0.09f;

    private float charactorDesiredHeight = baseSizeFactor * 0.8f; // 10% of the screen height
    private float charactorDesiredWidth = 0; // 10% of the screen width
    private int mapWidthInTiles = 43;
    private int mapHeightInTiles = 33;
    private MapGenerator mapGenerator;

    private TilePoint exitLocation;

    public GameScreen(FinalProjectGame game, GameCharacter currentCharacter) {
        this.currentCharacter = currentCharacter;
        characterX = 0;
        characterY = 0;
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        characterTexture = currentCharacter.getImageTexture();
        float aspectRatio = (float) this.characterTexture.getWidth() / (float) this.characterTexture.getHeight();
        charactorDesiredWidth = charactorDesiredHeight * aspectRatio; // width to maintain aspect ratio

        statusBarBackground = new Texture(Gdx.files.internal("backgrounds/statusbarbg.png"));

        settingsStage = new Stage(new ScreenViewport());
        // Generate map, baseSizeFactor is the size of each tile
        mapGenerator = new MapGenerator(mapWidthInTiles, mapHeightInTiles, (int) baseSizeFactor);
        mapGenerator.generateMap();
        TilePoint entranceLocation = mapGenerator.getEntranceLocation();
        characterX = entranceLocation.x * baseSizeFactor;
        characterY = entranceLocation.y * baseSizeFactor;
        exitLocation = mapGenerator.getExitLocation();
    }

    @Override
    public void show() {
        // Create labels
        Label.LabelStyle gameFont = new Label.LabelStyle(game.font, Color.WHITE);
        healthLabel = new Label("Health: " + health, gameFont);
        attackLabel = new Label("Attack: " + attack, gameFont);
        defenseLabel = new Label("Defense: " + defense, gameFont);

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
        backToMainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isSettingsOpen = false;
                game.setScreen(new WelcomeScreen(game));
                dispose();
            }
        });

        TextButton resumeButton = GameButton.createButton("Resume", game.font);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isSettingsOpen = false;
                isPaused = false;
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
        // Adjust camera to follow character, while clamping to map bounds
        camera.position.set(
                MathUtils.clamp(characterX, camera.viewportWidth / 2,
                        mapWidthInTiles * baseSizeFactor - camera.viewportWidth / 2),
                MathUtils.clamp(characterY, camera.viewportHeight / 2,
                        mapHeightInTiles * baseSizeFactor - camera.viewportHeight / 2),
                0);
        camera.update();
        if (isSettingsOpen) {
            settingsStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
            settingsStage.draw();
        } else {
            handleMoveCharactor(delta);
            camera.update();
            game.batch.setProjectionMatrix(camera.combined);
            game.batch.begin();
            // draw map
            mapGenerator.renderMap(game.batch);

            game.batch.draw(characterTexture, characterX, characterY, charactorDesiredWidth, charactorDesiredHeight);

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

        // Convert pixel coordinates to tile coordinates safely
        int tileX = (int) (potentialX / baseSizeFactor);
        int tileY = (int) (potentialY / baseSizeFactor);

        // Ensure that the tile coordinates stay within the array bounds
        tileX = MathUtils.clamp(tileX, 0, mapWidthInTiles - 1);
        tileY = MathUtils.clamp(tileY, 0, mapHeightInTiles - 1);

        // Map boundaries
        float mapWidthPixels = mapWidthInTiles * baseSizeFactor;
        float mapHeightPixels = mapHeightInTiles * baseSizeFactor;

        float exitX = exitLocation.x * baseSizeFactor;
        float exitY = exitLocation.y * baseSizeFactor;

        if (Math.abs(characterX - exitX) < 5 && Math.abs(characterY - exitY) < 5) {
            System.out.println("Exit reached, loading new map");

            // Dispose of the current GameScreen properly
            this.dispose();

            // Create a new GameScreen with a new character or the current character
            // Ensure that the constructor of GameScreen creates a new map
            game.setScreen(new GameScreen(game, currentCharacter));
            return;
            // No need to call dispose() here, as setScreen() should handle the old screen
        }

        // Convert potential coordinates to tile coordinates for wall checking
        int leftTile = (int) ((potentialX));
        int rightTile = (int) ((potentialX + charactorDesiredWidth));
        int topTile = (int) ((potentialY + charactorDesiredHeight));
        int bottomTile = (int) ((potentialY));

        // Check if any of the four corners of the character are inside a wall
        boolean topLeftIsWall = mapGenerator.isWall(leftTile, topTile);
        boolean topRightIsWall = mapGenerator.isWall(rightTile, topTile);
        boolean bottomLeftIsWall = mapGenerator.isWall(leftTile, bottomTile);
        boolean bottomRightIsWall = mapGenerator.isWall(rightTile, bottomTile);

        // Allow movement if none of the corners are colliding with a wall
        if (!topLeftIsWall && !topRightIsWall && !bottomLeftIsWall && !bottomRightIsWall) {
            // Additional check for boundaries
            if (potentialX >= 0 && potentialX <= mapWidthPixels - charactorDesiredWidth &&
                    potentialY >= 0 && potentialY <= mapHeightPixels - charactorDesiredHeight) {
                characterX = potentialX;
                characterY = potentialY;
            }
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

    // @Override
    // public void resize(int width, int height) {
    // camera.setToOrtho(false, width, height);
    // camera.update();
    // stage.getViewport().update(width, height, true);
    // // Your code to update any other necessary components/layouts
    // }

    // ... (implement other Screen methods but leave them empty for now)
}
