package ch.templer.services.multimedia;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import ch.templer.utils.logging.Logger;

/**
 * Created by Templer on 5/6/2016.
 */
public class SoundService {
    private MediaPlayer mediaPlayer;
    private int currentSong = 0;
    private int currentSongPosition = 0;
    private int songId;
    private CompletionListener completionListener = new CompletionListener();
    private boolean isPlayerReleased = true;
    private int length;
    private Logger log = Logger.getLogger();
    Context context;

    public SoundService (Context context){
        this.context = context;
    }

    private MediaPlayer newMediaPlayerInstance(int id) {
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

    public void playSound(int id) {

        playSound(id, 0);
    }

    public void playSound(int id, int songPosition) {
        currentSong = 0;
        songId = id;
        currentSongPosition = songPosition;
        PlaySoundAsyncTask httpGetAsyncTask = new PlaySoundAsyncTask();
        httpGetAsyncTask.execute();

    }
    public void pause() {
        if (mediaPlayer != null && !isPlayerReleased) {
            length = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
        }
    }

    public void resume() {
        if (mediaPlayer != null && !isPlayerReleased) {
            mediaPlayer.seekTo(length);
            playSound(songId, length);
        }
    }

    public void stop() {
        if (mediaPlayer != null && !isPlayerReleased) {
            mediaPlayer.stop();
        }
    }

    public boolean isPlaying() {
        if (mediaPlayer != null && !isPlayerReleased) {
            return mediaPlayer.isPlaying();
        }
        return false;
    }

    public int getSongPosition(){
        if (mediaPlayer != null && !isPlayerReleased) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    class PlaySoundAsyncTask extends AsyncTask<String,Integer, Void>
    {

        protected void onPreExdcute()
        {

        }

        @Override
        protected Void doInBackground(String... arg0)
        {
            mediaPlayer = newMediaPlayerInstance(songId);
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

    private class CompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            mp.release();
            mp = null;
            log.d("released");
            isPlayerReleased = true;
        }
    }
}
