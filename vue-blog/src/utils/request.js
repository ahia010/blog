import {http}  from "@/utils/http.js";

const URL = {
    login: '/user/login',
    register: '/register',
    postList:'/post/list',
}



const register = async (params)=>{
    return http.request({
        url: URL.register,
        method: 'GET',
        params:params
    })
}

const login = async (params)=>{
    return http.request({
        url: URL.login,
        method: 'POST',
        params:params
    })
}

const getPostList = async (params)=> {
    return http.request({
        url: URL.postList,
        method: 'POST',
        params: params
    })
}
export {login,register};