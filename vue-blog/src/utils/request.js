import {http}  from "@/utils/http.js";

const URL = {
    login: '/user/login',
    register: '/register'
}



const register = (params)=>{
    return http.request({
        url: URL.register,
        method: 'GET',
        params:params
    })
}

const login = (params)=>{
    return http.request({
        url: URL.login,
        method: 'POST',
        params:params
    })
}

export {login,register};