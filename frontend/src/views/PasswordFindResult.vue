<template>
  <div class="password-find-result">
    <Form>
      <h1>비밀번호 찾기</h1>
      <div>
        임시 비밀번호가 <strong>{{ email }}</strong> 으로 발송되었습니다.
      </div>
      <template v-slot:actions>
        <Button type="button" @click="back" cancel value="돌아가기" />
      </template>
    </Form>
  </div>
</template>

<script>
import { Form, Button } from "@/components/form"

export default {
  name: "PasswordFindResult",
  components: {
    Form,
    Button,
  },
  props: {
    email: {
      type: String,
      required: true,
    },
  },
  methods: {
    back() {
      this.$router.push("/recruits")
    },
  },
}
</script>

<style scoped>
.password-find-result {
  display: flex;
  flex-direction: column;
  align-items: center;
}
</style>
