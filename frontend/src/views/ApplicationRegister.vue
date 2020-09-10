<template>
  <div class="application-register">
    <Form @submit.prevent="submit">
      <h1>지원서 작성</h1>
      <TextField
        v-model="name"
        name="name"
        type="text"
        label="이름"
        placeholder="이름을 입력해 주세요."
        :rule="rules.name"
        required
      />
      <TextField
        v-model="password"
        name="password"
        type="password"
        label="비밀번호"
        placeholder="비밀번호를 입력해 주세요"
        :rules="rules.password"
        required
      />
      <TextField
        v-model="rePassword"
        name="re-password"
        type="password"
        label="비밀번호 확인"
        placeholder="비밀번호를 다시 한 번 입력해 주세요"
        :rules="[...rules.rePassword, v => v === password || '비밀번호가 일치하지 않습니다']"
        required
      />
      <TextField
        v-model="url"
        name="url"
        type="url"
        :description="'블로그, GitHub, 포트폴리오 주소 등을 입력해 주세요.'"
        label="URL"
        placeholder="내용을 입력해주세요"
      />
      <div class="actions">
        <Button type="reset" value="초기화" />
        <Button type="save" value="임시 저장" />
        <Button type="submit" value="제출" />
      </div>
      <footer>
        <a class="logo" href="#"/>
      </footer>
    </Form>
  </div>
</template>

<script>
  import {
    Form,
    Button,
    TextField,

  } from "../components/form";

export default {
  components: {
    Form,
    Button,
    TextField,
    TextInput,
  },
  data: () => ({
    name: "",
    password: "",
    rePassword: "",
    url: "",
    rules: { ...regist },
  }),
  methods: {
    submit() {
      confirm("정말로 제출하시겠습니까?");
    },
  },
}
</script>

<style scoped>
  .application-register {
    display: flex;
    flex-direction: column;
    justify-content: center;
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
</style>
