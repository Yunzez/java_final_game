package com.finalproject.game.views;

import com.badlogic.gdx.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.finalproject.game.FinalProjectGame;
import com.finalproject.game.components.GameButton;
import com.finalproject.game.models.Attack;
import com.finalproject.game.models.FontGenerator;
import com.finalproject.game.models.GameCharacter;

import java.util.ArrayList;
import java.util.Random;

public class BattleScreen implements Screen {

    private FinalProjectGame game;
    private OrthographicCamera camera;
    private Stage stage;
    private GameCharacter playerCharacter;
    private GameCharacter monster;
    private GameScreen mapScreen;
    private float baseSizeFactor;
    private Texture battleBottomTabTexture;
    private BitmapFont battleFont;
    private BitmapFont describeFont;

    private Label activityLabel;
    private Label testHPUser;
    private Label testHPMonster;
    private Table hpBarUser;
    private Table hpBarMonster;
    private Image hpFillPlayer;
    private Image hpFillMonster;

    private float bottomHeightRatio = 0.25f;
    private float topHeightRatio = 0.25f;
    private float middleHeightRatio = 1 - bottomHeightRatio - topHeightRatio - 0.05f;

    private int currentTurn; // 0 for player, 1 for monster
    private Label waitingLabel;

    public BattleScreen(FinalProjectGame game, GameCharacter playerCharacter, GameCharacter monster,
            GameScreen mapScreen, float baseSizeFactor) {
        this.game = game;
        this.playerCharacter = playerCharacter;
        this.monster = monster;
        this.monster.assignRandomAttacks(3);
        this.mapScreen = mapScreen;
        this.baseSizeFactor = baseSizeFactor;
        if (playerCharacter.getSpeed() >= monster.getSpeed()) {
            this.currentTurn = 0;
        } else {
            this.currentTurn = 1;
        }
        // Create an instance of your font generator
        FontGenerator fontGenerator = new FontGenerator("fonts/PixelGameFont.ttf");
        battleFont = fontGenerator.generate(25, Color.WHITE, 0.5f, Color.WHITE);
        fontGenerator.dispose();

        FontGenerator fontGenerator2 = new FontGenerator("fonts/VCR_OSD_MONO_1.001.ttf");
        describeFont = fontGenerator2.generate(20, Color.WHITE, 0, Color.WHITE);
        fontGenerator2.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle(battleFont, Color.WHITE);
        waitingLabel = new Label("Waiting for opponent...", labelStyle);
        waitingLabel.setVisible(false); // Initially invisible
    }

    private void initializeCamera() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
    }

    private void initializeStage() {
        stage = new Stage(new ScreenViewport());
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        // Add other input processors if needed
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void loadTextures() {
        battleBottomTabTexture = new Texture(Gdx.files.internal("backgrounds/battleBottomTab.png"));
    }

    private void createUI() {
        createTopUI();
        createMiddleUI();
        createBottomUI();
    }

    private void createMiddleUI() {

        Container<Table> middleContainerWrapper = new Container<>();
        middleContainerWrapper.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() * middleHeightRatio);
        middleContainerWrapper.setPosition(0, Gdx.graphics.getHeight() * bottomHeightRatio); // Position at
        middleContainerWrapper.align(Align.left); // the top
        float desiredHeight = Gdx.graphics.getHeight() * middleHeightRatio * 0.8f;
        Table middleTable = new Table();
        middleTable.setFillParent(true);
        middleTable.setDebug(false);
        // Retrieve the textures from the GameCharacter class
        Texture characterTexture = playerCharacter.getImageTexture();
        Texture monsterTexture = monster.getImageTexture();

        float userAspectRatio = (float) characterTexture.getWidth() / (float) characterTexture.getHeight();
        float characterDesiredWidth = desiredHeight * userAspectRatio; // width to maintain aspect ratio

        float monsterAspectRatio = (float) monsterTexture.getWidth() / (float) monsterTexture.getHeight();
        float monsterDesiredWidth = desiredHeight * monsterAspectRatio;
        // Create images from the textures
        Image characterImage = new Image(characterTexture);
        Image monsterImage = new Image(monsterTexture);

        // Set the size of the images to maintain the aspect ratio
        characterImage.setSize(characterDesiredWidth, desiredHeight);
        monsterImage.setSize(monsterDesiredWidth, desiredHeight); // assuming you want same height for monster

        // Add the images to the middle table
        middleTable.add(characterImage).width(characterDesiredWidth).height(desiredHeight).expandX().align(Align.left)
                .padLeft(20);
        middleTable.add(monsterImage).width(monsterDesiredWidth).height(desiredHeight).expandX().align(Align.right)
                .padRight(20);
        // Add the middle table to the stage
        middleContainerWrapper.setActor(middleTable);
        stage.addActor(middleContainerWrapper);

        // set the waiting label here
        waitingLabel.setDebug(true);
        waitingLabel.setPosition(
                Gdx.graphics.getWidth() / 2 - waitingLabel.getWidth() / 2,
                Gdx.graphics.getHeight() * bottomHeightRatio + middleContainerWrapper.getHeight() / 2
                        - waitingLabel.getHeight() / 2);
        stage.addActor(waitingLabel);
    }

    @Override
    public void render(float delta) {
        // Render battle UI and handle user input

        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        // Add any other rendering here

        updateHealthIndicators();

        game.batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    private void createTopUI() {
        Container<Table> topContainerWrapper = new Container<>();
        float topContainerHeight = Gdx.graphics.getHeight() * topHeightRatio;
        topContainerWrapper.setSize(Gdx.graphics.getWidth(), topContainerHeight);
        topContainerWrapper.setPosition(0, Gdx.graphics.getHeight() - topContainerHeight); // Position at the top
        topContainerWrapper.align(Align.left);

        Table topTable = new Table();
        topTable.top().left().padTop(50);

        topContainerWrapper.setDebug(false);
        topTable.setDebug(false);
        // Enable debugging to see the outlines of the table and cells
        // topTable.setDebug(true);

        TextButton escapeButton = GameButton.createButton("Escape", describeFont, "small");

        escapeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                returnToGameScreen("Escaped");
            }
        });

        Label.LabelStyle labelStyle = new Label.LabelStyle(battleFont, Color.WHITE);
        this.hpBarUser = createHpBar(playerCharacter.getMaxHealth(), playerCharacter.getCurrentHealth(), true);
        this.hpBarMonster = createHpBar(monster.getMaxHealth(), monster.getCurrentHealth(), false);

        Label characterNameLabel = new Label(playerCharacter.getName(), labelStyle);
        Label monsterNameLabel = new Label(monster.getName(), labelStyle);

        activityLabel = new Label("You have encoutered: " + monster.getName(),
                new Label.LabelStyle(describeFont, Color.WHITE));
        this.testHPUser = new Label("HP: " + playerCharacter.getCurrentHealth() + "/" + playerCharacter.getMaxHealth(),
                labelStyle); // Assuming getHp() method exists
        this.testHPMonster = new Label("HP: " + monster.getCurrentHealth() + "/" + monster.getMaxHealth(), labelStyle); // Assuming
                                                                                                                        // getMp()
                                                                                                                        // method
        float hpBarWidth = Gdx.graphics.getWidth() * 0.4f; // exists
        topTable.setWidth(Gdx.graphics.getWidth());
        Table escapeAndActivity = new Table();
        escapeAndActivity.add(escapeButton).padLeft(10);
        escapeAndActivity.add(activityLabel).padLeft(10);

        topTable.add(escapeAndActivity).left().padLeft(10);

        topTable.setFillParent(true);
        topTable.setDebug(false);
        // Add the character and monster names and stats
        topTable.row();
        topTable.add(characterNameLabel).expandX().left().padLeft(30);

        topTable.add(monsterNameLabel).expandX().right().padRight(30);

        topTable.row();
        topTable.add(testHPUser).expandX().left().padLeft(20);
        topTable.add(testHPMonster).expandX().right().padRight(20);

        topTable.row();
        topTable.add(hpBarUser).width(hpBarWidth).left().padLeft(10).padTop(50); // Add some padding on top for spacing
        topTable.add(hpBarMonster).width(hpBarWidth).right().padRight(10).padTop(50); // Same
        topContainerWrapper.setActor(topTable);
        stage.addActor(topContainerWrapper);
    }

    private void createBottomUI() {
        // The Container will hold the bottomTable, which includes the attackContainer
        // and itemContainer
        Container<Table> bottomContainerWrapper = new Container<>();
        bottomContainerWrapper.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() * bottomHeightRatio);
        bottomContainerWrapper.setPosition(0, 0);
        bottomContainerWrapper.fillX(); // Ensure it fills the x-axis

        // This is the main table that holds all bottom UI elements
        Table bottomTable = new Table();
        bottomTable.setBackground(new TextureRegionDrawable(new TextureRegion(battleBottomTabTexture)));
        bottomTable.padBottom(35);

        // Create attack and item containers and add them to the bottomTable
        Table attackContainer = new Table();
        Table itemContainer = new Table();

        // Create a label style for the tooltip
        Label.LabelStyle tooltipStyle = new Label.LabelStyle(game.font, Color.WHITE);

        // Create the tooltip label and initially set it to invisible
        Label tooltipLabel = new Label("Test", tooltipStyle);
        tooltipLabel.setVisible(false);
        stage.setDebugAll(false);
        // Add the tooltip label to the stage
        stage.addActor(tooltipLabel);

        tooltipLabel.toFront(); // Brings the tooltip to the front
        ArrayList<Attack> attacks = playerCharacter.getAttacks();

        for (Attack attack : attacks) {
            final Attack currentAttack = attack;
            final String attackDescription = attack.getDescription(); // Get the description for the tooltip
            final int attackHarm = attack.getHarm();
            final Label currentTooltipLabel = tooltipLabel;
            final TextButton button = GameButton.createButton(attack.getName(), game.font, "small");
            button.addListener(new InputListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    currentTooltipLabel.setDebug(true); // button

                    currentTooltipLabel.setText(attackDescription + "harm: " + attackHarm);
                    currentTooltipLabel.pack(); // This will update the size of the label to fit the text
                    currentTooltipLabel.setPosition(button.getX() + x, button.getY() + y);
                    currentTooltipLabel.toFront(); // Ensure tooltip is in front
                    currentTooltipLabel.setVisible(true);
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    // When the mouse exits the button, hide the tooltip
                    currentTooltipLabel.setVisible(false);
                }

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    // This will get executed when the button is pressed down
                    // Here, you can issue the attack:
                    issueAttack(currentAttack);
                    return true; // Return true to indicate the event was handled
                }

            });

            attackContainer.add(button).size(150, 60).pad(10);
        }

        String[] itemLabels = { "item1", "item2", "item3", "item4" };
        for (String label : itemLabels) {
            TextButton button = GameButton.createButton(label, game.font, "small");
            itemContainer.add(button).size(75, 75).pad(10);
        }
        // Define item container, even though we're not adding items yet

        // Add the attack and item containers to the bottomTable
        bottomTable.add(attackContainer).expandX().fillX();
        bottomTable.add(itemContainer).expandX().fillX();

        // Set the bottomTable as the actor for bottomContainerWrapper
        bottomContainerWrapper.setActor(bottomTable);

        // Finally, add the bottomContainerWrapper to the stage
        stage.addActor(bottomContainerWrapper);
    }

    public void issueAttack(Attack attack) {
        // Check if the attack can be performed (e.g., enough mana, correct turn, etc.)
        if (currentTurn == 0) {
            System.out.println("player attack");
            monster.setHealth(monster.getCurrentHealth() - attack.getHarm());
            System.out.println("monster health: " + monster.getCurrentHealth());
            activityLabel.setText("You attack " + monster.getName()
                    + " with " + attack.getName() + " and it does " + attack.getHarm() + " damage");
            if (monster.getCurrentHealth() <= 0) {
                returnToGameScreen("Won");
            } else {
                // Schedule the monster's attack after a delay
                waitingLabel.setVisible(true);
                float delay = 3; // seconds
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        // This block will be executed after the delay
                        performMonsterAttack();
                    }
                }, delay);
            }
            currentTurn = 1;
        }

    }

    private void performMonsterAttack() {
        waitingLabel.setVisible(false);
        // Logic for the monster to choose and perform an attack
        Attack monsterAttack = monster.getRandomAttack();// ... logic to select an attack ...
        playerCharacter.setHealth(playerCharacter.getCurrentHealth() - monsterAttack.getHarm());
        activityLabel.setText(monster.getName() + " attacks you with " + monsterAttack.getName()
                + " causing " + monsterAttack.getHarm() + " damage");
        if (playerCharacter.getCurrentHealth() <= 0) {
            returnToGameScreen("Lost");
        }
        currentTurn = 0; // Player's turn
    }

    private void updateHealthIndicators() {
        // Update the labels with the current health
        testHPUser.setText("HP: " + playerCharacter.getCurrentHealth() + "/" + playerCharacter.getMaxHealth());
        testHPMonster.setText("HP: " + monster.getCurrentHealth() + "/" + monster.getMaxHealth());

        updateHpBarImage(hpFillPlayer, playerCharacter.getMaxHealth(), playerCharacter.getCurrentHealth(), true);
        updateHpBarImage(hpFillMonster, monster.getMaxHealth(), monster.getCurrentHealth(), false);
    }

    private void updateHpBarImage(Image hpFill, int maxHp, int currentHp, boolean isPlayer) {
        float fullWidth = Gdx.graphics.getWidth() * 0.4f; // 40% of screen width, the full width of the health bar
        float hpFillWidth = fullWidth * ((float) currentHp / maxHp);

        if (isPlayer) {
            // Player's health bar decreases from right to left, so we just set the width
            hpFill.setSize(hpFillWidth, 30);
        } else {
            // Monster's health bar decreases from left to right, so we adjust the X
            // position as well as the width
            hpFill.setSize(hpFillWidth, 30);
            hpFill.setPosition(fullWidth - hpFillWidth, hpFill.getY());
        }
    }

    @Override
    public void show() {
        initializeCamera();
        initializeStage();
        loadTextures();
        createUI();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.update();
        stage.getViewport().update(width, height, true);
    }

    private Table createHpBar(int maxHp, int currentHp, boolean isPlayer) {
        // Calculate the width of the HP fill based on the current HP
        float hpFillWidth = (Gdx.graphics.getWidth() * 0.4f) * ((float) currentHp / maxHp);

        // Create the background image
        Image bgImage = new Image(new Texture(Gdx.files.internal("battleAssets/emptyHpBar.png")));
        bgImage.setSize(Gdx.graphics.getWidth() * 0.4f, 30); // 40% of screen width

        // Create the health fill image
        Image hpImage = new Image(new Texture(Gdx.files.internal("battleAssets/hpBar.png")));
        hpImage.setSize(hpFillWidth, 30); // Set the width based on current HP

        // If the health bar is for the monster (right aligned), we position the HP fill
        // image to the right
        if (!isPlayer) {
            hpImage.setPosition(bgImage.getWidth() - hpImage.getWidth(), 0);
        }
        if (isPlayer) {
            this.hpFillPlayer = hpImage;
        } else {
            this.hpFillMonster = hpImage;
        }

        // Create a table to hold the images
        Table hpBarTable = new Table();
        hpBarTable.addActor(bgImage);
        hpBarTable.addActor(hpImage);

        // Set the table size to match the background image
        hpBarTable.setSize(bgImage.getWidth(), bgImage.getHeight());

        return hpBarTable;
    }

    // private void handleBattleAction(BattleAction action) {
    // // Implement logic for different battle actions
    // // Update game state and UI based on action results
    // }

    private void returnToGameScreen(String result) {
        // Update the waiting label with the result message
        String message = "You escaped!";
        if (result.equals("Won")) {
            playerCharacter.addExperience(100);
            playerCharacter.levelUp();
            message = "You won! Gained 100 experience";
        } else if (result.equals("Lost")) {
            message = "You lost!";
        }

        // Show the message immediately
        waitingLabel.setText(message);
        waitingLabel.setVisible(true);

        // Delay the switch back to the game screen
        float delay = 3; // seconds
        final String currentResult = result;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                // Hide the waiting label
                waitingLabel.setVisible(false);

                // This block will be executed after the delay
                // Switch back to the game screen
                mapScreen.setBattleResult(currentResult);
                game.setScreen(mapScreen);
            }
        }, delay);
    }

    // Implement other required methods from Screen interface
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
        stage.dispose();
        battleBottomTabTexture.dispose();
    }

}
