package no.hvl.dat110.aciotdevice.client;

import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.net.Socket;

public class RestClient {

	public OkHttpClient client;

	public RestClient() {
		client = new OkHttpClient();
	}

	public void doPostAccessEntry(String message) {

		//TODO: Not posting messages.

		Gson gson = new Gson();
		client.newBuilder().build();
		AccessMessage message1 = new AccessMessage(message);
		RequestBody requestBody = RequestBody
				.create(MediaType.parse("application/json"), gson.toJson(message1));
		String logpath = "/accessdevice/log/";

		Request request = new Request.Builder()
				.url("http://" + Configuration.host + ":" + Configuration.port + logpath)
				.post(requestBody)
				.addHeader("Content-Type", "application/json")
				.build();
		try (Response responseBody = client.newCall(request).execute()) {
			System.out.println(responseBody.code());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//TODO: Doesn't read properly?

	public AccessCode doGetAccessCode() {

		AccessCode code = null;
		client.newBuilder().build();

		Gson gson = new Gson();
		Response response;
		ResponseBody body;
		String codepath = "/accessdevice/code";

		Request request = new Request.Builder()
				.url("http://" + Configuration.host + ":" + Configuration.port + codepath)
				.method("GET", null)
				.addHeader("Content-Type", "application/json")
				.build();
		try {
			response = client.newCall(request).execute();
			if (response != null) {
				body = response.body();
				code = gson.fromJson(body.string(), AccessCode.class);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return code;

		// TODO: implement a HTTP GET on the service to get current access code
	}
}
