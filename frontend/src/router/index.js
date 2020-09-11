import Vue from "vue"
import VueRouter from "vue-router"
import Register from "@/views/Register"
import RecruitmentRegister from "@/views/RecruitmentRegister"
import Login from "@/views/Login"

Vue.use(VueRouter)

const routes = [
  {
    path: "/register",
    component: Register,
  },
  {
    path: "/application/regist",
    component: RecruitmentRegister,
  },
  {
    path: "/login",
    component: Login,
  },
]

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
})

export default router
