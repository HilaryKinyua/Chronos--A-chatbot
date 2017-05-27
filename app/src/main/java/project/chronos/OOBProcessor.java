package project.chronos;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OOBProcessor {

	MainActivity1 activity;
	Exception exception = null;
	static final int REQUEST_IMAGE_CAPTURE = 1;

	public OOBProcessor(MainActivity1 activity) {
		this.activity = activity;
	}

	// remove the oob tags and send it along to the processor
	public void removeOobTags(String output) throws Exception {
		if (output != null) {
			Pattern pattern = Pattern.compile("<oob>(.*)</oob>");
			Matcher matcher = pattern.matcher(output);
			if (matcher.find()) {
				String oobContent = matcher.group(1);
				processInnerOobTags(oobContent);
				activity.showBotResponse(matcher.replaceAll(""));
			}
		}
	}

	// check inner oob command and take appropriate action
	public void processInnerOobTags(String oobContent) throws Exception {
		if (oobContent.contains("<camera>")) {
			oobCamera();
		}
		if (oobContent.contains("<calendar>")) {
			oobCalendar(oobContent);
		}

		if(oobContent.contains("<battery>"))
		{
			oobBattery();
		}

		if(oobContent.contains("<launch>com.google.android.apps.maps</launch>")){
			Intent launchIntent = activity.getPackageManager().getLaunchIntentForPackage("com.google.android.apps.maps");
			if (launchIntent != null) {
				activity.startActivity(launchIntent);//null pointer check in case package name was not found
			}
		}
		if(oobContent.contains("<launch>com.google.android.googlequicksearchbox</launch>")){
			Intent launchIntent = activity.getPackageManager().getLaunchIntentForPackage("com.google.android.googlequicksearchbox");
			if (launchIntent != null) {
				activity.startActivity(launchIntent);//null pointer check in case package name was not found
			}
		}
		if(oobContent.contains("<launch>com.google.browser</launch>")){
			Intent launchIntent = activity.getPackageManager().getLaunchIntentForPackage("com.google.browser");
			if (launchIntent != null) {
				activity.startActivity(launchIntent);//null pointer check in case package name was not found
			}
		}
		if(oobContent.contains("<launch>com.android.gallery</launch>")){
			Intent launchIntent = activity.getPackageManager().getLaunchIntentForPackage("com.android.gallery");
			if (launchIntent != null) {
				activity.startActivity(launchIntent);//null pointer check in case package name was not found
			}
		}

		if(oobContent.contains("<launch>com.google.android.gm</launch>")){
			Intent launchIntent = activity.getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
			if (launchIntent != null) {
				activity.startActivity(launchIntent);//null pointer check in case package name was not found
			}
		}
		if(oobContent.contains("<launch>com.android.music</launch>")){
			Intent launchIntent = activity.getPackageManager().getLaunchIntentForPackage("com.android.music");
			if (launchIntent != null) {
				activity.startActivity(launchIntent);//null pointer check in case package name was not found
			}
		}
		if(oobContent.contains("<url>http://www.square-bear.co.uk/mitsuku/jpgto.php?picture=<star/></url>")) {
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.square-bear.co.uk/mitsuku/"));
			activity.startActivity(intent);
		}
		if(oobContent.contains("<url>http://www.facebook.com</url>")) {
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.facebook.com"));
			activity.startActivity(intent);
		}
		if(oobContent.contains("<url>http://www.wikipedia.com</url>")) {
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.wikipedia.com"));
			activity.startActivity(intent);
		}
		if(oobContent.contains("<url>http://www.flipkart.com</url>")) {
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.flipkart.com"));
			activity.startActivity(intent);
		}
		if(oobContent.contains("<url>http://www.instagram.com</url>")) {
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.instagram.com"));
			activity.startActivity(intent);
		}
		if(oobContent.contains("<url>http://www.twitter.com</url>")) {
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.twitter.com"));
			activity.startActivity(intent);
		}
		if(oobContent.contains("<url>http://www.youtube.com</url>")) {
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.youtube.com/"));
			activity.startActivity(intent);
		}
		if(oobContent.contains("<url>http://www.snapdeal.com</url>")) {
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.snapdeal.com"));
			activity.startActivity(intent);
		}
		if(oobContent.contains("<url>http://www.rgpv.ac.in")) {
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.rgpv.ac.in/"));
			activity.startActivity(intent);
		}



	}

	private void oobBattery() {


		BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				context.unregisterReceiver(this);
				int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
				int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
				int level = -1;
				if (rawlevel >= 0 && scale > 0) {
					level = (rawlevel * 100) / scale;
				}
				activity.showBotResponse("Battery Level Remaining: " + level + "%");
			}
		};
		IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		activity.registerReceiver(batteryLevelReceiver, batteryLevelFilter);


	}



	private void oobDial() {


		Intent callIntent = new Intent(Intent.ACTION_CALL);
		//callIntent.setData(Uri.parse("tel:" + ));//change the number
		if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		activity.startActivity(callIntent);
	}

	// open the camera
	public void oobCamera() {	
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (cameraIntent.resolveActivity(activity.getPackageManager()) != null) {
			activity.startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
		}
	}
	
	// make the calendar event
	@SuppressLint("NewApi")
	public void oobCalendar(String oobContent) {
		//match and extract the content in the various fields within the calendar event
		Pattern bHTimePattern = Pattern.compile("<starthour>(.*)</starthour>");
		Matcher bHTimeMatcher = bHTimePattern.matcher(oobContent);
		Pattern bMTimePattern = Pattern.compile("<startminute>(.*)</startminute>");
		Matcher bMTimeMatcher = bMTimePattern.matcher(oobContent);
		Pattern eHTimePattern = Pattern.compile("<endhour>(.*)</endhour>");
		Matcher eHTimeMatcher = eHTimePattern.matcher(oobContent);
		Pattern eMTimePattern = Pattern.compile("<endminutes>(.*)</endminutes>");
		Matcher eMTimeMatcher = eMTimePattern.matcher(oobContent);
		Pattern dayPattern = Pattern.compile("<day>(.*)</day>");
		Matcher dayMatcher = dayPattern.matcher(oobContent);
		Pattern yearPattern = Pattern.compile("<year>(.*)</year>");
		Matcher yearMatcher = yearPattern.matcher(oobContent);
		Pattern monthPattern = Pattern.compile("<month>(.*)</month>");
		Matcher monthMatcher = monthPattern.matcher(oobContent);
		Pattern titlePattern = Pattern.compile("<title>(.*)</title>");
		Matcher titleMatcher = titlePattern.matcher(oobContent);
		Pattern locPattern = Pattern.compile("<location>(.*)</location>");
		Matcher locMatcher = locPattern.matcher(oobContent);
		if (bHTimeMatcher.find() && bMTimeMatcher.find() && eHTimeMatcher.find() && eMTimeMatcher.find() && dayMatcher.find() && yearMatcher.find() && monthMatcher.find() && titleMatcher.find() && locMatcher.find()) {
			Intent calendarIntent = new Intent(Intent.ACTION_EDIT);
			calendarIntent.setType("vnd.android.cursor.item/event");
			try {
				// create the calendar intent with the data as specified from the conversation
				int year = Integer.parseInt(yearMatcher.group(1).toString());
				int day = Integer.parseInt(dayMatcher.group(1).toString());
				int month = Integer.parseInt(monthMatcher.group(1).toString());
				int hourOfDay = Integer.parseInt(bHTimeMatcher.group(1).toString());
				int minute = Integer.parseInt(bMTimeMatcher.group(1).toString());
				Calendar beginTime = Calendar.getInstance();
				beginTime.set(year, month, day, hourOfDay, minute);	
				hourOfDay = Integer.parseInt(eHTimeMatcher.group(1).toString());
				minute = Integer.parseInt(eMTimeMatcher.group(1).toString());
				Calendar endTime = Calendar.getInstance();
				endTime.set(year, month, day, hourOfDay, minute);
				calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
				calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
				calendarIntent.putExtra(Events.TITLE, titleMatcher.group(1).toString());
				calendarIntent.putExtra(Events.EVENT_LOCATION, locMatcher.group(1).toString());
				activity.startActivity(calendarIntent);
			} catch (Exception ex) {
				activity.processBotResponse("There was an error in scheduling your event.");
			}
		} else activity.showBotResponse("There was an issue with one of the details you specificied. Please try and schedule your event again.");
	}
	
}
