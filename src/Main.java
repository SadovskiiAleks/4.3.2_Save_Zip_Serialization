import java.io.*;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        GameProgress save1 = new GameProgress
                (100, 0, 0, 1.15);
        GameProgress save2 = new GameProgress
                (78, 2, 6, 2.95);
        GameProgress save3 = new GameProgress
                (55, 10, 88, 5.99);
        saveGame("A:/Games/temp/save.dat",save1);
        saveGame("A:/Games/temp/save2.dat",save2);
        saveGame("A:/Games/temp/save3.dat",save3);
        zipFiles("A:/Games/temp/save.zip","A:/Games/temp");
        deleteFile("A:/Games/temp");
        openZip("A:/Games/temp/save.zip","A:/Games/temp/test");
        System.out.println(openProgress("A:/Games/temp/test/save.dat").toString());
    }

    public static void saveGame(String way, GameProgress save) {
        try (FileOutputStream fin = new FileOutputStream(way)) {
            ObjectOutputStream biteToFile = new ObjectOutputStream(fin);
            biteToFile.writeObject(save);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void zipFiles(String wayZip, String wayOfDat) {
        try (ZipOutputStream wayZipIn = new ZipOutputStream(
                new FileOutputStream(wayZip))) {
            //пробежаться по файлам
            File dir = new File(wayOfDat);
                if (dir.isDirectory()){
                    for (File item : dir.listFiles()){
                        if (item.isFile() && item.getName().indexOf(".dat") != -1) {
                            FileInputStream file = new FileInputStream(item);
                            ZipEntry entry = new ZipEntry(item.getName());
                            wayZipIn.putNextEntry(entry);
                            byte[] buffer = new byte[file.available()];
                            file.read(buffer);
                            wayZipIn.write(buffer);
                            file.close();
                        }
                    }
                }
            wayZipIn.closeEntry();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void deleteFile(String wayToDel) {
        // Пробежаться по файлам и удалиь их
        File dir = new File(wayToDel);
        try {
            if (dir.isDirectory()) {
                for (File item : dir.listFiles()) {
                    if (item.isFile() && item.getName().indexOf(".dat") != -1) {
                        File newDir2 = new File(item.getParent().replace('\\', '/') + "/" + item.getName());
                        newDir2.delete();
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }


    }

    public static void openZip(String wayToFile, String wayToOpenZip){
        try (ZipInputStream zin = new ZipInputStream
                (new FileInputStream(wayToFile))){
            ZipEntry entry;
            String name;

            while ((entry = zin.getNextEntry()) != null){
                name = entry.getName();

                FileOutputStream fout = new FileOutputStream(wayToOpenZip + "/" + name);
                for (int c = zin.read(); c != -1; c = zin.read()){
                    fout.write(c);
                }

                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static GameProgress openProgress(String wayOfFile) {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(wayOfFile);
            ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return gameProgress;
    }

}