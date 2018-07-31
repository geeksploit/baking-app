package me.geeksploit.bakingapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public final class GridWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(getApplicationContext());
    }
}
