package com.crazydevstuff.infoshare.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.crazydevstuff.infoshare.Models.ProductModel;

import java.util.List;

@Dao
public interface CacheProductsDAO {
    @Insert
    void insert(ProductModel product);

    @Update
    void update(ProductModel product);

    @Delete
    void delete(ProductModel product);

    @Query("DELETE FROM saved_products")
    void deleteAllProducts();

    @Query("SELECT * FROM saved_products")
    LiveData<List<ProductModel>> getAllProducts();
}
