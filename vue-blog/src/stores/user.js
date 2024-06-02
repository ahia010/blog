import {reactive, ref} from "vue";
import {defineStore} from "pinia";

export const userStore = defineStore('user', () => {
    const userInfo = reactive(
        {username: '', token: '', role: '',avatar:''});
    const setUserInfo = (username,token,role,avatar) => {
        userInfo.username = username;
        userInfo.token = token;
        userInfo.role = role;
        userInfo.avatar = avatar;
    }
    const getUserInfo = () => {
        return userInfo;
    }

    const logout = () => {
        userInfo.username = '';
        userInfo.token = '';
        userInfo.role = '';
        userInfo.avatar = '';
    }


    return {
        getUserInfo,
        userInfo,
        setUserInfo,
        logout
    }
},{
    persist: true,
})
