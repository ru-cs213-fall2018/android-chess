package com.qwezey.androidchess;

import android.content.Context;

import com.qwezey.androidchess.logic.board.Coordinate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Storage {

    private static final String recordingsName = "recordings";

    Context context;

    public Storage(Context context) {
        this.context = context;
    }

    public void recordGame(String name, List<Coordinate> record) {
        try {
            File root = context.getFilesDir();
            File recordingFolder = new File(root, recordingsName);
            if (!recordingFolder.isDirectory()) recordingFolder.mkdir();
            File gameFile = new File(recordingFolder, name);
            OutputStream os = new FileOutputStream(gameFile);
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(record);
            oos.close();
            os.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean doesRecordExist(String name) {
        File root = context.getFilesDir();
        File recordingFolder = new File(root, recordingsName);
        if (!recordingFolder.isDirectory()) return false;
        File gameFile = new File(recordingFolder, name);
        return gameFile.isFile();
    }

    public List<String> getAllRecordNames() {
        File root = context.getFilesDir();
        File recordingFolder = new File(root, recordingsName);
        if (!recordingFolder.isDirectory()) return new ArrayList<>();
        File[] files = recordingFolder.listFiles();
        return Arrays.stream(files)
                .map(file -> file.getName())
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Coordinate> getGameRecord(String name) {
        try {
            File root = context.getFilesDir();
            File recordingFolder = new File(root, recordingsName);
            if (!recordingFolder.isDirectory()) recordingFolder.mkdir();
            File gameFile = new File(recordingFolder, name);
            InputStream is = new FileInputStream(gameFile);
            ObjectInputStream ois = new ObjectInputStream(is);
            List<Coordinate> record = (List<Coordinate>) ois.readObject();
            ois.close();
            is.close();
            return record;
        } catch (Exception e) {
            throw new UncheckedIOException((IOException) e);
        }
    }
}
