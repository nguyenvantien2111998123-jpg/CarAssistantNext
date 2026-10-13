

package com.carassistant.next;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.car.app.AppManager;
import androidx.car.app.CarContext;
import androidx.car.app.Screen;
import androidx.car.app.SurfaceCallback;
import androidx.car.app.SurfaceContainer;
import androidx.car.app.navigation.model.NavigationTemplate;

public final class P2ASurfaceProbeScreen extends Screen {

    private static final String TAG = "P2ASurfaceProbe";

    public P2ASurfaceProbeScreen(@NonNull CarContext carContext) {
        super(carContext);

        getCarContext()
                .getCarService(AppManager.class)
                .setSurfaceCallback(new SurfaceCallback() {

                    @Override
                    public void onSurfaceAvailable(
                            @NonNull SurfaceContainer container) {

                        Surface surface = container.getSurface();

                        Log.i(TAG, "SURFACE_AVAILABLE"
                                + " width=" + container.getWidth()
                                + " height=" + container.getHeight()
                                + " dpi=" + container.getDpi()
                                + " valid=" + surface.isValid());

                        Canvas canvas = null;

                        try {
                            canvas = surface.lockCanvas(null);
                            canvas.drawColor(Color.rgb(20, 35, 50));

                            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                            paint.setColor(Color.WHITE);
                            paint.setTextSize(36f);
                            paint.setTextAlign(Paint.Align.CENTER);

                            canvas.drawText(
                                    "P2A SURFACE OK",
                                    canvas.getWidth() / 2f,
                                    canvas.getHeight() / 2f,
                                    paint);

                            surface.unlockCanvasAndPost(canvas);
                            canvas = null;

                            Log.i(TAG, "SURFACE_DRAW_OK");

                        } catch (Exception e) {
                            Log.e(TAG, "SURFACE_DRAW_FAILED", e);

                            if (canvas != null) {
                                try {
                                    surface.unlockCanvasAndPost(canvas);
                                } catch (Exception ignored) {
                                    Log.w(TAG, "Canvas cleanup failed");
                                }
                            }
                        }
                    }

                    @Override
                    public void onSurfaceDestroyed(
                            @NonNull SurfaceContainer container) {

                        Log.i(TAG, "SURFACE_DESTROYED");

                        container.getSurface().release();
                    }
                });
    }

    @NonNull
    @Override
    public NavigationTemplate onGetTemplate() {
        return new NavigationTemplate.Builder().build();
    }
}
