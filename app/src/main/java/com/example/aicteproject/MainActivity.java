package com.example.aicteproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private Bitmap selectedBitmap;
    private ImageView imageView;
    private EditText messageInput;
    private Button encodeButton, decodeButton, selectImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        messageInput = findViewById(R.id.messageInput);
        encodeButton = findViewById(R.id.encodeButton);
        decodeButton = findViewById(R.id.decodeButton);
        selectImageButton = findViewById(R.id.selectImageButton);

        // Select Image
        selectImageButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });

        // Encode Message
        encodeButton.setOnClickListener(view -> {
            if (selectedBitmap != null && !messageInput.getText().toString().isEmpty()) {
                Bitmap encodedImage = SteganographyUtils.encodeMessage(selectedBitmap, messageInput.getText().toString());
                imageView.setImageBitmap(encodedImage);
                Toast.makeText(this, "Message Encoded!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Select an image and enter a message!", Toast.LENGTH_SHORT).show();
            }
        });

        // Decode Message
        decodeButton.setOnClickListener(view -> {
            if (selectedBitmap != null) {
                String hiddenMessage = SteganographyUtils.decodeMessage(selectedBitmap);
                Toast.makeText(this, "Decoded Message: " + hiddenMessage, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Select an encoded image first!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                selectedBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                imageView.setImageBitmap(selectedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}