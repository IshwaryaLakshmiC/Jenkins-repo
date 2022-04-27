def inject_env (String build_branch){
    env.deploy_test_var1='Var 1 - Hello'
    env.deploy_test_var2='Var 2 - World'
    env.environ_file='.Build-Dir/project-Jenkins/.build/env'    
    switch(build_branch) {
        case 'main':
            env.deploy_test_var2='Var 2 - World - Develop branch'
            env.deploy_airfolw_db_host='s-ccls-dashboard-dashboard-rds.cuqu1kbrsam6.us-west-2.rds.amazonaws.com'
            env.deploy_airflow_db_port='blah blah'
            env.deploy_airflow_db_name='another blah blah'
            env.deploy_airflow_db_user='jenkins blah blah'
            break
    }
}

def inject_stage (String build_branch){
    def brr="${env.build_branch}"
    echo "${build_branch}"
    echo "${env.build_branch}"
    switch(build_branch){
        case 'main':
            echo "We are in the switch condition of inject_stage method!"
            echo "brr"
            echo '${brr}'
            echo '"${brr}"'
            break
    }
}

def configuratioin(String build_branch) {
    inject_env(build_branch)
    //     sed -i "s/<% DEPLOY_AIRFLOW_DB_HOST %>/${deploy_airfolw_db_host}/g" ${environ_file}
    sh """
    echo 'Setting env values'
    sed -i "s/<% DEPLOY_AIRFLOW_DB_PORT %>/${deploy_airflow_db_port}/g" ${environ_file}
    sed -i "s/<% DEPLOY_AIRFLOW_DB_NAME %>/${deploy_airflow_db_name}/g" ${environ_file}
    sed -i "s/<% DEPLOY_AIRFLOW_DB_USER %>/${deploy_airflow_db_user}/g" ${environ_file}
    cat '.Build-Dir/project-Jenkins/.build/env'
    """
}

// def mainfunc(String build_branch, String build_number, String build_job, String build_url) {
def mainfunc(String parallel_stage){
    switch('Frontend'){
        case 'Frontend':
            echo "We are in the frontend section"
            echo "HELLO FRONTEND"
            sleep(5)
            echo "HELLO Still in FRONTEND"
            break
        case 'Backend':
            echo "We are in the Backend section"
            echo "HELLO BACKEND"
            sleep(15)
            break
    }

    // inject_env(build_branch)
    // inject_stage(build_branch)   
    // sleep 15     
}

def mainfunc2(String build_branch, String build_number, String build_job, String build_url){
    inject_env(build_branch)    
    stage('Only stage'){

        echo "hello-World"
        withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'alchemy_core_mock_deploy_django_db_creds_develop' , usernameVariable: 'deploy_django_db_user', passwordVariable: 'deploy_django_db_password']]) {
        sh """
            sed -i "s/<% DEPLOY_DJANGO_DB_PASSWORD %>/${deploy_django_db_password}/g" ${environ_file}
            sed -i "s/<% DEPLOY_DJANGO_DB_USER %>/${deploy_django_db_user}/g" ${environ_file}
        """
        }

        sh """
        cat ${environ_file}
        ls -larth
        """
        sleep 20
    }
    stage('Parallel stage'){
        echo "hello-World - Parallel"

        sh """
        echo 'This is the parallel stage'
        ls -larth
        """  
    }
}

return this
