package com.msaggik.eighthsessionbeginnerartist11;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // создание полей
    private DrawingView drawingView; // создание поля созданного нами класса
    private ImageButton currPaint; // создание поля кнопки цвета краски в палитре
    private ImageButton drawButton; // создание поля кнопки кисти рисования
    private ImageButton eraseButton; // добавим поле кнопки ластика
    private ImageButton newButton; // добавим поле кнопки нового рисунка
    private float smallBrush, mediumBrush, largeBrush; // поля размеров кистей

    // создание дополнительных полей
    private ImageButton saveButton; // добавим поле кнопки сохранения рисунка


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // присваивание полям соответствующих id
        drawingView = findViewById(R.id.drawing);
        LinearLayout paintLayout = findViewById(R.id.paint_colors); // извлечение переменной контейнера по id

        // присваивание дополнительным полям соответствующих id
        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);
        drawButton = findViewById(R.id.fabBrush);
        eraseButton = findViewById(R.id.fabErase);
        newButton = findViewById(R.id.fabAdd);
        saveButton = findViewById(R.id.fabSave);

        currPaint = (ImageButton) paintLayout.getChildAt(0); // получение первой кнопки
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed)); // при выборе данной кнопки она должна изменить вид в соответствии с ресурсом

        // обработка нажатия кнопок
        drawButton.setOnClickListener(this);
        eraseButton.setOnClickListener(this);
        newButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    // метод выбора цвета
    public void paintClicked(View view){

        drawingView.setErase(false); // задание выбора кисти
        drawingView.setBrushSize(drawingView.getLastBrushSize()); // установление размера кисти до использования ластика

        // проверка выбранного цвета кнопки
        if(view != currPaint){
            // извлечение тега кнопки
            ImageButton imgView = (ImageButton) view;
            String color = view.getTag().toString();

            // задание цвета по извлечённому тегу кнопки
            drawingView.setColor(color);

            // обновление пользовательского интерфейса
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed)); // присваивание новой формы кнопке (формы выбора)
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint)); // возврат прошлой кнопке обычной формы
            currPaint = (ImageButton) view; // присваивание кнопки обратно view
        }
    }

    // слушатель размера кисти
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.fabBrush){ // если выбрана кнопка рисования
            // действия при нажатии кнопки кисти
            final Dialog brushDialog = new Dialog(this, R.style.Dialog); // создадим диалоговое окно выбора размера кисти
            brushDialog.setTitle("Размер кисти: "); // зададим заголовок диалогового окна
            brushDialog.setContentView(R.layout.brush_chooser); // добавим ресурс диалогового окна
            drawingView.setBrushSize(mediumBrush); // зададим средний размер кисти по умолчанию

            ImageButton smallBtn = brushDialog.findViewById(R.id.small_brush); // создание кнопки маленького размера кисти
            // добавим слушателя кнопки задания маленького размера кисти
            smallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawingView.setErase(false); // задание выбора кисти
                    drawingView.setBrushSize(smallBrush); // задание малого размера кисти
                    drawingView.setLastBrushSize(smallBrush); // задание последнего малого размера кисти
                    brushDialog.dismiss(); // закрытие диалога
                }
            });

            ImageButton mediumBtn = brushDialog.findViewById(R.id.medium_brush); // создание кнопки среднего размера кисти
            // добавим слушателя кнопки задания среднего размера кисти
            mediumBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawingView.setErase(false); // задание выбора кисти
                    drawingView.setBrushSize(mediumBrush); // задание среднего размера кисти
                    drawingView.setLastBrushSize(mediumBrush); // задание последнего среднего размера кисти
                    brushDialog.dismiss(); // закрытие диалога
                }
            });

            ImageButton largeBtn = brushDialog.findViewById(R.id.large_brush); // создание кнопки большого размера кисти
            // добавим слушателя кнопки задания большого размера кисти
            largeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawingView.setErase(false); // задание выбора кисти
                    drawingView.setBrushSize(largeBrush); // задание большого размера кисти
                    drawingView.setLastBrushSize(largeBrush); // задание последнего большого размера кисти
                    brushDialog.dismiss(); // закрытие диалога
                }
            });

            brushDialog.show(); // отображение на экране данного диалога
        } else if(view.getId() == R.id.fabErase) { // если выбрана кнопка ластика
            // действия при нажатии кнопки ластика
            final Dialog brushDialog = new Dialog(this, R.style.Dialog); // создадим диалоговое окно выбора размера ластика
            brushDialog.setTitle("Размер ластика: "); // зададим заголовок диалогового окна
            brushDialog.setContentView(R.layout.brush_chooser); // добавим ресурс диалогового окна
            drawingView.setErase(true); // задание выбора ластика
            drawingView.setBrushSize(mediumBrush); // зададим средний размер ластика по умолчанию

            ImageButton smallBtn = brushDialog.findViewById(R.id.small_brush); // создание кнопки маленького размера ластика
            // добавим слушателя кнопки задания маленького размера ластика
            smallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawingView.setErase(true); // задание выбора ластика
                    drawingView.setBrushSize(smallBrush); // задание последнего малого размера ластика
                    brushDialog.dismiss(); // закрытие диалога
                }
            });

            ImageButton mediumBtn = brushDialog.findViewById(R.id.medium_brush); // создание кнопки среднего размера ластика
            // добавим слушателя кнопки задания среднего размера ластика
            mediumBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawingView.setErase(true); // задание выбора ластика
                    drawingView.setBrushSize(mediumBrush); // задание последнего среднего размера ластика
                    brushDialog.dismiss(); // закрытие диалога
                }
            });

            ImageButton largeBtn = brushDialog.findViewById(R.id.large_brush); // создание кнопки большого размера ластика
            // добавим слушателя кнопки задания большого размера ластика
            largeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawingView.setErase(true); // задание выбора ластика
                    drawingView.setBrushSize(largeBrush); // задание последнего большого размера ластика
                    brushDialog.dismiss(); // закрытие диалога
                }
            });

            brushDialog.show(); // отображение на экране данного диалога
        } else if(view.getId()==R.id.fabAdd){ // если выбрана кнопка нового документа
            // код для создания нового документа
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this); // создание диалогового окна типа AlertDialog
            newDialog.setTitle("Новый рисунок"); // заголовок диалогового окна
            newDialog.setMessage("Новый рисунок (имеющийся будет удалён)?"); // сообщение диалога
            newDialog.setPositiveButton("Да", new DialogInterface.OnClickListener(){ // пункт выбора "да"
                public void onClick(DialogInterface dialog, int which){
                    drawingView.startNew(); // старт нового рисунка
                    dialog.dismiss(); // закрыть диалог
                }
            });
            newDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){ // пункт выбора "нет"
                    dialog.cancel(); // выход из диалога
                }
            });
            newDialog.show(); // отображение на экране данного диалога
        } else if(view.getId()==R.id.fabSave){ // если выбрана кнопка сохранения документа
            // код для сохранения документа
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this); // создание диалогового окна типа AlertDialog
            newDialog.setTitle("Сохранить"); // заголовок диалогового окна
            newDialog.setMessage("Сохранить рисунок?"); // сообщение диалога
            newDialog.setPositiveButton("Да", new DialogInterface.OnClickListener(){ // пункт выбора "да"
                public void onClick(DialogInterface dialog, int which){
                    // код сохранения рисунка
                    drawingView.setDrawingCacheEnabled(true); // сохраним кэш имеющегося рисунка

                    // сохранение изображения в файл
                    // (метод insertImage() записывает изображение в постоянную память устройства,
                    // UUID.randomUUID().toString() - генерирует случайную строку для названия имени файла)
                    String imageSaved = MediaStore.Images.Media.insertImage(
                            getContentResolver(), drawingView.getDrawingCache(),
                            UUID.randomUUID().toString()+".png", "drawing");

                    // вывод тоста информации о сохранении рисунка
                    if(imageSaved != null) { // если изображение сохранено, то вывод тоста об успешности сохранения
                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                "Изображение успешно сохранено в галлерею!", Toast.LENGTH_SHORT);
                        savedToast.show();
                    } else { // иначе, вывод тоста об неудачном сохранении
                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                "Сохранить изображение не удалось!", Toast.LENGTH_SHORT);
                        unsavedToast.show();
                    }
                    drawingView.destroyDrawingCache(); // удаление кэша рисунка
                }
            });
            newDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){ // пункт выбора "нет"
                    dialog.cancel(); // выход из диалога
                }
            });
            newDialog.show(); // отображение на экране данного диалога
        }
    }
}