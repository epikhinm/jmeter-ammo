package me.schiz.load.ammo;

import java.io.IOException;

abstract public class Ammo {
    protected volatile long ammosize = -1;
    protected volatile boolean first_loop;

    public abstract long getAmmosize();

    public abstract Bullet take() throws InterruptedException, IOException;

    public abstract void close();
}
