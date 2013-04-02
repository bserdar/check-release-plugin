check-release plugin makes sure that if the build artifact of a
project is semantically modified, it gets a new version number. This
ensures that any project depending on that artifact can have reliable
repeatable builds. The plugin assumes that once the project is ready
to "release" its build artifacts, it uses Maven "deploy" goal to
deploy them to a release repository. At this phase, check-release
plugin is used to make sure that:
  - if the artifact on release repository is to be overwritten, it is
  semantically identical to the new version, and
  - if the artifact is different, it is not overwritten.

Semantically identical means that changes that do not affect the
operation of the artifact are allowed, such as comments in XML files. 

For instance, suppose we're working on a Java project
my-project:my-project version 2.0. Once the development is ready, we
deploy it to a Maven repo on http://my.host/release-repo.

<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>my-project</groupId>
  <artifactId>my-project</artifactId>
  <packaging>jar</packaging>
  <version>2.0</version>
  <distributionManagement>
    <repository>
      <id>ssh-repository</id>
      <url>http://my.host/release-repo</url>
    </repository>
  </distributionManagement>
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>check-release-maven-plugin</artifactId>
        <version>1.1.0</version>
        <configuration>
           <repoUrl>http://my.host/release-repo</repoUrl>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>


Running 

  mvn clean deploy

will build and deploy the artifact to http://my.host/release-repo. If changes are made to the project, and then mvn deploy is run again, the existing version (2.0) will be overwritten. However, if you deploy using

  mvn clean deploy check-release:check-release

version 2.0 will be overwritten only if the new version is
semantically equivalent to the existing version 2.0. If there are
changes that affect the operation of the artifact, check-release
plugin will fail the build.


Using 

  mvn clean install check-release:report 

will write WARNING messages instead of failing the build. This can be
run locally before publishing project artifacts to make sure
check-release:check-release will succeed.
