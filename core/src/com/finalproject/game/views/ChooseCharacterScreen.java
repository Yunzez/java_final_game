
package com.finalproject.game.views;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.finalproject.game.FinalProjectGame;
import com.finalproject.game.components.GameButton;
import com.finalproject.game.models.FontGenerator;
import com.finalproject.game.models.GameCharacter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ChooseCharacterScreen implements Screen {
    private FinalProjectGame game;
    private OrthographicCamera camera;
    private Stage stage;
    private Texture characterTexture;

    private Table characterTable;

    private Table selectedCharacterCard = null;
    private GameCharacter selectedCharacter = null;
    private Texture cardtexture;
    private Table characterDetailsTable;

    private Texture cardSelectedTexture;

    private ArrayList<GameCharacter> characterList;

    private BitmapFont currentFont;
    private BitmapFont describeFont;

    public ChooseCharacterScreen(FinalProjectGame game) {
        this.game = game;
        characterList = game.getCharacterList();
        cardtexture = new Texture(Gdx.files.internal("backgrounds/cardNormal.png"));
        cardSelectedTexture = new Texture(Gdx.files.internal("backgrounds/cardSelected.png"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        stage = new Stage(new ScreenViewport(camera));

        FontGenerator fontGenerator = new FontGenerator("fonts/PixelGameFont.ttf");
        currentFont = fontGenerator.generate(25, Color.WHITE, 0.3f, Color.WHITE);
        fontGenerator.dispose();

        FontGenerator fontGenerator2 = new FontGenerator("fonts/VCR_OSD_MONO_1.001.ttf");
        describeFont = fontGenerator2.generate(20, Color.WHITE, 0, Color.WHITE);
        fontGenerator2.dispose();
        // stage.setDebugAll(true); // This will enable debug lines around all actors in
        // the stage

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        addBackground();
        Label.LabelStyle biggerFont = new Label.LabelStyle(currentFont, Color.WHITE);
        biggerFont.font.getData().setScale(2.0f); // Example to make the font larger
        Label title = new Label("Choose your characters: ", biggerFont);
        title.setPosition(100, stage.getHeight() - 100); // Adjust the position

        characterTable = new Table(); // No fixed height

        addCharacters(characterList);

        // Then add this characterTable to your scrollPane
        ScrollPane scrollPane = new ScrollPane(characterTable);
        scrollPane.setBounds(25, stage.getHeight() * 0.3f, stage.getWidth() - 50, stage.getHeight() * 0.5f);
        scrollPane.toBack();
        addButtons();
        addCharacterDetailsSection();

        stage.addActor(title);
        stage.addActor(scrollPane);

    }

    private void addBackground() {
        Texture overallBackground = new Texture(Gdx.files.internal("backgrounds/chooseCharacterBackground.png"));
        Image backgroundImage = new Image(overallBackground);
        backgroundImage.setScaling(Scaling.stretch);
        backgroundImage.setFillParent(true); // This will make the background image fill the stage

        // Add the background image to the stage
        stage.addActor(backgroundImage);
    }

    private void addCharacterDetailsSection() {
        characterDetailsTable = new Table();
        characterDetailsTable.bottom().left(); // Align to bottom left
        characterDetailsTable.setWidth(stage.getWidth()); // Take up the whole bottom
        characterDetailsTable.setHeight(stage.getHeight() * 0.16f); // Set the height

        // Set background
        Texture bgtexture = new Texture(Gdx.files.internal("backgrounds/battleBottomTab.png"));
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(bgtexture));
        characterDetailsTable.setBackground(drawable);

        // Add to stage
        stage.addActor(characterDetailsTable);
    }

    private void updateCharacterDetails(GameCharacter character) {
        characterDetailsTable.clear(); // Clear previous details

        Label.LabelStyle normalFont = new Label.LabelStyle(describeFont, Color.WHITE);

        normalFont.font.getData().setScale(1.1f);

        // Left section: Story
        Label storyLabel = new Label(character.getDescription(), normalFont);
        storyLabel.setWrap(true);
        storyLabel.setAlignment(Align.left);

        // Middle section: Stats
        Table statsTable = new Table();
        Label hpLabel = new Label("HP: " + character.getMaxHealth(), normalFont);
        Label attackLabel = new Label("Attack: " + character.getAttack(), normalFont);
        Label defenseLabel = new Label("Defense: " + character.getDefense(), normalFont);
        Label speedLabel = new Label("Defense: " + character.getSpeed(), normalFont);

        statsTable.add(hpLabel).row();
        statsTable.add(attackLabel).row();
        statsTable.add(defenseLabel).row();
        statsTable.add(speedLabel).row();

        // Add to table
        float widgetHeight = characterDetailsTable.getHeight();
        characterDetailsTable.add(storyLabel).width((Gdx.graphics.getWidth() * 0.7f)).height(widgetHeight).pad(10)
                .padLeft(5).top().left();
        characterDetailsTable.add(statsTable).height(widgetHeight).expandX().pad(10);

    }

    private void addButtons() {
        TextButton backButton = createButton("Back", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new WelcomeScreen(game));
            }
        });
        backButton.setPosition(100, stage.getHeight() * 0.17f);

        TextButton startButton = GameButton.createButton("Start", game.font);
        startButton.setPosition(200, 50); // You had 'backButton' here, change it to 'startButton'

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedCharacter.assignRandomAttacks(3);
                if (!game.showTutorial) {
                    game.setScreen(new BaseScreen(game, selectedCharacter));
                } else {
                    game.setScreen(new TutorialScreen(game, selectedCharacter));
                }

                dispose();
            }
        });
        startButton.setPosition(250, stage.getHeight() * 0.17f);

        TextButton loadFromSavingButton = GameButton.createButton("Load From Saving", game.font);
        loadFromSavingButton.setSize(300, 60);
        loadFromSavingButton.setPosition(stage.getWidth() * 0.1f, stage.getHeight() * 0.8f);
        loadFromSavingButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LoadFromPreviousSavingScreen(game));
                dispose();
            }
        });
        loadFromSavingButton.toFront();
        backButton.toFront();
        startButton.toFront();
        stage.addActor(loadFromSavingButton);
        stage.addActor(backButton);
        stage.addActor(startButton);
    }

    private TextButton createButton(String text, ClickListener listener) {
        TextButton button = GameButton.createButton(text, game.font);
        button.addListener(listener);
        return button;
    }

    public void addCharacters(ArrayList<GameCharacter> characterList) {
        for (GameCharacter character : characterList) {
            final GameCharacter finalCharacter = character;
            // Create a table for each character's card
            Table characterCard = new Table();
            // Create an Image for the character
            Texture texture = character.getImageTexture();
            Image characterImage = new Image(texture);
            characterImage.setScaling(Scaling.fit);

            // Create Labels for the character's stats or description
            Label.LabelStyle normalFont = new Label.LabelStyle(currentFont, Color.WHITE);
            normalFont.font.getData().setScale(1.3f);
            Label nameLabel = new Label(character.getName(), normalFont);
            // ... more labels

            // Load the texture
            Texture bgtexture = new Texture(Gdx.files.internal("backgrounds/cardNormal.png"));
            TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(bgtexture));
            characterCard.setBackground(drawable);
            // Add the Image and Labels to the characterCard
            characterCard.add(characterImage).height(250).fill();
            characterCard.row(); // Move to next row
            characterCard.add(nameLabel);
            characterCard.setTouchable(Touchable.enabled);

            final Table finalCharacterCard = characterCard;

            // Add the characterCard to the main characterTable
            characterTable.add(finalCharacterCard).width(stage.getWidth() * 0.2f).pad(40).expand();

            finalCharacterCard.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (selectedCharacterCard != null) {
                        deselectCharacter(selectedCharacterCard); // Reset the background of the previously selected
                                                                  // card
                    }
                    updateCharacterDetails(finalCharacter);
                    // Select the new character card
                    selectedCharacter = finalCharacter;
                    highlightSelectedCharacter(finalCharacterCard);

                    // Update the reference to the currently selected character card
                    selectedCharacterCard = finalCharacterCard;
                }
            });

        }
    }

    private void highlightSelectedCharacter(Table characterCard) {
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(cardSelectedTexture));
        characterCard.setBackground(drawable);
    }

    private void deselectCharacter(Table characterCard) {
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(cardtexture));
        characterCard.setBackground(drawable); // Reset the background to deselect
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        // Also, dispose of any resources specific to this screen
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
        camera.setToOrtho(false, width, height);
        camera.update();
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // Implement if needed
    }

    @Override
    public void resume() {
        // Implement if needed
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
