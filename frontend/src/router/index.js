import Vue from "vue"
import VueRouter from "vue-router"
import Regist from "@/views/Regist"

Vue.use(VueRouter)

const routes = [
  {
    path: "/regist",
    component: Regist,
  },
]

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
})

export default router
