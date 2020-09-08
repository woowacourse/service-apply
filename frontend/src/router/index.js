import Vue from "vue"
import VueRouter from "vue-router"
import Regist from "@/views/Regist"
import LoginPage from "@/views/LoginPage.vue"

Vue.use(VueRouter)

const routes = [
  {
    path: "/regist",
    component: Regist,
  },
  {
    path: "/login",
    name: "LoginPage",
    component: LoginPage,
  },
]

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
})

export default router
