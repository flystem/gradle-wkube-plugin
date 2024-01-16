# gradle-kubernetes-plugin
## requirements:
- [kubectl](https://kubernetes.io/docs/tasks/tools/)
## config
```
wKube{
    kubeconfig = "kubeconfig-test.yaml"
    data{
        namespace=getProject().name
    }
}
```
## template
Template file path is src/main/wkube/deployment.yaml