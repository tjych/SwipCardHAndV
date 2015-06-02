package com.tjych.swip.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import java.lang.Override;import java.lang.String;

public final class TestFragment extends Fragment
{
    private static final String KEY_CONTENT = "TestFragment:Content";
	
	public static TestFragment newInstance(int pic) {
		TestFragment fragment = new TestFragment();
		fragment.pic = pic;
		
		return fragment;
	}
	
	private String mContent = "???";
    private int pic;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            pic = savedInstanceState.getInt(KEY_CONTENT);
		}

		LinearLayout layout = new LinearLayout(getActivity());
		layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		layout.setGravity(Gravity.CENTER);
        layout.setBackgroundResource(pic);
		return layout;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(KEY_CONTENT, pic);
	}
}