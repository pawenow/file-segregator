package com.paweln.filesegregatesystem;

import org.apache.tika.Tika;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class App {
    //file type


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

        while (true) {

            WatchKey key;
            if (!((key = watchService.take()) != null)) break;

            for (WatchEvent<?> event : key.pollEvents()) {
                if(event.kind().equals(StandardWatchEventKinds.ENTRY_CREATE)){
                    MoveMechanism moveMechanism = new MoveMechanism(event,pathHome,TEST_PATH,DEV_PATH);
                    Thread t = new Thread(moveMechanism);
                    t.start();

                }else{
                    CounterHelper.updateAmountFile();
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
