package com.agilent.shipit.pancakemobile.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodeUtils {
	private static final int QR_CODE_HEIGHT = 400;
	private static final int QR_CODE_WIDTH = 400;

	public static byte[] generateQRCodeImage(final String content) throws IOException, WriterException {
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			// ZXing QR-code encoding
			BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, QR_CODE_WIDTH,
					QR_CODE_HEIGHT);

			// Convert to PNG image and write to stream
			MatrixToImageWriter.writeToStream(bitMatrix, "png", outputStream);

			return outputStream.toByteArray();
		}
	}

	public static String readQRCode(byte[] qrCode)
			throws IOException, NotFoundException {
		try (ByteArrayInputStream inputStream = new ByteArrayInputStream(qrCode)) {
			BinaryBitmap binaryBitmap = new BinaryBitmap(
					new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(inputStream))));
			Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap);
			return qrCodeResult.getText();
		}
	}
}
