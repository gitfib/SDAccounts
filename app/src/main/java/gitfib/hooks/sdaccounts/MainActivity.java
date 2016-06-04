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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private boolean isEnabled = false;
    private NavView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isEnabled) {
            ((TextView) findViewById(R.id.status_text)).setText(R.string.enabled);
            ((ImageView) findViewById(R.id.status_image)).setImageResource(R.drawable.ic_check_circle_black_36dp);
        }

        Animation anim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        anim.setDuration(1000);
        findViewById(R.id.info).startAnimation(anim);

        // TODO: Better scrolling method
        ((TextView) findViewById(R.id.about_text)).setMovementMethod(new ScrollingMovementMethod());
// TODO: Make links clickable
        nav = ((NavView) findViewById(R.id.nav));
        nav.autoUpButton(getActionBar());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**
         * Set the behavior of Up button on the action bar.
         */
        if (item.getItemId() == android.R.id.home) {
            if (nav != null)
                nav.navTo("view_main");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /**
         * Override Back button to act like Up button on the action bar.
         */
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (nav != null) {
                if (nav.getCurrentLayoutName().equals("view_main"))
                    finish();
                else
                    nav.navTo("view_main");
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void gotoAbout(View v) {
        nav.navTo("view_about");
    }

    public void gotoForum(View v) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://forum.xda-developers.com/xposed/modules/xposed-sdaccounts-t3392173")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public void gotoSource(View v) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/gitfib/SDAccounts")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public void gotoUninstall(View v) {
        startActivity(new Intent(Intent.ACTION_UNINSTALL_PACKAGE, Uri.parse("package:gitfib.hooks.sdaccounts")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
