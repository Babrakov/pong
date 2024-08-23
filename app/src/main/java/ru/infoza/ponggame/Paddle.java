package ru.infoza.ponggame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class Paddle {

    private RectF rect;
    private float length, height;
    private float x, y;
    private float paddleSpeed;
    private int screenX, screenY;

    public enum MovementState {
        STOPPED, MOVE_LEFT, MOVE_RIGHT
    }

    private MovementState paddleMoving = MovementState.STOPPED;

    public Paddle(int screenX, int screenY, boolean isPlayer) {
        this.screenX = screenX;
        this.screenY = screenY;
        length = screenX / 8;
        height = screenY / 25;
        x = screenX / 2;
        y = isPlayer ? screenY - 50 : 50;
        paddleSpeed = 10;
        rect = new RectF(x, y, x + length, y + height);
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

}
