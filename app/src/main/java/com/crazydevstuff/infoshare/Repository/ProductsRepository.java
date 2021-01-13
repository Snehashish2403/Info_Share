package com.crazydevstuff.infoshare.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.crazydevstuff.infoshare.DAO.CacheProductsDAO;
import com.crazydevstuff.infoshare.Database.ProductsDatabase;
import com.crazydevstuff.infoshare.Models.ProductModel;

import java.util.List;

public class ProductsRepository {
    private CacheProductsDAO cacheProductsDAO;
    private LiveData<List<ProductModel>> allCachedProducts;

    public ProductsRepository(Application application){
        ProductsDatabase database=ProductsDatabase.getInstance(application);
        cacheProductsDAO =database.cachedProductsDAO();
        allCachedProducts= cacheProductsDAO.getAllProducts();
    }

    public void addCache(ProductModel product){
            new AddCacheAsyncTask(cacheProductsDAO).execute(product);
    }

    public void deleteCache(ProductModel product){
            new DeleteCacheAsyncTask(cacheProductsDAO).execute(product);
    }

    public void deleteAllCache(){
            new DeleteAllCacheAsyncTask(cacheProductsDAO).execute();
    }

    public LiveData<List<ProductModel>> getAllCachedProducts(){
        return allCachedProducts;
    }

    private static class AddCacheAsyncTask extends AsyncTask<ProductModel,Void,Void>{
        private CacheProductsDAO cacheDAO;
        private AddCacheAsyncTask(CacheProductsDAO cacheDAO){
            this.cacheDAO=cacheDAO;
        }

        @Override
        protected Void doInBackground(ProductModel... productModels) {
            cacheDAO.insert(productModels[0]);
            return null;
        }
    }

    private static class DeleteCacheAsyncTask extends AsyncTask<ProductModel,Void,Void>{
        private CacheProductsDAO cacheDAO;
        private DeleteCacheAsyncTask(CacheProductsDAO cacheDAO){
            this.cacheDAO=cacheDAO;
        }

        @Override
        protected Void doInBackground(ProductModel... productModels) {
            cacheDAO.delete(productModels[0]);
            return null;
        }
    }

    private static class DeleteAllCacheAsyncTask extends AsyncTask<Void,Void,Void>{
        private CacheProductsDAO cacheDAO;
        private DeleteAllCacheAsyncTask(CacheProductsDAO cacheDAO){
            this.cacheDAO=cacheDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            cacheDAO.deleteAllProducts();
            return null;
        }
    }
}
