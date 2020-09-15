import Vue from "vue"
import VueRouter from "vue-router"
import Recruits from "@/views/Recruits.vue"
import ApplicantRegister from "@/views/ApplicantRegister"
import ApplicationRegister from "@/views/ApplicationRegister"
import Login from "@/views/Login"

Vue.use(VueRouter)

const routes = [
  {
    path: "/recruits",
    component: Recruits,
  },
  {
    path: "/register/applicant/:recruitmentId",
    component: ApplicantRegister,
    props: route => ({
      recruitmentId: Number(route.params.recruitmentId),
    }),
  },
  {
    path: "/register/application/:recruitmentId/:applicantName",
    component: ApplicationRegister,
    props: route => ({
      recruitmentId: Number(route.params.recruitmentId),
      applicantName: route.params.applicantName,
    }),
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
