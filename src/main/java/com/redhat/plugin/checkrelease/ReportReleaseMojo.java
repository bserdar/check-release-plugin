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

import java.util.ArrayList;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.ResolutionScope;

/**
 * Determines if the build artifact with its current version already
 * exists in the release repository. If the release artifact is
 * modified but the version number is kept the same, prints a warning.
 *
 * Configuration Items:
 * <ul>
 * <li>repoUrl: (required) URL of the deployment repository</li>
 * <li>ignore: (optional) If set to true, check-release artifact 
 * skips the current project.</li>
 * </ul>
 *
 * Usage:
 * <pre>
 *    mvn clean install check-release:report
 * </pre>
 *
 * @author Burak Serdar (bserdar@redhat.com)
 */

@Mojo(name = "report",
      requiresDependencyResolution = ResolutionScope.COMPILE,
      defaultPhase = LifecyclePhase.PREPARE_PACKAGE,
      threadSafe = true
)
public class ReportReleaseMojo extends AbstractReleaseMojo {  

    private static ArrayList<String> messages=new ArrayList<String>();
    
    protected void modifiedButSameVersion(ReleaseArtifact a,
                                          String diff)
        throws MojoExecutionException, MojoFailureException {
        messages.add(a+" is modified, but version number is not changed\n"+diff);
        getLog().warn(a+" is modified, but version number is not changed\n"+diff);
    }

    protected void end() {
    }
    
    protected void modifiedButSemanticallyEqual(ReleaseArtifact a)
        throws MojoExecutionException, MojoFailureException {}

    protected void identical(ReleaseArtifact a)
        throws MojoExecutionException, MojoFailureException {}

}

