

package com.carassistant.next;

import android.app.Activity;
import android.os.Bundle;

public final class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new LauncherShellView(this));
    }
}
