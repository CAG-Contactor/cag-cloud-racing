import axios from 'axios';

const env = "//x7acufjsp7.execute-api.eu-central-1.amazonaws.com/ccgaoz"

const API = {
    signUp: (body: any) => axios.post(`${env}/registered-user`, body),
    signIn: (body: any) => axios.post(`${env}/user-login`, body)
}

export default API