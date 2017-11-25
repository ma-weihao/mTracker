package club.wello.mtracker.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class PushService extends Service {
    public PushService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
