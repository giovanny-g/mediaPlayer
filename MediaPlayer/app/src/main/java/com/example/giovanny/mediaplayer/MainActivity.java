package com.example.giovanny.mediaplayer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;


class Mp3Filter implements FilenameFilter{
    @Override
    public boolean accept(File dir, String name) {
        return (name.endsWith(".mp3"));
    }
}

public class MainActivity extends Activity{
    TextView title;
    //ImageView imgForward1;
    ListView lv;
    String[] items;

    //Path absoluto de la tarjeta SD externa, para buscar mas paths ingresar al adb shell"
    String SD_PATH = new String("/storage/extSdCard/");

    public List<String> songs = new ArrayList<String>();
    ArrayList<File> mySongs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = (TextView) this.findViewById(R.id.tvTitle);
        //imgForward1 = (ImageView) this.findViewById(R.id.imgForward);
        lv = (ListView) findViewById(R.id.lvPlayList);


        title.setTextSize(22);
        title.setTextColor(Color.BLUE);
        title.setText("My Play List\n");

        //imgForward1.setOnClickListener(this);

        mySongs = findSongs(Environment.getExternalStorageDirectory());
        items = new String[mySongs.size()];

        for (int i = 0; i < mySongs.size(); i++) {

            //toast(mySongs.get(i).getName().toString());
            items[i] = mySongs.get(i).getName().toString().replace(".mp3", "");

        }
        ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(), R.layout.song_layout, R.id.textView, items);
        lv.setAdapter(adp);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                startActivity(new Intent(getApplicationContext(), Player.class).putExtra("pos", position).putExtra("songList", mySongs));

             }
        });
    }


    public ArrayList<File> findSongs(File root){
        ArrayList<File> al = new ArrayList<File>();
        File[] files = root.listFiles();

        for(File singleFile : files){
            if (singleFile.isDirectory() && !singleFile.isHidden()){

                al.addAll(findSongs(singleFile));

            }else{
                if(singleFile.getName().endsWith(".mp3")){

                    al.add(singleFile);
                }
            }

        }
        return al;

    }
    public void  toast(String text){

        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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