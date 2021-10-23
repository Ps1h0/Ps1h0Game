package ru.gb.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.gb.base.BaseScreen;


public class MenuScreen extends BaseScreen {

    private Texture img;
    private Vector2 touch;
    private Vector2 pos;
    private Vector2 v;

    @Override
    public void show() {
        super.show();
        img = new Texture("badlogic.jpg");
        touch = new Vector2();
        pos = new Vector2();
        v = new Vector2();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        batch.draw(img, pos.x, pos.y);
        batch.end();
        if(pos.dst(touch) <= v.len()){
            pos.set(touch);
        } else{
            pos.add(v);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        img.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touch.set(screenX, Gdx.graphics.getHeight() - screenY);
        v.set(touch.cpy().sub(pos)).setLength(1.8f);
        return super.touchDown(touch, pointer, button);
    }

}
