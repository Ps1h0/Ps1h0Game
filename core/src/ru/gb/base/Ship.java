package ru.gb.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.gb.math.Rect;
import ru.gb.pool.BulletPool;
import ru.gb.sprite.Bullet;

public class Ship extends Sprite{

    protected BulletPool bulletPool;
    protected Sound bulletSound;
    protected TextureRegion bulletRegion;
    protected Vector2 bulletV;
    protected Vector2 bulletPos;
    protected float bulletHeight;
    protected int damage;
    protected int hp;

    protected Vector2 v;
    protected Vector2 v0;

    protected float reloadTimer;
    protected float reloadInterval;
    protected Rect worldBounds;

    public Ship() {
    }

    public Ship(TextureRegion region, int rows, int cols, int frames) {
        super(region, rows, cols, frames);
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        reloadTimer += delta;
        if (reloadTimer >= reloadInterval){
            reloadTimer = 0f;
            shoot();
        }
        bulletPos.set(pos);
    }

    public void damage(int hp){
        this.hp -= hp;
        if(this.hp <= 0){
            this.hp = 0;
            destroy();
        }
    }

    public int getDamage() {
        return damage;
    }

    private void shoot(){
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, bulletPos, bulletV, worldBounds, bulletHeight, damage);
        bulletSound.play();
    }
}
