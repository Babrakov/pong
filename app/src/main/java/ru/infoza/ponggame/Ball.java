package ru.infoza.ponggame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class Ball {

    private RectF rect;
    private float x, y, ballWidth, ballHeight;
    private float velocityX, velocityY;
    private int screenX, screenY;

    public Ball(int screenX, int screenY) {
        this.screenX = screenX;
        this.screenY = screenY;
        ballWidth = screenX / 50;
        ballHeight = screenX / 50;
        x = screenX / 2;
        y = screenY / 2;
        velocityX = 4;
        velocityY = 4;
        rect = new RectF(x, y, x + ballWidth, y + ballHeight);
    }

    public void update(Paddle playerPaddle, Paddle aiPaddle) {
        x += velocityX;
        y += velocityY;

        if (rect.left < 0 || rect.right > screenX) {
            velocityX = -velocityX;
            x += 2*velocityX;
        }

        if (rect.top < 0 || rect.bottom > screenY) {
            velocityY = -velocityY;
            y += 2*velocityY;
        }

        if (RectF.intersects(rect, playerPaddle.getRect()) || RectF.intersects(rect, aiPaddle.getRect())) {
            velocityY = -velocityY;
            y += 2*velocityY;
        }

        rect.left = x;
        rect.top = y;
        rect.right = x + ballWidth;
        rect.bottom = y + ballHeight;
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawRect(rect, paint);
    }

}
