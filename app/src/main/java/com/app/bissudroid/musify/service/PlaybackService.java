/*
 * MIT License
 *
 * Copyright (c) 2017 MIchael Obi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.app.bissudroid.musify.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import com.app.bissudroid.musify.events.PlaybackState;
import com.app.bissudroid.musify.events.RequestPlaybackState;
import com.app.bissudroid.musify.events.RxBus;
import com.app.bissudroid.musify.events.Seek;
import com.app.bissudroid.musify.interfaces.QueueManager;

/**
 * PaperPlayer Michael Obi 11 01 2017 10:46 PM
 */
public class PlaybackService extends Service implements MediaPlayer.OnErrorListener, AudioManager
        .OnAudioFocusChangeListener, MediaPlayer.OnCompletionListener, MediaPlayer
        .OnPreparedListener {
    private static final String TAG = "PlaybackService";
    private QueueManager queueManager;
//    private MusicRepositoryInterface musicRepository;
    private RxBus eventBus;
    private MediaPlayer player;
    private int songSeek = 0;

    public PlaybackService() throws InstantiationException, IllegalAccessException {
//        eventBus = Injector.provideEventBus();
//        queueManager = Injector.provideQueueManager();
//        musicRepository = Injector.provideMusicRepository(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
        player.setOnCompletionListener(this);
        player.setOnPreparedListener(this);
        player.setOnErrorListener(this);
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        registerEvents();
//        musicRepository.getAllSongs().subscribe(songs -> queueManager.setQueue(songs, 0));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        eventBus.post(new RequestPlaybackState());
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventBus.cleanup(this);
        player.release();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS:
                stopMusic();
                break;
            case AudioManager.AUDIOFOCUS_GAIN:
                playMusic();
                player.setVolume(1.0f, 1.0f);
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                pauseMusic();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                player.setVolume(0.2f, 0.2f);
                break;
            default:
        }
    }

    private void pauseMusic() {
        if (player != null) {
            if (player.isPlaying()) {
                songSeek = player.getCurrentPosition();
                player.pause();
            }
            PlaybackState playbackState = new PlaybackState(queueManager.getCurrentSong(),
                    player.isPlaying(), player.getDuration(), player.getCurrentPosition());
            eventBus.post(playbackState);
        }
    }

    private boolean requestAudioFocus() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = am.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    private void abandonAudioFocus() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(this);
    }

    private void registerEvents() {
//        eventBus.subscribe(PlayEvent.class, this, event -> musicRepository.getAllSongs()
//                .subscribe(songs -> {
//                    stopMusic();
//                    queueManager.setQueue(songs, event.getStartSongId());
//                    playMusic();
//                }));

//        eventBus.subscribe(TogglePlayback.class, this, event -> {
//            if (player.isPlaying()) {
//                pauseMusic();
//                return;
//            }
//            playMusic();
//        });

        eventBus.subscribe(RequestPlaybackState.class, this, requestPlaybackState -> {

            int duration = player.isPlaying() ? player.getDuration() : 0;
            PlaybackState playbackState = new PlaybackState(queueManager.getCurrentSong(),
                    player.isPlaying(), duration, player.getCurrentPosition());
//            eventBus.post(new ShuffleState(queueManager.isShuffled()));
//            eventBus.post(new RepeatState(queueManager.getRepeatState()));
            if (queueManager.hasSongs()) {
                eventBus.post(playbackState);
            }
        });
//        eventBus.subscribe(NextSong.class, this,
//                nextSong -> playNextSong(true));
//
//        eventBus.subscribe(PreviousSong.class, this,
//                nextSong -> playPreviousSong());

        eventBus.subscribe(Seek.class, this, seek -> {
            songSeek = seek.getSeekTo();
            player.seekTo(songSeek);
            eventBus.post(new RequestPlaybackState());
        });

//        eventBus.subscribe(ToggleShuffle.class, this, toggleShuffle -> {
//            boolean isShuffled = queueManager.toggleShuffle();
//            eventBus.post(new ShuffleState(isShuffled));
//        });
//        eventBus.subscribe(ToggleRepeat.class, this,
//                toggleRepeat -> eventBus.post(new RepeatState(queueManager.toggleRepeat())));
    }

    private void playPreviousSong() {
        boolean playing = player.isPlaying();
        pauseMusic();
        if (player.getCurrentPosition() > 3) {
            songSeek = 0;
            if (queueManager.previous() != null) {
                if (playing) {
                    playMusic();
                }
            }
        } else {
            songSeek = 0;
            player.seekTo(songSeek);
        }
        eventBus.post(RequestPlaybackState.class);
    }

    private void stopMusic() {
        pauseMusic();
        songSeek = 0;
        abandonAudioFocus();
    }

    private void playMusic() {
        if (!player.isPlaying()) {
            player.reset();
//            try {
//                if (queueManager.hasSongs()) {
//                    Uri uri = Uri.parse(queueManager.getCurrentSong().getSongUri());
//                    player.setDataSource(this, uri);
//                    player.prepareAsync();
//                }
//            } catch (IOException e) {
//                Log.e(TAG, e.getMessage(), e);
//            }
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {


        playNextSong(false);
    }

    private void playNextSong(boolean ignoreRepeatOnce) {
        boolean playing = player.isPlaying();
        pauseMusic();
        if (queueManager.next(ignoreRepeatOnce) != null) {
            songSeek = 0;
            if (playing) {
                playMusic();
            }
        }
        eventBus.post(new RequestPlaybackState());
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        if (requestAudioFocus()) {
            mediaPlayer.seekTo(songSeek);
            mediaPlayer.start();
            PlaybackState playbackState = new PlaybackState(queueManager.getCurrentSong(),
                    mediaPlayer.isPlaying(), mediaPlayer.getDuration(), mediaPlayer.getCurrentPosition());
            eventBus.post(playbackState);
            eventBus.post(new RequestPlaybackState());
        }
    }
}