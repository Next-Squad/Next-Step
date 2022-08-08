package http;

import java.util.Arrays;

public enum MimeType {
	HTML(".html"), CSS(".css"), JS(".js"), WOFF(".woff"), ICO(".ico"),
	TTF(".ttf"), EOT(".eot"), SVG(".svg"), OTF(".otf");

	private final String extension;

	MimeType(String extension) {
		this.extension = extension;
	}

	public static boolean isSupportedExtension(String url) {
		return searchExtension(url) != null;
	}

	private static MimeType searchExtension(String url){
		 return Arrays.stream(MimeType.values())
			.filter(m -> url.endsWith(m.extension))
			.findAny()
			 .orElse(null);
	}
}
