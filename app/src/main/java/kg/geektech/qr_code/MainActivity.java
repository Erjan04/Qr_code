package kg.geektech.qr_code;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import kg.geektech.qr_code.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final ActivityResultLauncher<ScanOptions> barcode = registerForActivityResult(new ScanContract(), result -> {
        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(result.getContents()));
        intent.setPackage("com.instagram.android");

        try {
            startActivity(intent);
        }catch (ActivityNotFoundException e){
            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(result.getContents())));
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        createQrCode();

        binding.btnScan.setOnClickListener(v -> {
            ScanOptions options = new ScanOptions();
            options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
            options.setPrompt("Сканирование");
            options.setBeepEnabled(true);
            options.setCameraId(0);
            barcode.launch(options);
        });
    }

    private void createQrCode() {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap("https://www.instagram.com/cristiano/",
                    BarcodeFormat.QR_CODE,200,200);
            binding.QrCodeIv.setImageBitmap(bitmap);
        }catch (Exception e){
            Log.e("TAG", "createQrCode: " + e.getMessage());
        }
    }
}