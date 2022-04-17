package com.company;

import java.io.*;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) {

        GameProgress one = new GameProgress(98, 9, 2, 10.5);
        GameProgress two = new GameProgress(54, 15, 6, 35.6);
        GameProgress three = new GameProgress(68, 11, 9, 55);

        saveGame("D://Games/savegames/saveOne.dat", one);
        saveGame("D://Games/savegames/saveTwo.dat", two);
        saveGame("D://Games/savegames/saveThree.dat", three);

        String[] stringsFile = new String[]{
                "D://Games/savegames/saveOne.dat",
                "D://Games/savegames/saveTwo.dat",
                "D://Games/savegames/saveThree.dat"
        };

        zipFiles("D://Games/savegames/saveGameZip.zip", stringsFile);

        for (String path : stringsFile) {
            File fileDelete = new File(path);
            fileDelete.delete();
        }

        openZip("D://Games/savegames/saveGameZip.zip");

        GameProgress newOne = null;
        GameProgress newTwo = null;
        GameProgress newThree = null;

        openProgress("D://Games/savegames/saveOne.dat", newOne);
        openProgress("D://Games/savegames/saveTwo.dat", newTwo);
        openProgress("D://Games/savegames/saveThree.dat", newThree);

    }

    public static void saveGame(String string, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(string);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void zipFiles(String string, String[] strings) {
        try (ZipOutputStream saveGameZip = new ZipOutputStream(new FileOutputStream(string))) {
            for (String path : strings) {
                try (FileInputStream any = new FileInputStream(path)) {
                    ZipEntry entry = new ZipEntry(path);
                    saveGameZip.putNextEntry(entry);
                    byte[] buffer = new byte[any.available()];
                    any.read(buffer);
                    saveGameZip.write(buffer);
                    saveGameZip.closeEntry();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openZip(String zipPath) {
        try (ZipInputStream unpack = new ZipInputStream(new FileInputStream(zipPath))) {
            ZipEntry afreshEntry;
            String name;
            while ((afreshEntry = unpack.getNextEntry()) != null) {
                name = afreshEntry.getName();
                FileOutputStream fout = new FileOutputStream(name);
                for (int c = unpack.read(); c != -1; c = unpack.read()) {
                    fout.write(c);
                }
                fout.flush();
                unpack.closeEntry();
                fout.close();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void openProgress(String string, GameProgress gameProgress) {
        try (FileInputStream fis = new FileInputStream(string);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println(gameProgress);
    }
}



