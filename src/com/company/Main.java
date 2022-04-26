package com.company;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {

    public static final String loadPath = ".\\load\\";
    public static final String savePath = ".\\sevedGame\\";

    public static ArrayList<String> scanSMG() {
        ArrayList<String> filesToZip = new ArrayList<>();
        File dir = new File(loadPath);
        if (dir.isDirectory()) {
            for (File item : Objects.requireNonNull(dir.listFiles())) {
                if (item.isFile()) {
                    if (item.getName().contains(".smg")) {
                        filesToZip.add(item.getName());
                    }

                }
            }
        }
        return filesToZip;
    }

    public static GameProgress openProgress(String file) {
        try (FileInputStream fis = new FileInputStream(loadPath + file);
             ObjectInputStream gameProgress = new ObjectInputStream(fis)) {
            return (GameProgress) gameProgress.readObject();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static boolean openZip(String zipPath, String UnZipPath) {

        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(zipPath))) {
            ZipEntry entry;
            String fileName;
            while ((entry = zin.getNextEntry()) != null) {
                fileName = entry.getName();
                FileOutputStream fout = new FileOutputStream(UnZipPath + fileName);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public static void main(String[] args) {
        openZip(savePath + "zipMSG.zip", loadPath);
        System.out.println(scanSMG());
        System.out.println("введите имя файла, который необходимо загрузить");
        Scanner scanner = new Scanner(System.in);
        String nameFile = scanner.nextLine();

        GameProgress gameProgress = openProgress(nameFile);

        if (gameProgress != null) {
            System.out.println(gameProgress);
        }

    }
}
