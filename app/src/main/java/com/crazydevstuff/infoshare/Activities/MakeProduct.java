package com.crazydevstuff.infoshare.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.crazydevstuff.infoshare.Models.ProductModel;
import com.crazydevstuff.infoshare.Models.RegisterModel;
import com.crazydevstuff.infoshare.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class MakeProduct extends AppCompatActivity {
    private ImageView backIcon;
    private ImageView itemDisplay;

    private TextInputEditText itemName,itemDescription,itemPrice;

    private ImageView selectImage;
    private Uri filePath;
    private Button saveItem;
    private ProgressBar progressBar;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private StorageTask storageTask;
    private FirebaseAuth firebaseAuth;
    Intent tempIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_product);

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_sell_prod);

        tempIntent=getIntent();
        firebaseAuth = FirebaseAuth.getInstance();
        itemDescription=findViewById(R.id.itemDescpET);
        itemName=findViewById(R.id.itemNameET);
        itemPrice=findViewById(R.id.itemPriceTV);
        progressBar=findViewById(R.id.progressBar);
        saveItem = findViewById(R.id.saveButton);
        itemDisplay = findViewById(R.id.item_displayIV);
        selectImage = findViewById(R.id.selectImageIV);
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItemImage();
            }
        });
        backIcon = findViewById(R.id.bacToMain_icon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        storageReference= FirebaseStorage.getInstance().getReference("uploads");
        databaseReference= FirebaseDatabase.getInstance().getReference("uploads");
        firebaseAuth=FirebaseAuth.getInstance();
        saveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(storageTask!=null&&storageTask.isInProgress()){
                    Toast.makeText(MakeProduct.this,"Please wait until the previous job is finished",Toast.LENGTH_SHORT).show();
                }else{
                    saveImage();
                }
            }
        });





    }
    public void saveImage(){
        if(filePath!=null){
            final String uploadId=databaseReference.push().getKey();
            final StorageReference fileReference=storageReference.child(uploadId+"."+getFileExtension(filePath));
            storageTask=fileReference.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.VISIBLE);
                            progressBar.setProgress(0);
                            Toast.makeText(MakeProduct.this,"Upload successful!",Toast.LENGTH_SHORT).show();
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUri=uri;
                                    String username=tempIntent.getStringExtra("username");
                                    String imageURL=downloadUri.toString();
                                    String description=itemDescription.getText().toString();
                                    String price=itemPrice.getText().toString();
                                    String name=itemName.getText().toString();
                                    String email = firebaseAuth.getCurrentUser().getEmail();
                                    ProductModel product=new ProductModel(name,description,imageURL,username,Integer.parseInt(price),email);
                                    product.setKey(uploadId);
                                    databaseReference.child(uploadId).setValue(product);
                                    finish();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MakeProduct.this,"Upload Failed!",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress=(100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                            progressBar.setProgress((int)progress);
                        }
                    });
        }else{
            Toast.makeText(MakeProduct.this,"File not selected!",Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri fileUri) {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }

    private void selectItemImage(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, 1);
    }
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {
        super.onActivityResult(requestCode,
                resultCode,
                data);
        if (requestCode == 1
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            itemDisplay.setVisibility(View.VISIBLE);
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                itemDisplay.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}