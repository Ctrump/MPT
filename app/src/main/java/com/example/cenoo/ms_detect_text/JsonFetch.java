package com.example.cenoo.ms_detect_text;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonFetch {
	private static String API_KEY = "5bea09475648495d8759fff641a0b11b";

	public static String getJsonString(String text) {
		String postJson = "{\"documents\": [{\"id\": \"1\",\"text\": \"" + text + "\"}]}";
		HttpURLConnection connection = null;
		try {
			byte [] bytes = postJson.getBytes();
			URL url = new URL("https://westus.api.cognitive.microsoft.com/text/analytics/v2.0/sentiment");
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Ocp-Apim-Subscription-Key", API_KEY);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Content-Length", String.valueOf(bytes.length));
			connection.setDoInput(true);
			connection.setDoOutput(true);

			OutputStream write = connection.getOutputStream();
			write.write(bytes);

			if (connection.getResponseCode() == 200) {
				InputStream in = connection.getInputStream();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				int bytesRead;
				byte [] buffer = new byte[1024];

				while ((bytesRead = in.read(buffer)) > 0) {
					out.write(buffer, 0, bytesRead);
				}
				out.close();
				return new String(out.toByteArray());

			} else {
				System.out.println("HTTP ResponseCode = " + connection.getResponseCode());
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null)
				connection.disconnect();
		}

		return null;
	}

}
