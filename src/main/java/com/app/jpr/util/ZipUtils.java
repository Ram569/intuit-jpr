package com.app.jpr.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtils {
    public static void copyFilesFromZip(String zipFilePath, Path directory) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File destFile = new File(directory.toFile(), zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    destFile.mkdirs();
                } else {
                    destFile.getParentFile().mkdirs();
                    Files.copy(zis, destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
                zipEntry = zis.getNextEntry();
            }
        }
    }
}
