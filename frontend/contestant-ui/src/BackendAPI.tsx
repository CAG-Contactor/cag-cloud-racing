import axios from 'axios';

const env = "https://j6fq9dc1mh.execute-api.eu-central-1.amazonaws.com/ccgaoz"

const API = {
    signUp: (body: any) => axios.post(`${env}/registered-user`, body),
    signIn: (body: any) => axios.post(`${env}/user-login`, body),

    addMeInQueue: (body: any) => axios.post(`${env}/race-queue`, body),
    getQueue: () => axios.get(`${env}/race-queue`),
    bailOutFromQueue: (body: any) => axios.delete(`${env}/race-queue`, { data: body }),
    getRaces: (name: string) => axios.get(`${env}/registered-user/${name}/race`),

    getLeaderboard: () => axios.get(`${env}/leader-board`)
}

export default API

