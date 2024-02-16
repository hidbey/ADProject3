package iss.workshop.adproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import jp.wasabeef.richeditor.RichEditor;


public class BlankFragment4 extends Fragment {

    EditText contentTitle, subTitle,content;
    Button nextButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blank4, container, false);

        nextButton  = rootView.findViewById(R.id.next_button);
        contentTitle = rootView.findViewById(R.id.content_title_edit_text);
        subTitle = rootView.findViewById(R.id.sub_title_edit_text);
        content = rootView.findViewById(R.id.content_edit_text);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击 nextButton 后，跳转到 BlogTypeFragment

                if (contentTitle.getText().toString().isEmpty()){
                    contentTitle.setError("Content title cannot be null");
                }

                if (content.getText().toString().isEmpty()){
                    content.setError("Content cannot be null");
                }

                if (content.getError()==null&&contentTitle.getError()==null){
                    Intent intent = new Intent(getActivity(), BlogTypeActivity.class);
                    intent.putExtra("contentTitle",contentTitle.getText().toString());
                    intent.putExtra("subTitle",subTitle.getText().toString());
                    intent.putExtra("content",content.getText().toString());
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }
}