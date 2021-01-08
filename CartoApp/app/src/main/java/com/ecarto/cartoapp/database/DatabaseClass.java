package com.ecarto.cartoapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ecarto.cartoapp.R;
import com.ecarto.cartoapp.database.DAOs.FileDAO;
import com.ecarto.cartoapp.database.DAOs.InvoiceDAO;
import com.ecarto.cartoapp.database.DAOs.InvoiceDetailDAO;
import com.ecarto.cartoapp.database.DAOs.ProjectDAO;
import com.ecarto.cartoapp.database.DAOs.UserDAO;
import com.ecarto.cartoapp.database.Entities.FileEntity;
import com.ecarto.cartoapp.database.Entities.InvoiceDetailEntity;
import com.ecarto.cartoapp.database.Entities.InvoiceEntity;
import com.ecarto.cartoapp.database.Entities.ProjectEntity;
import com.ecarto.cartoapp.database.Entities.UserEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
        InvoiceEntity.class,
        InvoiceDetailEntity.class,
        FileEntity.class,
        ProjectEntity.class,
        UserEntity.class
}, version = 1, exportSchema = false)

@TypeConverters({})
public abstract class DatabaseClass extends RoomDatabase {
    public abstract InvoiceDAO invoiceDAO();
    public abstract InvoiceDetailDAO invoiceDetailDAO();
    public abstract FileDAO fileDAO();
    public abstract ProjectDAO projectDAO();
    public abstract UserDAO userDAO();

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
