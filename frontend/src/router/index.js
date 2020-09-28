import Vue from "vue"
import VueRouter from "vue-router"
import Recruits from "@/views/Recruits.vue"
import ApplicantRegister from "@/views/ApplicantRegister"
import ApplicationRegister from "@/views/ApplicationRegister"
import Login from "@/views/Login"
import PasswordFind from "@/views/PasswordFind"
import MyApplications from "@/views/MyApplications"

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
    path: "/register/application/:recruitmentId",
    component: ApplicationRegister,
    props: route => ({
      recruitmentId: Number(route.params.recruitmentId),
    }),
  },
  {
    path: "/login",
    component: Login,
  },
  {
    path: "/find",
    component: PasswordFind,
  },
  {
    path: "/my-applications",
    component: MyApplications,
  },
]

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
})

export default router
