
enable api

    gcloud services enable container.googleapis.com

install the kubectl component

    gcloud components install kubectl
    kubectl version --client

install the gcloud auth plugin

    gcloud components install gke-gcloud-auth-plugin
    gcloud components list | grep gke-gcloud-auth-plugin
    gke-gcloud-auth-plugin --version

if the plugin is not found, find the installation root and add it to the path

    gcloud info | grep Installation Root


create a new gke cluster

    gcloud container clusters create wordsmith-cluster \
        --num-nodes=1 --zone=europe-west1-b \
        --service-account=wordsmith-sa@wordsmith-1234.iam.gserviceaccount.com

configure kubectl to use the new cluster

    gcloud container clusters get-credentials wordsmith-cluster --zone=europe-west1-b

deploy the application

    kubectl apply -f k8s/namespace.yaml
    kubectl apply -f k8s/deployment.yaml
    kubectl apply -f k8s/service.yaml

check the deployment

    kubectl get nodes
    kubectl get deployments --namespace=wordsmith
    kubectl get pods --namespace=wordsmith

view deployment status

    kubectl describe deployment wordsmith-deployment --namespace=wordsmith

view logs of container in a certain pod (example):

    kubectl logs wordsmith-deployment-bfd9c6c8d-82g84 -c wordsmith

create a kubernetes service account and link to the GCP service account

    kubectl create serviceaccount wordsmith-sa --namespace=wordsmith
    kubectl annotate serviceaccount wordsmith-sa \
        --namespace wordsmith \
        iam.gke.io/gcp-service-account=912454425069-compute@developer.gserviceaccount.com

edit the deployment to use the new service account
add `serviceAccountName` field in deployment file under `spec.template.spec`:

    kubectl edit deployment wordsmith-deployment --namespace wordsmith

restart deployment

    kubectl rollout restart deployment wordsmith-deployment --namespace wordsmith

