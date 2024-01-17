package com.flystem.wukong.grade;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

abstract public class GenerateTask extends DefaultTask {
    @InputFile
    abstract public RegularFileProperty getTemplateFile();

    @OutputFile
    abstract public RegularFileProperty getDeploymentFile();

    @Input
    abstract public MapProperty<String, Object> getData();

    @TaskAction
    public void run() throws IOException, TemplateException {
        System.out.println("run GenerateTask:" + getTemplateFile());
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
        File fullFile = getTemplateFile().get().getAsFile();
        File parent = fullFile.getParentFile();
        cfg.setDirectoryForTemplateLoading(parent);
        cfg.setDefaultEncoding("UTF-8");
        String fileName = fullFile.getName();
        Template template = cfg.getTemplate(fileName);
        Map<String, Object> root = getData().get();
        Writer out = new FileWriter(getDeploymentFile().getAsFile().getOrElse(new File(new File(getProject().getBuildDir(), "kube"), fileName)));
        template.process(root, out);
    }
}