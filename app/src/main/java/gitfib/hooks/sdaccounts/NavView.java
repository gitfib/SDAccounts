/*
 * Copyright (c) 2016 Amir Maimon, and contributors
 *
 * This software is provided 'as-is', without any express or implied
 * warranty. In no event will the authors be held liable for any damages
 * arising from the use of this software.
 *
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 *
 * 1. The origin of this software must not be misrepresented; you must not
 *    claim that you wrote the original software. If you use this software
 *    in a product, an acknowledgement in the product documentation would be
 *    appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 *    misrepresented as being the original software.
 * 3. This notice may not be removed or altered from any source distribution.
 */

package gitfib.hooks.sdaccounts;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.ActionBar;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Inflates different layouts, and allows navigating between each in the same activity layout.
 */
public class NavView extends FrameLayout {
    private HashMap<String, FrameLayout> layouts = new HashMap<>();
    private String firstLayoutName;
    private String currentLayoutName;

    private ActionBar actionBar = null;

    public NavView(Context context) {
        super(context);
    }

    public NavView(Context context, AttributeSet attrs) {
        super(context, attrs);

        processAttrs(attrs, 0);
        applyAnimation(layouts.get(currentLayoutName), VISIBLE);
    }

    public NavView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        processAttrs(attrs, defStyleAttr);
        applyAnimation(layouts.get(currentLayoutName), VISIBLE);
    }

    /**
     * Whether to automatically hide-and-show the Up button on the action bar.
     * It will be shown when the current layout isn't the first one.
     * @param actionBar Action bar of the activity that contains this view.
     */
    public void autoUpButton(ActionBar actionBar) {
        if (actionBar != null)
            (this.actionBar = actionBar).setDisplayHomeAsUpEnabled(false);
    }

    /**
     * Navigates to another layout by name.
     * @param layoutName Layout name to navigate.
     */
    public void navTo(String layoutName) {
        if (layoutName.equals(currentLayoutName))
            return;

        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(!layoutName.equals(firstLayoutName));

        applyAnimation(layouts.get(currentLayoutName), GONE);
        applyAnimation(layouts.get(currentLayoutName = layoutName), VISIBLE);
    }

    /**
     * Applies custom animation when navigating.
     */
    private void applyAnimation(ViewGroup viewGroup, int visibility) {
        ArrayList<View> views;
        viewGroup.setVisibility(VISIBLE);

        for (int i = 0; i < (views = getAnimatableViews(viewGroup)).size(); i++) {
            Animation anim = AnimationUtils.loadAnimation(getContext(), visibility == VISIBLE?R.anim.slide_in:R.anim.slide_out);
            anim.setStartOffset(i * (1000 / views.size()));

            if (i == views.size() - 1)
                anim.setAnimationListener(new AnimListener(viewGroup, visibility));
            else
                anim.setAnimationListener(new AnimListener(views.get(i), visibility));
// TODO: Buggy when moving to About
            views.get(i).setVisibility(VISIBLE);
            views.get(i).startAnimation(anim);
        }
    }

    /**
     * Retrieves an array list of all animatable views in group.
     * @param viewGroup View group to search from.
     * @return Array list of all animatable views in specified group.
     */
    private ArrayList<View> getAnimatableViews(ViewGroup viewGroup) {
        ArrayList<View> res = new ArrayList<>();

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            if (viewGroup.getChildAt(i) instanceof ViewGroup)
                for (View view : getAnimatableViews(((ViewGroup) viewGroup.getChildAt(0))))
                    res.add(view);

            if (    viewGroup.getChildAt(i).getTag() != null &&
                    viewGroup.getChildAt(i).getTag().equals("animatable"))
                res.add(viewGroup.getChildAt(i));
        }

        return res;
    }

    /**
     * Retrieves the currently displayed layout's name.
     * @return Current layout's name.
     */
    public String getCurrentLayoutName() {
        return currentLayoutName;
    }

    /**
     * Animation listener that changes the specified view visibility upon end.
     */
    private class AnimListener implements Animation.AnimationListener {
        private View view;
        private int visibility;

        public AnimListener(View view, int visibility) {
            this.view = view;
            this.visibility = visibility;
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            view.setVisibility(visibility);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    /**
     * Processes NavView's custom attributes.
     * android:entries takes an integer array of the views to add to NavView. Only the first view is displayed by default.
     * @param attrs Attribute set from the constructor.
     * @param defStyleAttr Default style attribute from the constructor, or 0 for none.
     */
    private void processAttrs(AttributeSet attrs, int defStyleAttr) {
        LayoutInflater inflater = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        TypedArray attributes = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.NavView, defStyleAttr, 0);

        try {
            /**
             * about:entries
             */
            FrameLayout layout;
            Field field;
            String[] views = getContext().getResources().getStringArray(attributes.getResourceId(R.styleable.NavView_android_entries, -1));
            for (int i = 0; i < views.length; i++) {
                try {
                    field = R.layout.class.getDeclaredField(views[i]);
                    layouts.put(views[i], layout = new FrameLayout(getContext(), attrs, defStyleAttr));
                    inflater.inflate(field.getInt(field), layout).setVisibility(INVISIBLE);
                    addView(layout);
                    if (i == 0) {
                        firstLayoutName = currentLayoutName = views[i];
                        layout.setVisibility(VISIBLE);
                    }
                } catch (Exception e) {
                    // Ignore exceptions
                }
            }

        } finally {
            attributes.recycle();
        }
    }
}
