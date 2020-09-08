<template>
  <div class="loginPage">
    <h1>내 지원서 보기</h1>
    <div class="login-form-box">
      <div class="login-form">
        <div class="block">
          <label for="name" class="form-label">이름</label>
          <input type="text" id="name" size="40" v-model="user.name" />
        </div>
        <div class="block">
          <label for="email" class="form-label">이메일</label>
          <input type="email" id="email" size="40" v-model="user.email" />
        </div>
        <div class="block">
          <label for="date" class="form-label">생년월일</label>
          <input type="date" id="date" v-model="user.birthDay" />
        </div>
        <div class="block">
          <label for="password" class="form-label">비밀번호</label>
          <input type="password" id="password" size="40" v-model="user.password" />
        </div>
        <button class="button" @click="submit">확인</button>
      </div>
    </div>
    <div class="password-button">
      비밀번호 찾기
    </div>
  </div>
</template>

<script>
export default {
  name: "LoginPage",
  components: {},
  data() {
    return {
      user: {
        name: "",
        email: "",
        birthDay: "",
        password: "",
      },
    }
  },
  methods: {
    submit() {
    },
  },
}
</script>

<style scoped>
.loginPage {
  margin-top: 5%;
  align-items: center;
  margin-left: 15vw;
  margin-right: 15vw;
}
.login-form-box {
  background: #dadada;
  position: relative;
  margin-left: 15vw;
  margin-right: 15vw;
  padding-top: 10px;
  padding-bottom: 10px;
}
.login-form {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 1vw;
}
input {
  padding: 10px;
}
.block {
  margin-bottom: 10px;
}
.form-label {
  display: inline-block;
  width: 80px;
  text-align: left;
  margin-bottom: 2vh;
}
.button {
  display: block;
  width: 70%;
  border: none;
  background-color: rgb(52, 152, 219);
  color: white;
  padding: 14px 28px;
  font-size: 16px;
  text-align: center;
  cursor: pointer;
}
.password-button {
  position: relative;
  margin-left: 15vw;
  margin-right: 15vw;
  margin-top: 1vh;
  text-align: right;
}
</style>
