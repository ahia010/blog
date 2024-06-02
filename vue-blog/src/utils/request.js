import {http} from "@/utils/http.js";

const URL = {
    login: '/user/login',
    register: '/register',
    postList: '/post/list',
    userList: '/user/page',
}


const register = async (params) => {
    return http.request({
        url: URL.register,
        method: 'GET',
        params: params
    })
}

const login = async (params) => {
    return http.request({
        url: URL.login,
        method: 'POST',
        params: params
    })
}

const getPostList = async (params) => {
    return http.request({
        url: URL.postList,
        method: 'POST',
        params: params
    })
}

const getPostDetail =async (id)=>{
    return http.request({
        url:'/post/getInfo/'+id,
        method:'GET',
    })

}

const getUserList = async (params, headers) => {
    return http.request({
        url: URL.userList,
        method: 'GET',
        params: params,
        headers: headers
    })
}

const addPost = async (params, headers) => {
    return http.request({
        url: '/post/save',
        method: 'POST',
        headers: headers,
        params: params
    })

}

const getHomeList = async (params) => {
    return http.request({
        url: '/post/page',
        method: 'GET',
        params: params
    })
}

const getUserName = async (headers) => {
    return http.request({
        url: '/user/getUserName',
        method: 'GET'
    })
}

export {login, register, getPostList, getUserList, addPost, getHomeList, getUserName,getPostDetail};