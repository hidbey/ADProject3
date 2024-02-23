package iss.workshop.adproject;


import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
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

import iss.workshop.adproject.Adapters.ExpandableGroupAdapter;

import iss.workshop.adproject.DataService.BlogDataService;
import iss.workshop.adproject.Model.Blog;
import iss.workshop.adproject.Model.BlogGroup;
import iss.workshop.adproject.Model.BlogHistoryViewModel;
public class HistoryViewingFragment extends Fragment {//只观察数据变化并更新UI
    private RecyclerView recyclerView;
    private ExpandableGroupAdapter adapter;

    private Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setContext(Context context)
    {
        this.context = context;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // 使用Application上下文来获取资源
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
        viewModel.setContext(context);
        viewModel.getBlogGroups().observe(getViewLifecycleOwner(), blogGroups -> {//当数据变化时执行该程序
            adapter.setBlogGroups(blogGroups);
        });

        SharedPreferences pref = getActivity().getSharedPreferences("user", MODE_PRIVATE);
        int id = pref.getInt("user", -1);
        viewModel.loadBlogs(id);
    }

}