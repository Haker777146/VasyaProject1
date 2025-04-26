package ru.cuty.vasyaproject;

public class SpaceObject {
    public float x, y;
    public float width, height;
    public float vx, vy;
    public int type;

    private float hitboxWidthFactor = 3.2f;    // Ширина зоны столкновения
    private float hitboxTopFactor = 2f;       // Верхняя часть (уже)
    private float hitboxBottomFactor = 2.8f;  // Нижняя часть (шире)
    private float hitboxOffsetY = 0f;

    public SpaceObject(float x, float y, float vx, float vy) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
    }

    public SpaceObject() {
    }

    public void move(){
        x += vx;
        y += vy;
    }

    public float scrX(){
        return x-width/2;
    }

    public float scrY(){
        return y-height/2;
    }

    public boolean overlap(SpaceObject o) {
        // Ширина зоны столкновения
        float thisWidth = width / hitboxWidthFactor;
        float otherWidth = o.width / o.hitboxWidthFactor;

        // Высота зоны столкновения (разная для верха и низа)
        float thisHeight, otherHeight;

        if (o.y > y + hitboxOffsetY) { // Если объект выше текущего
            thisHeight = height / hitboxTopFactor;
            otherHeight = o.height / o.hitboxTopFactor;
        } else { // Если объект ниже или на уровне
            thisHeight = height / hitboxBottomFactor;
            otherHeight = o.height / o.hitboxBottomFactor;
        }

        // Проверка столкновения
        return Math.abs(x - o.x) < thisWidth + otherWidth &&
            Math.abs(y - o.y - hitboxOffsetY) < thisHeight + otherHeight;
    }
}
