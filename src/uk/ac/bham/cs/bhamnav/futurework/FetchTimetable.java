package uk.ac.bham.cs.bhamnav.futurework;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.util.Log;

public class FetchTimetable {

//	private final String path = "/data/data/uk.ac.bham.cs.bhamnav"; // destination

	public void download(String timetableURL, String nameoffile){

		File file = new File(nameoffile);

		try {
			ByteArrayBuffer array = new ByteArrayBuffer(100);

			URL url = new URL("http://www.cs.bham.ac.uk/");
			Log.i("NavMap", "Initiating download");
			URLConnection connection = url.openConnection();

			InputStream in = connection.getInputStream();
			BufferedInputStream buff = new BufferedInputStream(in);

			int read = 0;
			while ((read = buff.read()) != -1) {
				array.append((byte) read);
			}

			FileOutputStream out = new FileOutputStream(file);
			out.write(array.toByteArray());
			out.close();
		}

		catch (IOException e) {
			Log.i("NavMap", "Error downloading timetable: " + e.toString());
		}
	}
}