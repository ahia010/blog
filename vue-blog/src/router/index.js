import {createRouter, createWebHistory} from 'vue-router'
import Home from "@/views/Home.vue";
import Login from "@/views/Login.vue";
import Dashboard from "@/views/admin/Dashboard.vue";
import UserAdmin from "@/views/admin/UserAdmin.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            component: Home
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
                    component: UserAdmin
                }
            ]
        }
    ]
})

export default router
