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

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

public class ClickableRelativeLayout extends RelativeLayout {
    public ClickableRelativeLayout(Context context) {
        super(context);
    }

    public ClickableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClickableRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(new ClickListener(l));
    }

    private class ClickListener implements OnClickListener {
        private OnClickListener l;

        public ClickListener(OnClickListener l) {
            this.l = l;
        }

        @Override
        public void onClick(View v) {
            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.click);
            anim.setAnimationListener(new AnimListener(v));
            v.startAnimation(anim);
        }

        private class AnimListener implements Animation.AnimationListener {
            private View v;

            public AnimListener(View v) {
                this.v = v;
            }

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                l.onClick(v);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        }
    }
}
