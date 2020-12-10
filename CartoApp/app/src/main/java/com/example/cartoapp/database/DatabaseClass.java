package com.example.cartoapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.cartoapp.R;
import com.example.cartoapp.database.DAOs.InvoiceDAO;
import com.example.cartoapp.database.Entities.InvoiceEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
        InvoiceEntity.class
}, version = 1, exportSchema = false)

@TypeConverters({})
public abstract class DatabaseClass extends RoomDatabase {
    public abstract InvoiceDAO invoiceDAO();

    private static volatile DatabaseClass INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static DatabaseClass getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DatabaseClass.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room
                            .databaseBuilder(context.getApplicationContext(), DatabaseClass.class, context.getString(R.string.database_name))
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
