package com.finalproject.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class GameButton {

    private static Skin defaultSkin;
    private static TextureAtlas defaultButtonAtlas;

    // Initialize the default skin and buttonAtlas
    static {
        defaultButtonAtlas = new TextureAtlas(Gdx.files.internal("button.atlas"));
        defaultSkin = new Skin();
        defaultSkin.addRegions(defaultButtonAtlas);
    }

    public static TextButton createButton(String buttonText, BitmapFont font) {
        return createButton(buttonText, font, null, null);
    }

    public static TextButton createButton(String buttonText, BitmapFont font, Skin skin, TextureAtlas buttonAtlas) {
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = font;

        Skin effectiveSkin = (skin != null) ? skin : defaultSkin;

        textButtonStyle.up = effectiveSkin.getDrawable("up-button");
        textButtonStyle.down = effectiveSkin.getDrawable("down-button");
        textButtonStyle.disabled = effectiveSkin.getDrawable("disable-button");
        textButtonStyle.over = effectiveSkin.getDrawable("highlight-button");

        return new TextButton(buttonText, textButtonStyle);
    }
}
