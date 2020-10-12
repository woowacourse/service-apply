<template>
  <div class="login">
    <Form @submit.prevent="submit">
      <h1>내 지원서 보기</h1>
      <TextField
        v-model="name"
        name="name"
        type="text"
        label="이름"
        placeholder="이름을 입력해 주세요."
        :rules="rules.name"
        @valid="v => (this.validName = v)"
        required
      />
      <TextField
        v-model="email"
        name="email"
        type="email"
        label="이메일"
        placeholder="이메일 주소를 입력해 주세요."
        :rules="rules.email"
        @valid="v => (this.validEmail = v)"
        required
      />
      <BirthField v-model="birth" @valid="v => (this.validBirth = v)" required />
      <TextField
        v-model="password"
        name="password"
        type="password"
        label="비밀번호"
        placeholder="비밀번호를 입력해 주세요."
        :rules="rules.password"
        @valid="v => (this.validPassword = v)"
        required
      />
      <template v-slot:actions>
        <Button type="button" @click="back" cancel value="이전" />
        <Button
          type="submit"
          :disabled="!validName || !validEmail || !validPassword || !validBirth"
          value="확인"
        />
      </template>
      <template v-slot:footer>
        <router-link class="find-password" to="/find">비밀번호 찾기</router-link>
      </template>
    </Form>
  </div>
</template>

<script>
import { BirthField, Button, Form, TextField } from "@/components/form"
import { login } from "@/utils/validation"
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
    rules: { ...login },
    validName: false,
    validEmail: false,
    validBirth: false,
    validPassword: false,
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
        this.$router.push("/my-application-forms")
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

.find-password {
  text-decoration: none;
  font-weight: 500;
  color: #2c3e50;
  margin-right: 5px;
}
</style>
