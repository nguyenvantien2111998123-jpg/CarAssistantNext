

package com.carassistant.next;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.car.app.Screen;
import androidx.car.app.Session;

public final class P2ASurfaceProbeSession extends Session {

    @NonNull
    @Override
    public Screen onCreateScreen(@NonNull Intent intent) {
        return new P2ASurfaceProbeScreen(getCarContext());
    }
}
