node{
    def registry =  'ghcr.io'
    def image_name = "hexagonal_template_${env.BUILD_ID}"
    stage('clone'){
        git 'https://github.com/mohamed-youssoufou/batch-backend.git'
    }
    stage('build'){
        sh 'cd build/hackathon-hexagonal/'
        docker.build('$image_name', '.')
        sh 'cd ../../'
        sh 'docker compose build'
    }
    stage('run') {
        sh 'docker compose up'
    }
}
