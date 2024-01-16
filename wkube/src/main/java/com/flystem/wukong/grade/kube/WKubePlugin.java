package com.flystem.wukong.grade.kube;


import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.RegularFile;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;

import java.util.List;

public class WKubePlugin implements Plugin<Project> {
    public static final String GROUP_NAME = "wkube";

    public RegularFile defaultTemplateFile(Project project) {
        return project.getLayout().getProjectDirectory().dir("src").dir("main").dir(GROUP_NAME).file("deployment.yaml");
    }

    private RegularFile defaultDeploymentFile(Project project, RegularFileProperty templateFileProperty) {
        String name = templateFileProperty.getAsFile().get().getName();
        return project.getLayout().getBuildDirectory().dir(GROUP_NAME).get().file(name);
    }



    @Override
    public void apply(Project project) {
        WKubePluginExtension extension =
                project.getExtensions().create(GROUP_NAME, WKubePluginExtension.class);

        extension.getTemplateFile().convention(defaultTemplateFile(project));
        extension.getDeploymentFile().convention(defaultDeploymentFile(project, extension.getTemplateFile()));
        TaskContainer tasks = project.getTasks();
        tasks.register("listData", ListDataTask.class, task -> {
            task.setGroup(GROUP_NAME);
            task.getData().set(extension.getAllData(project));
        });
        TaskProvider<GenerateTask> generateTask = tasks.register("genDeploy", GenerateTask.class, task -> {
            task.setGroup(GROUP_NAME);
            task.getTemplateFile().set(extension.getTemplateFile());
            task.getDeploymentFile().set(extension.getDeploymentFile());
            task.getData().set(extension.getAllData(project));
        });
        tasks.register("applyDeploy", ApplyTask.class, task -> {
            task.setGroup(GROUP_NAME);
            task.setDependsOn(List.of(generateTask));
            task.getDeploymentFile().set(generateTask.get().getDeploymentFile());
            task.getKubeconfig().set(extension.getKubeconfig());
        });

    }

    private static void log(String message) {
        System.out.println(message);
    }
}
