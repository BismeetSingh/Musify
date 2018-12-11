package com.app.bissudroid.musify.service
import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.IBinder
import com.app.bissudroid.musify.events.*
import com.app.bissudroid.musify.utils.NotificationUtils
import com.app.bissudroid.musify.utils.SharedPreferenceUtils
import io.reactivex.disposables.Disposable
import timber.log.Timber

class MusicForegroundService : Service(), MediaPlayer.OnPreparedListener,
  MediaPlayer.OnErrorListener
,MediaPlayer.OnCompletionListener,AudioManager.OnAudioFocusChangeListener{
    private var songSeek = 0
    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCompletion(mp: MediaPlayer?) {

    }
    lateinit var notificationUtils:NotificationUtils
    override fun onAudioFocusChange(focusChange: Int) {
        when (focusChange) {
            AudioManager.AUDIOFOCUS_LOSS -> {
//                stopMusic()
            }
            AudioManager.AUDIOFOCUS_GAIN -> {
//                playMusic()
                mediaPlayer.setVolume(1.0f, 1.0f)
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
//                pauseMusic()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                mediaPlayer.setVolume(0.2f, 0.2f)
            }
        }
    }



    override fun onPrepared(mp: MediaPlayer?) {
        mp?.seekTo(SharedPreferenceUtils.getCurrentSeekPosition(applicationContext)!!)
        Timber.d("%d",mp?.currentPosition);
        mp?.start()
//        startForegroundService()
    }

    lateinit var mediaPlayer: MediaPlayer
    private var songName: String = ""
    private lateinit var personDisposable: Disposable

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()

        notificationUtils = NotificationUtils(baseContext)
        startForegroundService()


    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {


//        val action = intent.getAction()
        RxBus.listen(PlaySong::class.java).subscribe{
            playSong()
            startForegroundService()
        }
        RxBus.listen(SavePosition::class.java).subscribe{
            Timber.d("Position %d",mediaPlayer.currentPosition)
            SharedPreferenceUtils.setCurrentSeekPosition(applicationContext,mediaPlayer.currentPosition)

        }

        RxBus.listen(PlaybackState::class.java).subscribe {
//            adapter.addPerson(person = it.)/
            Timber.d("%s",it.playing)
            startForegroundService()

            if(it.playing){
                resumeSong()
            }
            else{
                pauseSong()
            }

        }



//        when (action) {
////            Constants.ACTION_START_FOREGROUND_SERVICE -> {
////                startForegroundService()
////                Toast.makeText(applicationContext, "Foreground service is started.", Toast.LENGTH_LONG).show()
////            }
//            Constants.ACTION_STOP_FOREGROUND_SERVICE -> {
//                stopForegroundService()
//                Toast.makeText(applicationContext, "Foreground service is stopped.", Toast.LENGTH_LONG).show()
//            }
//
//            Constants.ACTION_PAUSE -> {
//                pauseSong()
//
//            }
//            Constants.ACTION_RESUME -> {
//                resumeSong()
//
//            }

//        }
        return super.onStartCommand(intent, flags, startId)
    }

    /* Used to build and start foreground service. */
    private fun startForegroundService() {

        val builder = notificationUtils.getAndroidChannelNotification("Now Playing")
        val notification = builder.build()
        startForeground(1, notification)
    }

    private fun playSong() {
        Timber.d("Played")
        songName = SharedPreferenceUtils.getCurrentSongPath(applicationContext)!!
        mediaPlayer.reset()

        Timber.d("%d",SharedPreferenceUtils.getCurrentSeekPosition(applicationContext!!))

        if (!mediaPlayer.isPlaying && !songName.isEmpty()) {

            mediaPlayer.setDataSource(songName)
            mediaPlayer.setOnPreparedListener(this)

            mediaPlayer.prepareAsync()

        } else if (SharedPreferenceUtils.isPlaying(applicationContext) && mediaPlayer.isPlaying) {


            mediaPlayer.stop()
            mediaPlayer.reset()


            mediaPlayer.setDataSource(songName)


        }
    }

    //This method pauses the current song
    private fun pauseSong() {
        Timber.d("Paused")
        SharedPreferenceUtils.setCurrentSeekPosition(applicationContext,mediaPlayer.currentPosition)
        Timber.d("%d",mediaPlayer.currentPosition);
        mediaPlayer.pause()



    }

    //This method resumes the current song
    private fun resumeSong() {
        Timber.d("Resumed")

        if(songName.isEmpty()){
            playSong()
        }
        else
        {
//            mediaPlayer.seekTo(SharedPreferenceUtils.getCurrentSeekPosition(applicationContext)!!)
            mediaPlayer.start()
        }


    }

    //This method puts song to loop
    private fun loopSong() {
        mediaPlayer.isLooping = true
    }

    private fun stopForegroundService() {

        // Stop foreground service and remove the notification.
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForegroundService()
        notificationUtils.unregisterReceiver()
    }
//    public enum PlayerStates{PAUSED,STOPPED,PLAYING};
//    PlayerStates state;
//
//
//    public void pause(){
//        mPlayer.pause();
//        state=PlayerStates.PAUSED;
//    }
//
//    public void play(){
//        mPlayer.start();
//        state=PlayerStates.PLAYING;
//    }
//
//    public void stop(){
//        mPlayer.stop();
//        state=PlayerStates.STOPPED;
//    }
//
//
//// returns the current state of MediaPlayer
//    public PlayerStates getState(){
//        return state;
//    }
}