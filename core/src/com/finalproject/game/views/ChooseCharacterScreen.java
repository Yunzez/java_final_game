
package com.finalproject.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.finalproject.game.FinalProjectGame;
import com.finalproject.game.components.GameButton;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ChooseCharacterScreen implements Screen {
    private FinalProjectGame game;
    private OrthographicCamera camera;
    private Stage stage;
    private Texture characterTexture;
    private int selectedCharacter;

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
        title.setPosition(100, 1000); // Adjust the position

        Table characterTable = new Table();
        characterTable.add(new Label("Character 1", biggerFont)).pad(20);
        characterTable.add(new Label("Character 2", biggerFont)).pad(20);
        characterTable.add(new Label("Character 3", biggerFont)).pad(20);
        // add more

        ScrollPane scrollPane = new ScrollPane(characterTable);
        scrollPane.setBounds(50, 700, 1820, 100); // adjust as needed

        TextButton backButton = GameButton.createButton("Back", game.font);
        backButton.setPosition(50, 50); // Set the position where you want the button to appear
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new WelcomeScreen(game));
            }
        });

        TextButton startButton = GameButton.createButton("Start", game.font);
        startButton.setPosition(150, 50); // You had 'backButton' here, change it to 'startButton'

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
