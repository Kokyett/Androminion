package com.mehtank.androminion.fragments.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.documentfile.provider.DocumentFile;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.mehtank.androminion.R;

public class UserInterfaceFragment extends PreferenceFragmentCompat {
    private SharedPreferences sharedPreferences;
    private CheckBoxPreference chkDownloadImages;
    private CheckBoxPreference chkEnableFileLogging;
    private Preference prefDirectory;
    private ActivityResultLauncher<Intent> openDocumentTreeLauncher;

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.pref_user_interface, rootKey);

        sharedPreferences = getPreferenceManager().getSharedPreferences();
        if (sharedPreferences == null)
            return;

        openDocumentTreeLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Context context = getContext();
                if (result.getResultCode() != Activity.RESULT_OK || context == null) {
                    chkDownloadImages.setChecked(false);
                    chkEnableFileLogging.setChecked(false);
                    return;
                }

                Intent data = result.getData();
                if (data != null && data.getData() != null) {
                    Uri treeUri = data.getData();
                    getContext().getContentResolver().takePersistableUriPermission(treeUri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    prefDirectory.setSummary(treeUri.toString());
                    var editor = sharedPreferences.edit();
                    editor.putString("pref_game_directory", treeUri.toString());
                    editor.apply();

                    createNoMediaFile(treeUri);
                } else  {
                    chkDownloadImages.setChecked(false);
                    chkEnableFileLogging.setChecked(false);
                }
            });

        chkDownloadImages = findPreference("autodownload");
        if (chkDownloadImages != null) {
            chkDownloadImages.setOnPreferenceClickListener(preference -> {
                var dir = sharedPreferences.getString("pref_game_directory", null);
                if (dir == null && chkDownloadImages.isChecked())
                    selectDirectory();
                return true;
            });
        }

        chkEnableFileLogging = findPreference("enable_logging");
        if (chkEnableFileLogging != null) {
            chkEnableFileLogging.setOnPreferenceClickListener(preference -> {
                var dir = sharedPreferences.getString("pref_game_directory", null);
                if (dir == null && chkEnableFileLogging.isChecked())
                    selectDirectory();
                return true;
            });
        }

        prefDirectory = findPreference("pref_game_directory");
        if (prefDirectory != null) {
            prefDirectory.setSummary(sharedPreferences.getString("pref_game_directory", null));
            prefDirectory.setOnPreferenceClickListener(preference -> {
                selectDirectory();
                return true;
            });
        }
    }

    private void createNoMediaFile(Uri treeUri) {
        if (getContext() == null)
            return;
        DocumentFile pickedDir = DocumentFile.fromTreeUri(getContext(), treeUri);
        if (pickedDir == null || !pickedDir.canWrite()) {
            return;
        }
        var df = pickedDir.findFile(".nomedia");
        if (df == null || !df.exists())
            pickedDir.createFile("application/octet-stream", ".nomedia");
    }

    private void selectDirectory() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
        intent.putExtra("android.content.extra.SHOW_ADVANCED", true);
        intent.putExtra("android.content.extra.FANCY", true);
        intent.putExtra("android.content.extra.SHOW_FILESIZE", true);
        openDocumentTreeLauncher.launch(intent);
    }
}