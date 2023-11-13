package com.finalproject.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class GameButton {

    private static Skin defaultSkin;
    private static TextureAtlas defaultButtonAtlas;
    private static Skin smallSkin;
    private static TextureAtlas smallButtonAtlas;

    // Initialize the default skin and buttonAtlas
    static {
        defaultButtonAtlas = new TextureAtlas(Gdx.files.internal("buttons/normal/button.atlas"));
        defaultSkin = new Skin();
        defaultSkin.addRegions(defaultButtonAtlas);

        smallButtonAtlas = new TextureAtlas(Gdx.files.internal("buttons/small/button_small.atlas"));
        smallSkin = new Skin();
        smallSkin.addRegions(smallButtonAtlas);
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
                }
                
                
                textButtonStyle.up = effectiveSkin.getDrawable("up-button");
                textButtonStyle.down = effectiveSkin.getDrawable("down-button");
                textButtonStyle.disabled = effectiveSkin.getDrawable("disable-button");
                textButtonStyle.over = effectiveSkin.getDrawable("highlight-button");
        
                return new TextButton(buttonText, textButtonStyle);
    }

}
