package com.example.frontend;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.frontend.config.ApiResponse;
import com.example.frontend.config.ApiService;
import com.example.frontend.config.RetrofitClient;
import com.example.frontend.data.database.AppDatabase;
import com.example.frontend.data.database.Question;
import com.example.frontend.data.database.QuestionDao;
import com.example.frontend.data.database.Word;
import com.example.frontend.data.database.WordDao;
import com.example.frontend.fragments.QuestionsFragment;
import com.example.frontend.fragments.SectionsFragment;
import com.example.frontend.fragments.WordsFragment;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    private AppDatabase db;
    private WordDao wordDao;
    private QuestionDao questionDao;
    private ApiService apiService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "word-database").build();
        wordDao = db.wordDao();
        questionDao = db.questionDao();
        apiService = RetrofitClient.getApiService();

        // Referințe la butoane
        Button btn1 = findViewById(R.id.button1);
        Button btn2 = findViewById(R.id.button2);
        Button btn3 = findViewById(R.id.button3);
        Button btn4 = findViewById(R.id.button4);
        Button btn5 = findViewById(R.id.button5);

        // Listeneri pentru butoane
        btn1.setOnClickListener(v -> loadFragment(new SectionsFragment()));
        btn4.setOnClickListener(v -> loadFragment(new WordsFragment()));
        btn5.setOnClickListener(v -> {
            fetchWordsFromApi();
            fetchQuestionsFromApi();
        });
        btn3.setOnClickListener(v -> loadFragment(new QuestionsFragment()));

        // Încarcă implicit primul fragment
        if (savedInstanceState == null) {
            loadFragment(new SectionsFragment());
        }

        Log.d(TAG, "HomeActivity created");
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
        Log.d(TAG, "Fragment loaded: " + fragment.getClass().getSimpleName());
    }

    private void fetchWordsFromApi() {
        Log.d(TAG, "Fetching words from API...");
        apiService.getWords().enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                Log.d(TAG, "API response received for words.");
                if (response.isSuccessful() && response.body() != null) {
                    List<LinkedTreeMap> data = (List<LinkedTreeMap>) response.body().getData();
                    List<Word> words = new ArrayList<>();
                    for (LinkedTreeMap map : data) {
                        int id = ((Double) map.get("id")).intValue();
                        String englishWord = (String) map.get("englishWord");
                        String romanianWord = (String) map.get("romanianWord");
                        Word word = new Word();
                        word.id = id;
                        word.englishWord = englishWord;
                        word.romanianWord = romanianWord;
                        words.add(word);
                    }
                    Log.d(TAG, "Words parsed from API response, inserting into database...");
                    executorService.execute(() -> {
                        wordDao.insertAll(words.toArray(new Word[0]));
                        Log.d(TAG, "Words inserted into database.");
                    });
                } else {
                    Log.e(TAG, "Failed to fetch words from API");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "API call failed: " + t.getMessage());
            }
        });
    }

    private void fetchQuestionsFromApi() {
        Log.d(TAG, "Fetching questions from API...");
        apiService.getQuestions().enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                Log.d(TAG, "API response received for questions.");
                if (response.isSuccessful() && response.body() != null) {
                    List<LinkedTreeMap> data = (List<LinkedTreeMap>) response.body().getData();
                    List<Question> questions = new ArrayList<>();
                    for (LinkedTreeMap map : data) {
                        int id = ((Double) map.get("id")).intValue();
                        String type = (String) map.get("type");
                        String questionText = (String) map.get("questionText");
                        String correctAnswer = (String) map.get("correctAnswer");
                        String otherAnswers = (String) map.get("otherAnswers");
                        Long testId = ((Double) map.get("testId")).longValue();
                        Question question = new Question();
                        question.id = id;
                        question.type = type;
                        question.questionText = questionText;
                        question.correctAnswer = correctAnswer;
                        question.otherAnswers = otherAnswers;
                        question.testId = testId;
                        questions.add(question);
                    }
                    Log.d(TAG, "Questions parsed from API response, inserting into database...");
                    executorService.execute(() -> {
                        questionDao.insertAll(questions.toArray(new Question[0]));
                        Log.d(TAG, "Questions inserted into database.");
                    });
                } else {
                    Log.e(TAG, "Failed to fetch questions from API");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "API call failed: " + t.getMessage());
            }
        });
    }
}
