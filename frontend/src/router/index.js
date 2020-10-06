import Vue from "vue"
import VueRouter from "vue-router"
import Recruits from "@/views/Recruits.vue"
import ApplicantRegister from "@/views/ApplicantRegister"
import ApplicationRegister from "@/views/ApplicationRegister"
import Login from "@/views/Login"
import PasswordFind from "@/views/PasswordFind"
import PasswordFindResult from "@/views/PasswordFindResult"
import MyApplications from "@/views/MyApplications"
import store from "@/store"

Vue.use(VueRouter)

const requireAuth = (to, from, next) => {
  if (store.getters["token"] !== "") {
    return next()
  }
  alert("로그인이 필요합니다.")
  next("/login")
}

const routes = [
  {
    path: "/",
    redirect: "/recruits",
  },
  {
    path: "/recruits",
    component: Recruits,
  },
  {
    path: "/register/applicant",
    component: ApplicantRegister,
    props: route => ({
      recruitmentId: Number(route.query.recruitmentId),
    }),
  },
  {
    path: "/applications/:status",
    component: ApplicationRegister,
    props: route => ({
      recruitmentId: Number(route.query.recruitmentId),
      status: status,
    }),
    children: [
      {
        name: "edit",
        path: "edit",
      },
    ],
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
    path: "/find/result",
    props: route => ({
      email: route.query.email,
    }),
    component: PasswordFindResult,
  },
  {
    path: "/my-applications",
    component: MyApplications,
    beforeEnter: requireAuth,
  },
]

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
})

export default router
