package com.redhat.plugin.checkrelease;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.parsers.*;
import org.w3c.dom.*;

import java.io.*;
import java.util.*;

import  org.apache.maven.plugin.logging.SystemStreamLog;

public class ArchiveComparatorTest {


    @Test
    public void archiveTest() throws Exception {
        //Use this test to compare two archives. you need to put the archives under test resources dir.

//         SystemStreamLog log=new SystemStreamLog();
//         ComparatorFactory factory=new ComparatorFactory(log);
//         FileComparator comparator=factory.getComparator(".war");
        
//         InputStream s1=ArchiveComparatorTest.class.getResourceAsStream("/1.war");
//         InputStream s2=ArchiveComparatorTest.class.getResourceAsStream("/2.war");
//         System.out.println(comparator.compare(s1,s2));
    }

}
