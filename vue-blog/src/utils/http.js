import axios from "axios";

const http = axios.create({
    timeout: 1000 * 86400,
    withCredentials: true,
    baseURL: 'http://127.0.0.1:8888/',
    // headers: {
    //     'Content-Type': 'application/json; charset=utf-8'
    // }
})
export {http};