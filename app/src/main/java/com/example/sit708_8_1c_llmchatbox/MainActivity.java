package com.example.sit708_8_1c_llmchatbox;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        EditText etUsername = findViewById(R.id.etUsername);
        Button btnGo = findViewById(R.id.btnGo);


        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = etUsername.getText().toString().trim();


                if (!username.isEmpty()) {


                    Intent intent = new Intent(MainActivity.this, ChatActivity.class);


                    intent.putExtra("USER_NAME", username);


                    startActivity(intent);

                } else {

                    Toast.makeText(MainActivity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}