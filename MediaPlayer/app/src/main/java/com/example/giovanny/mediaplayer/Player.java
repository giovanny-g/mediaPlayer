    package com.example.giovanny.mediaplayer;

    import android.content.Intent;
    import android.media.MediaPlayer;
    import android.net.Uri;
    import android.support.v7.app.ActionBarActivity;
    import android.os.Bundle;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.ImageView;
    import android.widget.SeekBar;
    import android.widget.TextView;

    import java.io.File;
    import java.util.ArrayList;

    public class Player extends ActionBarActivity implements View.OnClickListener {

    static MediaPlayer mp = null;
    ArrayList<File> mySongs;
    int position;
    Uri u;
    SeekBar sb;
    Thread updateSeekBar;
    public ImageView imgPlay, imgPrevious, imgNext, imgBackward1, imgfastForward1, imgfastBackward1;
    public TextView currentSong, timeCurrent, timeEnd, tvPosition;
    File cancion;
    Bundle b;
    int  totalDuration;
    float secondsTotal, minutesTotal;


        @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_player);

    sb = (SeekBar) findViewById(R.id.seekBar);

    imgPlay = (ImageView) findViewById(R.id.imgPlay);
    imgPrevious = (ImageView) findViewById(R.id.imgPrevious);
    imgNext = (ImageView) findViewById(R.id.imgNext);
    imgBackward1 = (ImageView) this.findViewById(R.id.imgBackward);
    imgfastBackward1 = (ImageView) this.findViewById(R.id.imgfastBackward);
    imgfastForward1 = (ImageView) this.findViewById(R.id.imgfastForward);

    currentSong = (TextView) findViewById(R.id.tvCurrentSong);
    timeCurrent = (TextView) findViewById(R.id.timeStart);
    timeEnd = (TextView) findViewById(R.id.timeEnd);
    //tvPosition = (TextView) findViewById(R.id.tvPosition);

    updateSeekBar = new Thread() {
        @Override
        public void run() {
           super.run();

            getDuration();

            Integer curretPosition = 0;
            sb.setMax(totalDuration);
            while(curretPosition < totalDuration){
                try{
                    sleep(1000);
                    curretPosition = mp.getCurrentPosition();
                    sb.setProgress(curretPosition);

                    final Integer CurretPosition = curretPosition;

                /*Internal Thread that shows the song time advanced */
                    runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        float minutesCurrent,secondsCurrent;

                            minutesCurrent = CurretPosition / 1000 / 60;
                            secondsCurrent = CurretPosition / 1000 % 60;
                            timeCurrent.setText(String.valueOf(Math.round(minutesCurrent)) + ":" + String.valueOf(Math.round(secondsCurrent)));

                            }
                    });
                /*End of internal thread*/
                }catch (InterruptedException e){
                    e.printStackTrace();

                }catch(IllegalStateException e1){

                    e1.getMessage();
                }
            }
        }
    };
    imgPlay.setOnClickListener(this);
    imgPrevious.setOnClickListener(this);
    imgNext.setOnClickListener(this);
    imgBackward1.setOnClickListener(this);
    imgfastForward1.setOnClickListener(this);
    imgfastBackward1.setOnClickListener(this);

    if (mp != null){

        mp.stop();
        mp.release();
        mp = null;

    }
    Intent i = getIntent();
    b = i.getExtras();

    mySongs = (ArrayList) b.getParcelableArrayList("songList");

    position = b.getInt("pos", 0);

    u = Uri.parse(mySongs.get(position).toString());
    mp = MediaPlayer.create(getApplicationContext(), u);

    cancion = new File(""+ u);
    currentSong.setTextSize(20);
    currentSong.setText(cancion.getName().toString().replace(".mp3", ""));

    mp.start();
    imgPlay.setImageResource(R.drawable.ic_pause);
    updateSeekBar.start();

    sb.setMax(mp.getDuration());

    sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

            mp.seekTo(seekBar.getProgress());
        }
    });
    }
    @Override
    public void onClick(View v) {
    int id = v.getId();
    switch (id) {
        case R.id.imgPlay:
            if (mp.isPlaying()) {
                imgPlay.setImageResource(R.drawable.ic_play);
                mp.pause();

            } else {
                imgPlay.setImageResource(R.drawable.ic_pause);
                mp.start();
            }
            break;
        case R.id.imgfastForward:

            mp.seekTo(mp.getCurrentPosition() + 5000);

            break;
        case R.id.imgfastBackward:

            mp.seekTo(mp.getCurrentPosition() - 5000);

            break;
        case R.id.imgNext:
                mp.stop();
                mp.release();

                position = (position + 1% mySongs.size() < mySongs.size()? position+=1: 0);

                /*
                if(position + 1 % mySongs.size() < mySongs){

                positio = position + 1;

                }else{

                position = 0;

                }

                */

                u = Uri.parse(mySongs.get(position).toString());

                //tvPosition.setText(String.valueOf(position));

                mp = MediaPlayer.create(getApplicationContext(), u);

                cancion = new File(""+ u);
                currentSong.setTextSize(20);
                currentSong.setText(cancion.getName().toString().replace(".mp3", ""));
                imgPlay.setImageResource(R.drawable.ic_pause);

                getDuration();

                mp.start();
                sb.setMax(mp.getDuration());

                break;

        case R.id.imgPrevious:

                //tvPosition.setText(String.valueOf(position));

                mp.stop();
                mp.release();
                position = (position - 1 < 0) ? mySongs.size() - 1 : position - 1;
                /*if (position - 1 < 0){

                    position = mySongs.size() - 1;

                }else{

                    position -= 1;
                }*/
                u = Uri.parse(mySongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(), u);

                cancion = new File(""+ u);
                currentSong.setTextSize(20);
                currentSong.setText(cancion.getName().toString().replace(".mp3", ""));

                imgPlay.setImageResource(R.drawable.ic_pause);

                getDuration();

                mp.start();
                sb.setMax(mp.getDuration());

                break;

        case R.id.imgBackward:

                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(myIntent);
                break;
     }

    }
    //Getting song duration time
    public void getDuration(){

        totalDuration = mp.getDuration();
        minutesTotal = totalDuration/1000/60;
        secondsTotal = totalDuration/1000%60;

        timeEnd.setText(String.valueOf(Math.round(minutesTotal))+":"+String.valueOf(Math.round(secondsTotal)));

    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_player, menu);
    return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
        return true;
    }

    return super.onOptionsItemSelected(item);
    }

    }
