package com.example.sit708_8_1c_llmchatbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ChatActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private RecyclerView rvChatMessages;
    private EditText etMessageInput;
    private Button btnSend;
    private String username;

    private List<Message> messageList;
    private ChatAdapter chatAdapter;


    private ChatDatabase db;
    private MessageDao messageDao;


    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        rvChatMessages = findViewById(R.id.rvChatMessages);
        etMessageInput = findViewById(R.id.etMessageInput);
        btnSend = findViewById(R.id.btnSend);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("USER_NAME")) {
            username = intent.getStringExtra("USER_NAME");
        } else {
            username = "User";
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rvChatMessages.setLayoutManager(layoutManager);

        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);
        rvChatMessages.setAdapter(chatAdapter);


        db = ChatDatabase.getInstance(this);
        messageDao = db.messageDao();


        loadChatHistory();


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = etMessageInput.getText().toString().trim();

                if (!messageText.isEmpty()) {

                    String currentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());


                    Message newMessage = new Message(messageText, true, currentTime, username);


                    messageList.add(newMessage);
                    chatAdapter.notifyItemInserted(messageList.size() - 1);
                    rvChatMessages.scrollToPosition(messageList.size() - 1);
                    etMessageInput.setText("");


                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            messageDao.insert(newMessage);
                        }
                    });

                    sendToOllama(messageText);
                }
            }
        });
    }

    private void loadChatHistory() {
        executor.execute(new Runnable() {
            @Override
            public void run() {

                List<Message> history = messageDao.getMessagesByUser(username);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        messageList.clear();
                        messageList.addAll(history);
                        chatAdapter.notifyDataSetChanged();


                        if (!messageList.isEmpty()) {
                            rvChatMessages.scrollToPosition(messageList.size() - 1);
                        }
                    }
                });
            }
        });
    }

        private void sendToOllama(String userText) {

            String url = "http://10.0.2.2:11434/api/generate";


            JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("model", "qwen2.5:3b"); // Make sure this perfectly matches the model name you pulled!
                jsonBody.put("prompt", userText);
                jsonBody.put("stream", false); // We want the whole message at once, not word-by-word
            } catch (Exception e) {
                e.printStackTrace();
            }


            RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();


            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    displayBotReply("Error: Could not connect to Ollama. Is it running?");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful() && response.body() != null) {
                        try {

                            String responseData = response.body().string();
                            JSONObject jsonObject = new JSONObject(responseData);
                            String botReply = jsonObject.getString("response");


                            displayBotReply(botReply);

                        } catch (Exception e) {
                            e.printStackTrace();
                            displayBotReply("Error: Couldn't read the AI's response.");
                        }
                    } else {
                        displayBotReply("Error: Received a bad response from the server.");
                    }
                }
            });
        }


        private void displayBotReply(String text) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String currentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                    Message botMessage = new Message(text, false, currentTime, username);


                    messageList.add(botMessage);
                    chatAdapter.notifyItemInserted(messageList.size() - 1);
                    rvChatMessages.scrollToPosition(messageList.size() - 1);


                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            messageDao.insert(botMessage);
                        }
                    });
                }
            });
        }
    }
