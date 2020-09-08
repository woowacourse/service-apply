import Vue from "vue"
import VueRouter from "vue-router"
import Recruits from "@/views/Recruits.vue"

Vue.use(VueRouter)

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes: [
    {
      path: "/recruits",
      component: Recruits,
    },
  ],
})

export default router
