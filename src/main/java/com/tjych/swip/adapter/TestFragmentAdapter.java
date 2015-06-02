package com.tjych.swip.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tjych.swip.R;

import java.lang.Override;import java.lang.String;

public class TestFragmentAdapter extends FragmentPagerAdapter {

    protected static final String[] CONTENT = new String[] { "This", "Is Is", "A A A", "Test", };
    protected int[] PICS = new int[]{R.drawable.pic_01,R.drawable.pic_02,R.drawable.pic_03,R.drawable.pic_04,R.drawable.pic_05,R.drawable.pic_06};
	public TestFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		return TestFragment.newInstance(PICS[position]);
	}

	@Override
	public int getCount() {
		return PICS.length;
	}
}