package com.finalproject.game.views;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.finalproject.game.FinalProjectGame;
import com.finalproject.game.components.GameButton;
import com.finalproject.game.models.CharacterItem;
import com.finalproject.game.models.FontGenerator;
import com.finalproject.game.models.Item;

public class InventoryScreen implements Screen {

    private FinalProjectGame game;
    private OrthographicCamera camera;
    private Stage stage;
    private Screen gameScreen;

    private BitmapFont currentFont;

    private ArrayList<CharacterItem> inventory;
    private HorizontalGroup inventoryGroup;

    // constructor
    public InventoryScreen(FinalProjectGame game, Screen gameScreen, ArrayList<CharacterItem> inventory) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.inventory = inventory;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        stage = new Stage(new ScreenViewport());
       

        // for font
        FontGenerator fontGenerator = new FontGenerator("fonts/PixelGameFont.ttf");
        currentFont = fontGenerator.generate(25, Color.WHITE, 0.3f, Color.WHITE);
        fontGenerator.dispose();
    }

    // add the back ground image for the inventory screen
    private void addBackground() {
        Texture background = new Texture(Gdx.files.internal("backgrounds/inventoryBackground.png"));
        Image backgroundImg = new Image(background);
        backgroundImg.setScaling(Scaling.stretch);
        backgroundImg.setFillParent(true);
        stage.addActor(backgroundImg);
    }

    // helper method to create a button
    private TextButton createButton(String text, ClickListener listener) {
        TextButton button = GameButton.createButton(text, game.font);
        button.addListener(listener);
        return button;
    }

    @Override
    public void show() {
         Gdx.input.setInputProcessor(stage);
        // add the background
        addBackground();
        // add the title
        Label.LabelStyle titleFont = new Label.LabelStyle(currentFont, Color.WHITE);
        titleFont.font.getData().setScale(2.0f);
        Label title = new Label("Inventory", titleFont);
        title.setPosition(100, stage.getHeight() - 100);
        stage.addActor(title);
        // add the back to game button
        TextButton backToGame = createButton("Back", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gameScreen instanceof GameScreen) {
                    // Cast the Screen to a GameScreen
                    GameScreen gameScreenInstance = (GameScreen) gameScreen;
                    gameScreenInstance.isPaused = false;
                }
                game.setScreen(gameScreen);
                dispose();
            }
        });
        backToGame.setPosition(stage.getWidth() - 300, stage.getHeight() - 100);
        stage.addActor(backToGame);
        // Load Inventory (Maybe fetch from database)

       

        // Display Inventory
        inventoryGroup = new HorizontalGroup();

        updateInventoryTable(inventory);

        ScrollPane scrollPane = new ScrollPane(inventoryGroup);
        scrollPane.setBounds(100, stage.getHeight() * 0.2f, stage.getWidth() - 50, stage.getHeight() * 0.7f);

        stage.addActor(scrollPane);
    }

    private void updateInventoryTable(ArrayList<CharacterItem> inventory) {
        Label.LabelStyle itemFont = new Label.LabelStyle(currentFont, Color.WHITE);
        itemFont.font.getData().setScale(1.0f);
        for (CharacterItem item : inventory) {
            if(item.getCount() == 0) continue;
            Item currentItem = item.getItem();
            Table itemTable = new Table();
            itemTable.setSize(1000, 1000);
            Texture itemTexture = currentItem.getIcon();
            Image itemImage = new Image(itemTexture);
            itemImage.setScaling(Scaling.fit);
            itemTable.center().add(itemImage).size(210, 210);
            itemTable.row();
            itemTable.add(new Label(currentItem.getName(), itemFont));
            itemTable.row();
            itemTable.add(new Label("x" + item.getCount(), itemFont));
            // Add a background to the table
            Texture backgroundTexture = new Texture("backgrounds/cardNormal.png");
            itemTable.setBackground(new TextureRegionDrawable(new TextureRegion(backgroundTexture)));
            itemTable.pad(10);
            Container<Table> itemContainer = new Container<>(itemTable);
            itemContainer.pad(50);
            inventoryGroup.addActor(itemContainer);

            // discription
            final Table itemDescriptionTable = new Table();
            itemDescriptionTable.setPosition(0.5f * stage.getWidth() - 400, 0.2f * stage.getHeight());
            itemDescriptionTable.setSize(700, 100);
            final Label itemDescriptionLabel = new Label(currentItem.getDescription(),
                    new Label.LabelStyle(currentFont, Color.WHITE));
            itemDescriptionLabel.setWrap(true);
            itemDescriptionTable.add(itemDescriptionLabel).width(600);
            itemDescriptionTable.setBackground(
                    new TextureRegionDrawable(new TextureRegion(new Texture("backgrounds/battleBottomTab.png"))));
            itemDescriptionTable.setVisible(false);
            stage.addActor(itemDescriptionTable);

            // Add a listener to the table
            itemContainer.addListener(new InputListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    // Show the label when the mouse enters the table
                    itemDescriptionTable.setVisible(true);
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    // Hide the label when the mouse leaves the table
                    itemDescriptionTable.setVisible(false);
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
    public void hide() {
        game.font.getData().setScale(1.0f);
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.update();
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
