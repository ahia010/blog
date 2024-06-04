import {http} from "@/utils/http.js";

const URL = {
    login: '/user/login',
    register: '/user/register',
    postList: '/post/list',
    userList: '/user/page',
}


const register = async (params) => {
    return http.request({
        url: URL.register,
        method: 'POST',
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

const getUserInfo = async (id, headers) => {
    return http.request({
        url: '/user/getInfo/' + id,
        method: 'GET',
        headers: headers
    })
}

const saveUserRequest = async (params, headers) => {
    return http.request({
        url: '/user/save',
        method: 'PUT',
        params: params,
        headers: headers
    })
}

const deleteUserRequest = async (ids, headers) => {
    return http.request({
        url: '/user/delete',
        method: 'DELETE',
        params: ids,
        headers: headers
    })
}

const getPostList = async (params) => {
    return http.request({
        url: '/post/page',
        method: 'GET',
        params: params
    })
}

const getPostDetail = async (id) => {
    return http.request({
        url: '/post/getInfo/' + id,
        method: 'GET',
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
const updatePost = async (params, headers) => {
    return http.request({
        url: '/post/update',
        method: 'PUT',
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
        url: '/user/getUsername',
        method: 'GET',
        headers: headers
    })
}

export {
    updatePost,
    login,
    deleteUserRequest,
    saveUserRequest,
    register,
    getPostList,
    getUserList,
    addPost,
    getHomeList,
    getUserName,
    getPostDetail,
    getUserInfo
}