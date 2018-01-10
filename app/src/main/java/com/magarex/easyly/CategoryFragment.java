package com.magarex.easyly;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    private View myview;
    private GridLayout categories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_category, container, false);

        categories = myview.findViewById(R.id.categories);
        setSingleEvent(categories);

        return myview;

    }

    private void setSingleEvent(GridLayout categories) {
        for (int i = 0; i < categories.getChildCount(); i++) {
            CardView cardview = (CardView) categories.getChildAt(i);
            final int I = i;
            cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("Category", I);
                    startActivity(intent);
                }
            });
        }
    }
}

