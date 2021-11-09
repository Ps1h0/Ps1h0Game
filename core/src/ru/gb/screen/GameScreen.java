package ru.gb.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import ru.gb.base.BaseScreen;
import ru.gb.math.Rect;
import ru.gb.pool.BulletPool;
import ru.gb.pool.EnemyPool;
import ru.gb.pool.ExplosionPool;
import ru.gb.sprite.Background;
import ru.gb.sprite.Bullet;
import ru.gb.sprite.EnemyShip;
import ru.gb.sprite.GameOver;
import ru.gb.sprite.NewGameButton;
import ru.gb.sprite.StarShip;
import ru.gb.sprite.Star;
import ru.gb.util.EnemyEmitter;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 64;

    private TextureAtlas atlas;
    private Texture bg;
    private Background background;

    private Star[] stars;
    private BulletPool bulletPool;
    private ExplosionPool explosionPool;
    private EnemyPool enemyPool;

    private StarShip starShip;

    private Music music;
    private Sound laserSound;
    private Sound bulletSound;
    private Sound explosionSound;

    private EnemyEmitter enemyEmitter;

    private GameOver gameOver;
    private NewGameButton newGameButton;

    private int frags;

    @Override
    public void show() {
        super.show();
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        music.play();
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        atlas = new TextureAtlas("textures/mainAtlas.tpack");
        bg = new Texture("textures/bg.png");
        background = new Background(bg);
        stars = new Star[STAR_COUNT];
        for (int i = 0; i < stars.length; i++){
            stars[i] = new Star(atlas);
        }
        bulletPool = new BulletPool();
        explosionPool = new ExplosionPool(atlas, explosionSound);
        enemyPool = new EnemyPool(bulletPool, explosionPool, worldBounds, bulletSound);
        starShip = new StarShip(atlas, bulletPool, explosionPool, laserSound);
        enemyEmitter = new EnemyEmitter(enemyPool, worldBounds, atlas);
        gameOver = new GameOver(atlas);
        newGameButton = new NewGameButton(atlas, this);
        frags = 0;
    }

    public void startNewGame(){
        frags = 0;
        starShip.startNewGame();
        bulletPool.freeAllActiveObjects();
        explosionPool.freeAllActiveObjects();
        enemyPool.freeAllActiveObjects();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        chekCollision();
        freeAllDestroyed();
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star : stars){
            star.resize(worldBounds);
        }
        starShip.resize(worldBounds);
        gameOver.resize(worldBounds);
        newGameButton.resize(worldBounds);
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        atlas.dispose();
        bulletSound.dispose();
        enemyPool.dispose();
        music.dispose();
        laserSound.dispose();
        bulletPool.dispose();
        explosionPool.dispose();
        explosionSound.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        starShip.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        starShip.keyUp(keycode);
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (!starShip.isDestroyed()){
            starShip.touchDown(touch, pointer, button);
        } else {
            newGameButton.touchDown(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (!starShip.isDestroyed()){
            starShip.touchUp(touch, pointer, button);
        }else {
            newGameButton.touchUp(touch, pointer, button);
        }
        return false;
    }

    private void update(float delta){
        for (Star star : stars){
            star.update(delta);
        }
        if (!starShip.isDestroyed()) {
            bulletPool.updateActiveObjects(delta);
            enemyPool.updateActiveObjects(delta);
            starShip.update(delta);
            enemyEmitter.generate(delta);
        }
        explosionPool.updateActiveObjects(delta);
    }

    private void chekCollision(){
        if (starShip.isDestroyed()) {
            return;
        }
        List<EnemyShip> enemyShipList = enemyPool.getActiveObjects();
        for (EnemyShip enemyShip : enemyShipList){
            float minDist = starShip.getWidth() + enemyShip.getWidth();
            if (!enemyShip.isDestroyed() && starShip.pos.dst(enemyShip.pos) < minDist) {
                enemyShip.destroy();
                starShip.damage(enemyShip.getDamage() * 2);
            }
        }
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        for (Bullet bullet : bulletList){
            if (bullet.isDestroyed()){
                continue;
            }
            if (bullet.getOwner() != starShip){
                if (starShip.isBulletCollision(bullet)) {
                    starShip.damage(bullet.getDamage());
                    bullet.destroy();
                }
                continue;
            }
            for (EnemyShip enemyShip: enemyShipList){
                if (enemyShip.isBulletCollision(bullet)){
                    enemyShip.damage(bullet.getDamage());
                    bullet.destroy();
                }
            }
        }
    }

    private void freeAllDestroyed(){
        bulletPool.freeAllDestroyed();
        explosionPool.freeAllDestroyed();
        enemyPool.freeAllDestroyed();
    }

    private void draw(){
        batch.begin();
        background.draw(batch);
        for (Star star : stars){
            star.draw(batch);
        }
        if (!starShip.isDestroyed()){
            bulletPool.drawActiveObjects(batch);
            enemyPool.drawActiveObjects(batch);
            starShip.draw(batch);
        }else {
            gameOver.draw(batch);
            newGameButton.draw(batch);
        }
        explosionPool.drawActiveObjects(batch);
        batch.end();
    }
}
