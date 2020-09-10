import Vue from "vue"
import VueRouter from "vue-router"
import Recruits from "@/views/Recruits.vue"

Vue.use(VueRouter)

const routes = [
  {
    path: "/recruits",
    component: Recruits,
  },
]

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
})

export default router
