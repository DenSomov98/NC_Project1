package server.zip;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ToZip {
    private static final String PATH = "C:\\Users\\aaa\\IdeaProjects\\NC_Project2\\";
    public static void main(String[] args) {
        try {
            String inFile = PATH + "data.xml";
            File file = new File(inFile);
            byte[] data = getBytesFromFile(file);
            createZip("data.xml", new String(data, StandardCharsets.UTF_8), "");
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public static void createZip(String fileName, String content, String encoding) {
        byte[] buf = new byte[1024];
        try {
            String outFilename = PATH + "outfile.zip";
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
            ByteArrayInputStream sourceStream;
            if (encoding != null && encoding.length() != 0)
                sourceStream = new ByteArrayInputStream(content.getBytes(encoding));
            else
                sourceStream = new ByteArrayInputStream(content.getBytes());
            out.putNextEntry(new ZipEntry(fileName));
            int len;
            while ((len = sourceStream.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            out.closeEntry();
            out.close();
        } catch (IOException e) {
            System.out.print(e);
        }

    }


    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        long length = file.length();
        if (length > Integer.MAX_VALUE) {
        }
        byte[] bytes = new byte[(int)length];
        int offset = 0;
        int numRead;
        while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }
        is.close();
        return bytes;
    }
}