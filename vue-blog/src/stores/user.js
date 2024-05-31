import {reactive, ref} from "vue";
import {defineStore} from "pinia";

export const userStore = defineStore('user', () => {
    const userInfo = reactive(
        {username: '', token: '', role: ''});
    const setUserInfo = (info) => {
        userInfo.value.username = info.username;
        userInfo.value.token = info.token;
        userInfo.value.role = info.role;
    }
    const getUserInfo = () => {
        return userInfo;
    }

    return {
        getUserInfo,
        userInfo,
        setUserInfo
    }
},{
    persist: true,
})
