

package com.carassistant.next;

import androidx.annotation.NonNull;
import androidx.car.app.CarAppService;
import androidx.car.app.HostValidator;
import androidx.car.app.Session;
import androidx.car.app.SessionInfo;

public final class P2ASurfaceProbeService extends CarAppService {

    @NonNull
    @Override
    public Session onCreateSession(@NonNull SessionInfo sessionInfo) {
        return new P2ASurfaceProbeSession();
    }

    @NonNull
    @Override
    public HostValidator createHostValidator() {
        // Chỉ dùng cho bản thử nghiệm sideload P2A.
        // Không giữ cấu hình cho phép mọi host trong bản phát hành.
        return HostValidator.ALLOW_ALL_HOSTS_VALIDATOR;
    }
}
