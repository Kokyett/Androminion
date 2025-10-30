package com.mehtank.androminion.ui;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.documentfile.provider.DocumentFile;
import androidx.preference.PreferenceManager;

import com.mehtank.androminion.R;
import com.mehtank.androminion.util.FileManager;

/**
 * Display of game log
 *
 */
public class GameScrollerView extends HorizontalScrollView {
    private static final String TAG = "GameScrollerView";

    private Context top;
    private LinearLayout gameEventsRow;
    private ScrollView latestTurnSV;
    private TextView latestTurn;
    private boolean onlyShowOneTurn = false;
    private int numPlayers;
    private ArrayList<View> views = new ArrayList<View>();
    private DocumentFile logFile;


    public GameScrollerView(Context context) {
        this(context, null);
    }

    public GameScrollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.top = context;

        gameEventsRow = new LinearLayout(top);
        gameEventsRow.setOrientation(LinearLayout.HORIZONTAL);

        addView(gameEventsRow);
    }

    public void clear() {
        gameEventsRow.removeAllViews();
        String filename = "latest.txt";

        if (PreferenceManager.getDefaultSharedPreferences(top).getBoolean("enable_logging", false)) {
            filename = new SimpleDateFormat("'log_'yyyy-MM-dd_HH-mm-ss'.txt'", Locale.US).format(new Date());
        }

        logFile = FileManager.getLogFile(getContext(), filename);
        var contentResolver = getContext().getContentResolver();
        if (logFile != null && contentResolver != null) {
            try {
                var os = contentResolver.openOutputStream(logFile.getUri(), "wa");
                if (os != null) {
                    os.write(new SimpleDateFormat("'New game started on 'yyyy/MM/dd' at 'HH:mm:ss'\n'", Locale.US).format(new Date()).getBytes());
                    os.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
        if (PreferenceManager.getDefaultSharedPreferences(top).getBoolean("one_turn_logs", false)) {
            onlyShowOneTurn = true;
        }
    }

    public void setGameEvent(String s, boolean b, int turnCount, boolean isFleetRound) {
        if (b) {
            latestTurnSV = (ScrollView) LayoutInflater.from(top).inflate(R.layout.view_gamescrollercolumn, gameEventsRow, false);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
            layoutParams.setMargins((int) getResources().getDimension(R.dimen.margin_gamelog),
                                    (int) getResources().getDimension(R.dimen.margin_gamelog),
                                    (int) getResources().getDimension(R.dimen.margin_gamelog),
                                    (int) getResources().getDimension(R.dimen.margin_gamelog));
            gameEventsRow.addView(latestTurnSV, layoutParams);

            latestTurn = (TextView) latestTurnSV.findViewById(R.id.latestTurn);
            String turnText = s + (turnCount > 0 ? (top.getString(R.string.turn_header) + turnCount) : "");
            if (isFleetRound) {
            	turnText += top.getString(R.string.turn_header_fleet);
            }
            latestTurn.setText(turnText);

            latestTurn.setPadding(0, 0, (int) getResources().getDimension(R.dimen.margin_gamelog), 0);

            if (onlyShowOneTurn) {
                views.add(latestTurnSV);
                while (views.size() > numPlayers + 1) {
                    View view = views.remove(0);
                    gameEventsRow.removeView(view);
                }
            }
        } else
            latestTurn.setText(latestTurn.getText() + "\n" + s);

        latestTurnSV.fullScroll(FOCUS_DOWN);
        fullScroll(FOCUS_RIGHT);

        var contentResolver = getContext().getContentResolver();
        if (logFile != null && contentResolver != null) {
            try {
                var os = contentResolver.openOutputStream(logFile.getUri(), "wa");
                if (os != null) {
                    if (b) {
                        os.write(top.getString(R.string.log_turn_separator).getBytes());
                        s += (turnCount > 0 ? (top.getString(R.string.turn_header) + turnCount) : "");
                    }
                    os.write((s + "\n").getBytes());
                    os.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
