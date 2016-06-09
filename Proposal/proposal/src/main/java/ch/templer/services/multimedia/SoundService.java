package ch.templer.services.multimedia;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import ch.templer.utils.logging.Logger;

/**
 * Created by Templer on 5/6/2016.
 */
public class SoundService {
    private static MediaPlayer mediaPlayer;
    private static int currentSong = 0;
    private static int currentSongPosition = 0;
    private static int songId;
    private static Context soundServiceContext;
    private static CompletionListener completionListener = new CompletionListener();
    private static boolean isPlayerReleased = true;
    private static int length;
    private static Logger log = Logger.getLogger();

    private static MediaPlayer newMediaPlayerInstance(Context context, int id) {
        if (mediaPlayer != null) {
            // flag hast to be set, because a seperate thread can call other service methods in the meantime
            isPlayerReleased = true;
            log.d("released");
            mediaPlayer.release();
            mediaPlayer = null;
        }
        isPlayerReleased = false;
        log.d("initalized");
        return MediaPlayer.create(context, id);
    }

    public static void playSound(Context context, int id) {
        playSound(context, id, 0);
    }

    public static void playSound(Context context, int id, int songPosition) {
        currentSong = 0;
        songId = id;
        soundServiceContext = context;
        currentSongPosition = songPosition;
        httpGetAsynchTask httpGetAsyncTask = new httpGetAsynchTask();
        httpGetAsyncTask.execute();

    }
    public static void pause() {
        if (mediaPlayer != null && !isPlayerReleased) {
            length = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
        }
    }

    public static void resume() {
        if (mediaPlayer != null && !isPlayerReleased) {
            mediaPlayer.seekTo(length);
            playSound(soundServiceContext, songId, length);
        }
    }

    public static void stop() {
        if (mediaPlayer != null && !isPlayerReleased) {
            mediaPlayer.stop();
        }
    }

    public static boolean isPlaying() {
        if (mediaPlayer != null && !isPlayerReleased) {
            return mediaPlayer.isPlaying();
        }
        return false;
    }

    public static int getSongPosition(){
        if (mediaPlayer != null && !isPlayerReleased) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    static class httpGetAsynchTask extends AsyncTask<String,Integer, Void>
    {

        protected void onPreExdcute()
        {

        }

        @Override
        protected Void doInBackground(String... arg0)
        {
            mediaPlayer = newMediaPlayerInstance(soundServiceContext, songId);
            mediaPlayer.seekTo(currentSongPosition);
            //mediaPlayer.setLooping(true);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(completionListener);
            return null;
        }

        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
        }

    }

    private static class CompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            mp.release();
            mp = null;
            log.d("released");
            isPlayerReleased = true;
        }
    }
}
