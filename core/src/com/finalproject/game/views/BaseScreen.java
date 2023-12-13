package com.finalproject.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.CubemapAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.TextureProvider;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.compression.lzma.Base;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.finalproject.game.FinalProjectGame;
import com.finalproject.game.models.FontGenerator;
import com.finalproject.game.models.GameCharacter;
import com.finalproject.game.views.LoadFromPreviousSavingScreen;

public class BaseScreen implements Screen {
    private FinalProjectGame game;
    private PerspectiveCamera camera;
    private Stage stage;
    private GameCharacter selectedCharacter;
    private ModelBatch modelBatch;
    private Environment environment;
    private ModelInstance environmentModel; // 3D environment model
    private CameraInputController camController;
    private Texture characterTexture;

    private DecalBatch decalBatch;
    public AssetManager assets;
    private ModelInstance roomModel; // 3D room model
    private ModelInstance characterModel; // 3D character model
    private ModelInstance battleEntrance;
    private ModelInstance treasureBox;
    private Decal entrancePlateDecal;
    private Decal characterImageDecal;

    private BoundingBox roomBounds = new BoundingBox();
    private boolean assetsLoaded = false;
    private Model backgroundSphereModel;
    private ModelInstance backgroundSphereInstance;
    private AnimationController characterAniController;
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    private float lastRotationAngle = 0f;

    private boolean resetCharacterPosition = false;

    BoundingBox battleEntranceBounds = new BoundingBox();
    BoundingBox savingEntranceBounds = new BoundingBox();
    BoundingBox treasureBoxBounds = new BoundingBox();
    BoundingBox characterBounds = new BoundingBox();
    BoundingBox monitorBounds = new BoundingBox();

    private Stage loadingStage;
    private Stage baseStage;
    private Stage currentStage;
    ShapeRenderer shapeRenderer = new ShapeRenderer();

    // * loading utils
    private Label loadingLabel;
    private Label messageLabel;
    private SpriteBatch spriteBatch;
    private BitmapFont font;
    private float loadingDotTimer = 0;
    private int loadingDotCount = 0;
    private float messageTimer = 0;
    private int messageIndex = 0;
    private String[] loadingMessages = new String[] {
            "this might take a while ...",
            "still working on it ...",
            "almost done ...",
            // Add more messages as needed
    };

    public BaseScreen(FinalProjectGame game, GameCharacter selectedCharacter) {
        this.game = game;
        this.selectedCharacter = selectedCharacter;
        this.loadingStage = new Stage(new ScreenViewport());
        this.baseStage = new Stage(new ScreenViewport());

        font = new BitmapFont(); // Use LibGDX's default font for simplicity
        spriteBatch = new SpriteBatch();
        FontGenerator fontGenerator = new FontGenerator("fonts/PixelOperator.ttf");
        this.font = fontGenerator.generate(40, Color.WHITE, 1f, Color.DARK_GRAY);
        fontGenerator.dispose();

        currentStage = loadingStage;
        Texture backgroundImage = new Texture(Gdx.files.internal("backgrounds/loading_bg.png"));
        TextureRegionDrawable bgdrawable = new TextureRegionDrawable(new TextureRegion(backgroundImage));
        Color backgroundColor = new Color(1, 1, 1, 0.5f);
        Drawable tintedDrawable = bgdrawable.tint(backgroundColor);
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.setBackground(tintedDrawable);

        loadingLabel = new Label("Loading...", new LabelStyle(font, Color.WHITE));
        messageLabel = new Label("", new LabelStyle(font, Color.WHITE));
        // Add labels to your main table
        mainTable.add(loadingLabel).center().padTop(120).row();
        mainTable.add(messageLabel).center().padTop(10);
        // Add the main table to the stage
        loadingStage.addActor(mainTable);

        camera = new PerspectiveCamera(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(10f, 10f, 20f);
        camera.lookAt(0, 0, 0);
        camera.near = 0.5f;
        camera.far = 1550f; // Increase if necessary
        camera.update();

        modelBatch = new ModelBatch();
        environment = new Environment(); // Set up environment (lights, etc.)

        // Camera controller for testing
        camController = new CameraInputController(camera);
        Gdx.input.setInputProcessor(camController);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        // Initialize models once they are loaded
        createBackgroundSphere();

        decalBatch = new DecalBatch(new CameraGroupStrategy(camera));

    }

    private void initializeModels() {
        Model character = assets.get("models/Character/Y-Bot.g3db", Model.class);
        characterModel = new ModelInstance(character);
        characterModel.transform.scl(0.8f);
        characterModel.transform.setToTranslation(-100f, 10f, 0f);

        // characterModel.transform.rotate(Vector3.Y, 180f);
        // Create an animation controller
        for (Animation animation : characterModel.animations) {
            System.out.println("Animation name: " + animation.id);
        }
        characterAniController = new AnimationController(characterModel);

        // Start the animation
        characterAniController.setAnimation("mixamo.com", -1, 1f, null);

        Model room = assets.get("models/game_room/final_game_room.g3db", Model.class);
        roomModel = new ModelInstance(room);
        roomModel.calculateBoundingBox(roomBounds);

        Model entrance = assets.get("models/teleport_door/obj.g3db", Model.class);
        battleEntrance = new ModelInstance(entrance);
        battleEntrance.transform.scl(0.8f);
        battleEntrance.transform.setToTranslation(-100f, 200f, -480f);

        Model model = assets.get("models/Chest_box/obj.g3db", Model.class);
        treasureBox = new ModelInstance(model);
        treasureBox.transform.setToTranslation(200f, 10f, -420f);
        treasureBox.transform.scl(50f);

        // Model campfire = assets.get("models/campfire/campfire.g3db", Model.class);
        // ModelInstance campfireInstance = new ModelInstance(campfire);
        // campfireInstance.transform.setToTranslation(350f, 10f, -420f);
        // campfireInstance.transform.scl(2f);

        Model drive = assets.get("models/drive/drive.g3db", Model.class);
        ModelInstance driveInstance = new ModelInstance(drive);
        driveInstance.transform.setToTranslation(350f, 50f, -100f);
        driveInstance.transform.scl(20f);
        driveInstance.transform.rotate(Vector3.Y, 45f);
        driveInstance.transform.rotate(Vector3.Z, -100f);

        Model monitor = assets.get("models/monitor/monitor.g3db", Model.class);
        ModelInstance monitorInstance = new ModelInstance(monitor);
        monitorInstance.transform.setToTranslation(-330f, 10f, -380f);
        monitorInstance.transform.scl(1f);
        // monitorInstance.transform.rotate(Vector3.Y, 125f);

        battleEntranceBounds = calculateTransformedBoundingBox(battleEntrance);
        treasureBoxBounds = calculateTransformedBoundingBox(treasureBox);
        savingEntranceBounds = calculateTransformedBoundingBox(driveInstance);
        monitorBounds = calculateTransformedBoundingBox(monitorInstance);

        Texture entranceImage = new Texture(Gdx.files.internal("models/start.png"));
        entrancePlateDecal = Decal.newDecal(entranceImage.getWidth(), entranceImage.getHeight(),
                new TextureRegion(entranceImage), true);
        entrancePlateDecal.setPosition(-75f, 360f, -450f);
        entrancePlateDecal.setScale(0.1f);


        // ! load character image
        float desiredHeight = 200f;
        Texture characterImage = this.selectedCharacter.getImageTexture();
        float originalHeight = characterImage.getHeight();
        float originalWidth = characterImage.getWidth();
        float aspectRatio = originalWidth / originalHeight;
        // Calculate the new width to maintain the aspect ratio
        float newWidth = desiredHeight * aspectRatio;

        // Calculate the scale factor based on the desired height
        float scaleFactor = desiredHeight / originalHeight;

        // Now create the Decal with new dimensions
        characterImageDecal = Decal.newDecal(newWidth, desiredHeight, new TextureRegion(characterImage), true);

        characterImageDecal.setPosition(-185f, 360f, -450f);
        // characterImageDecal.setScale(scaleFactor);


        instances.add(characterModel, roomModel, battleEntrance, treasureBox);
        instances.add(driveInstance, monitorInstance);
        assetsLoaded = true;

    }

    private BoundingBox calculateTransformedBoundingBox(ModelInstance modelInstance) {
        // Calculate the original bounding box
        BoundingBox originalBounds = new BoundingBox();
        modelInstance.calculateBoundingBox(originalBounds);

        // Create a new bounding box for transformed bounds
        BoundingBox transformedBounds = new BoundingBox(originalBounds);

        // Apply the transformation of the model instance to the bounding box
        transformedBounds.mul(modelInstance.transform);

        return transformedBounds;
    }

    public void createBackgroundSphere() {
        // Load the texture for the background
        Texture backgroundTexture = new Texture(Gdx.files.internal("skybox/skybox_z.png"));

        // Create a model builder
        ModelBuilder modelBuilder = new ModelBuilder();

        // Create the sphere model
        backgroundSphereModel = modelBuilder.createSphere(1800f, 1800f, 1800f, 32, 32,
                new Material(TextureAttribute.createDiffuse(backgroundTexture)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
                        | VertexAttributes.Usage.TextureCoordinates);

        // Create a ModelInstance of your sphere model
        backgroundSphereInstance = new ModelInstance(backgroundSphereModel);
        backgroundSphereInstance.transform.scale(-1f, 1f, 1f);

        // Set the position of the sphere if needed
        // backgroundSphereInstance.transform.setToTranslation(x, y, z);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(currentStage);
        // Implement if needed
        assets = new AssetManager();
        assets.load("models/Character/Y-Bot.g3db", Model.class);
        // assets.load("models/cone/obj.obj", Model.class);
        assets.load("models/game_room/final_game_room.g3db", Model.class);
        assets.load("models/teleport_door/obj.g3db", Model.class);
        assets.load("models/Chest_box/obj.g3db", Model.class);
        // assets.load("models/campfire/campfire.g3db", Model.class);
        assets.load("models/drive/drive.g3db", Model.class);
        assets.load("models/monitor/monitor.g3db", Model.class);
    }

    private void showLoadingMessage(float delta) {
        // Update the loading label with dynamic dots
        loadingDotTimer += delta;
        if (loadingDotTimer >= 0.5f) {
            loadingDotTimer = 0;
            loadingDotCount = (loadingDotCount + 1) % 4;
        }
        StringBuilder loadingText = new StringBuilder("Loading");
        for (int i = 0; i < loadingDotCount; i++) {
            loadingText.append(".");
        }
        loadingLabel.setText(loadingText.toString());

        // Update the message label
        messageTimer += delta;
        if (messageTimer >= 2f) {
            messageTimer = 0;
            messageIndex = (messageIndex + 1) % loadingMessages.length;
        }
        messageLabel.setText(loadingMessages[messageIndex]);
    }

    @Override
    public void render(float delta) {
        // Update models if not initialized
        if (!assetsLoaded) {
            if (assets.update()) { // Check if all assets are loaded
                initializeModels(); // Initialize your 3D models here
                assetsLoaded = true;
                currentStage = baseStage;
            } else {
                currentStage = loadingStage;
                // Render your loading screen
                Gdx.gl.glClearColor(0, 0, 0, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                currentStage.act(delta);
                currentStage.draw();
                showLoadingMessage(delta);
                return; // Return early if assets are still loading
            }
        }

        characterAniController.update(delta);
        // Handle character movement
        handleCharacterMovement(delta);

        // Update camera position to follow the character
        updateCameraPosition();

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(camera);
        // Disable depth writing to ensure the sphere is always rendered in the
        // background
        if (backgroundSphereInstance != null)
            modelBatch.render(backgroundSphereInstance);

        modelBatch.render(instances, environment);

        modelBatch.end();

        // Render models
        decalBatch.add(entrancePlateDecal);
        decalBatch.add(characterImageDecal);
        decalBatch.flush();

        // ! ---
        // Disable depth test for shape rendering
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);

        // Draw bounding boxes
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // Draw character bounding box
        // drawBoundingBox(shapeRenderer, characterBounds, Color.RED);

        // Draw battle entrance bounding box
        // drawBoundingBox(shapeRenderer, battleEntranceBounds, Color.BLUE);

        shapeRenderer.end();

        // Re-enable depth test if needed elsewhere
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
    }

    private void drawBoundingBox(ShapeRenderer shapeRenderer, BoundingBox box, Color color) {
        shapeRenderer.setColor(color);
        shapeRenderer.box(box.min.x, box.min.y, box.min.z,
                box.getWidth(), box.getHeight(), box.getDepth());
    }

    private void handleCharacterMovement(float delta) {

        float characterSpeed = 100.0f;
        float animationSpeedFactor = 1f;
        Vector3 newPosition = characterModel.transform.getTranslation(new Vector3());

        if (resetCharacterPosition) {
            newPosition.z = newPosition.z + 30f;
            resetCharacterPosition = false;
        }
        // Use the bounding box to set boundaries
        float offset = 10f;
        float minX = roomBounds.min.x - offset, maxX = roomBounds.max.x - offset;
        float minZ = roomBounds.min.z - offset, maxZ = roomBounds.max.z - offset;

        boolean isMoving = false;
        float desiredRotationAngle = lastRotationAngle;

        boolean movingLeft = Gdx.input.isKeyPressed(Input.Keys.LEFT) && newPosition.x - characterSpeed * delta > minX;
        boolean movingRight = Gdx.input.isKeyPressed(Input.Keys.RIGHT) && newPosition.x + characterSpeed * delta < maxX;
        boolean movingUp = Gdx.input.isKeyPressed(Input.Keys.UP) && newPosition.z - characterSpeed * delta > minZ;
        boolean movingDown = Gdx.input.isKeyPressed(Input.Keys.DOWN) && newPosition.z + characterSpeed * delta < maxZ;

        if (movingLeft) {
            newPosition.x -= characterSpeed * delta;
            desiredRotationAngle = -90;
            isMoving = true;
        }
        if (movingRight) {
            newPosition.x += characterSpeed * delta;
            desiredRotationAngle = 90;
            isMoving = true;
        }
        if (movingUp) {
            newPosition.z -= characterSpeed * delta;
            desiredRotationAngle = 180;
            isMoving = true;
        }
        if (movingDown) {
            newPosition.z += characterSpeed * delta;
            desiredRotationAngle = 0;
            isMoving = true;
        }

        // Adjust rotation for diagonal movement
        if (movingUp && movingLeft) {
            desiredRotationAngle = -135;
        } else if (movingUp && movingRight) {
            desiredRotationAngle = 135;
        } else if (movingDown && movingLeft) {
            desiredRotationAngle = -45;
        } else if (movingDown && movingRight) {
            desiredRotationAngle = 45;
        }

        // Rotate character only if there's a change in the rotation angle
        if (desiredRotationAngle != lastRotationAngle) {
            rotateCharacter(desiredRotationAngle - lastRotationAngle); // Adjust the rotation based on the difference
            lastRotationAngle = desiredRotationAngle; // Update the last rotation angle
        }

        characterModel.transform.setTranslation(newPosition);

        // Control the walking animation
        if (isMoving) {
            if (characterAniController.current != null) {
                characterAniController.setAnimation("mixamo.com", -1, animationSpeedFactor, null);
            }
        } else {
            if (characterAniController.current != null) {
                characterAniController.animate("mixamo.com", -1, 0f, null, 0f); // Stop the animation
            }
        }

        // battleEntrance.calculateBoundingBox(battleEntranceBounds);
        // treasureBox.calculateBoundingBox(treasureBoxBounds);
        characterBounds = calculateTransformedBoundingBox(characterModel);
        // characterModel.calculateBoundingBox(characterBounds);
        // System.out.println("Character Bounds: " + characterBounds);
        // System.out.println("Battle Entrance Bounds: " + battleEntranceBounds);
        if (characterBounds.intersects(battleEntranceBounds)) {
            // Trigger action for battle soy entrance
            System.out.println("Battle entrance");

            game.setScreen(new GameScreen(game, selectedCharacter, 2, 1));
        }

        if (characterBounds.intersects(treasureBoxBounds)) {
            resetCharacterPosition = true;
            game.setScreen(new InventoryScreen(game, BaseScreen.this, selectedCharacter.getInventory()));
        }

        if (characterBounds.intersects(savingEntranceBounds)) {
            // Trigger action for saving entrance
            System.out.println("Saving entrance");
            game.setScreen(new LoadFromPreviousSavingScreen(game, selectedCharacter));
        }

        if (characterBounds.intersects(monitorBounds)) {
            resetCharacterPosition = true;
            game.setScreen(new ScoreBoardScreen(game, BaseScreen.this, selectedCharacter));
        }
    }

    private void rotateCharacter(float angle) {
        characterModel.transform.rotate(Vector3.Y, angle);
    }

    private void updateCameraPosition() {
        // Assuming roomCenter is the center of your room model
        Vector3 roomCenter = new Vector3();
        roomModel.calculateBoundingBox(roomBounds).getCenter(roomCenter);

        // Height and distance from the center for the camera
        float cameraHeight = 320f; // Adjust this value as necessary
        float distanceFromCenter = 520f; // Adjust this value as necessary

        // Calculate the camera position for a 45-degree angle
        Vector3 cameraPosition = new Vector3(
                roomCenter.x,
                roomCenter.y + cameraHeight,
                roomCenter.z + distanceFromCenter);

        // Set camera position
        camera.position.set(cameraPosition);

        // Calculate a point behind the character model to look at
        Vector3 characterPosition = new Vector3();
        characterModel.transform.getTranslation(characterPosition);
        Vector3 lookAtPoint = new Vector3(characterPosition.x, characterPosition.y, characterPosition.z - 1); // Adjust
                                                                                                              // '1' to
                                                                                                              // increase/decrease
                                                                                                              // the
                                                                                                              // offset

        // Look at the point behind the character model
        camera.lookAt(lookAtPoint);
        camera.up.set(Vector3.Y); // Ensure the camera's 'up' is Y-axis aligned
        camera.update();
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
    public void hide() {
        // Implement if needed
    }

    @Override
    public void dispose() {
        if (modelBatch != null) {
            modelBatch.dispose();
        }
        if (decalBatch != null) {
            decalBatch.dispose();
        }
        if (environmentModel != null && environmentModel.model != null) {
            environmentModel.model.dispose();
            environmentModel = null;
        }
        if (characterTexture != null) {
            characterTexture.dispose();
            characterTexture = null;
        }
        shapeRenderer.dispose();
        currentStage.dispose();
        loadingStage.dispose();
        assets.dispose();

    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    // Implement other required methods...
}
