package project.chronos;

/** Copyright 2014 Dr Richard Wallace */

import android.os.AsyncTask;


public class DoRequest extends AsyncTask<String, Void, String> {
    private final MainActivity1 main;
    private static final String TAG = "PandorabotsTalkAPIDemo";
    public static String botname = "chrono";
	PandorabotsAPI pApi;
    
    public DoRequest(MainActivity1 main, String clientName) {
        super();
        this.main = main;
        this.pApi = new PandorabotsAPI(MagicParameters.hostname, MagicParameters.username, MagicParameters.userkey, clientName);
    }
   
    @Override
    protected String doInBackground(String... strings) {
        return pApi.talk(botname, strings[0]);
    }

    @Override
    protected void onPostExecute(String result) {
    	main.processBotResponse(result);

    }
}
