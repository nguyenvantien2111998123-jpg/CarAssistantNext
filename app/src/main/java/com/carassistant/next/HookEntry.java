package com.carassistant.next;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public final class HookEntry implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(
            XC_LoadPackage.LoadPackageParam param) {
        String name = param.packageName;

        if ("android".equals(name)
                || "com.google.android.projection.gearhead".equals(name)) {
            XposedBridge.log(
                "GD01 Core: loaded in " + name);
        }
    }
}
