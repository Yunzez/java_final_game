package com.finalproject.game.views;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.finalproject.game.FinalProjectGame;
import com.finalproject.game.components.GameButton;
import com.finalproject.game.models.FontGenerator;
import com.finalproject.game.models.GameCharacter;
import com.finalproject.game.models.UserGameCharacters;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class LoadFromPreviousSavingScreen implements Screen {
    private FinalProjectGame game;
    OrthographicCamera camera;
    Stage stage;
    private Texture background;
    private ArrayList<UserGameCharacters> savedCharacters = new ArrayList<UserGameCharacters>();
    private Label label;
    private BitmapFont describeFont;
    private Label.LabelStyle labelStyle;
    private Texture backgroundTexture;
    private Image backgroundImage;
    private Table selectedSavingTable;
    private UserGameCharacters selectedSaveCharacter;
    private GameCharacter currentCharacter;
    private TextureRegionDrawable savingBg;
    private Table parentTable;

    public LoadFromPreviousSavingScreen(FinalProjectGame finalProjectGame, GameCharacter character) {
        this.game = finalProjectGame;
        this.currentCharacter = character;
        init();

    }

    public LoadFromPreviousSavingScreen(FinalProjectGame finalProjectGame) {
        this.game = finalProjectGame;
        init();

    }

    public void init() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        stage = new Stage(new ScreenViewport());

        // First, initialize the font
        FontGenerator fontGenerator2 = new FontGenerator("fonts/VCR_OSD_MONO_1.001.ttf");
        describeFont = fontGenerator2.generate(20, Color.WHITE, 0, Color.WHITE);
        fontGenerator2.dispose();
        // Make sure the font is not null
        if (describeFont == null) {
            throw new IllegalStateException("Font not loaded");
        }

        // Then, create the label style with the initialized font
        labelStyle = new Label.LabelStyle(describeFont, Color.WHITE);

        backgroundTexture = new Texture(Gdx.files.internal("backgrounds/character_choosing_bg.png"));

        // Create an Image actor with this texture
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setScaling(Scaling.stretch); // Set the image to stretch
        backgroundImage.setFillParent(true); // Make the image fill the parent (stage)

        // Add the background image to the stage as the first actor
        stage.addActor(backgroundImage);

        TextButton backButton = GameButton.createButton("Back", game.font);
        backButton.setPosition(100, 100);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("TestButton", "Button clicked");
                if (currentCharacter == null) {
                    game.setScreen(new ChooseCharacterScreen(game));
                } else {
                    game.setScreen(new BaseScreen(game, currentCharacter));
                }

            }
        });

        stage.addActor(backButton);
        TextButton startButton = GameButton.createButton("Start", game.font);
        startButton.setPosition(300, 100);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (selectedSaveCharacter != null) {
                    Gdx.app.log("TestButton", "Button clicked");
                    game.setScreen(new BaseScreen(game, selectedSaveCharacter));
                }
                Gdx.app.log("TestButton", "Button clicked");
            }
        });
        startButton.toFront();
        stage.addActor(startButton);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        // Finally, load the saved game data
        loadSavedGame();
        displaySavedCharacters();
        if (currentCharacter != null) {
            addSaveButton();
        }

    }

    private void addSaveButton() {
        TextButton saveButton = GameButton.createButton("Save This Game", game.font);
        saveButton.setPosition(500, 100);
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                Gdx.app.log("TestButton", "Button clicked");
                showSaveDialog();
            }
        });
        saveButton.toFront();
        stage.addActor(saveButton);
    }

    private void showSaveDialog() {
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        final Dialog dialog = new Dialog("Save Game", skin);

        final Label messageLabel = new Label("Enter the name for your save:", skin);
        if (selectedSaveCharacter != null) {
            messageLabel.setText("Enter the a name for your save, this will cover the previous save named:  "
                    + selectedSaveCharacter.getSavingName());
        }

        dialog.getContentTable().add(messageLabel).padTop(10);

        // Add a text field for input
        final TextField saveNameField = new TextField("", skin);
        dialog.getContentTable().row(); // Ensure the TextField is on a new line
        dialog.getContentTable().add(saveNameField).padTop(10);

        // Create "Okay" button
        TextButton okayButton = new TextButton("Okay", skin);
        okayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
                System.out.println("Okay button clicked");
                loadSavedGame();
                displaySavedCharacters();
            }
        });
        dialog.setSize(600, 300); // Set the size of the dialog

        // Create and add a "Save" button
        TextButton saveButton = new TextButton("Save", skin);
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String saveName = saveNameField.getText();

                if (checkSaveNameValid(saveName)) {
                    saveGame(saveName);
                    dialog.getContentTable().clear();
                    messageLabel.setText("Success!");
                    dialog.getContentTable().add(messageLabel).padTop(10);

                    dialog.getButtonTable().clear(); // Clear the button table
                    dialog.getButtonTable().add(okayButton).padTop(10); // Add the "Okay" button
                } else {
                    messageLabel
                            .setText("There is already a saving named: " + saveName + ", please choose another name");
                    dialog.setSize(600, 300);
                }
                // Clear the content table and show the success message

            }
        });

        dialog.getButtonTable().add(saveButton).padTop(10);

        // Show the dialog
        dialog.show(stage);
    }

    private boolean checkSaveNameValid(String saveName) {
        for (UserGameCharacters character : savedCharacters) {
            System.out.println(character.getSavingName() + "   " + saveName);
            if (character.getSavingName().trim().equals(saveName.trim())) {
                return false;
            }
        }
        return true;
    }

    private void saveGame(String saveName) {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);

        // Exclude complex fields from serialization
        json.setSerializer(UserGameCharacters.class, new Json.Serializer<UserGameCharacters>() {
            @Override
            public void write(Json json, UserGameCharacters character, Class knownType) {

                json.writeObjectStart();
                json.writeValue("savingName", character.getSavingName());
                json.writeValue("id", character.getId());
                json.writeValue("health", character.getCurrentHealth());
                json.writeValue("maxHealth", character.getMaxHealth());
                json.writeValue("attack", character.getAttack());
                json.writeValue("defense", character.getDefense());
                json.writeValue("speed", character.getSpeed());
                json.writeValue("level", character.getLevel());
                json.writeValue("name", character.getName());
                json.writeValue("imagePath", character.getImagePath());
                json.writeValue("monsterKilled", character.getMonsterKilled());
                json.writeValue("points", character.getPoints());
                // ... other primitive fields
                json.writeObjectEnd();
            }

            @Override
            public UserGameCharacters read(Json json, JsonValue jsonData, Class type) {
                // Implement if needed for deserialization
                return null;
            }
        });

        UserGameCharacters newCharacter = new UserGameCharacters(
                currentCharacter.getCurrentHealth(),
                currentCharacter.getAttack(),
                currentCharacter.getDefense(),
                currentCharacter.getSpeed(),
                currentCharacter.getLevel(),
                currentCharacter.getName(),
                currentCharacter.getImagePath(),
                0, // monsterKilled
                0, // points
                saveName);
        savedCharacters.add(newCharacter);

        // Serialize the list of characters
        String jsonString = json.toJson(savedCharacters);

        // Write to file
        try {
            FileWriter writer = new FileWriter(Gdx.files.local("assets/document/savedCharacters.json").file());
            writer.write(jsonString);
            writer.close();
            Gdx.app.log("SaveGame", "Game saved as: " + saveName);

        } catch (IOException e) {
            Gdx.app.error("SaveGame", "Error saving game: ", e);
        }
    }

    private void loadSavedGame() {
        System.out.println("Loading saved game");
        savedCharacters.clear();
        JsonReader jsonReader = new JsonReader();
        JsonValue base;

        try {
            base = jsonReader.parse(Gdx.files.internal("document/savedCharacters.json"));
        } catch (Exception e) {
            base = null; // File not found or error in reading
        }

        if (base == null || base.size == 0) { // Check if the file is empty or invalid
            System.out.println("No saved characters found");
            label = new Label("No saved characters found ", labelStyle);
            stage.addActor(label);
        } else {
            // Iterate through the array of characters in the JSON file
            for (JsonValue characterJson : base) {
                int health = characterJson.getInt("health");
                int attack = characterJson.getInt("attack");
                int defense = characterJson.getInt("defense");
                int speed = characterJson.getInt("speed");
                int level = characterJson.getInt("level");
                String name = characterJson.getString("name");
                String imagePath = characterJson.getString("imagePath");
                String savingName = characterJson.getString("savingName");
                int monsterKilled = characterJson.getInt("monsterKilled"); // if needed
                int points = characterJson.getInt("points"); // if needed

                // Create an instance of UserGameCharacters
                UserGameCharacters character = new UserGameCharacters(health, attack, defense, speed, level, name,
                        imagePath, monsterKilled, points, savingName);

                savedCharacters.add(character);
            }
        }
    }

    private void setCharacterTableBg(Table characterTable, int isReset) {
        // Create a non-transparent drawable
        switch (isReset) {
            case 0:
                // ! not reset
                selectedSavingTable = characterTable;
                savingBg.setMinWidth(characterTable.getWidth());
                savingBg.setMinHeight(characterTable.getHeight());
                characterTable.setBackground(savingBg);
                break;

            case 1:
                // reset
                if (selectedSavingTable != null) {
                    savingBg.setMinWidth(characterTable.getWidth());
                    savingBg.setMinHeight(characterTable.getHeight());

                    Color backgroundColor = new Color(1, 1, 1, 0.5f); // Transparency set to 0.1f

                    // Apply the tint and get the new drawable
                    Drawable tintedDrawable = savingBg.tint(backgroundColor);

                    characterTable.setBackground(tintedDrawable);
                }
                break;
            default:
                break;
        }

        // Set the non-transparent drawable as the background

    }

    private void displaySavedCharacters() {
        float padding = 30f;
        float tableWidth = Gdx.graphics.getWidth() * 0.7f;
        float tableHeight = Gdx.graphics.getHeight() * 0.13f;
        if (parentTable != null) {
            parentTable.clear();
        }

        parentTable = new Table();

        // ! add scroll panel first so it stays bottom
        ScrollPane scrollPane = new ScrollPane(parentTable);
        scrollPane.setSize(stage.getWidth() * 0.8f, stage.getHeight() * 0.75f);
        scrollPane.setPosition(stage.getWidth() * 0.1f, stage.getHeight() * 0.17f);
        scrollPane.setScrollingDisabled(true, false); // Disable horizontal scrolling
        scrollPane.toBack();
        scrollPane.setDebug(true);
        stage.addActor(scrollPane);

        parentTable.setFillParent(true);
        for (UserGameCharacters character : savedCharacters) {

            Table characterTable = new Table();
            savingBg = new TextureRegionDrawable(
                    new TextureRegion(new Texture("backgrounds/battleBottomTab.png")));
            savingBg.setMinWidth(tableWidth);
            savingBg.setMinHeight(tableHeight);

            Color backgroundColor = new Color(1, 1, 1, 0.5f); // Transparency set to 0.1f

            // Apply the tint and get the new drawable
            Drawable tintedDrawable = savingBg.tint(backgroundColor);

            characterTable.setBackground(tintedDrawable);
            characterTable.setSize(tableWidth, tableHeight);
            characterTable.pad(padding);
            characterTable.setTouchable(Touchable.enabled);

            Label nameLabel = new Label("Name: " + character.getName(), labelStyle);
            Label levelLabel = new Label("Level: " + character.getLevel(), labelStyle);
            Label pointsLabel = new Label("Points: " + character.getPoints(), labelStyle);
            Label savingNameLabel = new Label("Saving: " + character.getSavingName(), labelStyle);

            characterTable.add(nameLabel).row();
            characterTable.add(levelLabel).row();
            characterTable.add(pointsLabel).row();
            characterTable.add(savingNameLabel);

            // Add the character table to the parent table
            parentTable.add(characterTable).width(tableWidth).height(tableHeight).padBottom(padding).row();

            // Add Click Listener

            final Table characterTableFinal = characterTable;
            final UserGameCharacters characterFinal = character;
            characterTable.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("Table Click", "Table clicked: ");
                    selectedSaveCharacter = characterFinal;
                    setCharacterTableBg(selectedSavingTable, 1);
                    setCharacterTableBg(characterTableFinal, 0);
                    // Additional logic for when a character is selected
                }
            });

        }

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        // Add any other rendering here
        // Clear the screen
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the background
        game.batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Resize your UI components based on the new width and height
    }

    @Override
    public void pause() {
        // Pause your game (e.g. background music)
    }

    @Override
    public void resume() {
        // Resume your game (e.g. background music)
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        stage.dispose();

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    // Implement other necessary methods (render, resize, pause, resume, hide,
    // dispose)
}
