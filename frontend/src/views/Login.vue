<template>
  <div class="login">
    <Form @submit="submit">
      <h1>내 지원서 보기</h1>
      <TextField
        v-model="name"
        name="name"
        type="text"
        label="이름"
        placeholder="이름을 입력해 주세요."
        required
      />
      <TextField
        v-model="email"
        name="email"
        type="email"
        label="이메일"
        placeholder="이메일 주소를 입력해 주세요."
        required
      />
      <BirthField v-model="birth" required />
      <TextField
        v-model="password"
        name="password"
        type="password"
        label="비밀번호"
        placeholder="비밀번호를 입력해 주세요."
        required
      />
      <div class="actions">
        <Button type="button" cancel value="취소" />
        <Button type="submit" value="다음" />
      </div>
      <footer>
        <a class="logo" href="#"></a>
        <Label class="click" @click.native="findPassword">비밀번호 찾기</Label>
      </footer>
    </Form>
  </div>
</template>

<script>
import { Form, Button, TextField, BirthField, Label } from "@/components/form"

export default {
  name: "Login",
  components: {
    Form,
    Button,
    TextField,
    BirthField,
    Label,
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
    submit(e) {
      e.preventDefault()
    },
    findPassword() {
      this.$router.push("/find")
    },
  },
}
</script>

<style scoped>
* {
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", "Apple SD Gothic Neo", Roboto,
    "Helvetica Neue", Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol",
    "Noto Color Emoji";
  box-sizing: border-box;
}

.login {
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
