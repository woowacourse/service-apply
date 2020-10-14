import Vue from "vue"
import VueRouter from "vue-router"
import Recruits from "@/views/Recruits.vue"
import ApplicantRegister from "@/views/ApplicantRegister"
import ApplicationRegister from "@/views/ApplicationRegister"
import Login from "@/views/Login"
import PasswordFind from "@/views/PasswordFind"
import PasswordFindResult from "@/views/PasswordFindResult"
import PasswordEdit from "@/views/PasswordEdit"
import store from "@/store"

Vue.use(VueRouter)

const requireAuth = (to, from, next) => {
  if (store.state.token.value !== "") {
    return next()
  }
  alert("로그인이 필요합니다.")
  next("/login")
}

const routes = [
  {
    path: "/index.html",
    redirect: "/",
  },
  {
    path: "/",
    redirect: "/recruits",
  },
  {
    path: "/recruits",
    component: Recruits,
  },
  {
    path: "/applicants/new",
    component: ApplicantRegister,
    props: route => ({
      recruitmentId: Number(route.query.recruitmentId),
    }),
  },
  {
    path: "/application-forms/new",
    component: ApplicationRegister,
    props: route => ({
      recruitmentId: Number(route.query.recruitmentId),
      status: "new",
    }),
    beforeEnter: requireAuth,
  },
  {
    path: "/application-forms/edit",
    component: ApplicationRegister,
    props: route => ({
      recruitmentId: Number(route.query.recruitmentId),
      status: "edit",
    }),
    beforeEnter: requireAuth,
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
    path: "/edit",
    component: PasswordEdit,
    beforeEnter: requireAuth,
  },
  {
    path: "/find/result",
    props: route => ({
      email: route.query.email,
    }),
    component: PasswordFindResult,
  },
]

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
})

export default router
