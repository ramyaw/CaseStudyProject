package com.mount;

import org.hamcrest.core.Is;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class MountPointTest {
    
    private static final Logger log = LoggerFactory.getLogger(MountPointTest.class);
    
    MountPoint mountPoint = new MountPoint();
    
    
    @Test
    public void getDiskUsage() {
        try {
            JSONObject data = mountPoint.getFileSystemDiskUsage();
            log.info("\n Data :" + data.toJSONString());
            
            Assert.assertThat(data.size(), Is.is(1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    @Test
    public void getDiskUsageByLocation() {
        File folder = new File("src/test/java/com/mount");
        JSONObject data = mountPoint.folderSize(folder);
        log.info("\n Data :" + data.toJSONString());
        
        Assert.assertThat(data.size(), Is.is(1));
    }
}

