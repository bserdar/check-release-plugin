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

import java.io.File;

/**
 * @author Burak Serdar (bserdar@redhat.com)
 */
public class ReleaseArtifact {

    /**
     * Artifact ID
     */
    private String artifactId;

    /**
     * Artifact group
     */
    private String group;

    /**
     * Version of the artifact
     */
    private String version;

    /**
     * ArtifactFile
     */
    private File file;

    private boolean pom;


    /**
     * Gets the value of artifactId
     *
     * @return the value of artifactId
     */
    public String getArtifactId() {
        return this.artifactId;
    }

    /**
     * Sets the value of artifactId
     *
     * @param argArtifactId Value to assign to this.artifactId
     */
    public void setArtifactId(String argArtifactId) {
        this.artifactId = argArtifactId;
    }

    /**
     * Gets the value of group
     *
     * @return the value of group
     */
    public String getGroup() {
        return this.group;
    }

    /**
     * Sets the value of group
     *
     * @param argGroup Value to assign to this.group
     */
    public void setGroup(String argGroup) {
        this.group = argGroup;
    }

    /**
     * Gets the value of version
     *
     * @return the value of version
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Sets the value of version
     *
     * @param argVersion Value to assign to this.version
     */
    public void setVersion(String argVersion) {
        this.version = argVersion;
    }

    /**
     * Gets the value of file
     *
     * @return the value of file
     */
    public File getFile() {
        return this.file;
    }

    /**
     * Sets the value of file
     *
     * @param argFile Value to assign to this.file
     */
    public void setFile(File argFile) {
        this.file = argFile;
    }

    public boolean isPom() {
        return pom;
    }

    public void setPom(boolean b) {
        pom=b;
    }
    
    public String toString() {
        return group+":"+artifactId+":"+version+" "+file;
    }

    public String getRepoDir() {
        return group.replace('.','/')+"/"+artifactId+"/"+version;
    }

    public String getRepoFileName() {
        return artifactId+"-"+version+
            (pom?".pom":file.getName().substring(file.getName().lastIndexOf('.')));
    }
}
