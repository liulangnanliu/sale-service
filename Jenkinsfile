pipeline {
  agent {
    label "jenkins-maven"
  }
  options {
        disableConcurrentBuilds()
        // Add a  gitlab connection in Jenkins > Manage Jenkins > Configure System > Gitlab and name it 'gitlab'
        gitLabConnection('gitlab')
    }
  environment {
    ORG = 'jinnjo'
    APP_NAME = 'sale-service'
    CHARTMUSEUM_CREDS = credentials('jenkins-x-chartmuseum')
    DOCKER_REGISTRY_ORG = 'jinnjo'
  }
  stages {
    stage('Build Release') {
      when {
        anyOf { branch 'develop'; branch 'master'; branch 'feature*'; branch 'develop-*'}
      }
      steps {
        container('maven') {

          // ensure we're not on a detached head
          sh "git checkout $BRANCH_NAME"
          sh "git config --global credential.helper store"
          sh "jx step git credentials"

          // so we can retrieve the version in later steps
          sh "echo \$(jx-release-version) > VERSION"
          sh "mvn versions:set -DnewVersion=\$(cat VERSION)"
          sh "jx step tag --version \$(cat VERSION)"
          sh "mvn clean deploy"
          sh "skaffold version"
          sh "export VERSION=`cat VERSION` && skaffold build -f skaffold.yaml"
          sh "jx step post build --image $DOCKER_REGISTRY/$ORG/$APP_NAME:\$(cat VERSION)"
        }
      }
    }
    stage('Promote to PROD-Environments') {
      when {
        branch 'master'
      }
      steps {
        container('maven') {
          dir('charts/sale-service') {
            sh "docker tag $DOCKER_REGISTRY/$ORG/$APP_NAME:\$(cat ../../VERSION) registry.cn-hangzhou.aliyuncs.com/jinnjo/$APP_NAME:\$(cat ../../VERSION)"
            sh "docker push registry.cn-hangzhou.aliyuncs.com/jinnjo/$APP_NAME:\$(cat ../../VERSION)"
            sh "echo registry.cn-hangzhou.aliyuncs.com/jinnjo/$APP_NAME:\$(cat ../../VERSION)"
            sh "jx step changelog --version v\$(cat ../../VERSION)"

            // release the helm chart
            sh "helm init --client-only --stable-repo-url=http://chartmuseum.jx.shequren.cn"
            sh "jx step helm release"

            // promote through all 'Auto' promotion Environments
            // sh "jx promote -b --all-auto --no-wait --timeout 1h --version \$(cat ../../VERSION)"
          }
        }
      }
    }
    stage('Promote to Staging-Environments') {
      when {
         anyOf { branch 'develop'; branch 'develop-*'}
      }
      steps {
        container('maven') {
          dir('charts/sale-service') {
            sh "jx step changelog --version v\$(cat ../../VERSION)"
            sh "helm init --client-only --stable-repo-url=http://chartmuseum.jx.shequren.cn"
            // release the helm chart
            //sh "jx step helm release"

            // promote through all 'Auto' promotion Environments
            //sh "jx promote -b --all-auto --env staging --timeout 1h --version \$(cat ../../VERSION)"
            sh "helm package ."
            sh "helm plugin install https://gitlab.shequren.cn/jinnjo/helm-push"
            sh "helm push -u $CHARTMUSEUM_CREDS_USR -p $CHARTMUSEUM_CREDS_PSW *.tgz http://chartmuseum.jx.shequren.cn"
            sh "helm repo add jinnjohelm http://chartmuseum.jx.shequren.cn"
            sh "helm repo list"
            sh "helm repo update"
            sh "helm search $APP_NAME"
            sh "helm upgrade -i $APP_NAME --namespace jx-staging jinnjohelm/$APP_NAME --version \$(cat ../../VERSION)"
          }
        }
      }
    }
    stage('Promote to Testing-Environments') {
      when {
        anyOf { branch 'feature'; branch 'feature*' }
      }
      steps {
        container('maven') {
          dir('charts/sale-service') {
            sh "jx step changelog --version v\$(cat ../../VERSION)"
            sh "helm init --client-only --stable-repo-url=http://chartmuseum.jx.shequren.cn"
            // release the helm chart
            sh "jx step helm release"

            // promote through all 'Auto' promotion Environments
            sh "jx promote -b --all-auto --no-wait --timeout 1h --version \$(cat ../../VERSION)"
          }
        }
      }
    }
  }
  post {
        failure {
          updateGitlabCommitStatus name: "build${env.BUILD_NUMBER}", state: 'failed'
        }
        success {
          updateGitlabCommitStatus name: "build${env.BUILD_NUMBER}", state: 'success'
        }
        always {
          cleanWs()
        }
  }
}
