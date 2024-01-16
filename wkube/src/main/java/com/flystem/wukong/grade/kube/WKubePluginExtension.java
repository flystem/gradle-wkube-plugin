package com.flystem.wukong.grade.kube;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Nested;

import java.util.HashMap;
import java.util.Map;

abstract public class WKubePluginExtension {
    /**
     * kubeconfig in $HOME/.kube directory
     * @return
     */
    abstract public Property<String> getKubeconfig();

    /**
     * template file name，default:src/main/kube/deployment.yaml
     * @return
     */
    abstract public RegularFileProperty getTemplateFile();

    /**
     * generated deployment file, default:build/kube/deployment.yaml
     * @return
     */
    abstract public RegularFileProperty getDeploymentFile();

    @Nested
    abstract public MapProperty<String, Object> getData();


    /**
     * config data pass in deployment.yaml.eg.<br/>
     * data{ <br/>
     *     key1="value" <br/>
     *     key2=123 <br/>
     *     namespace=getProject().name <br />
     * } <br/>
     * There are version,name,displayName those come from project <br />
     * In data config block,you should use getProject() instead of project
     * @param action
     */
    public void data(Action<MapProperty<String, Object>> action) {
        action.execute(getData());
    }

    public Map<String, Object> getAllData(Project project) {
        Map<String, Object> extra = getData().getOrElse(Map.of());
        Map<String, Object> data = new HashMap<>(extra);
        data.put("version", project.getVersion());
        data.put("name", project.getName());
        data.put("displayName", project.getDisplayName());
        return data;
    }
}
