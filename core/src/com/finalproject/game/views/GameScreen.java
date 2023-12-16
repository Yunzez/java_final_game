package com.finalproject.game.views;

import com.badlogic.gdx.math.Rectangle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import java.util.ArrayList;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.finalproject.game.FinalProjectGame;
import com.finalproject.game.components.GameButton;
import com.finalproject.game.components.MapGenerator;
import com.finalproject.game.models.GameCharacter;
import com.finalproject.game.models.TilePoint;
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

public class GameScreen implements Screen {
    private FinalProjectGame game;
    OrthographicCamera camera;
    private Random random = new Random();
    Stage stage;
    private Texture characterTexture;
    float characterX, characterY; // character position
    float speedX = 0.15f; // speed as a percentage of screen width
    float speedY = 0.2f; // speed as a percentage of screen height

    // ! game setting:
    private int maxGameLevel;
    private int currentGameLevel;
    // New variables for health, attack, and defense

    Label healthLabel;
    Label levelLabel;
    Label monsterKilledLabel;
    Label pointsLabel;
    Label gameLevelLabel;

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
    private float monsterDesiredWidth = 0;
    private int mapWidthInTiles = 43;
    private int mapHeightInTiles = 23;
    private MapGenerator mapGenerator;

    private TilePoint exitLocation;

    private HashMap<GameCharacter, TilePoint> gameMonsters = new HashMap<>();
    private ArrayList<TilePoint> monstersLocations;

    private String battleResult;
    private ArrayList<GameCharacter> charactersList = new ArrayList<>();
    private GameCharacter currentMonster;

    public GameScreen(FinalProjectGame game, GameCharacter currentCharacter, int maxGameLevel, int currentGameLevel) {
        this.currentCharacter = currentCharacter;
        characterX = 0;
        characterY = 0;
        this.game = game;

        this.maxGameLevel = maxGameLevel;
        this.currentGameLevel = currentGameLevel;
        charactersList = game.getCharacterList();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        stage = new Stage(new ScreenViewport());

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
        gameMonsters = new HashMap<>();
        createGameMonsters();
    }

    @Override
    public void show() {
        // Create labels
        Gdx.input.setInputProcessor(stage);
        Label.LabelStyle gameFont = new Label.LabelStyle(game.font, Color.DARK_GRAY);

        healthLabel = new Label(
                "Health: " + currentCharacter.getCurrentHealth() + "/" + currentCharacter.getMaxHealth(), gameFont);
        levelLabel = new Label("Level: " + currentCharacter.getLevel(), gameFont);
        monsterKilledLabel = new Label("Monsters killed: " + currentCharacter.getMonsterKilled(), gameFont);
        pointsLabel = new Label("Points: " + currentCharacter.getPoints(), gameFont);
        gameLevelLabel = new Label("Game Level: " + currentGameLevel + "/" + maxGameLevel, gameFont);
        // Create and add the Inventory button
        TextButton inventoryButton = GameButton.createButton("  Inventory  ", game.font);
        inventoryButton.setSize(240, 50);
        inventoryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isPaused = true;
                // Open inventory here
                game.setScreen(new InventoryScreen(game, GameScreen.this, currentCharacter.getInventory()));
            }
        });

        TextButton pauseButton = GameButton.createButton("Pause", game.font);
        pauseButton.setSize(240, 50);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isPaused = true;
            }
        });

        TextButton settingsButton = GameButton.createButton("Settings", game.font);
        settingsButton.setSize(240, 50);
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
        statusBarTable.add(healthLabel).pad(12);
        statusBarTable.add(levelLabel).pad(12);
        statusBarTable.add(monsterKilledLabel).pad(12);
        statusBarTable.add(pointsLabel).pad(12);
        statusBarTable.add(gameLevelLabel).pad(12);

        statusBarTable.setBounds(0, Gdx.graphics.getHeight() - 90, Gdx.graphics.getWidth(), 90);

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
            renderMonsters(game.batch);
            game.batch.end();

            stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
            stage.draw();
        }
    }

    public void setBattleResult(String result) {
        System.out.println("Battle result: " + result);
        this.battleResult = result;

        // Remove the monster from the game after the battle
        if (this.battleResult != null) {
            gameMonsters.remove(currentMonster);
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

        if (Math.abs(characterX - exitX) < this.charactorDesiredHeight / 2
                && Math.abs(characterY - exitY) < this.charactorDesiredHeight / 2) {
            System.out.println("Exit reached, loading new map");

            // Dispose of the current GameScreen properly
            this.dispose();

            // Create a new GameScreen with a new character or the current character
            // Ensure that the constructor of GameScreen creates a new map
            if (this.currentGameLevel == this.maxGameLevel) {
                game.setScreen(new BaseScreen(game, currentCharacter));
                return;
            } else {
                game.setScreen(new GameScreen(game, currentCharacter, this.maxGameLevel, this.currentGameLevel + 1));
            }

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
                currentMonster = isCollidingWithMonster(potentialX, potentialY);
                // Check for collision with monsters before actually moving
                if (currentMonster == null) {
                    // No collision with monster, update character position
                    characterX = potentialX;
                    characterY = potentialY;
                } else {
                    // Handle collision with monster
                    triggerBattle(currentMonster);
                }

            }
        }
    }

    private GameCharacter isCollidingWithMonster(float x, float y) {
        Rectangle characterBounds = new Rectangle(x, y, charactorDesiredWidth, charactorDesiredHeight);

        for (Entry<GameCharacter, TilePoint> entry : gameMonsters.entrySet()) {

            TilePoint location = entry.getValue();
            float monsterX = location.x * baseSizeFactor;
            float monsterY = location.y * baseSizeFactor;
            Rectangle monsterBounds = new Rectangle(monsterX,
                    monsterY, monsterDesiredWidth, charactorDesiredHeight);
            if (characterBounds.overlaps(monsterBounds)) {
                return entry.getKey(); // Return the monster if there's a collision
            }
        }
        return null; // Return null if no collision
    }

    private void triggerBattle(GameCharacter currentMonster) {
        System.out.println("battle triggered");
        if (currentMonster != null) {
            // Retrieve the monster's location for reporting or further processing
            TilePoint monsterLocation = gameMonsters.get(currentMonster);
            System.out.println("Battle triggered with monster at location: " + monsterLocation);

            game.getScreen().hide();

            // Increase the monster's health by a random amount if the game level is above 1
            if (currentGameLevel > 1) {
                int healthIncrease = (currentGameLevel - 1) * (currentMonster.getMaxHealth() + random.nextInt(50) - 25);
                currentMonster.setHealth(currentMonster.getMaxHealth() + healthIncrease);
                currentMonster.setMaxHealth(currentMonster.getMaxHealth() + healthIncrease);
            }

            game.setScreen(new BattleScreen(game, currentCharacter, currentMonster, this, baseSizeFactor));

        } else {
            System.out.println("No monster to battle.");
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.update();
        stage.getViewport().update(width, height, true);
    }

    public void createGameMonsters() {
        int numberOfMonsters = random.nextInt(5) + 6;
        monstersLocations = mapGenerator.returnAccessibleLocations(numberOfMonsters);

        for (int i = 0; i < numberOfMonsters; i++) {
            int index = random.nextInt(charactersList.size() - 1);
            GameCharacter monster = charactersList.get(index);
            while (monster.getName().equals(currentCharacter.getName())) {
                index = random.nextInt(charactersList.size() - 1);
                monster = charactersList.get(index);
            }
            TilePoint location = monstersLocations.get(i);
            gameMonsters.put(monster.clone(), location);
        }
    }

    public void renderMonsters(SpriteBatch batch) {
        for (Entry<GameCharacter, TilePoint> entry : gameMonsters.entrySet()) {
            GameCharacter monster = entry.getKey();
            TilePoint location = entry.getValue();

            // Assuming that the characterCard in GameCharacter is the texture you want to
            // draw
            Texture monsterTexture = monster.getImageTexture();
            int drawX = (int) (location.x * baseSizeFactor); // Calculate the actual x position in pixels
            int drawY = (int) (location.y * baseSizeFactor); // Calculate the actual y position in pixels

            // Draw the monster texture at its location
            float aspectRatio = (float) monsterTexture.getWidth() / (float) monsterTexture.getHeight();
            monsterDesiredWidth = charactorDesiredHeight * aspectRatio; // width to maintain aspect ratio
            batch.draw(monsterTexture, drawX, drawY, monsterDesiredWidth, charactorDesiredHeight);
        }
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
        System.out.println("Disposing GameScreen");
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
