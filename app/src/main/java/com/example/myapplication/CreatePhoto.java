package com.example.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreatePhoto extends AppCompatActivity {

    private FloatingActionButton mButton;
    private RecyclerView recyclerView;
    private PhotoAdapter photoAdapter;
    private Uri mUri;
    private String mCurrentPhotoPath;

    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.CAMERA
    };

    private final ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // Даем время на сохранение файла
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        addNewPhotoToRecyclerView();
                    }, 500);
                } else {
                    Toast.makeText(this, "Съемка фото отменена", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_photo);

        mButton = findViewById(R.id.floatingActionButton);
        recyclerView = findViewById(R.id.rvList);

        setupRecyclerView();
        loadExistingPhotos();

        mButton.setOnClickListener(v -> {
            if (checkPermissions()) {
                openCamera();
            }
        });
    }

    private void setupRecyclerView() {
        // Настраиваем RecyclerView с 2 колонками
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Создаем адаптер с пустым списком
        photoAdapter = new PhotoAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(photoAdapter);

        // Устанавливаем обработчики кликов
        photoAdapter.setOnItemClickListener(new PhotoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(File photoFile) {
                // Обычный клик - открыть фото
                openPhoto(photoFile);
            }

            @Override
            public void onItemLongClick(File photoFile, int position) {
                // Долгое нажатие - показать диалог удаления
                showDeleteDialog(photoFile, position);
            }
        });
    }

    // МЕТОД ДЛЯ ЗАГРУЗКИ СУЩЕСТВУЮЩИХ ФОТО
    private void loadExistingPhotos() {
        File photoFolder = new File(
                getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "Photos/Project/Defects"
        );

        if (photoFolder.exists() && photoFolder.isDirectory()) {
            File[] files = photoFolder.listFiles((dir, name) ->
                    name.toLowerCase().endsWith(".jpg") ||
                            name.toLowerCase().endsWith(".jpeg") ||
                            name.toLowerCase().endsWith(".png")
            );

            if (files != null && files.length > 0) {
                // Сортируем по дате (новые сверху)
                Arrays.sort(files, (f1, f2) ->
                        Long.compare(f2.lastModified(), f1.lastModified())
                );

                List<File> photoList = Arrays.asList(files);
                photoAdapter.setPhotos(photoList);

                Toast.makeText(this,
                        "Загружено фото: " + files.length,
                        Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(this, "В папке нет фото", Toast.LENGTH_SHORT).show();
            }
        } else {
            //Toast.makeText(this, "Папка не существует", Toast.LENGTH_SHORT).show();
        }
    }

    // МЕТОД ДЛЯ ДОБАВЛЕНИЯ НОВОГО ФОТО В RECYCLERVIEW
    private void addNewPhotoToRecyclerView() {
        if (mCurrentPhotoPath == null) {
            //Toast.makeText(this, "Путь к фото не найден", Toast.LENGTH_SHORT).show();
            return;
        }

        File newPhoto = new File(mCurrentPhotoPath);

        if (newPhoto.exists()) {
            // Добавляем фото в адаптер
            photoAdapter.addPhoto(newPhoto);

            // Прокручиваем к началу
            recyclerView.smoothScrollToPosition(0);

            // Добавляем в галерею
            addToGallery();

            Toast.makeText(this,
                    "Фото сохранено: " + newPhoto.getName(),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Если файл не найден, обновляем весь список
            //Toast.makeText(this, "Файл не найден, обновляю список...", Toast.LENGTH_SHORT).show();
            loadExistingPhotos();
        }
    }

    // МЕТОД ДЛЯ ПОКАЗА ДИАЛОГА УДАЛЕНИЯ
    private void showDeleteDialog(File photoFile, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Удалить фото?")
                .setMessage("Удалить \"" + photoFile.getName() + "\"?")
                .setPositiveButton("Удалить", (dialog, which) -> {
                    deletePhoto(photoFile, position);
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    // МЕТОД ДЛЯ УДАЛЕНИЯ ФОТО
    private void deletePhoto(File photoFile, int position) {
        if (!photoFile.exists()) {
            Toast.makeText(this, "Файл уже удален", Toast.LENGTH_SHORT).show();
            photoAdapter.removePhoto(position);
            return;
        }

        if (photoFile.delete()) {
            // Удаляем из адаптера
            photoAdapter.removePhoto(position);

            // Обновляем галерею
            updateGallery(photoFile);

            Toast.makeText(this, "Фото удалено", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Ошибка удаления", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateGallery(File deletedFile) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(deletedFile);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    private void openPhoto(File photoFile) {
        // Открыть фото
        Toast.makeText(this, "Открыто: " + photoFile.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Обновляем список при возвращении в приложение

    }

    // ===================== Camera Methods =====================

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Разрешите доступ к камере", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = createImageFileWithStructure();

            if (photoFile != null) {
                mUri = FileProvider.getUriForFile(this,
                        getApplicationContext().getPackageName() + ".provider",
                        photoFile);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION |
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                takePictureLauncher.launch(intent);
            } else {
                Toast.makeText(this, "Ошибка создания файла", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Не найдено приложение камеры", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFileWithStructure() {
        return createInPublicStorage();
    }

    private File createInPublicStorage() {
        File storageDir = new File(
                getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "Photos/Project/Defects"
        );
        return createFileInDirectory(storageDir, "DEFECT_");
    }

    private File createFileInDirectory(File directory, String prefix) {
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                Log.e("FILE_CREATION", "Не удалось создать папки: " + directory.getAbsolutePath());
                Toast.makeText(this, "Ошибка создания папок", Toast.LENGTH_LONG).show();
                return null;
            }

            Log.d("FILE_CREATION", "Созданы папки: " + directory.getAbsolutePath());
            Toast.makeText(this, "Создана папка: " + directory.getName(), Toast.LENGTH_SHORT).show();
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(new Date());
        String fileName = prefix + timeStamp + ".jpg";

        File imageFile = new File(directory, fileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();

        Log.d("FILE_PATH", "Файл будет сохранен: " + mCurrentPhotoPath);

        return imageFile;
    }

    private void showFileInfo() {
        if (mCurrentPhotoPath != null) {
            File file = new File(mCurrentPhotoPath);

            if (file.exists()) {
                String info = String.format(
                        "Фото сохранено!\n" +
                                "Папка: Pictures/Photos/Project/Defects/\n" +
                                "Файл: %s\n" +
                                "Размер: %d KB",
                        file.getName(),
                        file.length() / 1024
                );

                Toast.makeText(this, info, Toast.LENGTH_LONG).show();

                Log.d("SAVED_PHOTO", "Полный путь: " + mCurrentPhotoPath);
                Log.d("SAVED_PHOTO", "Размер файла: " + file.length() + " байт");
            }
        }
    }

    private void addToGallery() {
        if (mCurrentPhotoPath != null) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(mCurrentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            sendBroadcast(mediaScanIntent);

            Log.d("GALLERY", "Фото добавлено в галерею: " + mCurrentPhotoPath);
        }
    }
}