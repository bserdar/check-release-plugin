/*
   Copyright 2013 Red Hat, Inc. and/or its affiliates.

   This file is part of check-release plugin.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.redhat.plugin.checkrelease;

import java.io.InputStream;
import java.io.ByteArrayInputStream;

import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

import org.apache.maven.plugin.logging.Log;

/**
 * @author Burak Serdar (bserdar@redhat.com)
 */
public class ArchiveComparator implements FileComparator {

    private final ComparatorFactory factory;
    private final Log log;

    public ArchiveComparator(ComparatorFactory f,Log log) {
        this.factory=f;
        this.log=log;
    }

    private boolean ignore(String s) {
        return s.equals("META-INF/MANIFEST.MF")||
            s.startsWith("META-INF/maven/");
    }

    private int zread(InputStream in,byte[] arr) 
        throws Exception {
        int r;
        int off=0;
        log.debug("ZIP read attempt for "+arr.length+" bytes");
        while((r=in.read(arr,off,arr.length-off))>0)
            off+=r;
        log.debug("ZIP read "+off+" bytes");
        return off;
    }

    public String compare(InputStream s1,InputStream s2) {
        Map<String,byte[]> entries1=new HashMap<String,byte[]>();
        Map<String,byte[]> entries2=new HashMap<String,byte[]>();
        log.debug("Start archive comparison");
        try {
            ZipInputStream z1=new ZipInputStream(s1);
            ZipInputStream z2=new ZipInputStream(s2);
            boolean done=false;
            
            do {
                ZipEntry entry1=z1.getNextEntry();
                if(entry1!=null&&!ignore(entry1.getName())) {
                    byte[] arr=new byte[(int)entry1.getSize()];
                    zread(z1,arr);
                    entries1.put(entry1.getName(),arr);
                }
                ZipEntry entry2=z2.getNextEntry();
                if(entry2!=null&&!ignore(entry2.getName())) {
                    byte[] arr=new byte[(int)entry2.getSize()];
                    zread(z2,arr);
                    entries2.put(entry2.getName(),arr);
                }
                // The following loop can delete at most one entry
                for(Map.Entry<String,byte[]> e1:entries1.entrySet()) {
                    byte[] l2=entries2.get(e1.getKey());
                    if(l2!=null) {
                        String result=compare(e1.getKey(),e1.getValue(),l2);
                        if(result!=null) {
                            return result;
                        } else {
                            entries1.remove(e1.getKey());
                            entries2.remove(e1.getKey());
                            break;
                        }
                    }
                }
                // The following loop can delete at most one entry
                for(Map.Entry<String,byte[]> e2:entries2.entrySet()) {
                    byte[] l1=entries1.get(e2.getKey());
                    if(l1!=null) {
                        String result=compare(e2.getKey(),l1,e2.getValue());
                        if(result!=null) {
                            return result;
                        } else {
                            entries1.remove(e2.getKey());
                            entries2.remove(e2.getKey());
                            break;
                        }
                    }
                }
                if(entry1==null&&entry2==null)
                    done=true;
            } while(!done);
            log.debug("End archive comparison");
            if(!entries1.isEmpty()||!entries2.isEmpty())
                return "Archives contain different files";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private String compare(String name,byte[] l1,byte[] l2) throws Exception {
        // Do binary comparison first. If that fails, try semantic comparison
        if(!Arrays.equals(l1,l2)) {
            FileComparator cmp=factory.getComparator(name);
            if(!(cmp instanceof BinaryFileComparator)) {
                String result=cmp.compare(new ByteArrayInputStream(l1),
                                          new ByteArrayInputStream(l2));
                
                if(result!=null) 
                    return name+": Different content";
                else
                    return null;
            } else
                return name+": Different content";
        } else
            return null;
    }
}
