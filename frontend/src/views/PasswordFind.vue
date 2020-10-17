<template>
  <div class="password-find">
    <ValidationObserver v-slot="{ handleSubmit, passed }">
      <Form @submit.prevent="handleSubmit(submit)">
        <h1>비밀번호 찾기</h1>
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
        <template v-slot:actions>
          <Button type="button" @click="back" cancel value="이전" />
          <Button type="submit" :disabled="!passed" value="확인" />
        </template>
      </Form>
    </ValidationObserver>
  </div>
</template>

<script>
import { BirthField, Button, Form, TextField } from "@/components/form"
import * as Api from "@/api"
import * as DateUtil from "@/utils/date"

export default {
  name: "PasswordFind",
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
        await Api.fetchPasswordFind({
          name: this.name,
          email: this.email,
          birthday: DateUtil.formatLocalDate(this.birth),
        })
        this.$router.push({ path: `/find/result`, query: { email: this.email } })
      } catch (e) {
        alert(e.response.data.message)
      }
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
  align-items: center;
}

.rule-field {
  margin-left: 8px;
  font-size: 12px;
  color: #ff0000;
}
</style>
