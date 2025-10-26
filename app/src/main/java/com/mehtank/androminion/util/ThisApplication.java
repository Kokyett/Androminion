package com.mehtank.androminion.util;

import android.app.Application;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ThisApplication extends Application
{
	private final Thread.UncaughtExceptionHandler defaultUncaughtHandler = Thread.getDefaultUncaughtExceptionHandler();
	public void onCreate() {
		super.onCreate();
		ThemeSetter.setTheme(this, true);
		ThemeSetter.setLanguage(this);
		Thread.setDefaultUncaughtExceptionHandler((thread, e) -> {
			try {
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date();
				StringBuilder content = new StringBuilder("==========================================\r\n" +
                        dateFormat.format(date) + "\r\n" +
                        "Version: " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName + "\r\n" +
                        e.getMessage() + "\r\n");
				for (StackTraceElement stack : e.getStackTrace()) {
                    content.append(stack.toString()).append("\r\n");
				}
				content.append("\r\n\r\n");

				FileManager.appendRootFile(getApplicationContext(), "crashes.txt", content.toString().getBytes(StandardCharsets.UTF_8));
			} catch (Exception ignored) {
			}

			if (defaultUncaughtHandler != null)
				defaultUncaughtHandler.uncaughtException(thread, e);
		});
	}
}
