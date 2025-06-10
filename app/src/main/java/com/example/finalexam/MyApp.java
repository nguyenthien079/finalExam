package com.example.finalexam;

import android.app.Application;
import com.cloudinary.android.MediaManager;
import java.util.HashMap;
import java.util.Map;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Map config = new HashMap();
        config.put("cloud_name", "dskljyaxx");
        config.put("api_key", "221664417177996");       // Thay bằng API key của bạn
        config.put("api_secret", "kY84nG96Y7-3Da3SNpwc5tiKUXY");
        MediaManager.init(this, config);
    }
}
