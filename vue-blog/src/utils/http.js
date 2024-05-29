import axios from "axios";

const http = axios.create({
    timeout: 1000 * 86400,
    withCredentials: true,
    baseURL: '/api',
    // headers: {
    //     'Content-Type': 'application/json; charset=utf-8'
    // }
})
export {http};