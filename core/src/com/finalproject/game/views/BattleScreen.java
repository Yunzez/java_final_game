package com.finalproject.game.views;

import com.badlogic.gdx.Game;
import java.util.Random;

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
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
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
import com.finalproject.game.models.Buff;
import com.finalproject.game.models.BuffType;
import com.finalproject.game.models.CharacterItem;
import com.finalproject.game.models.FontGenerator;
import com.finalproject.game.models.GameCharacter;
import com.finalproject.game.models.Item;

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

    private ArrayList<Buff> playerBuff;
    private ArrayList<Buff> monsterBuff;

    private float bottomHeightRatio = 0.25f;
    private float topHeightRatio = 0.25f;
    private float middleHeightRatio = 1 - bottomHeightRatio - topHeightRatio - 0.05f;

    private int currentTurn; // 0 for player, 1 for monster
    private Label centerLabel;
    private Table centerTable;

    private Table userBuffContainer;
    private Table monsterBuffContainer;

    private Texture backgroundTexture;
    private Image backgroundImage;
    private Image characterImage;
    private Image monsterImage;

    public BattleScreen(FinalProjectGame game, GameCharacter playerCharacter, GameCharacter monster,
            GameScreen mapScreen, float baseSizeFactor) {
        this.game = game;
        this.playerCharacter = playerCharacter;
        this.monster = monster;
        this.monster.assignRandomAttacks(3);
        this.mapScreen = mapScreen;
        this.baseSizeFactor = baseSizeFactor;
        this.playerBuff = new ArrayList<Buff>();
        this.monsterBuff = new ArrayList<Buff>();

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
        createCenterTable();
    }

    public void createCenterTable() {
        centerTable = new Table();
        float tableWidth = Gdx.graphics.getWidth() * 0.2f;
        float tableHeight = Gdx.graphics.getHeight() * 0.2f;
        centerTable.setSize(tableWidth, tableHeight);

        Texture backgroundTexture = new Texture(Gdx.files.internal("backgrounds/infotable_bg.png"));
        TextureRegion region = new TextureRegion(backgroundTexture);
        Drawable backgroundDrawable = new TextureRegionDrawable(region);
        backgroundDrawable.setMinWidth(tableWidth);
        backgroundDrawable.setMinHeight(tableHeight);
        centerTable.setBackground(backgroundDrawable);

        centerTable.pad(10); // Add padding around the label

        Label.LabelStyle labelStyle = new Label.LabelStyle(battleFont, Color.WHITE);
        centerLabel = new Label("Waiting for opponent...", labelStyle);

        centerLabel.setVisible(true); // Make sure this is initialized
        centerTable.add(centerLabel).pad(5).align(Align.center).fill().expand();
        centerLabel.setWrap(true); // Enable word wrapping

        centerTable.setVisible(false); // Initially invisible, set to true when needed
    }

    public void setRandomBackground() {
        int bgNumber = (int) (Math.random() * 3) + 1;
        System.out.println("bgNumber: " + bgNumber);
        backgroundTexture = new Texture(Gdx.files.internal("backgrounds/battle_bg" + bgNumber + ".png"));

        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(backgroundImage);

        // Create a Pixmap with the desired color and alpha
        Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0.5f); // Black with 50% opacity
        pixmap.fill(); // Fill the pixmap with the set color

        // Create a Texture from Pixmap
        Texture overlayTexture = new Texture(pixmap);
        pixmap.dispose(); // Dispose of the pixmap to free memory

        // Create an Image actor with the overlay texture
        Image overlayImage = new Image(overlayTexture);
        overlayImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Add the overlay image to the stage
        stage.addActor(overlayImage);
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
        characterImage = new Image(characterTexture);
        monsterImage = new Image(monsterTexture);

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

        // * Create the table to hold the center label
        float tableWidth = Gdx.graphics.getWidth() * 0.2f;
        float tableHeight = Gdx.graphics.getHeight() * 0.2f;
        centerTable.setSize(tableWidth, tableHeight);

        // Position the table in the middle of the screen
        float posX = Gdx.graphics.getWidth() / 2 - tableWidth / 2;
        float posY = Gdx.graphics.getHeight() * bottomHeightRatio + middleContainerWrapper.getHeight() / 2
                - tableHeight / 2;
        centerTable.setPosition(posX, posY);

        // Add the table to the stage
        stage.addActor(centerTable);
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
        topTable.top().left().padTop(20);

        topContainerWrapper.setDebug(false);

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
        // Add the character and monster names and stats
        topTable.row();
        topTable.add(characterNameLabel).expandX().left().padLeft(30);

        topTable.add(monsterNameLabel).expandX().right().padRight(30);

        topTable.row();
        topTable.add(testHPUser).expandX().left().padLeft(20);
        topTable.add(testHPMonster).expandX().right().padRight(20);

        topTable.row();
        topTable.add(hpBarUser).width(hpBarWidth).left().padLeft(10).padTop(40); // Add some padding on top for spacing
        topTable.add(hpBarMonster).width(hpBarWidth).right().padRight(10).padTop(40); // Same

        userBuffContainer = new Table().align(Align.left).padLeft(5);
        monsterBuffContainer = new Table().align(Align.right).padRight(5);

        updateBuff(playerBuff, userBuffContainer);
        updateBuff(monsterBuff, monsterBuffContainer);

        topTable.row();
        topTable.add(userBuffContainer).left().width(hpBarWidth).padTop(10).height(80); // Use expand() and left() for
                                                                                        // alignment
        topTable.add(monsterBuffContainer).right().width(hpBarWidth).padTop(10).height(80); // Use expand() and right()
                                                                                            // for
        // alignment
        topTable.setDebug(true);
        topContainerWrapper.setActor(topTable);
        stage.addActor(topContainerWrapper);
    }

    private void updateBuff(ArrayList<Buff> buffList, Table buffContainer) {
        buffContainer.clear();
        for (Buff buff : buffList) {
            String iconPath = buff.getTypeIconPath();
            TextureRegionDrawable iconDrawable = new TextureRegionDrawable(
                    new TextureRegion(new Texture(Gdx.files.internal(iconPath))));
            Image buffIcon = new Image(iconDrawable);
            buffContainer.add(buffIcon).size(60, 60).pad(10); // Set your desired size for the
                                                              // icon
        }
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
        bottomTable.padBottom(5);

        // Create attack and item containers and add them to the bottomTable
        Table attackContainer = new Table();
        Table itemContainer = new Table();
        attackContainer.setWidth(Gdx.graphics.getWidth() / 2);
        // Create a label style for the tooltip
        Label.LabelStyle tooltipStyle = new Label.LabelStyle(game.font, Color.WHITE);

        // Create the tooltip label and initially set it to invisible

        // Set a maximum size for the tooltip table

        Texture backgroundTexture = new Texture(Gdx.files.internal("backgrounds/infotable_bg.png"));
        TextureRegion region = new TextureRegion(backgroundTexture);
        Drawable backgroundDrawable = new TextureRegionDrawable(region);
        float tableMaxWidth = Gdx.graphics.getWidth() * 0.4f;
        float tableMaxHeight = Gdx.graphics.getHeight() * 0.1f;

        // Create the tooltip label
        Label tooltipLabel = new Label("Test", tooltipStyle);
        tooltipLabel.setVisible(true);
        tooltipLabel.setWrap(true); // Enable word wrapping
        tooltipLabel.setAlignment(Align.center); // Align the text to the top left corner

        // Create the tooltip table with a background
        Table tooltipTable = new Table();
        tooltipTable.setBackground(backgroundDrawable);
        tooltipTable.pad(10);

        // Add the label to the tooltip table
        tooltipTable.add(tooltipLabel).width(tableMaxWidth * 0.9f).expand().fill().padLeft(10).padRight(10);

        // Set the initial size of the tooltip table
        tooltipTable.setSize(tableMaxWidth, tableMaxHeight);

        // Add the tooltip table to the stage and initially set it to invisible
        tooltipTable.setVisible(false);
        stage.addActor(tooltipTable);
        ArrayList<Attack> attacks = playerCharacter.getAttacks();
        int attackCount = 0;
        for (Attack attack : attacks) {
            final boolean[] tooltipVisible = new boolean[] { false };

            final float tableMaxWidthFinal = tableMaxWidth;
            final float tableMaxHeightFinal = tableMaxHeight;
            final Attack currentAttack = attack;
            final String attackDescription = attack.getDescription(); // Get the description for the tooltip
            final int attackHarm = attack.getHarm();
            final Table currentTooltipTable = tooltipTable;
            final Label currentTooltipLabel = tooltipLabel;
            final TextButton button = GameButton.createButton(attack.getName(), game.font, "square");
            attackCount++;
            // After every two buttons, start a new row
            if ((attackCount + 1) % 2 == 0) {
                attackContainer.row();
            }

            button.addListener(new InputListener() {
                private long lastEnterTime = 0;
                private long lastExitTime = 0;

                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastEnterTime > 50) { // 200 milliseconds delay
                        // Show tooltip
                        currentTooltipTable.setVisible(true);
                        lastEnterTime = currentTime;
                        currentTooltipLabel.setText(attackDescription + " Harm: " + attackHarm);
                        currentTooltipTable.setSize(tableMaxWidthFinal, tableMaxHeightFinal); // Keep the tooltip table
                                                                                              // // size
                        // consistent
                        // Update the tooltip table position relative to the button
                        currentTooltipTable.setPosition(button.getX() + x, button.getY() + y + 40);

                    }

                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    long currentTime = System.currentTimeMillis();
                    lastExitTime = currentTime;
                    new Timer().scheduleTask(new Timer.Task() {
                        @Override
                        public void run() {
                            if (System.currentTimeMillis() - lastExitTime >= 50) {
                                // Hide tooltip
                                currentTooltipTable.setVisible(false);
                            }
                        }
                    }, 0.05f);
                }

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    // This will get executed when the button is pressed down
                    // Here, you can issue the attack:
                    if (currentTurn == 1) {
                        System.out.println("not your turn");
                        return true;
                    }
                    issueAttack(currentAttack);
                    System.out.println("attack used");
                    return true; // Return true to indicate the event was handled
                }

            });

            attackContainer.add(button).size(350, 85).pad(8);
        }

        ArrayList<CharacterItem> itemLabels = playerCharacter.getInventory();
        Table itemTable = new Table();

        ScrollPane itemScrollPane = new ScrollPane(itemTable);

        float scrollPaneWidth = Gdx.graphics.getWidth() / 2 - 20;
        itemContainer.add(itemScrollPane).align(Align.right).width(scrollPaneWidth);
        itemScrollPane.setSize(scrollPaneWidth, itemContainer.getHeight());
        itemScrollPane.setScrollingDisabled(false, true);

        Label itemDescription = new Label("Item Description", new Label.LabelStyle(describeFont, Color.WHITE));
        itemDescription.setWrap(true); // Enable word wrap
        itemDescription.setWidth(scrollPaneWidth);
        itemDescription.setAlignment(Align.center);
        itemDescription.setVisible(false);

        //
        updateItemTable(itemTable, itemDescription);

        itemContainer.row();
        itemContainer.add(itemDescription).width(Gdx.graphics.getWidth() / 2 - 80).colspan(itemLabels.size()).pad(5);
        // Define item container, even though we're not adding items yet

        // Add the attack and item containers to the bottomTable
        bottomTable.add(attackContainer).expandX().fillX();
        bottomTable.add(itemContainer);

        // Set the bottomTable as the actor for bottomContainerWrapper
        bottomContainerWrapper.setActor(bottomTable);

        // Finally, add the bottomContainerWrapper to the stage
        stage.addActor(bottomContainerWrapper);
        tooltipTable.toFront(); // Brings the tooltip table to the front
        tooltipLabel.toFront(); // Brings the tooltip to the front

    }

    private void updateItemTable(Table itemTable, Label itemDescription) {
        itemTable.clear(); // Clear the existing items
        ArrayList<CharacterItem> itemLabels = playerCharacter.getInventory();
        for (CharacterItem item : itemLabels) {
            TextButton button = GameButton.createButtonWithIcon(item, describeFont, "item");

            if (item.getCount() == 0) {
                button.setDisabled(true);
                button.setVisible(false);
            } else {
                itemTable.add(button).size(130, 130).pad(4);
                // Add a listener to the button
                button.addListener(new InputListener() {
                    @Override
                    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                        itemDescription.setText(item.getItem().getDescription());
                        // button
                        itemDescription.setVisible(true);
                    }

                    @Override
                    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                        itemDescription.setVisible(false);
                    }

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        // This will get executed when the button is pressed down
                        // Here, you can issue the item:

                        if (item.getCount() > 0) {
                            System.out.println("item used");
                            Buff itemBuff = item.useItem();
                            if (itemBuff != null) {
                                if (itemBuff.getType() == BuffType.MONSTER_DAMAGE) {
                                    monsterBuff.add(itemBuff);
                                    updateBuff(monsterBuff, monsterBuffContainer);
                                } else {
                                    playerBuff.add(itemBuff);
                                    updateBuff(playerBuff, userBuffContainer);
                                }

                            }
                            centerTable.setVisible(true);
                            centerLabel.setText("You used " + item.getItem().getName());
                            currentTurn = 1;
                            delay(1, () -> {
                                centerTable.setVisible(false);
                                performMonsterAttack();
                            });
                            itemDescription.setVisible(false);
                            updateItemTable(itemTable, itemDescription);

                        }
                        return true; // Return true to indicate the event was handled
                    }
                });
            }
        }
    }

    public void issueAttack(Attack attack) {
        System.out.println("issue attack " + attack.getName() + " " + currentTurn);

        if (currentTurn == 0) {
            String buffMessage = applyPlayerBuff(playerBuff, playerCharacter);

            monster.setHealth(monster.getCurrentHealth() - attack.getHarm());
            String attackMessage = "You attack " + monster.getName() + " with " + attack.getName() + " and it does "
                    + attack.getHarm() + " damage";

            activityLabel.setText(buffMessage + attackMessage); // Combine buff and attack messages

            if (monster.getCurrentHealth() <= 0) {
                monsterImage.setDrawable(new TextureRegionDrawable(new TextureRegion(monster.getLostImageTexture())));
                returnToGameScreen("Won");
            } else {
                centerLabel.setText("Waiting for opponent...");
                centerTable.setVisible(true);
                delay(3, () -> performMonsterAttack());
            }
            currentTurn = 1;
        } else {
            performMonsterAttack();
        }
    }

    public String applyPlayerBuff(ArrayList<Buff> buffList, GameCharacter character) {
        StringBuilder buffMessage = new StringBuilder();

        for (Buff buff : buffList) {
            if (buff.getType() == BuffType.HEALTH) {
                int healAmount = Math.min(buff.getMagnitude(),
                        character.getMaxHealth() - character.getCurrentHealth());
                character.setHealth(character.getCurrentHealth() + healAmount);
                buffMessage.append("You heal yourself with ").append(buff.getName()).append(" and it heals ")
                        .append(healAmount).append(" health.\n");
            }

            if (buff.getType() == BuffType.PLAYER_DAMAGE) {
                monster.setHealth(monster.getCurrentHealth() - buff.getMagnitude());
                buffMessage.append("You attack ").append(monster.getName()).append(" with ").append(buff.getName())
                        .append(" and it does ").append(buff.getMagnitude()).append(" damage.\n");
            }
        }

        return buffMessage.toString();
    }

    public void processBuff(ArrayList<Buff> playerBuff, ArrayList<Buff> monsterBuff) {
        // Process monster buffs
        for (int i = monsterBuff.size() - 1; i >= 0; i--) {
            Buff buff = monsterBuff.get(i);
            buff.setDuration(buff.getDuration() - 1);
            if (buff.getDuration() == 0) {
                monsterBuff.remove(i);
            }
        }

        // Process player buffs
        for (int i = playerBuff.size() - 1; i >= 0; i--) {
            Buff buff = playerBuff.get(i);
            buff.setDuration(buff.getDuration() - 1);
            if (buff.getDuration() == 0) {
                playerBuff.remove(i);
            }
        }

        updateBuff(playerBuff, userBuffContainer);
        updateBuff(monsterBuff, monsterBuffContainer);
    }

    public void delay(float delayTime, Runnable taskToRun) {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                // This block will be executed after the delay
                taskToRun.run();
            }
        }, delayTime);
    }

    private void performMonsterAttack() {
        centerTable.setVisible(false);
        // Logic for the monster to choose and perform an attack
        Attack monsterAttack = monster.getRandomAttack();// ... logic to select an attack ...

        int harmDeduction = 0;
        String message = "";
        for (Buff buff : monsterBuff) {
            if (buff.getType() == BuffType.MONSTER_DAMAGE) {
                harmDeduction += buff.getMagnitude();
                message += "Monster got damange decrease from " + buff.getName() + " of " + buff.getMagnitude() + ".\n";
            }

            if (buff.getType() == BuffType.DEFENSE) {
                harmDeduction += buff.getMagnitude();
                message += "You defend useing " + buff.getName() + " of " + buff.getMagnitude() + " Harm.\n";
            }
        }

        int actualHarm = harmDeduction > monsterAttack.getHarm() ? 0 : monsterAttack.getHarm() - harmDeduction;
        playerCharacter.setHealth(playerCharacter.getCurrentHealth() - actualHarm);

        if (actualHarm == 0) {
            message += monster.getName() + " attacks you with " + monsterAttack.getName()
                    + " but it does no damage.\n";

        } else {
            message += (monster.getName() + " attacks you with " + monsterAttack.getName()
                    + " causing " + monsterAttack.getHarm() + " damage");
        }

        activityLabel.setText(message);

        if (playerCharacter.getCurrentHealth() <= 0) {
            characterImage
                    .setDrawable(new TextureRegionDrawable(new TextureRegion(playerCharacter.getLostImageTexture())));
            returnToGameScreen("Lost");
        }
        currentTurn = 0; // Player's turn
        processBuff(playerBuff, monsterBuff);
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
            if (fullWidth < 0) {
                hpFill.setPosition(0, hpFill.getY());
            } else {
                hpFill.setPosition(fullWidth - hpFillWidth, hpFill.getY());
            }

        }
    }

    @Override
    public void show() {
        initializeCamera();
        initializeStage();
        // ! we set background the moment we create the stage
        setRandomBackground();
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
        Random random = new Random();
        Screen targetScreen = mapScreen;
        if (result.equals("Won")) {
            playerCharacter.addExperience(100);
            // playerCharacter.levelUp();
            playerCharacter.addMonsterKilled();

            message = "You won! Gained 100 experience, level up!";

            if (random.nextFloat() < 1) { // ! 改爆率
                // Select a random item
                Item[] possibleItems = Item.values();
                int randomItemIndex = random.nextInt(possibleItems.length);
                Item randomItem = possibleItems[randomItemIndex];
                int numberOfItem = random.nextInt(2) + 1;
                playerCharacter.addInventory(randomItem, numberOfItem);

                message = "You won! Gained 100 experience " + " and " + numberOfItem + " " + randomItem.getName()
                        + " , level up!";
            }

        } else if (result.equals("Lost")) {
            playerCharacter.setHealth(playerCharacter.getMaxHealth());
            message = "You lost!";
            targetScreen = new BaseScreen(game, playerCharacter);
        }

        // Delay the switch back to the game screen
        final Screen targetScreenFinal = targetScreen;
        delay(5, () -> {
            final String currentResult = result;

            centerTable.setVisible(false);

            // This block will be executed after the delay
            // Switch back to the game screen
            mapScreen.setBattleResult(currentResult);
            game.setScreen(targetScreenFinal);
        });

        // Show the message immediately
        centerLabel.setText(message);
        centerTable.setVisible(true);

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
        backgroundTexture.dispose();
        battleFont.dispose();
        describeFont.dispose();

    }

}
