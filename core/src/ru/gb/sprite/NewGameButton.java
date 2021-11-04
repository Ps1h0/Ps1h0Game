package ru.gb.sprite;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.gb.base.BaseButton;
import ru.gb.math.Rect;
import ru.gb.screen.GameScreen;
import ru.gb.screen.MenuScreen;

public class NewGameButton extends BaseButton {

    private static final float HEIGHT = 0.1f;
    private static final float PADDING = 0.03f;

    private final Game game = new Game() {
        @Override
        public void create() {
            setScreen(new MenuScreen(this));
        }
    };

    public NewGameButton(TextureAtlas atlas) {
        super(atlas.findRegion("button_new_game"));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(HEIGHT);
        setLeft(worldBounds.getLeft() + PADDING);
        setBottom(worldBounds.getBottom() + PADDING);
    }

    @Override
    public void action() {
        game.setScreen(new GameScreen(game));
    }
}
