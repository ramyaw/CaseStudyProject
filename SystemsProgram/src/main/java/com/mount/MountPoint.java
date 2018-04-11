package com.mount;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;


public class MountPoint {
    
    public JSONObject getFileSystemDiskUsage() throws IOException {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj = new JSONObject();
    
        for (FileStore store : FileSystems.getDefault().getFileStores()) {
            JSONObject jsonObject = new JSONObject();
            long total = store.getTotalSpace() / 1024;
            long avail = store.getUsableSpace() / 1024;
            long used = (store.getTotalSpace() - store.getUnallocatedSpace()) / 1024;
            //System.out.format("%-20s %12d %12d %12d%n", store, total, used, avail);
    
            jsonObject.put(store+"", used+"");
            jsonArray.add(jsonObject);
        }
    
        jsonObj.put("files", jsonArray);
        return jsonObj;
    }
    
    public JSONObject folderSize(File directory) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj = new JSONObject();
    
        long size = 0;
        for (File file : directory.listFiles()) {
            JSONObject jsonObject = new JSONObject();
            if (file.isFile()) {
                size = file.getUsableSpace() / 1024;
            }
            //System.out.printf("\n" + file + "---" + size);
    
            jsonObject.put(file, size);
            jsonArray.add(jsonObject);
        }
        jsonObj.put("files", jsonArray);
        return jsonObj;
    }
    
}
