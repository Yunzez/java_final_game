package com.finalproject.game.views;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.finalproject.game.FinalProjectGame;
import com.finalproject.game.components.GameButton;
import com.finalproject.game.models.FontGenerator;
import com.finalproject.game.models.GameCharacter;

public class TutorialScreen implements Screen {

    private Stage stage;
    private FinalProjectGame game;
    private GameCharacter character;
    private OrthographicCamera camera;
    private BitmapFont font;
    private ArrayList<Image> imageList = new ArrayList<Image>();

    private ArrayList<String> imagesPath = new ArrayList<String>();

    private int imgNum = 0;
    private TextButton skipButton;
    private TextButton nextButton;
    private TextButton previousButton;

    public TutorialScreen(FinalProjectGame game, GameCharacter character) {
        this.character = character;
        this.game = game;
        this.stage = new Stage();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        stage = new Stage(new ScreenViewport(camera));

        Texture backgroundImage = new Texture(Gdx.files.internal("backgrounds/tutorial_bg.png"));
        TextureRegionDrawable bgdrawable = new TextureRegionDrawable(new TextureRegion(backgroundImage));
        Color backgroundColor = new Color(1, 1, 1, 0.5f);
        Drawable tintedDrawable = bgdrawable.tint(backgroundColor);
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.setBackground(tintedDrawable);
        stage.addActor(mainTable);

        addTutorialImages();
        FontGenerator fontGenerator = new FontGenerator("fonts/PixelGameFont.ttf");
        font = fontGenerator.generate(25, Color.WHITE, 0.3f, Color.WHITE);
        fontGenerator.dispose();
    }

    public void addTutorialImages() {
        float screenHeight = Gdx.graphics.getHeight();
        float desiredHeight = screenHeight * 0.8f; // 80% of screen height

        for (int i = 0; i < 6; i++) {
            Texture texture = new Texture(Gdx.files.internal("tutorial/" + (i + 1) + ".png"));
            Image tutorialImage = new Image(texture);

            float originalWidth = texture.getWidth();
            float originalHeight = texture.getHeight();
            float aspectRatio = originalWidth / originalHeight;

            // Calculate the new width while maintaining the aspect ratio
            float newWidth = desiredHeight * aspectRatio;

            // Set the size of the image
            tutorialImage.setSize(newWidth, desiredHeight);

            // Set the origin to the center of the image for scaling
            tutorialImage.setPosition(Gdx.graphics.getWidth() * 0.1f, screenHeight * 0.15f);
            ;

            tutorialImage.setVisible(false); // Set the image to be invisible initially
            imageList.add(tutorialImage);
            stage.addActor(tutorialImage); // Add image to the stage

        }
    }

    @Override
    public void show() {
        skipButton = GameButton.createButton("  Shut up Meg! (Skip)  ", font);
        imageList.get(0).setVisible(true);
        skipButton.setPosition(100, 100);
        skipButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new BaseScreen(game, character));
                game.showTutorial = false;
            }
        });

        nextButton = GameButton.createButton("Next", font);
        nextButton.setPosition(600, 100);
        nextButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                imgNum++;
            }
        });

        TextButton previousButton = GameButton.createButton("Previous", font);
        previousButton.setPosition(400, 100);
        previousButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (imgNum > 0) {
                    imgNum--;
                } else {
                    imgNum = 0;
                }
            }
        });

        Gdx.input.setInputProcessor(stage);
        stage.addActor(skipButton);
        stage.addActor(nextButton);
        stage.addActor(previousButton);

    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1); // Set clear color (black in this case)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen

        // Update the camera
        camera.update();

        // Draw the stage

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

        stage.draw();
        handleTutorialImage();

    }

    private void handleTutorialImage() {

        if (imgNum > 0 && imgNum < imageList.size()) {
            System.out.println(imgNum);
            imageList.get(imgNum).setVisible(true);
            imageList.get(imgNum - 1).setVisible(false);
            if (imgNum < imageList.size() - 1) {
                imageList.get(imgNum + 1).setVisible(false);
            }
        }
        if (imgNum == imageList.size() - 1) {
            nextButton.setText("Start!");
        }

        if (imgNum == imageList.size()) {
            game.setScreen(new BaseScreen(game, character));
            game.showTutorial = false;
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

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
