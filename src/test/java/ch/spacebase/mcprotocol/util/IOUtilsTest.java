package ch.spacebase.mcprotocol.util;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class IOUtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testReadMetadata() {
		try {
			ByteArrayOutputStream data = new ByteArrayOutputStream();
			DataOutputStream d = new DataOutputStream(data);
			d.writeByte(127);

			DataInputStream in = new DataInputStream(new ByteArrayInputStream(((ByteArrayOutputStream) data).toByteArray()));

			int b = in.readUnsignedByte();
			assertEquals(127, b);
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException should not occur");
		}
	}

}
