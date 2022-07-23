package db;

import java.util.ArrayList;
import java.util.List;

public class URLDataBase {

	private static final List<String> registerdURLs = new ArrayList<>();

	private URLDataBase() {}

	static {
		registerdURLs.add("/index.html");
		registerdURLs.add("/user/form.html");
		registerdURLs.add("/user/login.html");
		registerdURLs.add("/user/login_failed.html");
	}

	public static boolean contains(String url) {
		return registerdURLs.contains(url);
	}
}
