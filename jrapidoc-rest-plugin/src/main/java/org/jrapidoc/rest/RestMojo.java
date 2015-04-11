package org.jrapidoc.rest;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;
import org.codehaus.plexus.component.annotations.Component;
import org.jrapidoc.logger.Logger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by papa on 14.3.15.
 *
 * @phase compile
 */
@Mojo(name = "run", defaultPhase = LifecyclePhase.COMPILE/*,
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME*//*,
        configurator = "include-project-dependencies"*/
)
@Component(role = RestMojo.class)
public class RestMojo extends AbstractMojo {

    @Parameter(defaultValue = "${session}", readonly = true)
    MavenSession session;

    @Parameter(defaultValue = "${project}", readonly = true)
    MavenProject project;

    @Parameter(defaultValue = "${settings}", readonly = true)
    Settings settings;

    @Parameter(defaultValue = "${project.basedir}", readonly = true)
    File basedir;

    @Parameter(defaultValue = "${project.build.directory}", readonly = true)
    File target;

    @Parameter(name = "baseUrl")
    String baseUrl;

    @Parameter(name = "includes")
    List<String> includes;

    @Parameter(name = "excludes")
    List<String> excludes;

    @Parameter(name = "typeProviderClass")
    String typeProviderClass;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        long start = System.currentTimeMillis();
        try {
            Logger.setLogger(getLog());
            List<String> classpathElements = project.getCompileClasspathElements();
            List<URL> projectClasspathList = new ArrayList<URL>();
            for (String element : classpathElements) {
                try {
                    Logger.info("Adding project classpath element {0}", element);
                    projectClasspathList.add(new File(element).toURI().toURL());
                } catch (MalformedURLException e) {
                    getLog().error(e);
                    throw new MojoFailureException(e, element + " is an invalid classpath element", element + " is an invalid classpath element");
                }
            }
            URL[] urls = projectClasspathList.toArray(new URL[projectClasspathList.size()]);
            RestIntrospector restIntrospector = new RestIntrospector();
            restIntrospector.introspect(urls, includes, excludes, baseUrl, typeProviderClass, new File(target, "generated-resources/jrapidoc/jrapidoc.rest.model.json"));
        } catch (DependencyResolutionRequiredException e) {
            getLog().error(e);
            throw new MojoExecutionException(e.getMessage(), e);
        } catch (Exception e) {
              Logger.error(e, e.getMessage());
            e.printStackTrace();
        } finally {
            Logger.info("Finished in " + ((System.currentTimeMillis() - start) / 1000) + " seconds");
        }
    }
}
