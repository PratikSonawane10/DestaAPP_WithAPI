package com.choicespropertysolutions.desta;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.choicespropertysolutions.desta.Adapter.SpinnerItemsAdapter;
import com.choicespropertysolutions.desta.Connectivity.ImagesFormUpload;
import com.choicespropertysolutions.desta.CropImage.CropImage;
import com.choicespropertysolutions.desta.InternetConnectivity.NetworkChangeReceiver;
import com.choicespropertysolutions.desta.SessionManager.SessionManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.Manifest;

public class ImageUploadForm extends AppCompatActivity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int CAMERA_REQUEST = 1;
    private static final int GALLERY_REQUEST = 2;
    private static final int PIC_CAMERA_CROP = 3;
    private static final int PIC_GALLERY_CROP = 4;
    private static final int CAMERA_PERMISSION_REQUEST = 5;
    private static final int READ_STORAGE_PERMISSION_REQUEST = 6;
    private static final int WRITE_STORAGE_PERMISSION_REQUEST = 7;

    private ProgressDialog progressDialog = null;

    public AlertDialog alertDialog;
    public ArrayAdapter<String> dialogAdapter;

    //Spinner imagesCategory;
    Button selectImageButton;
    ImageView firstImageOfPet;
    ImageView secondImageOfPet;
    ImageView thirdImageOfPet;
    ImageView fourthImageOfPet;
    ImageView fifthImageOfPet;
    FloatingActionButton uploadFabButton;

    //String imageCategoryName;
    String currentImagePath;

    //private List<String> imageCategoryList = new ArrayList<String>();
    //private SpinnerItemsAdapter adapter;
    //String[] imageCategoryArrayList;

    Bitmap imageToShow;
    String timeStamp;
    File storageDir;
    File originalFile;
    File cropFile;
    String firstImagePath = "";
    String secondImagePath = "";
    String thirdImagePath = "";
    String fourthImagePath = "";
    String fifthImagePath = "";

    private long TIME = 5000;
    private Toolbar imageFormUploadToolbar;
    private String phone;
    private String state;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_upload_form);

        imageFormUploadToolbar = (Toolbar) findViewById(R.id.imageFormUploadToolbar);
        setSupportActionBar(imageFormUploadToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageFormUploadToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //imagesCategory = (Spinner) this.findViewById(R.id.imagesCategory);
        selectImageButton = (Button) this.findViewById(R.id.selectImage);
        firstImageOfPet = (ImageView) this.findViewById(R.id.firstImageOfPet);
        secondImageOfPet = (ImageView) this.findViewById(R.id.secondImageOfPet);
        thirdImageOfPet = (ImageView) this.findViewById(R.id.thirdImageOfPet);
        fourthImageOfPet = (ImageView) this.findViewById(R.id.fourthImageOfPet);
        fifthImageOfPet = (ImageView) this.findViewById(R.id.fifthImageOfPet);
        uploadFabButton = (FloatingActionButton) this.findViewById(R.id.imageFormSubmitFab);

        //GenarateSpinerForImageCategory();

        SessionManager sessionManager = new SessionManager(this.getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        phone = user.get(SessionManager.KEY_PHONE);
        state = user.get(SessionManager.KEY_STATE);

        selectImageButton.setOnClickListener(this);
        uploadFabButton.setOnClickListener(this);
        uploadFabButton.setVisibility(View.GONE);

        firstImageOfPet.setOnClickListener(this);
        secondImageOfPet.setOnClickListener(this);
        thirdImageOfPet.setOnClickListener(this);
        fourthImageOfPet.setOnClickListener(this);
        fifthImageOfPet.setOnClickListener(this);
    }

    /*public void GenarateSpinerForImageCategory(){
        imageCategoryArrayList = new String[]{
                "Select Image Category"
        };
        imageCategoryList = new ArrayList<>(Arrays.asList(imageCategoryArrayList));
        for(int i=0;i<=11;i++){
            String j=String.valueOf(i);
            imageCategoryList.add(j);
        }
        adapter = new SpinnerItemsAdapter(this, R.layout.spinneritem, imageCategoryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        imagesCategory.setAdapter(adapter);
        imagesCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                if(position > 0) {
                    imageCategoryName = parent.getItemAtPosition(position).toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }*/

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(final View v) {
        v.setEnabled(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                v.setEnabled(true);
            }
        }, TIME);

        if(v.getId() == R.id.selectImage) {
            if(fifthImageOfPet.getDrawable() != null) {
                Toast.makeText(ImageUploadForm.this, "Can not select more than 5 images", Toast.LENGTH_LONG).show();
                selectImageButton.setClickable(false);
            }
            else {
                if(ActivityCompat.checkSelfPermission(ImageUploadForm.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestWriteStoragePermission();
                }
                else {
                    if(ActivityCompat.checkSelfPermission(ImageUploadForm.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestReadStoragePermission();
                    }
                    else {
                        createImageFormSelectImageDialogChooser();
                    }
                }
            }
        }
        else if(v.getId() == R.id.imageFormSubmitFab) {
            /*if (imageCategoryName == null || imageCategoryName.isEmpty()) {
                Toast.makeText(ImageUploadForm.this, "Please select Image Category.", Toast.LENGTH_LONG).show();
                TextView errorText = (TextView) imagesCategory.getSelectedView();
                errorText.setError("Please select Pet Category");
            }
            else */if(firstImagePath == null || firstImagePath.isEmpty()) {
                Toast.makeText(ImageUploadForm.this, "Please select image of Pet.", Toast.LENGTH_LONG).show();
            }
            else {
                progressDialog = ProgressDialog.show(ImageUploadForm.this, "", "Uploading images...", true);

                new UploadToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            }
        }
        else if(v.getId() == R.id.firstImageOfPet) {
            createDeleteImageDialogChooser(firstImageOfPet);
        }
        else if(v.getId() == R.id.secondImageOfPet) {
            createDeleteImageDialogChooser(secondImageOfPet);
        }
        else if(v.getId() == R.id.thirdImageOfPet) {
            createDeleteImageDialogChooser(thirdImageOfPet);
        }
        else if(v.getId() == R.id.fourthImageOfPet) {
            createDeleteImageDialogChooser(fourthImageOfPet);
        }
        else if(v.getId() == R.id.fifthImageOfPet) {
            createDeleteImageDialogChooser(fifthImageOfPet);
        }
    }

    private void createImageFormSelectImageDialogChooser() {
        dialogAdapter = new ArrayAdapter<String>(ImageUploadForm.this, android.R.layout.select_dialog_item) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                text.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.alertDialogListNames));
                return view;
            }
        };
        dialogAdapter.add("Take from Camera");
        dialogAdapter.add("Select from Gallery");
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ImageUploadForm.this, R.style.AlertDialogCustom));
        builder.setTitle("Select Image");
        builder.setAdapter(dialogAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    alertDialog.dismiss();
                    if(ActivityCompat.checkSelfPermission(ImageUploadForm.this, android.Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestCameraPermission();
                    }
                    else {
                        new SelectCameraImage().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                    }
                } else if (which == 1) {
                    alertDialog.dismiss();
                    new SelectGalleryImage().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                }
            }
        });
        alertDialog = builder.create();

        alertDialog.show();
    }
    private void createDeleteImageDialogChooser(final ImageView selectedImage) {
        dialogAdapter = new ArrayAdapter<String>(ImageUploadForm.this, android.R.layout.select_dialog_item) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                text.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.alertDialogListNames));
                return view;
            }
        };
        dialogAdapter.add("Delete");
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ImageUploadForm.this, R.style.AlertDialogCustom));
        builder.setTitle("Options");
        builder.setAdapter(dialogAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    alertDialog.dismiss();
                    selectedImage.setImageBitmap(null);
                    if(selectedImage == firstImageOfPet) {
                        firstImagePath = "";
                    }
                    else if(selectedImage == secondImageOfPet) {
                        secondImagePath = "";
                    }
                    else if(selectedImage == thirdImageOfPet) {
                        thirdImagePath = "";
                    }
                    else if(selectedImage == fourthImageOfPet) {
                        fourthImagePath = "";
                    }
                    else if(selectedImage == fifthImageOfPet) {
                        fifthImagePath = "";
                    }
                }
            }
        });
        alertDialog = builder.create();

        alertDialog.show();
    }

    @TargetApi(23)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        try {
            super.onActivityResult(requestCode, resultCode, intent);
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == CAMERA_REQUEST) {
                    originalFile = new File(currentImagePath);
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    this.imageToShow = BitmapFactory.decodeFile(originalFile.getAbsolutePath(), bmOptions);

                    //doCropping(image, PIC_CAMERA_CROP);
                    setBitmapToImage(this.imageToShow);
                }
                else if(requestCode == GALLERY_REQUEST) {
                    Uri uri = intent.getData();
                    String[] projection = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(projection[0]);
                    currentImagePath = cursor.getString(columnIndex);
                    cursor.close();

                    originalFile = new File(currentImagePath);
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    this.imageToShow = BitmapFactory.decodeFile(originalFile.getAbsolutePath(), bmOptions);

                    //doCropping(image, PIC_GALLERY_CROP);
                    setBitmapToImage(this.imageToShow);
                }
                /*else if(requestCode == PIC_CAMERA_CROP) {
                    Bundle extras = intent.getExtras();
                    this.imageToShow = extras.getParcelable("data");
                    String filename=currentImagePath.substring(currentImagePath.lastIndexOf("/")+1);
                    this.imageToShow = saveCameraCropBitmap(filename, imageToShow);
                    setBitmapToImage(this.imageToShow);
                }
                else if(requestCode == PIC_GALLERY_CROP) {
                    Bundle extras = intent.getExtras();
                    this.imageToShow = extras.getParcelable("data");
                    this.imageToShow = saveGalleryCropBitmap(imageToShow);
                    setBitmapToImage(this.imageToShow);
                }*/
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(ImageUploadForm.this, "Did not taken any image!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.i(e.getMessage(), "Error");
            Toast.makeText(ImageUploadForm.this, "Error", Toast.LENGTH_LONG).show();
        }
    }

    private void setBitmapToImage(Bitmap imageToShow) {
        if(firstImageOfPet.getDrawable() == null) {
            firstImageOfPet.setImageBitmap(imageToShow);
            //firstImagePath = cropFile.getAbsolutePath();
            firstImagePath = originalFile.getAbsolutePath();
            selectImageButton.setText("Select More Images");
            uploadFabButton.setVisibility(View.VISIBLE);
        }
        else if(secondImageOfPet.getDrawable() == null) {
            secondImageOfPet.setImageBitmap(imageToShow);
            //secondImagePath = cropFile.getAbsolutePath();
            secondImagePath = originalFile.getAbsolutePath();
        }
        else if(thirdImageOfPet.getDrawable() == null) {
            thirdImageOfPet.setImageBitmap(imageToShow);
            //thirdImagePath = cropFile.getAbsolutePath();
            thirdImagePath = originalFile.getAbsolutePath();
        }
        else if(fourthImageOfPet.getDrawable() == null) {
            fourthImageOfPet.setImageBitmap(imageToShow);
            //fourthImagePath = cropFile.getAbsolutePath();
            fourthImagePath = originalFile.getAbsolutePath();
        }
        else if(fifthImageOfPet.getDrawable() == null) {
            fifthImageOfPet.setImageBitmap(imageToShow);
            //fifthImagePath = cropFile.getAbsolutePath();
            fifthImagePath = originalFile.getAbsolutePath();
        }
    }

    private void doCropping(File image, int request_code) {

        Intent cropIntent = new Intent(this, CropImage.class);

        cropIntent.putExtra("image-path", currentImagePath);
        cropIntent.putExtra("crop", true);
        cropIntent.putExtra("scale", true);
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        cropIntent.putExtra("return-data", true);

        try {
            startActivityForResult(cropIntent, request_code);
        } catch (Exception e) {
            Toast.makeText(ImageUploadForm.this, "Crop Error", Toast.LENGTH_LONG).show();
        }
    }

    private Bitmap saveCameraCropBitmap(String filename, Bitmap imageToShow) {
        FileOutputStream outStream = null;

        cropFile = new File(currentImagePath);
        if (cropFile.exists()) {
            cropFile.delete();
            cropFile = new File(storageDir, phone + "_" + state + "_" + timeStamp + ".png");
        }
        try {
            outStream = new FileOutputStream(cropFile);
            imageToShow.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageToShow;
    }

    private Bitmap saveGalleryCropBitmap(Bitmap imageToShow) {
        FileOutputStream outStream = null;

        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);

        cropFile = new File(storageDir, phone + "_" + state + "_" + timeStamp + ".png");
        try {
            outStream = new FileOutputStream(cropFile);
            imageToShow.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageToShow;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);

        File image = new File(storageDir, phone + "_" + state + "_" + timeStamp + ".png");
        try {
            currentImagePath = image.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    private void requestReadStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(ImageUploadForm.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST);
        } else {
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST);
        }
    }

    private void requestWriteStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(ImageUploadForm.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE_PERMISSION_REQUEST);
        } else {
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE_PERMISSION_REQUEST);
        }
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(ImageUploadForm.this,
                    new String[]{android.Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST);
        } else {
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST);
        }
    }

    public class SelectCameraImage extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    ex.printStackTrace();
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                }
            }
            return null;
        }
    }

    public class SelectGalleryImage extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // Create intent to Open Image applications like Gallery, Google Photos
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, GALLERY_REQUEST);
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new SelectCameraImage().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            } else {
                Toast.makeText(ImageUploadForm.this, "CAMERA permission was NOT granted.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == WRITE_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestReadStoragePermission();
            } else {
                Toast.makeText(ImageUploadForm.this, "Write storage permission was NOT granted.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == READ_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createImageFormSelectImageDialogChooser();
            } else {
                Toast.makeText(ImageUploadForm.this, "Read storage permission was NOT granted.", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public class UploadToServer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                //ImagesFormUpload.uploadToRemoteServer(imageCategoryName, firstImagePath, secondImagePath, thirdImagePath, fourthImagePath, fifthImagePath, phone, ImageUploadForm.this);
                ImagesFormUpload.uploadToRemoteServer(firstImagePath, secondImagePath, thirdImagePath, fourthImagePath, fifthImagePath, phone, ImageUploadForm.this);
                progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(ImageUploadForm.this, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = ImageUploadForm.this.getPackageManager();
        ComponentName component = new ComponentName(ImageUploadForm.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = ImageUploadForm.this.getPackageManager();
        ComponentName component = new ComponentName(ImageUploadForm.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}
