package com.crazydevstuff.infoshare.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.crazydevstuff.infoshare.DAO.CacheProductsDAO;
import com.crazydevstuff.infoshare.Models.ProductModel;

@Database(entities = ProductModel.class,version = 2)
public abstract class ProductsDatabase extends RoomDatabase {
    private static ProductsDatabase instance;
    public abstract CacheProductsDAO cachedProductsDAO();

    public static synchronized ProductsDatabase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context,ProductsDatabase.class,"Info_Share")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
