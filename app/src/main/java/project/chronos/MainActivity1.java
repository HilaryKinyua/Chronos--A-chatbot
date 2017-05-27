package project.chronos;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity1 extends AppCompatActivity {
    String client_name;
    EditText userinput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        userinput = (EditText) findViewById(R.id.userInput);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        client_name = prefs.getString("client_name", "");
        // set client name if there is none
        if (client_name == "") {
            PandorabotsAPI pApi = new PandorabotsAPI(MagicParameters.hostname, MagicParameters.username, MagicParameters.userkey, "");
            client_name = pApi.debugBot("chrono", "init", false, false, false, true);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("client_name", client_name);
            editor.commit();
        }
        setAskButton();
    }

    public void setAskButton() {
        FloatingActionButton askButton = (FloatingActionButton) findViewById(R.id.askButton);
        askButton.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.userInput);
                String message = editText.getText().toString();
                editText.setText("");
                        if (message.contains("call") || message.contains("dial")) {
                            String[] str = message.split(" ");
                           String callingnumber = str[1];

                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                           //callIntent.setData(Uri.parse("tel:xxxxxxx")); //This work


                            callIntent.setData(Uri.parse("tel:" + callingnumber));
                            if (ActivityCompat.checkSelfPermission(MainActivity1.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling

                           return;
                           }
                           startActivity(callIntent);

                           }



                        askHumanMessage(message);
                    }
                });
            }

            // initiates the chat with the bot
            public void askHumanMessage(String message) {
                DoRequest botChat = new DoRequest(this,client_name);
                botChat.execute(message);

            }

            /// process the bots response to id any non natural language response text
            public void processBotResponse(String result) {
                if (result.contains("<oob>")) {
                    OOBProcessor oob = new OOBProcessor(this);
                    try {
                        oob.removeOobTags(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    result = removeTags(result);
                    showBotResponse(result);
                }
            }

            /// display the bots response in the text view
            public void showBotResponse(String message) {
                TextView textView = (TextView) findViewById(R.id.botResponse);
                textView.setText(message);
            }

            private String removeTags(String string) {
                Pattern REMOVE_TAGS = Pattern.compile("<.+?>");

                if (string == null || string.length() == 0) {
                    return string;
                }
                Matcher m = REMOVE_TAGS.matcher(string);
                return m.replaceAll("");
            }

            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (requestCode == OOBProcessor.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap =  (Bitmap) extras.get("data");
                    //ImageView iv = (ImageView) findViewById(R.id.showImage);
                    //iv.setImageBitmap(imageBitmap);
                    showBotResponse("There you go.");
                }
            }
        }


