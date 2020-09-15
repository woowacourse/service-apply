<template>
  <div class="password-find">
    <Form @submit.prevent="submit">
      <h1>비밀번호 찾기</h1>
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
      <div class="actions">
        <Button type="button" @click="back" cancel value="이전" />
        <Button type="submit" :disabled="!validName || !validEmail || !validBirth" value="확인" />
      </div>
      <footer>
        <a class="logo" href="#"></a>
      </footer>
    </Form>
  </div>
</template>

<script>
import { Form, Button, TextField, BirthField } from "@/components/form"
import { login } from "@/utils/validation"

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
  }),
  methods: {
    submit() {
      //TODO 비밀번호 찾기 API
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
.password-find {
  display: flex;
  flex-direction: column;
  justify-content: center;
  height: 100%;
  align-items: center;
  background: #ced6e0;
}

.actions {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 20px 0;
}

.actions > .button {
  flex: 1;
}

.logo {
  display: flex;
  width: 100px;
  height: 32px;
  background: url("/assets/logo/logo_full_dark.png");
  background-size: 100% 100%;
}
footer {
  display: flex;
  justify-content: space-between;
}
.click {
  cursor: pointer;
}
</style>
