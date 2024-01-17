# wukong-gradle-plugin
This plugin helps you to generate kubernetes deployment file and apply to kubernetes.You can fill data in template file.Plugin uses [freemarker](https://freemarker.apache.org/) as template engine
## requirements:
- [kubectl](https://kubernetes.io/docs/tasks/tools)
- [docker](https://www.docker.com/products/docker-desktop)
## config
```
wukong{
    templateFile = layout.projectDirectory.file("src/main/wukong/deployment.yaml")
    deploymentFile = layout.buildDirectory.file("wukong/deployment.yaml")
    kubeconfig = "kubeconfig-test.yaml"
    data{
        namespace=getProject().name
    }
}
```
### templateFile
Template file's default path is src/main/wukong/deployment.yaml
### deploymentFile
Generated deployment file's default path is build/wukong/deployment.yaml
### kubeconfig
Kubeconfig value should be a file name in user kube config directory $HOME/.kube/
### data
Data is a model used to fill template to generate deployment file
## tasks
- listData: print data model
- genDeploy: generate deployment file
- applyDeploy: apply deployment file to kubernetes cluster