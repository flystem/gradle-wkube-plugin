package com.flystem.wukong.grade;

import org.gradle.api.DefaultTask;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import java.util.Map;

abstract public class ListDataTask extends DefaultTask {

    @Input
    abstract public MapProperty<String, Object> getData();

    @TaskAction
    public void run() {
        Map<String, Object> data = getData().getOrElse(Map.of());
        data.forEach((s, o) -> {
            System.out.println(" " +s + " = " + o);
        });
    }
}