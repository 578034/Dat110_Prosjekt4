package no.hvl.dat110.ac.restservice;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.put;
import static spark.Spark.post;
import static spark.Spark.delete;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Hello world!
 *
 */
public class App {
	
	static AccessLog accesslog = null;
	static AccessCode accesscode = null;

	public static void main(String[] args) {

		if (args.length > 0) {
			port(Integer.parseInt(args[0]));
		} else {
			port(8080);
		}

		// objects for data stored in the service
		
		accesslog = new AccessLog();
		accesscode  = new AccessCode();
		
		after((req, res) -> res.type("application/json"));

		post("/accessdevice/log/", (req, res) -> {
			Gson gson = new Gson();
			System.out.println(gson.toJson(req.body()));
			System.out.println("Received request from: " + req.host());
			AccessMessage accessMessage = gson.fromJson(req.body(), AccessMessage.class);
			String message = accessMessage.getMessage();
			int id = accesslog.add(message);
			AccessEntry entry = accesslog.get(id);
			return gson.toJson(entry);
		});

		get("/accessdevice/log/", (req, res) -> {
			Gson gson = new Gson();
			return gson.toJson(accesslog.log);
		});

		get("/accessdevice/log/:id", (req, res) -> {
			Gson gson = new Gson();
			int id = Integer.parseInt(req.params(":id"));

			AccessEntry entry = accesslog.get(id);
			return gson.toJson(entry);
		});

		put("/accessdevice/code", (req, res) -> {
			Gson gson = new Gson();

			AccessCode code = gson.fromJson(req.body(), AccessCode.class);
			accesscode.setAccesscode(code.getAccesscode());
			res.body(gson.toJson(code));
			System.out.println(res.body());
			accesslog.add("Code modified...");
			return res;
		});

		// OK
		get("/accessdevice/code", (req, res) -> {
			Gson gson = new Gson();
			AccessCode code = new AccessCode();

			code.setAccesscode(accesscode.getAccesscode());
			return gson.toJson(code);
		});

		delete("/accessdevice/log/", (req, res) -> {
			Gson gson = new Gson();
			accesslog.clear();
			return gson.toJson(accesslog.log);
		});
		
		// TODO: implement the routes required for the access control service
		// as per the HTTP/REST operations describined in the project description
		
    }
    
}