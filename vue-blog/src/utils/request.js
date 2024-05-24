import {http}  from "@/utils/http.js";

const URL = {
    login: '/login',
    register: '/register'
}



const register = (params)=>{
    return http.request({
        url: URL.register,
        method: 'GET',
        data:params
    })
}

const login = (params)=>{
    return http.request({
        url: URL.login,
        method: 'GET',
        data:params
    })
}

export {login,register};