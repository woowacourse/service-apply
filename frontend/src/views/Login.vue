<template>
  <div class="login">
    <ValidationObserver v-slot="{ handleSubmit, passed }">
      <Form @submit.prevent="handleSubmit(submit)">
        <h1>내 지원서 보기</h1>
        <ValidationProvider rules="name|required" v-slot="{ errors }">
          <TextField
            v-model="name"
            name="name"
            type="text"
            label="이름"
            placeholder="이름을 입력해 주세요."
            required
          />
          <p class="rule-field">{{ errors[0] }}</p>
        </ValidationProvider>
        <ValidationProvider rules="email|required" v-slot="{ errors }">
          <TextField
            v-model="email"
            name="email"
            type="email"
            label="이메일"
            placeholder="이메일 주소를 입력해 주세요."
            required
          />
          <p class="rule-field">{{ errors[0] }}</p>
        </ValidationProvider>
        <ValidationProvider rules="year|month|day|required" v-slot="{ errors }">
          <BirthField v-model="birth" required />
          <p class="rule-field">{{ errors[0] }}</p>
        </ValidationProvider>
        <ValidationProvider name="password" rules="password|required" v-slot="{ errors }">
          <TextField
            v-model="password"
            name="password"
            type="password"
            label="비밀번호"
            placeholder="비밀번호를 입력해 주세요."
            required
          />
          <p class="rule-field">{{ errors[0] }}</p>
        </ValidationProvider>
        <template v-slot:actions>
          <Button type="button" @click="back" cancel value="이전" />
          <Button type="submit" :disabled="!passed" value="확인" />
        </template>
        <template v-slot:footer>
          <router-link class="find-password" to="/find">비밀번호 찾기</router-link>
        </template>
      </Form>
    </ValidationObserver>
  </div>
</template>

<script>
import { BirthField, Button, Form, TextField } from "@/components/form"
import * as DateUtil from "@/utils/date"

export default {
  name: "Login",
  components: {
    Form,
    Button,
    TextField,
    BirthField,
  },
  data: () => ({
    name: "",
    email: "",
    password: "",
    birth: {
      year: "",
      month: "",
      day: "",
    },
  }),
  methods: {
    async submit() {
      try {
        await this.$store.dispatch("login", {
          name: this.name,
          email: this.email,
          birthday: DateUtil.formatLocalDate(this.birth),
          password: this.password,
        })
        alert("로그인 성공")
        this.$router.push({ path: "/recruits", query: { status: "applied" } })
      } catch (e) {
        alert(e.response.data.message)
      }
    },
    findPassword() {
      this.$router.push("/find")
    },
    back() {
      this.$router.go(-1)
    },
  },
}
</script>

<style scoped>
.login {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.rule-field {
  margin-left: 8px;
  font-size: 12px;
  color: #ff0000;
}

.find-password {
  text-decoration: none;
  font-weight: 500;
  font-size: 14px !important;
  color: #2c3e50;
  margin-right: 5px;
}
</style>
