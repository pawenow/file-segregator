package com.paweln.filesegregatesystem;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class CounterHelper {
    private static final String pathToCounter = "resources/pathToTest/count.txt";

    private static final String DEV_PATH = "resources/pathToTest/dev";
    private static final  String TEST_PATH = "resources/pathToTest/test";

    public static synchronized void counter(String path){
        try{

            Counter counter = getCurrentAmountOfFiles();
            BufferedWriter writer = new BufferedWriter(new FileWriter(pathToCounter));
            writer.write(convertObjToJSON(counter));

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String convertObjToJSON(Counter counter) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(counter);

    }

    private static Counter convertJSONToObj() throws IOException {
        return new ObjectMapper().readValue(new File(pathToCounter), Counter.class);
    }
    private static Counter getCurrentAmountOfFiles() throws IOException {
        Counter counter = new Counter();
        counter.setDevFilesAmount(Long.valueOf(Files.list(Paths.get(DEV_PATH)).collect(Collectors.toList()).size()));
        counter.setTestFilesAmount(Long.valueOf(Files.list(Paths.get(TEST_PATH)).collect(Collectors.toList()).size()));
        return counter;
    }
    public static void  updateAmountFile() throws IOException {
        Counter counter = getCurrentAmountOfFiles();
        BufferedWriter writer = new BufferedWriter(new FileWriter(pathToCounter));
        writer.write(convertObjToJSON(counter));

        writer.close();
    }
}
