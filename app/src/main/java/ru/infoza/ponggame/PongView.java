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

import androidx.annotation.NonNull;

public class PongView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

//    private static final String TAG = "PongGame";
    private static final int UPDATE_INTERVAL = 3;
    private Thread gameThread = null;
    private final SurfaceHolder holder;
    private boolean playing;
    private final Paint paint;
    private int screenX, screenY;
    private Paddle playerPaddle;
    private Paddle aiPaddle;
    private Ball ball;
    private int frameCount = 0;
    private int playerScore = 0;
    private int aiScore = 0;

    private float middleScreenX;
    private float middleScreenY;

    public PongView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        paint = new Paint();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        screenX = getWidth();
        screenY = getHeight();
        middleScreenX = (float) screenX / 2;
        middleScreenY = (float) screenY / 2;
        playerPaddle = new Paddle(screenX, screenY, true);
        aiPaddle = new Paddle(screenX, screenY, false);
        ball = new Ball(screenX, screenY, this);
        resume();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        // Реагируем на изменение размеров экрана
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        pause();
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

        smoothUpdateAiPaddle();
//        updateAiPaddle();
        frameCount++;
        if (frameCount % UPDATE_INTERVAL == 0) {
            aiPaddle.update();
        }

        ball.update(playerPaddle, aiPaddle);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (!playing) {
            paint.setColor(Color.WHITE);
            paint.setTextSize(100);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("Пауза", middleScreenX, middleScreenY, paint);
            String score = String.format("Счет: %s - %s", playerScore, aiScore);
            canvas.drawText(score, middleScreenX, middleScreenY + 100, paint);
        } else {
            canvas.drawColor(Color.BLACK);
            paint.setColor(Color.WHITE);
            playerPaddle.draw(canvas, paint);
            aiPaddle.draw(canvas, paint);
            ball.draw(canvas, paint);
        }
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

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                playerPaddle.setMovementState(Paddle.MovementState.MOVE_LEFT);
//                break;
//            case MotionEvent.ACTION_UP:
//                playerPaddle.setMovementState(Paddle.MovementState.STOPPED);
//                break;
//        }
//        return true;
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Log.d(TAG, "KeyCode: " + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_BUTTON_START:
            case KeyEvent.KEYCODE_BACK:
                if (playing) {
                    pause();
                } else {
                    resume();
                }
                return true;
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

    public void incrementPlayerScore() {
        playerScore++;
    }

    public void incrementAiScore() {
        aiScore++;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int score) {
        this.playerScore = score;
    }

    public int getAiScore() {
        return aiScore;
    }

    public void setAiScore(int score) {
        this.aiScore = score;
    }

    private void updateAiPaddle() {
        // Получение текущей позиции мяча и AI-плэйера
        float ballX = ball.getX();
        float aiPaddleX = aiPaddle.getX();

        // Вводим "ленивость" в движение AI
//        int lag = 5; // Чем больше значение, тем медленнее реакция
        // Определение скорости движения AI-плэйера
        int paddleSpeed = 10;

        // Если мяч левее AI-плэйера, двигаем AI-плэйер влево
//        if (ballX < aiPaddleX + aiPaddle.getLength() / 2 + lag) {
        if (ballX < aiPaddleX) {
            aiPaddle.setX(aiPaddleX - paddleSpeed);
        }
        // Если мяч ниже AI-плэйера, двигаем AI-плэйер вниз
//        else if (ballX > aiPaddleX + aiPaddle.getLength() / 2 + lag) {
        else if (ballX > aiPaddleX) {
            aiPaddle.setX(aiPaddleX + paddleSpeed);
        }

        // Убедитесь, что AI-плэйер не выходит за границы экрана
        if (aiPaddle.getX() < 0) {
            aiPaddle.setX(0);
        } else if (aiPaddle.getX() + aiPaddle.getLength() > screenX) {
            aiPaddle.setX(screenX - aiPaddle.getLength());
        }
    }

    private void smoothUpdateAiPaddle() {
        float ballX = ball.getX();
        float aiPaddleX = aiPaddle.getX();
        float aiPaddleLength = aiPaddle.getLength();
        int maxSpeed = 10;
        int acceleration = 1;

        // Вычисляем целевое положение
        float targetX = Math.max(0, Math.min(screenX - aiPaddleLength, ballX - aiPaddleLength / 2));

        // Ускорение
        if (aiPaddleX < targetX) {
            aiPaddle.setX(Math.min(aiPaddleX + maxSpeed, targetX));
            maxSpeed -= acceleration;
        } else if (aiPaddleX > targetX) {
            aiPaddle.setX(Math.max(aiPaddleX - maxSpeed, targetX));
            maxSpeed -= acceleration;
        }

        // Убедитесь, что AI-плэйер не выходит за границы экрана
        if (aiPaddle.getX() < 0) {
            aiPaddle.setX(0);
        } else if (aiPaddle.getX() + aiPaddle.getLength() > screenX) {
            aiPaddle.setX(screenX - aiPaddle.getLength());
        }
    }

}