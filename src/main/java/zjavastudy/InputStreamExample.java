package zjavastudy;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamExample {

	public static void main(String[] args) throws IOException {
		InputStream inputStream = System.in;

//		int read = inputStream.read();
//		int read1 = inputStream.read();
//		System.out.println(read);
//		System.out.println(read1);

		byte[] bytes = new byte[10];
		inputStream.read(bytes);

		for (byte aByte : bytes) {
			char chr = (char) aByte;
			System.out.println(chr);
		}
	}
}
