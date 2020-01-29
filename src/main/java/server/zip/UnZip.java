package server.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnZip {
    public static void main(String[] args) {
        final String OUTPUT_FOLDER = "C:\\Users\\aaa\\IdeaProjects\\NC_Project2";
        String FILE_PATH = "C:\\Users\\aaa\\IdeaProjects\\NC_Project2\\outfile.zip";

        // Создать папку Output если она не существует.
        File folder = new File(OUTPUT_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        // Создать buffer (Буфер).
        byte[] buffer = new byte[1024];

        ZipInputStream zipIs = null;
        try {
            // Создать объект ZipInputStream для чтения файла из 1 пути (path).
            zipIs = new ZipInputStream(new FileInputStream(FILE_PATH));

            ZipEntry entry = null;
            // Просмотр каждого Entry (С верзу до низу, до конца)
            while ((entry = zipIs.getNextEntry()) != null) {
                String entryName = entry.getName();
                String outFileName = OUTPUT_FOLDER + File.separator + entryName;
                System.out.println("Unzip: " + outFileName);

                if (entry.isDirectory()) {
                    // Создать папки.
                    new File(outFileName).mkdirs();
                } else {
                    // Создать Stream для записи данных в файл.
                    FileOutputStream fos = new FileOutputStream(outFileName);

                    int len;
                    // Чтение данных на текущем Entry.
                    while ((len = zipIs.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }

                    fos.close();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                zipIs.close();
            } catch (Exception e) {
            }
        }
    }



}
