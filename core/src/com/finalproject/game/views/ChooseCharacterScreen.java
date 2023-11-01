
package com.finalproject.game.views;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.finalproject.game.FinalProjectGame;
import com.finalproject.game.components.GameButton;
import com.finalproject.game.models.GameCharacter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ChooseCharacterScreen implements Screen {
    private FinalProjectGame game;
    private OrthographicCamera camera;
    private Stage stage;
    private Texture characterTexture;
    private int selectedCharacter;
    private Table characterTable;

    public ChooseCharacterScreen(FinalProjectGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void show() {
        Label.LabelStyle biggerFont = new Label.LabelStyle(game.font, Color.WHITE);
        biggerFont.font.getData().setScale(2.0f); // Example to make the font larger
        Label title = new Label("Choose your characters: ", biggerFont);
        title.setPosition(100, stage.getHeight() - 100); // Adjust the position
        ArrayList<GameCharacter> characterList = new ArrayList<>();
        // int health, int attack, int defence, int speed, int level, String name,
        String characterCardPath = "charactors/xiaochuan.png";
        characterList.add(new GameCharacter(100, 10, 10, 10, 1, "Mikado Sun", characterCardPath));
        characterList.add(new GameCharacter(80, 6, 12, 20, 1, "Relx Ding", "charactors/dingzhen.png"));
        characterList.add(new GameCharacter(80, 6, 12, 20, 1, "Mark Zucks", "charactors/mark.png"));
        characterList.add(new GameCharacter(88,10,10,10,1,"Medicine", "charactors/medicine.png"));
        characterList.add(new GameCharacter());
        characterList.add(new GameCharacter());
        characterList.add(new GameCharacter());

        characterTable = new Table(); // No fixed height

        addCharacters(characterList);

        // Then add this characterTable to your scrollPane
        ScrollPane scrollPane = new ScrollPane(characterTable);
        scrollPane.setBounds(25, stage.getHeight() * 0.2f, stage.getWidth() - 50, stage.getHeight() * 0.6f);

        TextButton backButton = GameButton.createButton("Back", game.font);
        backButton.setPosition(50, 50); // Set the position where you want the button to appear
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new WelcomeScreen(game));
            }
        });

        TextButton startButton = GameButton.createButton("Start", game.font);
        startButton.setPosition(200, 50); // You had 'backButton' here, change it to 'startButton'

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });
        stage.addActor(title);
        stage.addActor(scrollPane);
        stage.addActor(backButton);
        stage.addActor(startButton);

    }

    public void addCharacters(ArrayList<GameCharacter> characterList) {
        for (GameCharacter character : characterList) {
            // Create a table for each character's card
            Table characterCard = new Table();
            // Create an Image for the character
            Texture texture = character.getImageTexture();
            Image characterImage = new Image(texture);
            characterImage.setScaling(Scaling.fit);

            // Create Labels for the character's stats or description
            Label.LabelStyle normalFont = new Label.LabelStyle(game.font, Color.WHITE);
            normalFont.font.getData().setScale(1.3f);
            Label nameLabel = new Label(character.getName(), normalFont);
            Label healthLabel = new Label("Hp: " + character.getHealth(), normalFont);
            Label attackLabel = new Label("Attack: " + character.getAttack(), normalFont);
            Label defenceLabel = new Label("Defence: " + character.getDefence(), normalFont);
            // ... more labels

            // Add the Image and Labels to the characterCard
            characterCard.add(characterImage).height(250).expand().fill();

            characterCard.row(); // Move to next row
            characterCard.add(nameLabel).pad(2);
            characterCard.row();
            characterCard.add(healthLabel).pad(2);
            characterCard.row();
            characterCard.add(attackLabel).pad(2);
            characterCard.row();
            characterCard.add(defenceLabel).pad(2);

            // Add the characterCard to the main characterTable
            characterTable.add(characterCard).width(stage.getWidth() * 0.2f).pad(40).expand();

        }
    }

    @Override
    public void hide() {
        game.font.getData().setScale(1.0f);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        // Add any other rendering here
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
