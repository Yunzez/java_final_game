package com.finalproject.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.finalproject.game.models.CharacterItem;

public class GameButton {

    private static Skin defaultSkin;
    private static TextureAtlas defaultButtonAtlas;
    private static Skin smallSkin;
    private static TextureAtlas smallButtonAtlas;
    private static Skin squareSkin;
    private static TextureAtlas squareButtonAtlas;
    private static Skin itemSkin;
    private static TextureAtlas itemButtonAtlas;

    // Initialize the default skin and buttonAtlas
    static {
        defaultButtonAtlas = new TextureAtlas(Gdx.files.internal("buttons/normal/button.atlas"));
        defaultSkin = new Skin();
        defaultSkin.addRegions(defaultButtonAtlas);

        smallButtonAtlas = new TextureAtlas(Gdx.files.internal("buttons/small/button_small.atlas"));
        smallSkin = new Skin();
        smallSkin.addRegions(smallButtonAtlas);

        squareButtonAtlas = new TextureAtlas(Gdx.files.internal("buttons/square/square_button.atlas"));
        squareSkin = new Skin();
        squareSkin.addRegions(squareButtonAtlas);

        itemButtonAtlas = new TextureAtlas(Gdx.files.internal("buttons/item/item.atlas"));
        itemSkin = new Skin();
        itemSkin.addRegions(itemButtonAtlas);
    }

    public static TextButton createButton(String buttonText, BitmapFont font) {
        return createButton(buttonText, font, null, null, "normal");
    }

    public static TextButton createButton(String buttonText, BitmapFont font, String type) {
        return createButton(buttonText, font, null, null, type);
    }

    public static TextButton createButton(String buttonText, BitmapFont font, Skin skin, TextureAtlas buttonAtlas,
            String type) {
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = font;
        Skin effectiveSkin = (skin != null) ? skin : defaultSkin;
        System.out.println(type);
        switch (type) {
            case "normal":
                effectiveSkin = defaultSkin;
                break;
            case "small":
                effectiveSkin = smallSkin;
                break;
            case "square":
                effectiveSkin = squareSkin;
                break;
            case "item":
                effectiveSkin = squareSkin;
                break;
        }

        textButtonStyle.up = effectiveSkin.getDrawable("up-button");
        textButtonStyle.down = effectiveSkin.getDrawable("down-button");
        textButtonStyle.disabled = effectiveSkin.getDrawable("disable-button");
        textButtonStyle.over = effectiveSkin.getDrawable("highlight-button");

        return new TextButton(buttonText, textButtonStyle);
    }

    public static TextButton createButtonWithIcon(CharacterItem item, BitmapFont font, String type) {
        String buttonText = item.getItem().getName();
        Texture icon = item.getItem().getIcon();
        String count = String.valueOf(item.getCount());
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = font;

        Skin effectiveSkin = (type != null && type.equals("item")) ? itemSkin : defaultSkin;
        textButtonStyle.up = effectiveSkin.getDrawable("up-button");
        textButtonStyle.down = effectiveSkin.getDrawable("down-button");
        textButtonStyle.disabled = effectiveSkin.getDrawable("disable-button");
        textButtonStyle.over = effectiveSkin.getDrawable("highlight-button");

        TextButton button = new TextButton(buttonText, textButtonStyle);
        if (icon != null) {
            float aspectRatio = (float) icon.getWidth() / icon.getHeight();
            // Add the icon as an image to the button
            Image iconImage = new Image(new TextureRegionDrawable(new TextureRegion(icon)));
            button.clearChildren(); // Clear existing children if needed
            float iconHeight = 80; // Desired height
            float iconWidth = aspectRatio * iconHeight;
            button.add(iconImage).size(iconWidth, iconHeight);

            button.row();
            // Create a label for the count and add it to the button
            Label countLabel = new Label("X " + count, new Label.LabelStyle(font, Color.WHITE)); // Adjust the style as
                                                                                                 // needed
            button.add(countLabel);
        }

        return button;
    }

}
