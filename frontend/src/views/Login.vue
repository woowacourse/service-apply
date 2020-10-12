<template>
  <div class="login">
    <ValidationObserver v-slot="{ handleSubmit }">
      <Form @submit.prevent="handleSubmit(submit)">
        <h1>내 지원서 보기</h1>
        <ValidationProvider rules="name|required" :bails="false" v-slot="{ errors }">
          <TextField
            v-model="name"
            name="name"
            type="text"
            label="이름"
            placeholder="이름을 입력해 주세요."
            required
          />
          <p v-for="(error, index) in errors" :key="index" class="rule-field">{{ error }}</p>
        </ValidationProvider>

        <ValidationProvider rules="email|required" :bails="false" v-slot="{ errors }">
          <TextField
            v-model="email"
            name="email"
            type="email"
            label="이메일"
            placeholder="이메일 주소를 입력해 주세요."
            required
          />
          <p v-for="(error, index) in errors" :key="index" class="rule-field">{{ error }}</p>
        </ValidationProvider>

        <ValidationProvider rules="year|month|day|required" :bails="false" v-slot="{ errors }">
          <BirthField v-model="birth" required />
          <p v-for="(error, index) in errors" :key="index" class="rule-field">{{ error }}</p>
        </ValidationProvider>
        <ValidationProvider
          name="password"
          rules="password|required"
          :bails="false"
          v-slot="{ errors }"
        >
          <TextField
            v-model="password"
            name="password"
            type="password"
            label="비밀번호"
            placeholder="비밀번호를 입력해 주세요."
            required
          />
          <p v-for="(error, index) in errors" :key="index" class="rule-field">{{ error }}</p>
        </ValidationProvider>
        <div class="actions">
          <Button type="button" @click="back" cancel value="이전" />
          <Button type="submit" value="확인" />
        </div>
        <footer>
          <a class="logo" href="#"></a>
          <router-link class="find-password" to="/find">비밀번호 찾기</router-link>
        </footer>
      </Form>
    </ValidationObserver>
  </div>
</template>

<script>
import { BirthField, Button, Form, TextField } from "@/components/form"
import * as DateUtil from "@/utils/date"
import ValidationProvider from "@/utils/validation/validator"
import { ValidationObserver } from "vee-validate"

export default {
  name: "Login",
  components: {
    Form,
    Button,
    TextField,
    BirthField,
    ValidationProvider,
    ValidationObserver,
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

.rule-field {
  margin-left: 8px;
  font-size: 12px;
  color: #ff0000;
}

footer {
  display: flex;
  justify-content: space-between;
}

.find-password {
  text-decoration: none;
  font-weight: 500;
  color: #2c3e50;
}
</style>
