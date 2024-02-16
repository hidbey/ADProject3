package iss.workshop.adproject;


import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import iss.workshop.adproject.Adapters.ExpandableGroupAdapter;

import iss.workshop.adproject.DataService.BlogDataService;
import iss.workshop.adproject.Model.Blog;
import iss.workshop.adproject.Model.BlogGroup;
import iss.workshop.adproject.Model.BlogHistoryViewModel;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryViewingFragment extends Fragment {//只观察数据变化并更新UI
    private RecyclerView recyclerView;
    private ExpandableGroupAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history_viewing, container, false);
        recyclerView = view.findViewById(R.id.recyclerHistoryView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new ExpandableGroupAdapter(getActivity(),new ArrayList<>());
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BlogHistoryViewModel viewModel = new ViewModelProvider(this).get(BlogHistoryViewModel.class);

        viewModel.getBlogGroups().observe(getViewLifecycleOwner(), blogGroups -> {
            adapter.setBlogGroups(blogGroups);
        });

        SharedPreferences pref = getActivity().getSharedPreferences("user", MODE_PRIVATE);
        int id = pref.getInt("user", -1);
        viewModel.loadBlogs(id);
    }

}