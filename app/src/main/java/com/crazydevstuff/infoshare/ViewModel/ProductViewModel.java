package com.crazydevstuff.infoshare.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.crazydevstuff.infoshare.Models.ProductModel;
import com.crazydevstuff.infoshare.Repository.ProductsRepository;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {
    private ProductsRepository repository;
    private LiveData<List<ProductModel>> allCachedProducts;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        repository=new ProductsRepository(application);
        allCachedProducts=repository.getAllCachedProducts();
    }

    public void addCache(ProductModel product){
        repository.addCache(product);
    }

    public void deleteCache(ProductModel product){
        repository.deleteCache(product);
    }

    public void deleteAllCache(){
        repository.deleteAllCache();
    }

    public LiveData<List<ProductModel>> getAllCachedProducts(){
        return allCachedProducts;
    }
}
