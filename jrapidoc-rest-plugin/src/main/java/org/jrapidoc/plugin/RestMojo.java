package org.jrapidoc.plugin;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;
import org.codehaus.plexus.component.annotations.Component;
import org.jrapidoc.introspector.RestIntrospector;
import org.jrapidoc.logger.Logger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tomas "sarzwest" Jiricek on 14.3.15.
 *
 */
@Mojo(name = "run", defaultPhase = LifecyclePhase.PROCESS_CLASSES,
        requiresDependencyResolution = ResolutionScope.RUNTIME
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

    /**
     * Class implementing org.jrapidoc.model.type.provider.TypeProvider
     */
    @Parameter(alias = "typeProviderClass", name = "typeProviderClass", property = "typeProviderClass")
    String typeProviderClass;

    /**
     * List of classes implementing org.jrapidoc.model.handler.ModelHandler.
     * Classes are used to work with API model, such operations could be validation, changing some values in model etc.
     */
    @Parameter(alias = "modelHandlers", name = "modelHandlers", property = "modelHandlers")
    List<String> modelHandlers;

    /**
     * Map including custom property names as keys and custom values as values. These key-value pairs will be added to API model.
     */
    @Parameter(alias = "custom", name = "custom", property = "custom")
    Map<String, String> custom;

    /**
     * List of groups. Every group is identified by URI prefix and contains set of services with these prefix.
     */
    @Parameter(alias = "groups", name = "groups", property = "groups", required = true)
    List<ConfigGroup> groups;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        long start = System.currentTimeMillis();
        try {
            Logger.setLogger(getLog());
            addPluginVersionToInfo();
            List<String> classpathElements = project.getCompileClasspathElements();
            List<URL> projectClasspathList = new ArrayList<URL>();
            for (String element : classpathElements) {
                try {
                    Logger.debug("Adding project classpath element {0}", element);
                    projectClasspathList.add(new File(element).toURI().toURL());
                } catch (MalformedURLException e) {
                    getLog().error(e);
                    throw new MojoFailureException(e, element + " is an invalid classpath element", element + " is an invalid classpath element");
                }
            }
            URL[] urls = projectClasspathList.toArray(new URL[projectClasspathList.size()]);
            RestIntrospector restIntrospector = new RestIntrospector();
            restIntrospector.run(urls, groups, typeProviderClass, new File(target, "generated-resources/jrapidoc/jrapidoc.rest.model.json"), modelHandlers, custom);
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

    void addPluginVersionToInfo() {
        if(custom == null){
            custom = new HashMap<String, String>();
        }
        PluginDescriptor pluginDesc = ((PluginDescriptor)getPluginContext().get("pluginDescriptor"));
        custom.put(pluginDesc.getArtifactId(), pluginDesc.getVersion());
    }
}
