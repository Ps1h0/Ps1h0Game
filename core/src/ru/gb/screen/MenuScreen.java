package ru.gb.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import ru.gb.base.BaseScreen;
import ru.gb.base.Font;
import ru.gb.math.Rect;
import ru.gb.sprite.Background;
import ru.gb.sprite.ExitButton;
import ru.gb.sprite.PlayButton;
import ru.gb.sprite.Star;


public class MenuScreen extends BaseScreen {

    private static final int STAR_COUNT = 256;
    private static final float FONT_SIZE = 0.02f;
    private static final String RULES = "Press left/right or 'a'/'d' to move\n Press Space to on/off auto shoot";

    private final Game game;

    private TextureAtlas atlas;
    private Texture bg;

    private Background background;
    private Star[] stars;

    private PlayButton playButton;
    private ExitButton exitButton;

    private Font font;

    public MenuScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();
        atlas = new TextureAtlas("textures/menuAtlas.tpack");
        bg = new Texture("textures/bg.png");
        background = new Background(bg);
        stars = new Star[STAR_COUNT];
        for (int i = 0; i < stars.length; i++){
            stars[i] = new Star(atlas);
        }
        playButton = new PlayButton(atlas, game);
        exitButton = new ExitButton(atlas);
        font = new Font("font/font.fnt", "font/font.png");
        font.setSize(FONT_SIZE);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star : stars){
            star.resize(worldBounds);
        }
        playButton.resize(worldBounds);
        exitButton.resize(worldBounds);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        atlas.dispose();
        font.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        playButton.touchDown(touch, pointer, button);
        exitButton.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        playButton.touchUp(touch, pointer, button);
        exitButton.touchUp(touch, pointer, button);
        return false;
    }

    private void update(float delta){
        for (Star star : stars){
            star.update(delta);
        }
    }

    private void draw(){
        batch.begin();
        background.draw(batch);
        for (Star star : stars){
            star.draw(batch);
        }
        playButton.draw(batch);
        exitButton.draw(batch);
        printRules();
        batch.end();
    }

    private void printRules(){
        font.draw(batch, RULES, worldBounds.pos.x, worldBounds.pos.y, Align.center);
    }
}
