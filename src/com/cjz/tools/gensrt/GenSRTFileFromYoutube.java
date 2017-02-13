package com.cjz.tools.gensrt;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenSRTFileFromYoutube {

	public static void main(String[] args) throws Exception {

		String relativelyPath = System.getProperty("user.dir");

		GenSRTFileFromYoutube.gen(relativelyPath + "\\demo.txt", relativelyPath + "\\2.0 Unity Tower defense tutorial - Camera movement.srt");
	}

	public static File createFile(String filePath) throws Exception {
		File file = null;
		if (filePath != null && filePath.length() > 0) {
			file = new File(filePath);
			if (file != null) {
				String parent = file.getParent();
				File parentFile = new File(parent);
				if (!parentFile.exists()) {
					if (parentFile.mkdirs()) {
						file.createNewFile();
					} else {
						throw new IOException("create dir err");
					}
				} else {
					if (!file.exists()) {
						file.createNewFile();
					}
				}
			}
		}
		return file;
	}

	private static void gen(String inputPath, String outputPath) throws Exception {
		if (inputPath == null || inputPath.length() <= 0 || outputPath == null || outputPath.length() <= 0) {
			return;
		}
		File inputFile = new File(inputPath);
		if (!inputFile.exists()) {
			return;
		}
		File outputFile = createFile(outputPath);

		FileReader fileReader = new FileReader(inputFile);
		BufferedReader bufferReader = new BufferedReader(fileReader);

		FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
		BufferedWriter bufferWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

		String firstLine = bufferReader.readLine();

		String[] firstRet = cutString(firstLine);
		String nextLine = null;

		int line = 0;
		while (firstLine != null && (nextLine = bufferReader.readLine()) != null) {
			line++;
			bufferWriter.write("" + line);
			bufferWriter.write("\n");
			String[] nextRet = cutString(nextLine);
			bufferWriter.write(creatTime(firstRet[0]));
			bufferWriter.write(" --> ");
			bufferWriter.write(creatTime(nextRet[0]));
			bufferWriter.write("\n");
			bufferWriter.write(firstRet[1]);
			bufferWriter.write("\n");
			bufferWriter.write("\n");
			firstRet = nextRet;
		}

		line++;
		bufferWriter.write("" + line);
		bufferWriter.write("\n");
		bufferWriter.write(creatTime(firstRet[0]));
		bufferWriter.write(" --> ");
		bufferWriter.write(creatTime(firstRet[0], 5));
		bufferWriter.write("\n");
		bufferWriter.write(firstRet[1]);
		bufferWriter.write("\n");
		bufferWriter.write("\n");

		bufferReader.close();
		fileReader.close();
		bufferWriter.close();
		fileOutputStream.close();
	}

	private static String[] cutString(String str) {
		String[] ret = new String[2];
		String reg = "\\d+:\\d\\d";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(str);
		if (m.find()) {
			ret[0] = m.group();
		}
		ret[1] = "";
		String[] content = str.split(reg);
		if (content.length > 1) {
			ret[1] = content[1].trim();
		}
		return ret;
	}

	private static String creatTime(String str) {
		return creatTime(str, 0);
	}

	private static String creatTime(String str, int secPlus) {
		String[] time = str.split(":");
		int hour = 0;
		int min = Integer.parseInt(time[0]);
		int sec = Integer.parseInt(time[1]);
		sec += secPlus;
		min += sec / 60;
		sec = sec % 60;
		hour = min / 60;
		min = min % 60;
		String ss = String.format("%02d:%02d:%02d,000", hour, min, sec);
		return ss;
	}
}

