package com.athena.meerkat.controller.web.provisioning;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;

/**
 * <pre>
 * 
 * </pre>
 * @author Bongjin Kwon
 * @version 1.0
 */
public class UnzipUtility {
    
    private static final int BUFFER_SIZE = 4096;
    /**
     * <pre>
     * 
     * </pre>
     * @param zipFilePath
     * @param destDirectory
     * @param rightHere
     * @throws IOException
     */
    public static void unzip(String zipFilePath, String destDirectory, boolean rightHere) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = null;
        String firstEntryName = null;
        
        try {
        	zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
            ZipEntry entry = zipIn.getNextEntry();
            // iterates over entries in the zip file
            while (entry != null) {
            	
                String filePath = destDirectory + File.separator + entry.getName();
                if (firstEntryName == null) {
            		firstEntryName = entry.getName();
            		
            		zipIn.closeEntry();
                    entry = zipIn.getNextEntry();
                    
                    continue;
				} else {
					filePath = filePath.replaceFirst(firstEntryName, "");
				}
                
                System.out.println(filePath);
                
                if (!entry.isDirectory()) {
                    // if the entry is a file, extracts it
                    extractFile(zipIn, filePath);
                } else {
                    // if the entry is a directory, make the directory
                    File dir = new File(filePath);
                    dir.mkdir();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        } finally {
        	IOUtils.closeQuietly(zipIn);
        }
        
    }
    /**
     * Extracts a zip entry (file entry)
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
    	BufferedOutputStream bos = null;
    	
    	try {
    		bos = new BufferedOutputStream(new FileOutputStream(filePath));
            byte[] bytesIn = new byte[BUFFER_SIZE];
            int read = 0;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
    	} finally {
    		IOUtils.closeQuietly(bos);
    	}
         
    }
}