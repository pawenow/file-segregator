package com.paweln.filesegregatesystem;

import org.apache.tika.Tika;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class MoveMechanism implements Runnable {
    private static final String JAR_TYPE = "application/java-archive";
    private static final String XML_TYPE = "application/xml";
    WatchEvent<?> event;
    Path PATH_HOME;
    String TEST_PATH;
    String DEV_PATH;

    public MoveMechanism(WatchEvent<?> event, Path PATH_HOME, String TEST_PATH, String DEV_PATH) {
        this.event = event;
        this.PATH_HOME = PATH_HOME;
        this.TEST_PATH = TEST_PATH;
        this.DEV_PATH = DEV_PATH;
    }

    @Override
    public void run() {
        Tika tika = new Tika();
        WatchEvent<Path> ev = (WatchEvent<Path>)event;
        Path filename = ev.context();
        Path resolve = PATH_HOME.resolve(filename);
        System.out.println(Thread.currentThread().getId()+" " + Thread.currentThread().getName());
        try{
            BasicFileAttributes attr = Files.readAttributes(resolve, BasicFileAttributes.class);
            String fileType = tika.detect(resolve);
            if(JAR_TYPE.equals(fileType) && attr.creationTime().toMillis()%2==1
                    || XML_TYPE.equals(fileType) ){
                moveFileAndUpdateCounter(filename, resolve, TEST_PATH);

            }else if(JAR_TYPE.equals(fileType)){
                moveFileAndUpdateCounter(filename, resolve, DEV_PATH);
            }

        }catch (IOException e) {
            System.out.println("Connot finish operation");
            e.printStackTrace();
        }
        System.out.println("Finished : " + Thread.currentThread().getId()+" " + Thread.currentThread().getName());

    }

    private void moveFileAndUpdateCounter(Path filename, Path resolve, String path) throws IOException {
        Files.move(resolve, Paths.get(path + "/" + filename));
        CounterHelper.counter(path);
    }


}

