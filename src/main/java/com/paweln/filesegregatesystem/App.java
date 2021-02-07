package com.paweln.filesegregatesystem;

import org.apache.tika.Tika;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Logger;

public class App {
    //file type
    private static final String JAR_TYPE = "application/java-archive";
    private static final String XML_TYPE = "application/xml";

    private final String TEST_PATH;
    private final String DEV_PATH;
    private final String HOME_PATH;

    public App(String test_path, String dev_path, String home_path) {
        TEST_PATH = test_path;
        DEV_PATH = dev_path;
        HOME_PATH = home_path;
    }


    /**
     * @param watchService
     * @param pathHome
     * @throws InterruptedException
     * @throws IOException
     */
    private void segregateFile(WatchService watchService, Path pathHome) throws InterruptedException, IOException {
        Tika tika = new Tika();
        while (true) {

            WatchKey key;
            if (!((key = watchService.take()) != null)) break;

            for (WatchEvent<?> event : key.pollEvents()) {
                if(event.kind().equals(StandardWatchEventKinds.ENTRY_CREATE)){
                    WatchEvent<Path> ev = (WatchEvent<Path>)event;
                    Path filename = ev.context();
                    Path resolve = pathHome.resolve(filename);

                    BasicFileAttributes attr = Files.readAttributes(resolve, BasicFileAttributes.class);
                    String fileType = tika.detect(resolve);
                    if(JAR_TYPE.equals(fileType) && attr.creationTime().toMillis()%2==1
                            || XML_TYPE.equals(fileType) ){
                        Files.move(resolve,Paths.get(TEST_PATH +"/" + filename));
                    }else if(JAR_TYPE.equals(fileType)){
                        Files.move(resolve,Paths.get(DEV_PATH + "/"+ filename));
                    }

                }
            }
            key.reset();
        }
    }

    private void subscribePath(WatchService watchService, Path pathHome) throws IOException {

        pathHome.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY);

    }

    /**
     * @return
     * @throws IOException
     */
    private WatchService initializeWatchService() throws IOException {
        WatchService watchService = FileSystems.getDefault().newWatchService();

        return watchService;
    }


    public void run() throws IOException, InterruptedException {
        Path pathHome = Paths.get(HOME_PATH);
        WatchService watchService = initializeWatchService();
        subscribePath(watchService, pathHome);
        segregateFile(watchService,pathHome);
    }


}
