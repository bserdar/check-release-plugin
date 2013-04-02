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

/**
 * @author Burak Serdar (bserdar@redhat.com)
 */
public interface FileComparator {
    /**
     * Returns null if two streams are the same, or difference info if
     * not
     */
    public String compare(InputStream s1,InputStream s2);
}
