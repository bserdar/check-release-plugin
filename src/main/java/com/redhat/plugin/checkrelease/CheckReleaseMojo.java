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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.ResolutionScope;


/**
 * Determines if the build artifact with its current version already
 * exists in the release repository. If the release artifact is
 * modified but the version number is kept the same, fails the build.
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
 *    mvn clean deploy check-release:check-release
 * </pre>
 *
 * Run the check-release goal when you are ready to publish the
 * project to a release repository via the deploy plugin. This ensures
 * that if the artifact is modified, it gets a unique version, i.e. it
 * does not let you overwrite an existing version with a different
 * artifact. 
 *
 * @author Burak Serdar (bserdar@redhat.com)
 */
@Mojo(name = "check-release",
      requiresDependencyResolution = ResolutionScope.COMPILE,
      defaultPhase = LifecyclePhase.PREPARE_PACKAGE,
      threadSafe = true
)
public class CheckReleaseMojo extends AbstractReleaseMojo {    
    
    protected void modifiedButSameVersion(ReleaseArtifact a,
                                         String diff)
        throws MojoExecutionException, MojoFailureException {
        throw new MojoFailureException(a+" is modified, but version number is not changed\n"+diff);
    }

    protected void modifiedButSemanticallyEqual(ReleaseArtifact a)
        throws MojoExecutionException, MojoFailureException {
        getLog().warn(a+" is modified, but semantically equivalent to the released version");
   }

    protected void identical(ReleaseArtifact a)
        throws MojoExecutionException, MojoFailureException {
        getLog().info(a+" is identical to the released version");
    }
}

