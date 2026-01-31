package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    Context context;
    private final LayoutInflater inflater;
    private final List<File> photoFiles;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);
    private OnItemClickListener listener;

    // ИСПРАВЛЕННЫЙ ИНТЕРФЕЙС - добавлен onItemLongClick
    public interface OnItemClickListener {
        void onItemClick(File photoFile);           // Обычный клик - открыть фото
        void onItemLongClick(File photoFile, int position);  // Долгое нажатие - удалить
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    PhotoAdapter(Context context, List<File> photoFiles){
        this.photoFiles = photoFiles;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void addPhoto(File photoFile) {
        photoFiles.add(0, photoFile);
        notifyItemInserted(0);
    }

    public void setPhotos(List<File> files) {
        photoFiles.clear();
        photoFiles.addAll(files);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.photo_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File photoFile = photoFiles.get(position);
        holder.bind(photoFile, position);
    }

    @Override
    public int getItemCount() {
        return photoFiles.size();
    }

    // МЕТОД ДЛЯ УДАЛЕНИЯ ФОТО ИЗ АДАПТЕРА (публичный)
    public void removePhoto(int position) {
        if (position >= 0 && position < photoFiles.size()) {
            photoFiles.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, photoFiles.size() - position);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        private int currentPosition; // Сохраняем позицию

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);

            // ОБЫЧНЫЙ КЛИК - открыть фото
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(photoFiles.get(position));
                    openPhotoInNewLayout(photoFiles.get(position));
                }
            });

            // ДОЛГОЕ НАЖАТИЕ - удалить фото
            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemLongClick(photoFiles.get(position), position);
                    return true; // Важно: возвращаем true
                }
                return false;
            });
        }

        private void openPhotoInNewLayout(File photoFile) {
            // Создаем Intent
            Intent intent = new Intent(context, ShowSelected.class);

            // Передаем путь к фото
            intent.putExtra("PHOTO_PATH", photoFile.getAbsolutePath());

            // Запускаем Activity
            context.startActivity(intent);
        }
        private void showDeleteDialog(File photoFile, int position) {
            new AlertDialog.Builder(itemView.getContext())
                    .setTitle("Удалить фото?")
                    .setMessage("Удалить \"" + photoFile.getName() + "\"?")
                    .setPositiveButton("Удалить", (dialog, which) -> {
                        if (photoFile.delete()) {
                            // Используем публичный метод адаптера
                            PhotoAdapter.this.removePhoto(position);
                            Toast.makeText(itemView.getContext(),
                                    "Фото удалено", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Отмена", null)
                    .show();
        }

        public void bind(File photoFile, int position) {
            this.currentPosition = position;
            loadImage(photoFile);
        }

        private void loadImage(File photoFile) {
            if (!photoFile.exists()) {
                itemView.post(() -> {
                    imageView.setImageResource(android.R.drawable.ic_menu_camera);
                });
                return;
            }

            imageView.setTag(photoFile.getAbsolutePath());

            executorService.execute(() -> {
                try {
                    if (!photoFile.exists()) {
                        return;
                    }

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;

                    Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), options);

                    if (imageView.getTag().equals(photoFile.getAbsolutePath())) {
                        itemView.post(() -> {
                            if (bitmap != null) {
                                imageView.setImageBitmap(bitmap);
                            } else {
                                imageView.setImageResource(android.R.drawable.ic_menu_camera);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    itemView.post(() -> {
                        imageView.setImageResource(android.R.drawable.ic_menu_report_image);
                    });
                }
            });
        }
    }
}