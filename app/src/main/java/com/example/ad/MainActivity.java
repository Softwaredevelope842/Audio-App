package com.example.ad;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button button;
    ListView listView;
    ArrayList<String> alpath;
    ArrayList<String> alname;
    MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        button = findViewById(R.id.button1);
                listView = findViewById(R.id.audiolv);
                alname = new ArrayList<>();
                alpath = new ArrayList<>();

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkSelfPermission(android.Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED) {

                            ReadMusic();
                        } else {
                            requestPermissions(new String[]{android.Manifest.permission.READ_MEDIA_AUDIO}, 143);

                        }

                    }
                });
            }

            @SuppressLint("Range")
            private void ReadMusic() {
                Uri musicuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                ContentResolver resolver = getContentResolver();
                String sel = MediaStore.Audio.Media.IS_MUSIC + " !=0";

                String shortorder = MediaStore.Audio.Media.TITLE + " ASC";
                // Cursor cursor resolver.query(musicuri.null,sel,null,shortorder);
                Cursor cursor = resolver.query(musicuri, null, sel, null, shortorder);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                        long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                        alname.add(name);
                        Uri uri = ContentUris.withAppendedId(musicuri, id);
                        alpath.add(uri.toString());
                        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, alname);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String paths = alpath.get(i).toString();
                                Toast.makeText(MainActivity.this, paths, Toast.LENGTH_SHORT).show();

                                PlayMusic(paths);
                            }
                        });
                    }
                }
            }

            private void PlayMusic(String paths) {

                if (mediaPlayer != null) {
                    mediaPlayer.stop();


                } else {
                    mediaPlayer = new MediaPlayer();

                    try {
                        mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(paths));
                        mediaPlayer.prepare();
                        mediaPlayer.start();

                        // Toast maketext(this,"Music Started",Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "Music started", Toast.LENGTH_SHORT).show();


                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            public void onRequestPermissionResult(int requestcode,String[] Permissions,int[] grantresult){
                super.onRequestPermissionsResult(requestcode,Permissions,grantresult);
                if(requestcode==143){
                    if(grantresult[0]==PackageManager.PERMISSION_GRANTED){
                        ReadMusic();
                    }
                    else{
                        Toast.makeText(this,"permission Denied",Toast.LENGTH_SHORT).show();
                        requestPermissions(new String[]{Manifest.permission.READ_MEDIA_AUDIO}, 143);

                    }
                }
            }
        }









