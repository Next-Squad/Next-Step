package http;

public class Cookie {

	private final String key;
	private final String value;

	public Cookie(String key, String value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public String toString() {
		return key + "=" + value + ";";
	}
}
