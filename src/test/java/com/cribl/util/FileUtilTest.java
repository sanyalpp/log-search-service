package com.cribl.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class FileUtilTest {

    @Test(expected = NullPointerException.class)
    public void testCopyFileToLocation_fileIsNull() {
        FileUtil.copyFileToLocation(null, "location");
    }

    /*
    More tests ..

     */
}
