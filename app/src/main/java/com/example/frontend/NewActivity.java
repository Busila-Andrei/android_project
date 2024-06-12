package com.example.frontend;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.frontend.data.database.AppDatabase;
import com.example.frontend.data.database.Question;
import com.example.frontend.data.database.QuestionDao;
import com.example.frontend.fragments.FirstFragment;
import com.example.frontend.fragments.SecondFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewActivity extends FragmentActivity {

    private ProgressBar progressBar;
    private Button buttonVerificare;
    private ImageButton buttonClose;
    private int progress = 0;
    private List<Question> questions;
    private List<Question> wrongQuestions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private AppDatabase db;
    private TextView resultTextView;
    private boolean isAnswerChecked = false;
    private boolean isLastAnswerCorrect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        progressBar = findViewById(R.id.progress_bar);
        buttonVerificare = findViewById(R.id.button_verificare);
        buttonClose = findViewById(R.id.buttonClose);
        resultTextView = findViewById(R.id.result_text_view);

        db = AppDatabase.getDatabase(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                QuestionDao questionDao = db.questionDao();
                questions = questionDao.getQuestionsByTestId(1);
                Collections.shuffle(questions); // Amestecăm întrebările
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (questions != null && !questions.isEmpty()) {
                            progressBar.setMax(questions.size() * 10);
                            displayQuestion(questions.get(currentQuestionIndex));
                        }
                    }
                });
            }
        }).start();

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonVerificare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAnswerChecked) {
                    continueToNextStep();
                } else {
                    checkAnswer();
                }
            }
        });

        findViewById(R.id.root_layout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                View view = getCurrentFocus();
                if (view != null && !(view instanceof EditText)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
                return false;
            }
        });
    }

    private void displayQuestion(Question question) {
        if ("multiple-choice".equals(question.type)) {
            SecondFragment fragment = new SecondFragment();
            Bundle args = new Bundle();
            args.putString("question", question.questionText);
            args.putString("correctAnswer", question.correctAnswer);
            args.putString("otherAnswers", question.otherAnswers);
            fragment.setArguments(args);
            replaceFragment(fragment);
        } else if ("single-choice".equals(question.type)) {
            FirstFragment fragment = new FirstFragment();
            Bundle args = new Bundle();
            args.putString("question", question.questionText);
            fragment.setArguments(args);
            replaceFragment(fragment);
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }

    private void checkAnswer() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        boolean isCorrect = false;
        String givenAnswer = "";
        String correctAnswer = questions.get(currentQuestionIndex).correctAnswer;

        if (currentFragment instanceof FirstFragment) {
            FirstFragment firstFragment = (FirstFragment) currentFragment;
            givenAnswer = firstFragment.getAnswer();
            isCorrect = givenAnswer.equals(correctAnswer);
        } else if (currentFragment instanceof SecondFragment) {
            SecondFragment secondFragment = (SecondFragment) currentFragment;
            givenAnswer = secondFragment.getSelectedOption();
            isCorrect = givenAnswer.equals(correctAnswer);
        }

        // Display the result in the TextView
        resultTextView.setText(isCorrect ? "Correct" : "Wrong");
        resultTextView.setVisibility(View.VISIBLE);

        // If the answer is wrong, add the question to the wrongQuestions list
        if (!isCorrect) {
            wrongQuestions.add(questions.get(currentQuestionIndex));
        }

        // Change the button text to "Continua"
        buttonVerificare.setText("Continua");
        isAnswerChecked = true;
        isLastAnswerCorrect = isCorrect;
    }

    private void continueToNextStep() {
        // Hide the result text view
        resultTextView.setVisibility(View.GONE);

        if (isLastAnswerCorrect) {
            progress += 10;
            progressBar.setProgress(progress);
        }

        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
        } else if (!wrongQuestions.isEmpty()) {
            questions = new ArrayList<>(wrongQuestions);
            wrongQuestions.clear();
            currentQuestionIndex = 0;
            // Bara de progres nu mai este resetată
        } else {
            finish();
            return;
        }

        displayQuestion(questions.get(currentQuestionIndex));

        // Reset button text to "Verificare" for the next step
        buttonVerificare.setText("Verificare");
        isAnswerChecked = false;
    }
}
