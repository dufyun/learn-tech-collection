package org.dufy.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

public class GZFileReader {
	private final BufferedReader reader;
	public void close(){
		if(null != this.reader){
			try {
				this.reader.close();
			} catch (IOException e) {
			}
		}
	}
	
	public GZFileReader(File file) throws FileNotFoundException, IOException{
		this.reader = new BufferedReader(
				new InputStreamReader(
				new GZIPInputStream(
				new FileInputStream(file))));
	}
	
	public String readLine() throws IOException{
		if(null == this.reader)
			return null;

		return this.reader.readLine();
	}
}
