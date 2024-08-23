package ru.infoza.ponggame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PongView extends SurfaceView implements Runnable {

    private Thread gameThread = null;
    private SurfaceHolder holder;
    private boolean playing;
    private Paint paint;
    private int screenX, screenY;
    private Paddle playerPaddle, aiPaddle;
    private Ball ball;

    public PongView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        paint = new Paint();
        playerPaddle = new Paddle(screenX, screenY, true);
        aiPaddle = new Paddle(screenX, screenY, false);
        ball = new Ball(screenX, screenY);
    }

    @Override
    public void run() {
        while (playing) {
            if (holder.getSurface().isValid()) {
                Canvas canvas = holder.lockCanvas();
                update();
                draw(canvas);
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void update() {
        playerPaddle.update();
        aiPaddle.update();
        ball.update(playerPaddle, aiPaddle);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.BLACK);
        paint.setColor(Color.WHITE);
        playerPaddle.draw(canvas, paint);
        aiPaddle.draw(canvas, paint);
        ball.draw(canvas, paint);
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                playerPaddle.setMovementState(Paddle.MovementState.MOVE_LEFT);
                break;
            case MotionEvent.ACTION_UP:
                playerPaddle.setMovementState(Paddle.MovementState.STOPPED);
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                playerPaddle.setMovementState(Paddle.MovementState.MOVE_LEFT);
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                playerPaddle.setMovementState(Paddle.MovementState.MOVE_RIGHT);
                return true;
            case KeyEvent.KEYCODE_BUTTON_A:
                // Действие при нажатии кнопки A
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        playerPaddle.setMovementState(Paddle.MovementState.STOPPED);
        return super.onKeyUp(keyCode, event);
    }

}