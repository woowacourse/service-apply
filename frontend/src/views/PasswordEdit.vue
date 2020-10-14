<template>
  <div class="password-edit">
    <ValidationObserver v-slot="{ handleSubmit, passed }">
      <Form @submit.prevent="handleSubmit(submit)">
        <h1>비밀번호 변경</h1>
        <ValidationProvider name="before-password" rules="password|required" v-slot="{ errors }">
          <TextField
            v-model="beforePassword"
            name="before-password"
            type="password"
            label="기존 비밀번호"
            placeholder="기존 비밀번호를 입력해 주세요"
            required
          />
          <p class="rule-field">{{ errors[0] }}</p>
        </ValidationProvider>
        <ValidationProvider name="password" rules="password|required" v-slot="{ errors }">
          <TextField
            v-model="password"
            name="password"
            type="password"
            label="새 비밀번호"
            placeholder="비밀번호를 입력해 주세요"
            required
          />
          <p class="rule-field">{{ errors[0] }}</p>
        </ValidationProvider>
        <ValidationProvider rules="password|rePassword:@password|required" v-slot="{ errors }">
          <TextField
            v-model="rePassword"
            name="re-password"
            type="password"
            label="비밀번호 확인"
            placeholder="비밀번호를 다시 한 번 입력해 주세요"
            required
          />
          <p class="rule-field">{{ errors[0] }}</p>
        </ValidationProvider>
        <template v-slot:actions>
          <Button type="button" @click="back" cancel value="이전" />
          <Button type="submit" :disabled="!passed" value="확인" />
        </template>
      </Form>
    </ValidationObserver>
  </div>
</template>

<script>
import { Button, Form, TextField } from "@/components/form"
import * as Api from "@/api"

export default {
  name: "PasswordEdit",
  components: {
    Form,
    Button,
    TextField,
  },
  data: () => ({
    beforePassword: "",
    password: "",
    rePassword: "",
  }),
  methods: {
    back() {
      this.$router.go(-1)
    },
    async submit() {
      try {
        await Api.fetchPasswordEdit({
          token: this.$store.getters["token"],
          password: this.beforePassword,
          newPassword: this.password,
        })
        alert("비밀번호가 변경되었습니다. 다시 로그인해주세요.")
        this.$store.dispatch("resetToken")
        this.$router.push("/login")
      } catch (e) {
        alert(e.response.data.message)
      }
    },
  },
}
</script>

<style scoped>
.password-edit {
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
</style>
