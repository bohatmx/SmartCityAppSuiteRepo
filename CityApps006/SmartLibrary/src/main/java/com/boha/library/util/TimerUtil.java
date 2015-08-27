package com.boha.library.util;

import android.util.Log;



import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by aubreyM on 2014/08/28.
 */
public class TimerUtil {

    public interface TimerListener {
        public void onTimerExpired();
    }
     TimerListener listener;
     Timer timer;
    static final long FIFTEEN_SECONDS = 15 * 1000;
    public  void startTimer(TimerListener timerListener) {
        //
        Log.d("TimerUtil", "########## Websocket Session Timer starting .....");
        listener = timerListener;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.e("TimerUtil", "########## timer expired");
                listener.onTimerExpired();
            }
        }, FIFTEEN_SECONDS);
    }
    public  void killTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            Log.w("TimerUtil", "########## Websocket Session Timer KILLED");
        }
    }
}
