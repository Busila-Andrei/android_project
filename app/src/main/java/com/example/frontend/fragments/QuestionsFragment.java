package com.example.frontend.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.frontend.R;
import com.example.frontend.adapters.QuestionsAdapter;
import com.example.frontend.data.database.AppDatabase;
import com.example.frontend.data.database.Question;
import com.example.frontend.data.database.QuestionDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuestionsFragment extends Fragment {
    private QuestionDao questionDao;
    private List<Question> questions;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public QuestionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_questions, container, false);

        executorService.execute(() -> {
            AppDatabase db = Room.databaseBuilder(requireContext().getApplicationContext(), AppDatabase.class, "word-database")
                    .build();
            questionDao = db.questionDao();
            questions = questionDao.getAll();

            requireActivity().runOnUiThread(() -> {
                RecyclerView recyclerView = view.findViewById(R.id.recyclerViewQuestions);
                TextView emptyMessage = view.findViewById(R.id.emptyMessageQuestions);

                if (questions.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyMessage.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyMessage.setVisibility(View.GONE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(new QuestionsAdapter(questions));
                }
            });
        });

        return view;
    }
}

