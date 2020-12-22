package com.seanyj.mysamples.customview;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seanyj.mysamples.R;

public class ViewPagerActivity extends AppCompatActivity {
    private static final int NUM_PAGES = 5;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        ScreenSlidePageAdapter adapter = new ScreenSlidePageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Zoom out");
        menu.add("Depth");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String title = item.getTitle().toString();
        if ("Zoom out".equals(title)) {
            mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
            return true;
        } else if ("Depth".equals(title)) {
            mViewPager.setPageTransformer(true, new DepthPageTransformer());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ScreenSlidePageAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ScreenSlidePageFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    public static class ScreenSlidePageFragment extends Fragment {
        public static final String EXTRA_POS = "pos";
        private int mPos;

        public static ScreenSlidePageFragment newInstance(int pos) {
            ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(EXTRA_POS, pos);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.textView);
            mPos = getArguments().getInt(EXTRA_POS);
            int color = 0;
            switch (mPos) {
                case 0:
                    color = Color.RED;
                    break;
                case 1:
                    color = Color.YELLOW;
                    break;
                case 2:
                    color = Color.GREEN;
                    break;
                case 3:
                    color = Color.MAGENTA;
                    break;
                case 4:
                    color = Color.BLUE;
                    break;
            }
            textView.setBackgroundColor(color);
            return rootView;
        }
    }

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }


    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}
