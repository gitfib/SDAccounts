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

import android.os.Build;
import android.util.Log;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainModule implements IXposedHookLoadPackage {
    private static final String TAG = "SDAccounts-log";

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("android") &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {

            // TODO: Don't touch when uninstalling (as a default option)
            XposedHelpers.findAndHookMethod("com.android.server.accounts.AccountManagerService", lpparam.classLoader,
                    "validateAccountsInternal",
                    XposedHelpers.findClass("com.android.server.accounts.AccountManagerService$UserAccounts", lpparam.classLoader)  /* accounts */,
                    boolean.class                                                                                                   /* invalidateAuthenticatorCache */,
                    new XC_MethodHook() {
                        private Unhook hook;

                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);

                            Log.i(TAG, "validateAccountsInternal: before");
                            hook = XposedHelpers.findAndHookMethod("android.database.sqlite.SQLiteDatabase", lpparam.classLoader,
                                    "delete",
                                    String.class    /* table */,
                                    String.class    /* whereClause */,
                                    String[].class  /* whereArgs */,
                                    new XC_MethodHook() {
                                        @Override
                                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                            super.beforeHookedMethod(param);

                                            param.setResult(0);

                                            Log.i(TAG, "delete whereClause: " + param.args[1]);

                                            // Marshmallow: http://androidxref.com/6.0.1_r10/xref/frameworks/base/services/core/java/com/android/server/accounts/AccountManagerService.java#393
                                            // Lollipop: http://androidxref.com/5.0.0_r2/xref/frameworks/base/services/core/java/com/android/server/accounts/AccountManagerService.java
                                            // Kitkat: http://androidxref.com/4.4.4_r1/xref/frameworks/base/services/java/com/android/server/accounts/AccountManagerService.java
                                            // TODO: Prehistoric: validateAccountsAndPopulateCache(UserAccounts accounts): http://androidxref.com/4.1.1/xref/frameworks/base/core/java/android/accounts/AccountManagerService.java
                                        }
                                    });
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);

                            Log.i(TAG, "validateAccountsInternal: after");

                            hook.unhook();
                        }
                    });
        }
    }
}
