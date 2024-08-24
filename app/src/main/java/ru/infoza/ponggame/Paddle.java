package ru.infoza.ponggame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class Paddle {

    private final RectF rect;
    private final float length;
    private final float height;
    private final float paddleSpeed;
    private final int screenX;
    private final int screenY;
    private float x;
    private float y;
    private MovementState paddleMoving = MovementState.STOPPED;

    public Paddle(int screenX, int screenY, boolean isPlayer) {
        this.screenX = screenX;
        this.screenY = screenY;
        length = (float) screenX / 8;
        height = (float) screenY / 25;
        x = (float) screenX / 2;
        y = isPlayer ? screenY - 50 : 50;
        paddleSpeed = 10;
        rect = new RectF(x, y, x + length, y + height);
    }

    public float getLength() {
        return length;
    }

    public float getHeight() {
        return height;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void update() {
        if (paddleMoving == MovementState.MOVE_LEFT && x > 0) {
            x -= paddleSpeed;
        } else if (paddleMoving == MovementState.MOVE_RIGHT && x + length < screenX) {
            x += paddleSpeed;
        }
        rect.left = x;
        rect.right = x + length;
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawRect(rect, paint);
    }

    public void setMovementState(MovementState state) {
        paddleMoving = state;
    }

    public RectF getRect() {
        return rect;
    }

    public enum MovementState {
        STOPPED, MOVE_LEFT, MOVE_RIGHT
    }

}
