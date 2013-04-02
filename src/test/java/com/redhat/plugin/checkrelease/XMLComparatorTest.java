package com.redhat.plugin.checkrelease;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.parsers.*;
import org.w3c.dom.*;

import java.io.*;
import java.util.*;

public class XMLComparatorTest {

    private String testit(String s1,String s2) throws Exception {
        return testit(new StringBufferInputStream(s1),
                      new StringBufferInputStream(s2));
    }

    private String testit(InputStream s1,InputStream s2) throws Exception {
        XMLFileComparator cmp=new XMLFileComparator(null);
        return cmp.compare(s1,s2);
    }

    @Test
    public void iteratortest() throws Exception {
        final String s1="<el1><el2 attr1=\"1\"></el2></el1>";
        DocumentBuilder b=DocumentBuilderFactory.
            newInstance().newDocumentBuilder();
        Document doc=b.parse(new StringBufferInputStream(s1));
        DepthFirstIterator itr=new DepthFirstIterator(doc.getDocumentElement());
        Assert.assertEquals("el1",itr.next().getNodeName());
        Assert.assertEquals("el2",itr.next().getNodeName());
        Assert.assertNull(itr.next());
    }

    @Test
    public void testSame() throws Exception {
        final String s1="<el1><el2 attr1=\"1\"></el2></el1>";
        Assert.assertNull(testit(s1,s1));
    }

    @Test
    public void testWsSame() throws Exception {
        final String s1="<el1><el2 attr1=\"1\"></el2></el1>";
        final String s2="<el1>  <el2 attr1=\"1\">  </el2>  </el1>";
        Assert.assertNull(testit(s1,s2));
    }


    @Test
    public void testWsCommSame() throws Exception {
        final String s1="<el1><el2 attr1=\"1\"><!--some comments--></el2></el1>";
        final String s2="<el1>  <el2 attr1=\"1\">  </el2>  </el1>";
        Assert.assertNull(testit(s1,s2));
    }

    @Test
    public void testDiff() throws Exception {
        final String s1="<el1><el2 attr1=\"1\">x<!--some comments--></el2></el1>";
        final String s2="<el1>  <el2 attr1=\"1\">  </el2>  </el1>";
        Assert.assertNotNull(testit(s1,s2));
        System.out.println(testit(s1,s2));
    }
}
