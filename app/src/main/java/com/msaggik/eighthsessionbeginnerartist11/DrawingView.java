package com.msaggik.eighthsessionbeginnerartist11;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {

    // создание полей
    private Path drawPath; // путь для рисунка (траектория касания пальцем)
    private Paint drawPaint, canvasPaint; // рисунок и краска на холсте
    private int paintColor = 0xFF660000; // цвет по умолчанию
    private Canvas drawCanvas; // canvas
    private Bitmap canvasBitmap; // canvas bitmap
    private float brushSize, lastBrushSize; // поля размера кисти (brushSize - размер кисти, lastBrushSize - последний размер кисти)

    // создание дополнительных полей
    private boolean erase = false; // поле флага действия стирания или рисования (ластик или кисть) false - кисть, true - ластик

    // метод запуска нового документа
    public void startNew() {
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR); // очистка холста
        invalidate(); // обновление окна отображения рисунка
    }

    // добавим сеттер поля флага действия стирания или рисования
    public void setErase(boolean erase) {
        this.erase = erase;
        // параметр рисования (ластик или кисть)
        if(erase) { // если выбор ластика
            drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR)); // то назначается кисть очищения экрана
        } else { // иначе
            drawPaint.setXfermode(null); // обычная кисть
        }
    }

    // конструктор родительского класса
    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    // метод вызывается когда пользовательскому представлению присваивается размер
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888); // задание растрового изображения с шириной w и длиной h
        drawCanvas = new Canvas(canvasBitmap); // экземпляр холста
    }

    // метод позволяющий классу функционировать как представление
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // холст и контур рисования
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    // необходимо добавить слушателя событий касания экрана
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //return super.onTouchEvent(event);

        // извлечение координат касания экрана
        float touchX = event.getX();
        float touchY = event.getY();

        // обработка движения касания экрана вниз, движения и вверх
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }

        invalidate(); // анулирование View
        return true;
    }

    // метод настройки области рисования для взаимодействия
    private void setupDrawing() {
        drawPath = new Path(); // создание объекта траектории касания пальцем
        drawPaint = new Paint(); // создание объекта рисунка и краски

        drawPaint.setColor(paintColor); // установление цвета по умолчанию

        // установление параметров кисти
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        // создание экземпляра размера кисти
        brushSize = getResources().getInteger(R.integer.medium_size); // установление размера кисти по умолчанию
        lastBrushSize = brushSize; // присваивание последней кисти значения кисти по умолчанию

        drawPaint.setStrokeWidth(brushSize); // задание размера кисти

        canvasPaint = new Paint(Paint.DITHER_FLAG); // создание объекта класса Canvas
    }

    // метод изменения размера кисти
    public void setBrushSize(float newSize) {
        // изменение размера кисти
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics()); // обновление размера кисти по параметру newSize
        brushSize = pixelAmount; // переопределение brushSize
        drawPaint.setStrokeWidth(brushSize); // задание в параметры кисти нового размера
    }
    // геттер и сеттер поля lastBrushSize (они потребуются потом в MainActivity)
    public float getLastBrushSize() {
        return lastBrushSize;
    }
    public void setLastBrushSize(float lastBrushSize) {
        this.lastBrushSize = lastBrushSize;
    }

    // метод задания цвета
    public void setColor(String newColor){
        invalidate(); // признание представления недействительным
        // далее разбираем и устанавливаем цвет для рисования
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }
}
