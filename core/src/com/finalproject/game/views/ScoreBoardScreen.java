package com.finalproject.game.views;

import java.util.ArrayList;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.finalproject.game.FinalProjectGame;
import com.finalproject.game.components.GameButton;
import com.finalproject.game.models.FontGenerator;
import com.finalproject.game.models.ScoreBoardEntry;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;


public class ScoreBoardScreen implements Screen {
    private SpriteBatch spriteBatch;
    private BitmapFont font;
    private boolean isLoading;
    private FinalProjectGame game;
    private Screen preScreen;
    private Stage loadingStage;
    private Stage scoreBoardStage;
    private OrthographicCamera camera;
    private Stage currentStage;
    private TextureRegionDrawable bgdrawable;
    private TextButton backButton;
    // Other class members like network handling, leaderboard data, etc.

    // * loading utils
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

    public ScoreBoardScreen(FinalProjectGame game, Screen gameScreen) {
        // Initialize other class members here
        this.game = game;
        this.preScreen = gameScreen;
        Texture backgroundImage = new Texture(Gdx.files.internal("backgrounds/scoreboard_bg.png"));
        bgdrawable = new TextureRegionDrawable(new TextureRegion(backgroundImage));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        loadingStage = new Stage(new ScreenViewport());
        scoreBoardStage = new Stage(new ScreenViewport());
        currentStage = loadingStage;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(currentStage);
        spriteBatch = new SpriteBatch();
        font = new BitmapFont(); // Use LibGDX's default font for simplicity
        FontGenerator fontGenerator = new FontGenerator("fonts/PixelOperator.ttf");
        this.font = fontGenerator.generate(30, Color.WHITE, 0.6f, Color.DARK_GRAY);
        fontGenerator.dispose();
        isLoading = true;

        backButton = GameButton.createButton("Back", game.font);
        backButton.setPosition(100, 100);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(preScreen);
            }
        });
        // Initialize network fetch for scoreboard data
        loadingStage.addActor(backButton);
        fetchScoreBoardData();
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        if (isLoading) {
            // Handle the dynamic "Loading..." text
            loadingDotTimer += delta;
            if (loadingDotTimer >= 0.5f) { // Change the number of dots every 0.5 seconds
                loadingDotTimer = 0;
                loadingDotCount = (loadingDotCount + 1) % 4; // Cycle between 0 and 3 dots
            }
            StringBuilder loadingText = new StringBuilder("Loading");
            for (int i = 0; i < loadingDotCount; i++) {
                loadingText.append(".");
            }

            // Handle the changing message below
            messageTimer += delta;
            if (messageTimer >= 2f) { // Change the message every 2 seconds
                messageTimer = 0;
                messageIndex = (messageIndex + 1) % loadingMessages.length;
            }

            // Draw the "Loading..." text with dynamic dots
            font.draw(spriteBatch, loadingText.toString(), Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

            // Draw the message below
            font.draw(spriteBatch, loadingMessages[messageIndex], Gdx.graphics.getWidth() / 2,
                    Gdx.graphics.getHeight() / 2 - 50);
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // other rendering code

        currentStage.act(delta);
        currentStage.draw();
        spriteBatch.end();
    }

    private void fetchScoreBoardData() {
        System.out.println("Fetching data");
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);
        httpRequest.setUrl("https://spring-5m6ksrldgq-uc.a.run.app/api/records");
        httpRequest.setHeader("Content-Type", "application/json");

        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                final String response = httpResponse.getResultAsString();
                // Process the response here (e.g., parse JSON)

                // Since we're in a separate thread, use Gdx.app.postRunnable to interact with
                // the UI
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        currentStage = scoreBoardStage;
                        populateScoreBoard(response);

                        isLoading = false; // Update the loading flag
                    }
                });
            }

            @Override
            public void failed(Throwable t) {
                // Handle any errors here
            }

            @Override
            public void cancelled() {
                // Handle cancellation here
            }
        });
    }

    private void populateScoreBoard(String data) {
        ArrayList<ScoreBoardEntry> entries = parseScoreBoardData(data);
        createScoreBoard(entries);
    }

    private ArrayList<ScoreBoardEntry> parseScoreBoardData(String data) {
        ArrayList<ScoreBoardEntry> entries = new ArrayList<>();
        Json json = new Json();
        entries = json.fromJson(ArrayList.class, ScoreBoardEntry.class, data);
        if (entries == null)
            return null;

        // Assuming the data is an array of objects
        return entries;
    }

    private void createScoreBoard(ArrayList<ScoreBoardEntry> entries) {
        currentStage.clear();
        Gdx.input.setInputProcessor(currentStage);

        Table table = new Table();

        // Define the dimensions of the scroll panel
        float width = Gdx.graphics.getWidth() * 0.6f; // 60% of screen width
        float height = Gdx.graphics.getHeight() * 0.85f; // 80% of screen height

        // Set up the header
        LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        Table headerTable = new Table();
        headerTable.setWidth(width);
        headerTable.add(new Label("Score Board", labelStyle)).center().expandX().width(width).center().padLeft(50);
        headerTable.row();
        headerTable.add(new Label("Top 100", labelStyle)).center().expandX().width(width).center().padLeft(50);

        // Add each entry as a row
        if (entries == null) {
            table.add(new Label("No data yet", labelStyle));
            table.row();
            return;
        }

        // add header
        Table rowHeaderTable = new Table();
        rowHeaderTable.add(new Label("Rank", labelStyle)).center().expandX().padRight(10);
        rowHeaderTable.add(new Label("User Name", labelStyle)).center().expandX().padRight(10);
        rowHeaderTable.add(new Label("Character", labelStyle)).center().expandX().padRight(10);
        rowHeaderTable.add(new Label("Points", labelStyle)).center().expandX();
        rowHeaderTable.add(new Label("Monster Killed", labelStyle)).center().expandX()
                .padLeft(10);
        // table.add(rowHeaderTable).expandX().fillX().top().padTop(0);
        table.row();
        int count = 0;
        for (ScoreBoardEntry entry : entries) {
            count++;
            Table rowTable = new Table();
            rowTable.add(new Label(String.valueOf(count) + ".", labelStyle)).center().expandX().padRight(10);
            rowTable.add(new Label(entry.getUserId(), labelStyle)).center().expandX().padRight(10);
            rowTable.add(new Label(entry.getName(), labelStyle)).center().expandX().padRight(10);
            rowTable.add(new Label(String.valueOf(entry.getPoints()), labelStyle)).center().expandX();
            rowTable.add(new Label(String.valueOf(entry.getMonsterKilled()), labelStyle)).center().expandX()
                    .padLeft(10);
            table.add(rowTable).expandX().fillX().top().padTop(0);
            table.row();
        }

        // Create the scroll pane and add the table to it
        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setScrollingDisabled(true, false); // Disable horizontal, enable vertical
        scrollPane.setPosition(Gdx.graphics.getWidth() * 0.4f, // 35% from left to center it
                Gdx.graphics.getHeight() * 0.05f); // 10% from bottom

        // ! give scrollpane a tinted background
        // Create a Pixmap with the desired tint color and alpha value for transparency
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        Color tint = new Color(0, 0, 0, 0.5f); // White with 50% transparency
        pixmap.setColor(tint);
        pixmap.fill();

        // Create a Drawable from the Pixmap
        TextureRegionDrawable tintedBackground = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));

        // Dispose of the Pixmap after creating the Texture to prevent memory leaks
        pixmap.dispose();

        // Set the background of your ScrollPane
        scrollPane.getStyle().background = tintedBackground;

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        // Add headerTable to the mainTable
        mainTable.add(headerTable).expandX().fillX().top();
        mainTable.row();

        mainTable.add(rowHeaderTable).width(width).expandX().fillX().top().padLeft(Gdx.graphics.getWidth() * 0.21f)
                .padTop(0).padBottom(0);
        mainTable.row();
        // Add scrollPane to the mainTable

        mainTable.add(scrollPane).size(width, height).padLeft(Gdx.graphics.getWidth() * 0.2f).padTop(0).padBottom(0);
        mainTable.row();

        Color backgroundColor = new Color(1, 1, 1, 0.5f); // Transparency set to 0.1f
        Drawable tintedDrawable = bgdrawable.tint(backgroundColor);
        mainTable.setBackground(tintedDrawable);
        Table login = createLoginWidget();
        login.setPosition(50, Gdx.graphics.getHeight() / 2 - login.getHeight() / 2);
        currentStage.addActor(mainTable);
        currentStage.addActor(backButton);
        currentStage.addActor(login);
    }

    public Table createLoginWidget() {
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        // Create a table to organize your widgets
        Table loginTable = new Table();
        loginTable.setSize(500, 500);
        // Add a title label
        Label titleLabel = new Label("Upload Your Score!", skin);
        titleLabel.setFontScale(1.3f);
        loginTable.add(titleLabel).pad(10);
        loginTable.row();

        Label subTitle = new Label("Your username will be your name on the score board", skin);
        subTitle.setWrap(true);
        loginTable.add(subTitle).padLeft(50).padRight(10).align(Align.center).fillX().expandX();
        loginTable.row();

        // Add username input field
        final TextField usernameField = new TextField("", skin);
        usernameField.setMessageText("Username");
        loginTable.add(usernameField).width(200).pad(10);
        loginTable.row();

        // Add password input field
        final TextField passwordField = new TextField("", skin);
        passwordField.setMessageText("Password");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        loginTable.add(passwordField).width(200).pad(10);
        loginTable.row();

        // Add password visibility toggle button
        final TextButton passwordVisibilityButton = new TextButton("Show Password", skin);
        passwordVisibilityButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean isPasswordMode = passwordField.isPasswordMode();
                passwordField.setPasswordMode(!isPasswordMode); // Toggle password mode
                passwordVisibilityButton.setText(isPasswordMode ? "Hide Password" : "Show Password"); // Toggle button text
            }
        });
        loginTable.add(passwordVisibilityButton).pad(10);
        loginTable.row();

        // Add register button
        TextButton registerButton = new TextButton("Register an account", skin);
        loginTable.add(registerButton).pad(10);
        loginTable.row();

        // Add verify and upload score button
        TextButton verifyButton = new TextButton("Verify and upload score", skin);
        loginTable.add(verifyButton).pad(10);
        loginTable.row();
        // Listener for the register button
        final Label finalTitleLabel = titleLabel; // Declare titleLabel as final
        final Skin finalSkin = skin;
        final Stage finalStage = currentStage;

        registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Register button clicked");

                String username = usernameField.getText();
                String password = passwordField.getText();
                // check if username and password are valid

                String usernameRegex = "^[a-zA-Z0-9_]{6,20}$";
                String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";
                if (!username.matches(usernameRegex)) {
                    Dialog dialog = new Dialog("Warning", finalSkin);
                    dialog.text("Username must be 6-20 characters long and only contain letters, numbers and _");
                    dialog.button("OK");
                    dialog.show(finalStage);
                    return;
                }
                if (!password.matches(passwordRegex)) {
                    Dialog dialog = new Dialog("Warning", finalSkin);
                    dialog.text("Password must be 8-20 characters long and contain at least one uppercase letter, one lowercase letter, one number and one special character");
                    dialog.button("OK");
                    dialog.show(finalStage);
                    return;
                }
                // send request to register
                System.out.println("Sending request to register");
                Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.POST);
                httpRequest.setUrl("https://spring-5m6ksrldgq-uc.a.run.app/api/user/register");
                httpRequest.setHeader("Content-Type", "application/json");
                httpRequest.setContent("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}");
                Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
                    @Override
                    public void handleHttpResponse(Net.HttpResponse httpResponse) {
                        final String response = httpResponse.getResultAsString();
                        final int status = httpResponse.getStatus().getStatusCode();
                        // Process the response here (e.g., parse JSON)
                        // Since we're in a separate thread, use Gdx.app.postRunnable to interact with the UI
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                JsonValue json = new Json().fromJson(null, response);
                                if (status == 201) {
                                    Dialog dialog = new Dialog("Success", finalSkin);
                                    dialog.text("Register successfully");
                                    dialog.button("OK");
                                    dialog.show(finalStage);
                                } else {
                                    String errorMessages = "Register failed: ";
                                    for (JsonValue error : json.get("message")) {
                                        errorMessages += error.asString() + "\n";
                                    }
                                    Dialog dialog = new Dialog("Warning", finalSkin);
                                    dialog.text(errorMessages);
                                    dialog.button("OK");
                                    dialog.show(finalStage);
                                }
                            }
                        });
                    }

                    @Override
                    public void failed(Throwable t) {
                        // Handle any errors here
                    }

                    @Override
                    public void cancelled() {
                        // Handle cancellation here
                    }
                });


            }
        });

        // Listener for the verify and upload score button
        verifyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Verify button clicked");
                finalTitleLabel.setText("Uploading score...");
                // Step 1:
                // first verify the username and password
                final String username = usernameField.getText();
                String password = passwordField.getText();
                final boolean[] isUserValid = new boolean[]{false};
                // check if username and password are valid
                Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.POST);
                httpRequest.setUrl("https://spring-5m6ksrldgq-uc.a.run.app/api/user/login");
                httpRequest.setHeader("Content-Type", "application/json");
                httpRequest.setContent("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}");
                Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
                    @Override
                    public void handleHttpResponse(Net.HttpResponse httpResponse) {
                        final String response = httpResponse.getResultAsString();
                        final int status = httpResponse.getStatus().getStatusCode();
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                // check if the user is valid
                                JsonValue json = new Json().fromJson(null, response);
                                if (status == 200) {
                                    // login successfully
                                    isUserValid[0] = true;
                                    System.out.println(" Login successfully");
                                } else {
                                    String errorMessages = "Login failed: ";
                                    for (JsonValue error : json.get("message")) {
                                        errorMessages += error.asString() + "\n";
                                    }
                                    Dialog dialog = new Dialog("Warning", finalSkin);
                                    dialog.text(errorMessages);
                                    dialog.button("OK");
                                    dialog.show(finalStage);
                                    return;
                                }
                                // Step 2:
                                // check if th user has already uploaded the score
                                // if yes, update the score
                                System.out.println("Checking if the user has already uploaded the score");
                                final String[] userRecord = new String[1];
                                Net.HttpRequest httpRequest2 = new Net.HttpRequest(Net.HttpMethods.GET);
                                httpRequest2.setUrl("https://spring-5m6ksrldgq-uc.a.run.app/api/records/user/"+username);
                                httpRequest2.setHeader("Content-Type", "application/json");
                                Gdx.net.sendHttpRequest(httpRequest2, new Net.HttpResponseListener() {
                                    @Override
                                    public void handleHttpResponse(Net.HttpResponse httpResponse){
                                        final String response = httpResponse.getResultAsString();
                                        final int status = httpResponse.getStatus().getStatusCode();
                                        Gdx.app.postRunnable(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (status == 200) {
                                                    System.out.println("userRecord: "+response);
                                                    userRecord[0] = response;
                                                } else {
                                                    System.out.println("userRecord: null");
                                                    userRecord[0] = generateRecord(username);
                                                }
                                                // Step 3:
                                                // update the record
                                                System.out.println("Updating the record");

                                                // Step 4:
                                                // upload the record
                                                System.out.println("Uploading the record");
                                                Net.HttpRequest httpRequest3 = new Net.HttpRequest(Net.HttpMethods.POST);
                                                httpRequest3.setUrl("https://spring-5m6ksrldgq-uc.a.run.app/api/records");
                                                httpRequest3.setHeader("Content-Type", "application/json");
                                                httpRequest3.setContent(userRecord[0]);
                                                Gdx.net.sendHttpRequest(httpRequest3, new Net.HttpResponseListener() {
                                                    @Override
                                                    public void handleHttpResponse(Net.HttpResponse httpResponse) {
                                                        final String response = httpResponse.getResultAsString();
                                                        final int status = httpResponse.getStatus().getStatusCode();
                                                        Gdx.app.postRunnable(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                JsonValue json = new Json().fromJson(null, response);
                                                                if (status == 201) {
                                                                    Dialog dialog = new Dialog("Success", finalSkin);
                                                                    dialog.text("Upload score successfully");
                                                                    dialog.button("OK");
                                                                    dialog.show(finalStage);
                                                                    fetchScoreBoardData();
                                                                    // finalTitleLabel.setText("Upload score successfully");
                                                                } else {
                                                                    String errorMessages = "Upload score failed: ";
                                                                    for (JsonValue error : json.get("message")) {
                                                                        errorMessages += error.asString() + "\n";
                                                                    }
                                                                    Dialog dialog = new Dialog("Warning", finalSkin);
                                                                    dialog.text(errorMessages);
                                                                    dialog.button("OK");
                                                                    dialog.show(finalStage);
                                                                    // finalTitleLabel.setText("Upload score failed");
                                                                }
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void failed(Throwable t) {
                                                        // Handle any errors here
                                                    }

                                                    @Override
                                                    public void cancelled() {
                                                        // Handle cancellation here
                                                    }
                                                });
                                            }
                                        });
                                    }

                                    @Override
                                    public void failed(Throwable t) {
                                        // Handle any errors here
                                    }

                                    @Override
                                    public void cancelled() {
                                        // Handle cancellation here
                                    }
                                });


                            }
                        });
                    }
                    @Override
                    public void failed(Throwable t) {
                        // Handle any errors here
                    }

                    @Override
                    public void cancelled() {
                        // Handle cancellation here
                    }
                });
            }
        });


        return loginTable;
    }

    private String generateRecord(String username){
        String id = UUID.randomUUID().toString();
        String savingName = "savingName";
        String health = "100";
        String strength = "100";
        String defense = "100";
        String speed = "100";
        String level = "1";
        String name = "Liu Bo";
        String imagePath = "imagePath";
        String monsterKilled = "100";
        String points = "100";
        String userId = username;
        String newRecord = "{\"id\":\""+id
                            +"\",\"savingName\":\""+savingName+
                            "\",\"health\":\""+health+
                            "\",\"strength\":\""+strength+
                            "\",\"defense\":\""+defense+
                            "\",\"speed\":\""+speed+
                            "\",\"level\":\""+level+
                            "\",\"name\":\""+name+
                            "\",\"imagePath\":\""+imagePath+
                            "\",\"monsterKilled\":\""+monsterKilled+
                            "\",\"points\":\""+points+
                            "\",\"userId\":\""+userId+"\"}";
        return newRecord;
    }

    @Override
    public void hide() {
        spriteBatch.dispose();
        font.dispose();
        // Dispose other resources if necessary
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.update();
        currentStage.getViewport().update(width, height, true);
        currentStage.getCamera().update();
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'pause'");
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resume'");
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'dispose'");
    }
}
