package com.finalproject.game.models;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class FontGenerator {

    private FreeTypeFontGenerator generator;

    public FontGenerator(String fontFilePath) {
        generator = new FreeTypeFontGenerator(Gdx.files.internal(fontFilePath));
    }

    public BitmapFont generate(int size, Color color, float borderWidth, Color borderColor) {
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = color;
        parameter.borderWidth = borderWidth;
        parameter.borderColor = borderColor;

        return generator.generateFont(parameter); // Generate the font
    }

    public void dispose() {
        generator.dispose(); // Dispose of the generator to avoid memory leaks
    }
}
