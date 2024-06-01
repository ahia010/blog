import {createRouter, createWebHashHistory} from 'vue-router'
import Home from "@/views/Home.vue";
import Login from "@/views/Login.vue";
import Dashboard from "@/views/admin/Dashboard.vue";
import List from "@/views/List.vue";
import Mine from "@/views/Mine.vue";
import UserList from "@/views/admin/UserList.vue";
import Detail from "@/views/Detail.vue";
import Admin from "@/views/admin/Admin.vue";
import PostList from "@/views/admin/PostList.vue";
import PostDetail from "@/views/admin/PostDetail.vue";

const router = createRouter({
    history: createWebHashHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            component: Home
        },
        {
            path: '/detail/:id',
            name: 'detail',
            component: Detail
        },

        {
            path: '/list',
            name: 'list',
            component: List
        }, {
            path: '/mine',
            name: 'mine',
            component: Mine
        }, {
            path: '/register',
            name: 'register',
            component: Login
        },
        {
            path: '/login',
            name: 'login',
            component: Login
        }, {
            path: '/admin',
            name: 'admin',
            component:Admin,
            children: [
                {
                    path: '',
                    name: 'dashboard',
                    component: Dashboard
                }, {
                    path: 'user',
                    name: 'admin-user',
                    component: UserList
                }, {
                    path: 'post',
                    name: 'post',
                    component: PostList
                },{
                    path:'postDetail',
                    name:'postDetail',
                    component:PostDetail
                }
            ]
        }
    ]
})

export default router
