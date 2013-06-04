package me.schiz.load.ammo;

public class Bullet {
    public int length;
    public char[] name;
    public char[] content;

    public Bullet() {}

    public boolean parseTitle(String title) {
        if (title == null) return false;
//        String[] a = title.split(" ");
//        length = Integer.parseInt(a[0]);
//        if(a.length > 1) name = a[1].toCharArray();
        int index = title.indexOf(" ");
        if (index > 0) length = Integer.parseInt(title.substring(0, index));
        else length = Integer.parseInt(title);
        if(index+1 < title.length()) {
            name = title.substring(index+1, title.length()).toCharArray();
        }
        return true;
    }
}
