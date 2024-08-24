package ru.infoza.ponggame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Random;

public class Ball {

    private final RectF rect;
    private final float ballWidth;
    private final float ballHeight;
    private final int screenX;
    private final int screenY;
    private final PongView pongView;
    private final Random random = new Random();
    private float x;
    private float y;
    private float velocityX, velocityY;
    private float ACCELERATION_FACTOR = 1;

    public Ball(int screenX, int screenY, PongView pongView) {
        this.pongView = pongView;
        this.screenX = screenX;
        this.screenY = screenY;
        ballWidth = (float) screenX / 50;
        ballHeight = (float) screenX / 50;
        setInTheCenter();
        rect = new RectF(x, y, x + ballWidth, y + ballHeight);
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    private void setInTheCenter() {
        ACCELERATION_FACTOR = 1;
        x = (float) screenX / 2;
        y = (float) screenY / 2;
        setStartVelocityX();
        setStartVelocityY();
    }

    private void setToPlayerPaddle(Paddle paddle) {
        ACCELERATION_FACTOR = 1;
        x = paddle.getX() + paddle.getLength() / 2;
        y = paddle.getY() - paddle.getHeight();
        setStartVelocityX();
        setVelocityY(4);
    }

    private void setToAiPaddle(Paddle paddle) {
        ACCELERATION_FACTOR = 1;
        x = paddle.getX() + paddle.getLength() / 2;
        y = paddle.getY() + paddle.getHeight();
        setStartVelocityX();
        setVelocityY(-4);
    }

    private void setStartVelocityX() {
        velocityX = 4.0F;
        velocityX *= random.nextBoolean() ? -1 : 1;
    }

    private void setStartVelocityY() {
        velocityY = 4.0F;
        velocityY *= random.nextBoolean() ? -1 : 1;
    }

    public void update(Paddle playerPaddle, Paddle aiPaddle) {
        x += velocityX * ACCELERATION_FACTOR;
        y += velocityY * ACCELERATION_FACTOR;

        if (rect.left < 0 || rect.right > screenX) {
            velocityX = -velocityX;
            x += 2 * velocityX * ACCELERATION_FACTOR;
        }

        if (rect.bottom > screenY) { // bottom
            pongView.incrementAiScore();
            setToPlayerPaddle(playerPaddle);
        }

        if (rect.top < 0) { // up
            pongView.incrementPlayerScore();
            setToAiPaddle(aiPaddle);
        }

        if (RectF.intersects(rect, playerPaddle.getRect()) || RectF.intersects(rect, aiPaddle.getRect())) {
            velocityY = -velocityY;
            y += 2 * velocityY * ACCELERATION_FACTOR;
            ACCELERATION_FACTOR += 0.3F;
        }

        rect.left = x;
        rect.top = y;
        rect.right = x + ballWidth;
        rect.bottom = y + ballHeight;
    }

    public void draw(Canvas canvas, Paint paint) {
        float ballSize = 30;
//        canvas.drawRect(rect, paint);
        canvas.drawCircle(x + ballSize / 2, y + ballSize / 2, ballSize / 2, paint);
    }

    public float getX() {
        return x;
    }

}
