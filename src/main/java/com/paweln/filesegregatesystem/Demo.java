package com.paweln.filesegregatesystem;

import java.io.IOException;
import java.nio.file.*;

public class Demo {
    private static final String HOME_PATH = "resources/pathToTest/home";
    private static final String DEV_PATH = "resources/pathToTest/dev";
    private static final  String TEST_PATH = "resources/pathToTest/test";

    public static void main(String[] args) {

        try {
            App app = configureApp();
            app.run();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    /**
     * @return
     * @throws IOException
     */
    private static App configureApp() throws IOException {
        createPaths(HOME_PATH,DEV_PATH,TEST_PATH);
        App application = new App(TEST_PATH, DEV_PATH, HOME_PATH);
        return application;
    }

    private static void createPaths(String homePath, String devPath, String testPath) throws IOException {
        Path pathHome = Paths.get(homePath);
        Path pathDev = Paths.get(devPath);
        Path pathTest = Paths.get(testPath);

        Files.createDirectories(pathHome);
        Files.createDirectories(pathDev);
        Files.createDirectories(pathTest);

    }






}
