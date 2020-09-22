<template>
  <div class="register">
    <Form @submit.prevent="submit">
      <h1>지원자 정보</h1>
      <SummaryCheckField
        name="policy"
        label="개인정보 수집 및 이용 동의"
        v-model="policyCheck"
        required
      >
        <p class="summary">{{ policySummary }}</p>
      </SummaryCheckField>
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
        v-model="phoneNumber"
        name="phone-number"
        type="text"
        label="전화번호"
        placeholder="연락 가능한 전화번호를 입력해 주세요."
        :rules="rules.phoneNumber"
        @valid="v => (this.validPhoneNumber = v)"
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
      <TextField
        v-model="password"
        name="password"
        type="password"
        label="비밀번호"
        placeholder="비밀번호를 입력해 주세요."
        :rules="rules.password"
        @valid="validPasswordInputs"
        required
      />
      <TextField
        v-model="rePassword"
        name="re-password"
        type="password"
        label="비밀번호 확인"
        placeholder="비밀번호를 다시 한번 입력해 주세요."
        :rules="[...rules.rePassword, v => v === password || '비밀번호가 일치하지 않습니다.']"
        @valid="validPasswordInputs"
        required
      />
      <BirthField v-model="birth" @valid="v => (this.validBirth = v)" required />
      <GenderField v-model="gender" @valid="v => (this.validGender = v)" required />
      <div class="actions">
        <Button cancel value="취소" />
        <Button
          type="submit"
          :disabled="
            !policyCheck ||
              !validName ||
              !validPhoneNumber ||
              !validEmail ||
              !validPassword ||
              !validBirth ||
              !validGender
          "
          value="다음"
        />
      </div>
      <footer>
        <a class="logo" href="#"></a>
      </footer>
    </Form>
  </div>
</template>

<script>
import { mapActions } from "vuex"
import {
  BirthField,
  Button,
  Form,
  GenderField,
  SummaryCheckField,
  TextField,
} from "@/components/form"
import * as DateUtil from "@/utils/date"
import { register } from "@/utils/validation"
import { POLICY_SUMMARY } from "./constants"

export default {
  props: {
    recruitmentId: Number,
  },
  components: {
    Form,
    Button,
    TextField,
    BirthField,
    GenderField,
    SummaryCheckField,
  },
  data: () => ({
    policySummary: POLICY_SUMMARY,
    policyCheck: false,
    name: "",
    phoneNumber: "",
    email: "",
    password: "",
    rePassword: "",
    birth: {
      year: "",
      month: "",
      day: "",
    },
    gender: "",
    rules: { ...register },
    validName: false,
    validPhoneNumber: false,
    validEmail: false,
    validPassword: false,
    validBirth: false,
    validGender: false,
  }),
  methods: {
    ...mapActions(["fetchRegisterAndSetApplicantInfo"]),
    parseApplicantInfo() {
      return {
        name: this.name,
        phoneNumber: this.phoneNumber,
        email: this.email,
        password: this.password,
        gender: this.gender.toUpperCase(),
        birthday: DateUtil.formatLocalDate(this.birth),
      }
    },
    async submit() {
      try {
        await this.fetchRegisterAndSetApplicantInfo(this.parseApplicantInfo())
        this.$router.push({ path: `/register/application/${this.recruitmentId}` })
      } catch (e) {
        alert(e.response.data)
      }
    },
    validPasswordInputs(v) {
      return (this.validPassword = v && this.password === this.rePassword)
    },
  },
}
</script>

<style scoped>
.register {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: #ced6e0;
}

.summary {
  padding: 0;
  margin: 0;
  color: #333;
  font-size: 12px;
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
