import Vue from "vue"
import VueRouter from "vue-router"
import Register from "@/views/Register"
import ApplicationRegister from "@/views/ApplicationRegister"
import Login from "@/views/Login"

Vue.use(VueRouter)

const routes = [
  {
    path: "/register",
    component: Register,
  },
  {
    path: "/login",
    component: Login,
  },
  {
    path: "/application/register",
    component: ApplicationRegister,
  },
]

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
})

export default router
