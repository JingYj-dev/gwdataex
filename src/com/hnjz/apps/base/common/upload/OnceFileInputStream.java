package com.hnjz.apps.base.common.upload;

import java.io.*;

public class OnceFileInputStream extends FileInputStream{

	public OnceFileInputStream(File file) throws FileNotFoundException {
		super(file);
	}
	
	public OnceFileInputStream(FileDescriptor fdObj) {
		super(fdObj);
	}

	public OnceFileInputStream(String name) throws FileNotFoundException {
		super(name);
	}

	@Override
	public int read(byte[] b) throws IOException {
		int result =  super.read(b);
		if(result == -1){
			close();
		}
		return result;
	}
	
	@Override
	public int read() throws IOException {
		int result =  super.read();
		if(result == -1){
			close();
		}
		return result;
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int result =  super.read(b, off, len);
		if(result == -1){
			close();
		}
		return result;
	}

}
