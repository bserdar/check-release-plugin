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

import org.apache.maven.plugin.logging.Log;

/**
 * @author Burak Serdar (bserdar@redhat.com)
 */
public class ComparatorFactory {
    
    private final Log log;

    public ComparatorFactory(Log log) {
        this.log=log;
    }

    public FileComparator getComparator(String filename) {
        String x=filename.toLowerCase();
        if(x.endsWith(".pom")||
           x.endsWith(".xml"))
            return new XMLFileComparator(log);
        else if(x.endsWith(".zip")||
                x.endsWith(".ear")||
                x.endsWith(".war")||
                x.endsWith(".jar")||
                x.endsWith(".sar"))
            return new ArchiveComparator(this,log);
        else
            return new BinaryFileComparator(log);
    }
}
