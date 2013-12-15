package ch.spacebase.mc.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

public class IOUtils {

	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	public static void closeQuietly(Closeable close) {
		try {
			if(close != null) {
				close.close();
			}
		} catch(IOException e) {
		}
	}

	public static String toString(InputStream input, String encoding) throws IOException {
		StringWriter writer = new StringWriter();
		InputStreamReader in = encoding != null ? new InputStreamReader(input, encoding) : new InputStreamReader(input);
		char[] buffer = new char[DEFAULT_BUFFER_SIZE];
		int n = 0;
		while(-1 != (n = in.read(buffer))) {
			writer.write(buffer, 0, n);
		}

		in.close();
		return writer.toString();
	}

}
