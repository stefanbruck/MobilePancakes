package com.agilent.shipit.pancakemobile.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;

public class QRCodeUtilsTest {

	@Test
	public void text_to_qrcode_to_text() throws IOException, WriterException, NotFoundException {
		// Preparation
		String content = "test content";

		// Test
		byte[] qrCode = QRCodeUtils.generateQRCodeImage(content);
		String readQRCode = QRCodeUtils.readQRCode(qrCode);

		// Verification
		assertEquals(content, readQRCode);
	}

}
