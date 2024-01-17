package com.flystem.wukong.grade;

import org.apache.commons.io.FileUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.nio.file.Path;

abstract public class ApplyTask extends DefaultTask {
    @InputFile
    abstract public RegularFileProperty getDeploymentFile();

    @Input
    @Optional
    abstract public Property<String> getKubeconfig();

    private String buildConfigPath(){
        String config=getKubeconfig().getOrElse("config");
        return Path.of(FileUtils.getUserDirectory().getPath(), ".kube", config).toString();

    }
    @TaskAction
    public void run() {
        getProject().exec(execSpec -> {
            File fullFile = getDeploymentFile().get().getAsFile();
            execSpec.setWorkingDir(fullFile.getParentFile());
            String deploymentFile = fullFile.getName();
            execSpec.executable("kubectl");
            execSpec.args("apply");
            if (getKubeconfig().isPresent()) {
                execSpec.args("--kubeconfig", buildConfigPath());
            }
            execSpec.args("-f", deploymentFile);
        }).assertNormalExitValue();
    }
}
