/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Pubudu
 */
public class FileRepo {

//    public static void main(java.lang.String args[]) {
//
//        FileRepo f = new FileRepo();
//        String[] fl = f.getFilesFromRepo();
//
//        for (int i = 0; i < fl.length; i++) {
//
//            //  System.out.println(fl[i]);
//        }
//
//        System.out.println(f.isFileInRepo("Adventures"));
//        System.out.println(f.isFileInRepo("lo"));
//        System.out.println(f.isFileInRepo("of"));
//        System.out.println(f.isFileInRepo("Rings"));
//        System.out.println(f.isFileInRepo("Lord of the rings"));
//        // System.out.println("load of the rings");
//    }
    public ArrayList<String> files;

    public String[] fileArray = new String[]{"Adventures of Tintin", "Jack and Jill", "Glee", "The Vampire Diarie",
        "King Arthur", "Windows XP", "Harry Potter", "Kung Fu Panda", "Lady Gaga", "Twilight", "Windows 8",
        "Mission Impossible", "Turn Up The Music", "Super Mario", "American Pickers", "Microsoft Office 2010",
        "Happy Feet", "Modern Family", "American Idol", "Hacking for Dummies", "Lord of the rings", "Rings"};

    public FileRepo() {
        files = new ArrayList<String>();
        addFiles();
    }

    // Add files to Repo
    public void addFiles() {

        for (String fileArray1 : fileArray) {
            files.add(fileArray1);
        }
    }

    // To add a new file to the repo
    public void addFileToRepo(String fileName) {
        files.add(fileName);
    }

    // Returns files between 3 to 5 that are randomly generated
    public ArrayList<String> getFilesFromRepo() {
        Random randomGenerator = new Random();
        int low = 3;
        int high = 6;
        int noOfFiles = randomGenerator.nextInt(high - low) + low;
        ArrayList<String> returnFiles = new ArrayList<String>();

        ArrayList<Integer> fileNodes = new ArrayList<Integer>();

        while (noOfFiles > 0) {

            int fileId = randomGenerator.nextInt(files.size());
            boolean hasFile = false;

            for (int i = 0; i < fileNodes.size(); i++) {

                if (fileNodes.get(i) == fileId) {
                    hasFile = true;
                    break;
                }
            }

            if (!hasFile) {
                fileNodes.add(fileId);
                returnFiles.add(files.get(fileId));
                noOfFiles--;

            }

        }

        return returnFiles;

    }

//    public ArrayList<String> isFileInRepo(String fileName) {
//
//        // boolean hasFile = false;
//        ArrayList<String> fileList = new ArrayList<>();
//
//        for (int i = 0; i < files.size(); i++) {
//
//            fileName = fileName.toLowerCase();
//            String fileQ = files.get(i).toLowerCase();
//
//            if (fileQ.contains(fileName)) {
//                fileList.add(files.get(i));
//
//            }
//
//        }
//
//        for (int i = 0; i < fileList.size(); i++) {
//
//            String[] tokens = fileName.split(" ");  //m of
//            String[] results = fileList.get(i).split(" ");  //mic office
//            boolean valid = false;
//
//            for (int j = 0; j < tokens.length; j++) {
//
//                for (int k = 0; k < results.length; k++) {
//
//                    if (results[k].toLowerCase().equals(tokens[j].toLowerCase())) {
//                        valid = true;
//                        break;
//                    } else {
//                        valid = false;
//                    }
//                }
//
//            }
//
//            if (!valid) {
//                fileList.remove(i);
//            }
//
//        }
//
//        return fileList;
//
//    }

}
