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
import java.io.IOException;

import org.apache.maven.plugin.logging.Log;

/**
 * @author Burak Serdar (bserdar@redhat.com)
 */
public class BinaryFileComparator implements FileComparator {

    private final Log log;

    public BinaryFileComparator(Log log) {
        this.log=log;
    }

    public String compare(InputStream s1,InputStream s2) {
        try {
            int off=0;
            int r1,r2;
            log.debug("Starting binary comparison");
            do {
                r1=s1.read();
                r2=s2.read();
                if(r1!=r2) {
                    log.debug("End binary comparison: different");
                    return "Different content at offset "+off;
                }
                off++;
            } while(r1!=-1);
            log.debug("End binary comparison: equal");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
