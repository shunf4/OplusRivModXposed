package com.shunf4.oplusrivmodxposed;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.location.Location;
import android.os.Binder;
import android.os.Process;
import android.service.notification.NotificationListenerService;
import android.util.Log;

import java.util.Arrays;
import java.util.Locale;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {



    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
//        NotificationManager[] nmw = new NotificationManager[] { null };
//        XposedHelpers.findAndHookMethod("me.zhanghai.android.wechatnotificationtweaks.app.NotificationService",
//                lpparam.classLoader,
//                "a",
//                long.class,
//                String.class,
//                boolean.class,
//                boolean.class,
//                boolean.class,
//                String.class,
//                new XC_MethodHook() {
//                    @SuppressLint("NewApi")
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        if (param.getResult() == null) {
//                            return;
//                        }
//                        Object rawN = XposedHelpers.getObjectField(param.getResult(), "N");
//                        if (rawN == null || !(rawN instanceof Notification)) {
//                            return;
//                        }
//                        Notification n = ((Notification) rawN);
//
//                        Log.i("MainHook-XposedLog", "n.vibrate: " + Arrays.toString(n.vibrate));
//
//                        if (n.vibrate == null || n.vibrate.length == 0) {
//                            if (nmw[0] == null) {
//                                nmw[0] = ((NotificationListenerService) param.thisObject).getSystemService(NotificationManager.class);
//                            }
//                            nmw[0].createNotificationChannel(new NotificationChannel("WECHAT_NOTIF_TWEAK_NO_VIBRATE", "No vibrate (Set no vibrate please)", NotificationManager.IMPORTANCE_DEFAULT));
//                            XposedHelpers.setObjectField(n, "mChannelId", "WECHAT_NOTIF_TWEAK_NO_VIBRATE");
//
//                            Log.i("MainHook-XposedLog", "n.getChannelId: " + n.getChannelId());
//                            Log.i("MainHook-XposedLog", "n.getChannelId - 2: " + ((Notification) XposedHelpers.getObjectField(param.getResult(), "N")).getChannelId());
//                            Log.i("MainHook-XposedLog", Log.getStackTraceString(new Exception()));
//                        }
//
//                    }
//                }
//        );
//        XposedHelpers.findAndHookMethod(NotificationManager.class, "notify", String.class, int.class, Notification.class, new XC_MethodHook() {
//            @SuppressLint("NewApi")
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
////                Log.i("MainHook-XposedLog", "n t: " + param.args[0]);
////                Log.i("MainHook-XposedLog", "n i: " + param.args[1]);
////                Log.i("MainHook-XposedLog", "n n: " + param.args[2]);
////                Log.i("MainHook-XposedLog", Log.getStackTraceString(new Exception()));
//                Object rawN = param.args[2];
//                if (rawN == null || !(rawN instanceof Notification)) {
//                    return;
//                }
//                Notification n = ((Notification) rawN);
//
//                Log.i("MainHook-XposedLog", "n.vibrate: " + Arrays.toString(n.vibrate));
//
//                if (n.vibrate == null || n.vibrate.length == 0) {
//                    if (nmw[0] == null) {
//                        nmw[0] = ((NotificationManager) param.thisObject);
//                    }
//                    nmw[0].createNotificationChannel(new NotificationChannel("WECHAT_NOTIF_TWEAK_NO_VIBRATE", "No vibrate (Set no vibrate please)", NotificationManager.IMPORTANCE_DEFAULT));
//                    XposedHelpers.setObjectField(n, "mChannelId", "WECHAT_NOTIF_TWEAK_NO_VIBRATE");
////
////                    Log.i("MainHook-XposedLog", "n.getChannelId: " + n.getChannelId());
////                    Log.i("MainHook-XposedLog", "n.getChannelId - 2: " + ((Notification) XposedHelpers.getObjectField(param.getResult(), "N")).getChannelId());
////                    Log.i("MainHook-XposedLog", Log.getStackTraceString(new Exception()));
//                }
//            }
//        });

        if (lpparam.packageName.equals("android")) {
            Log.i("LSPosed-Bridge", "before hook OplusZenModeHelperExtImpl.adjustZenModeConfig " + lpparam.packageName);

            XposedHelpers.findAndHookMethod("com.android.server.notification.OplusZenModeHelperExtImpl", lpparam.classLoader, "adjustZenModeConfig", "android.service.notification.ZenModeConfig", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    //                super.beforeHookedMethod(param);
                    Log.i("LSPosed-Bridge", "in beforeHookedMethod OplusZenModeHelperExtImpl.adjustZenModeConfig");
                    param.setResult(param.args[0]);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                }
            });

            Log.i("LSPosed-Bridge", "after hook OplusZenModeHelperExtImpl.adjustZenModeConfig");

            XposedHelpers.findAndHookMethod("com.android.server.notification.OplusNotificationManagerServiceExtImpl", lpparam.classLoader, "adjustNotificationPolicy", java.lang.String.class, NotificationManager.Policy.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    if (param.args[1] != null) {
                        NotificationManager.Policy p = ((NotificationManager.Policy) param.args[1]);
                        param.setObjectExtra("oplusrivmodxposed_origSuppressedVisualEffects", p.suppressedVisualEffects);
                    }
                    super.beforeHookedMethod(param);
                }
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Object rawResult = param.getResult();
                    if (rawResult != null) {
                        NotificationManager.Policy p = ((NotificationManager.Policy) rawResult);
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                            NotificationManager.Policy newP =
                                    new NotificationManager.Policy(p.priorityCategories, p.priorityCallSenders, p.priorityMessageSenders, ((Integer) param.getObjectExtra("oplusrivmodxposed_origSuppressedVisualEffects")).intValue(), p.priorityConversationSenders);
                            param.setResult(newP);
                            Log.i("LSPosed-Bridge", "restored policy in OplusNotificationManagerServiceExtImpl.adjustNotificationPolicy");
                        }
                    }
                }
            });

            XposedHelpers.findAndHookMethod("com.android.server.pm.permission.OplusPermSupportedFunctionManager", lpparam.classLoader, "shouldRevokeShellPermission", android.content.Context.class, java.lang.String.class, int.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                }
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    if (param.getResult() != null && param.getResult() instanceof Boolean && ((Boolean) param.getResult()).booleanValue() == true) {
                        String logMsg = String.format(Locale.ENGLISH, "OplusRivModXposed : com.android.server.pm.permission.OplusPermSupportedFunctionManager.shouldRevokeShellPermission blocked shell's (uid=%d) permission \"%s\". Now reverting it !!!!",
                                param.args[2] != null ? ((Integer) param.args[2]).intValue() : -99991234,
                                param.args[1] != null ? ((String) param.args[1]) : "(null???)");
                        Log.w("LSPosed-Bridge", logMsg);
                        Log.w("LSPosed-Bridge", logMsg);
                        Log.w("LSPosed-Bridge", logMsg);
                        Log.w("LSPosed-Bridge", logMsg);
                        Log.w("LSPosed-Bridge", logMsg);
                        Log.w("LSPosed-Bridge", logMsg);
                        Log.w("LSPosed-Bridge", logMsg);
                        Log.w("LSPosed-Bridge", logMsg);
                        Log.w("LSPosed-Bridge", logMsg);
                        Log.w("LSPosed-Bridge", logMsg);
                        Log.w("LSPosed-Bridge", logMsg);
                        Log.w("LSPosed-Bridge", logMsg);
                        param.setResult(false);
                    }
                }
            });

            XposedHelpers.findAndHookMethod("android.location.Location", lpparam.classLoader, "toString", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                }
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    if (param.getResult() != null && param.getResult() instanceof String) {
                        int currProcessUid = Process.myUid();
                        int currProcessUid2 = Binder.getCallingUid();
                        Log.w("LSPosed-Bridge", "OplusRivModXposed : Location.toString hook : currProcessUid=" + currProcessUid + " currProcessUid2=" + currProcessUid2);
                        if (/* currProcessUid == 0 || */currProcessUid2 == 0) {
                            Log.w("LSPosed-Bridge", "OplusRivModXposed : returning real location koords");
                            StringBuilder s = new StringBuilder((String) param.getResult());
                            s.append(" ").append(String.format(Locale.ROOT, "realKoords=%.6f---%.6f", Double.valueOf(((Location) param.thisObject).getLatitude()), Double.valueOf(((Location) param.thisObject).getLongitude())));
                            param.setResult(s.toString());
                        }
                    }
                }
            });
        }
    }
}
