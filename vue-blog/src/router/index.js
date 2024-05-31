import {createRouter, createWebHashHistory} from 'vue-router'
import Home from "@/views/Home.vue";
import Login from "@/views/Login.vue";
import Dashboard from "@/views/admin/Dashboard.vue";
import List from "@/views/List.vue";
import Mine from "@/views/Mine.vue";
import UserList from "@/views/admin/UserList.vue";
import PageList from "@/views/admin/PageList.vue";

const router = createRouter({
    history: createWebHashHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            component: Home
        },
        {
            path: '/list',
            name: 'list',
            component: List
        }, {
            path: '/mine',
            name: 'mine',
            component: Mine
        },
        {
            path: '/login',
            name: 'login',
            component: Login
        }, {
            path: '/admin',
            name: 'admin',
            children: [
                {
                    path: 'dashboard',
                    name: 'dashboard',
                    component: Dashboard
                }, {
                    path: 'user',
                    name: 'admin-user',
                    component: UserList
                }, {
                    path: 'page',
                    name: 'page',
                    component: PageList
                }
            ]
        }
    ]
})

export default router
