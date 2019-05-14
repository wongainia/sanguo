package com.vikings.sanguo.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

@SuppressWarnings("unchecked")
public class ZipUtil {

	final static public void unzip(String srcfile, String destfile) {
		try {
			ZipFile zipFile = new ZipFile(srcfile);
			Enumeration emu = zipFile.entries();
			while (emu.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) emu.nextElement();
				if (entry.isDirectory()) {
					new File(destfile + "/" + entry.getName()).mkdirs();
					continue;
				}
				BufferedInputStream bis = new BufferedInputStream(zipFile
						.getInputStream(entry));
				File file = new File(destfile + "/" + entry.getName());
				File parent = file.getParentFile();
				if (parent != null && (!parent.exists())) {
					parent.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(file);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				int count;
				byte data[] = new byte[4096];
				while ((count = bis.read(data)) != -1) {
					bos.write(data, 0, count);
				}
				bos.flush();
				fos.close();
				bos.close();
				bis.close();
			}
			zipFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	final static public void zip(String srcfile, String destfile) {
		try {
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
					new FileOutputStream(destfile)));
			File src = new File(srcfile);
			zip(out, src, src.getName());
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void zip(ZipOutputStream out, File src, String path)
			throws IOException {
		if (src.isFile()) {
			BufferedInputStream origin = new BufferedInputStream(
					new FileInputStream(src));
			ZipEntry entry = new ZipEntry(path);
			out.putNextEntry(entry);
			int count = -1;
			byte data[] = new byte[4096];
			while ((count = origin.read(data)) != -1) {
				out.write(data, 0, count);
			}
			origin.close();
		} else {
			ZipEntry entry = new ZipEntry(path + "/");
			out.putNextEntry(entry);
			File[] srcFiles = src.listFiles();
			for (int i = 0; i < srcFiles.length; i++) {
				zip(out, srcFiles[i], path + "/" + srcFiles[i].getName());
			}
		}
	}

}
