package com.mehtank.androminion.util;

import android.content.Context;
import android.net.Uri;

import androidx.documentfile.provider.DocumentFile;
import androidx.preference.PreferenceManager;

public class FileManager {

    public static DocumentFile getImageFile(Context context, String fileName) {
        var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        var str = sharedPreferences.getString("pref_game_directory", null);
        if (str == null)
            return null;

        var uri = Uri.parse(str);
        var baseFile = DocumentFile.fromTreeUri(context, uri);
        if (baseFile == null)
            return null;

        var imgFile = DocumentFile.fromSingleUri(context, Uri.parse(baseFile.getUri() + "%2Fimages"));
        if (imgFile == null || !imgFile.exists())
            imgFile = baseFile.createDirectory("images");
        if (imgFile == null || !imgFile.isDirectory())
            return null;

        return DocumentFile.fromSingleUri(context, Uri.parse(imgFile.getUri() + "%2F" + fileName));
    }

    public static DocumentFile getLogFile(Context context, String fileName) {
        var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        var str = sharedPreferences.getString("pref_game_directory", null);
        if (str == null)
            return null;

        var uri = Uri.parse(str);
        var baseFile = DocumentFile.fromTreeUri(context, uri);
        if (baseFile == null)
            return null;

        var logDir = baseFile.findFile("logs");
        if (logDir == null || !logDir.exists())
            logDir = baseFile.createDirectory("logs");

        if (logDir == null || !logDir.isDirectory())
            return null;

        var logFile = logDir.findFile(fileName);
        if (logFile != null && logFile.exists())
            logFile.delete();
        return logDir.createFile("application/octet-stream", fileName);
    }

    public static void appendRootFile(Context context, String fileName, byte[] bytes) {
        var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        var str = sharedPreferences.getString("pref_game_directory", null);
        if (str == null)
            return;

        var uri = Uri.parse(str);
        var baseFile = DocumentFile.fromTreeUri(context, uri);
        if (baseFile == null)
            return;

        var file = DocumentFile.fromSingleUri(context, Uri.parse(baseFile.getUri() + "%2F" + fileName));
        if (file == null || !file.exists())
            file = baseFile.createFile("application/octet-stream", fileName);
        if (file == null)
            return;
        try {
            var os = context.getContentResolver().openOutputStream(file.getUri(), "wa");
            if (os != null) {
                os.write(bytes);
                os.close();
            }
        } catch (Exception e) {
        }
    }

    public static void writeImageFile(Context context, String fileName, byte[] bytes) {
        var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        var str = sharedPreferences.getString("pref_game_directory", null);
        if (str == null)
            return;

        var uri = Uri.parse(str);
        var baseFile = DocumentFile.fromTreeUri(context, uri);
        if (baseFile == null)
            return;

        var imgFile = baseFile.findFile("images");
        if (imgFile == null || !imgFile.exists())
            imgFile = baseFile.createDirectory("images");

        if (imgFile == null || !imgFile.isDirectory())
            return;

        imgFile = imgFile.createFile("application/octet-stream", fileName);
        if (imgFile == null)
            return;
        try {
            var os = context.getContentResolver().openOutputStream(imgFile.getUri());
            if (os != null) {
                os.write(bytes);
                os.close();
            }
        } catch (Exception e) {
            imgFile.delete();
        }
    }
}
