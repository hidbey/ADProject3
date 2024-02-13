package iss.workshop.adproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BlankFragment2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private boolean isVisible = false;// 对用户是否可见标识
    private boolean isViewCreated = false;// view是否已经加载完成标识

    private View view;

    public BlankFragment2() {
        // Required empty public constructor
    }

    public static BlankFragment2 newInstance(String param1, String param2) {
        BlankFragment2 fragment = new BlankFragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null && savedInstanceState == null){
            view = inflater.inflate(R.layout.fragment_blank2, container, false);
        }

        isViewCreated = true;// view 已经加载
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        lazyLoad();
    }

    /**
     * 延迟加载 - 对用户可见且view已经加载完成
     */
    private void lazyLoad() {
        if(isVisible && isViewCreated){
            loadData();
            reset();// 恢复标识，防止重复加载
        }
    }

    /**
     * 恢复标记 防止重复加载
     */
    private void reset(){
        isViewCreated = false;
        isVisible = false;
    }

    /**
     * 加载数据操作
     */
    private void loadData() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){

            isVisible = true;// 对用户可见
            lazyLoad();

        }else {
            isVisible = false;// 对用户不可见
        }
    }
}