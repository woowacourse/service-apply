<template>
  <div class="password-find-result">
    <Form>
      <h1>임시 비밀번호 발송</h1>
      <div>
        입력하신 이메일 주소로 임시 비밀번호를 발송해 드렸습니다.
      </div>
      <div class="actions">
        <Button type="button" @click="back" cancel value="돌아가기" />
      </div>
      <footer>
        <a class="logo" href="#"></a>
      </footer>
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
