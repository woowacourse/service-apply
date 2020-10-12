<template>
  <div class="register">
    <ValidationObserver v-slot="{ handleSubmit }">
      <Form @submit.prevent="handleSubmit(submit)">
        <h1>지원자 정보</h1>
        <ValidationProvider rules="required" v-slot="{ errors }">
          <SummaryCheckField
            name="policy"
            label="개인정보 수집 및 이용 동의"
            v-model="policyCheck"
            required
          >
            <p class="summary">{{ policySummary }}</p>
          </SummaryCheckField>
          <p class="rule-field">{{ errors[0] }}</p>
        </ValidationProvider>

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

        <ValidationProvider rules="phoneNumber|required" :bails="false" v-slot="{ errors }">
          <TextField
            v-model="phoneNumber"
            name="phone-number"
            type="text"
            label="전화번호"
            placeholder="연락 가능한 전화번호를 입력해 주세요."
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

        <ValidationObserver>
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

          <ValidationProvider
            rules="password|rePassword:@password|required"
            :bails="false"
            v-slot="{ errors }"
          >
            <TextField
              v-model="rePassword"
              name="re-password"
              type="password"
              label="비밀번호 확인"
              placeholder="비밀번호를 다시 한번 입력해 주세요."
              required
            />
            <p v-for="(error, index) in errors" :key="index" class="rule-field">{{ error }}</p>
          </ValidationProvider>
        </ValidationObserver>

        <ValidationProvider rules="year|month|day|required" :bails="false" v-slot="{ errors }">
          <BirthField v-model="birth" required />
          <p v-for="(error, index) in errors" :key="index" class="rule-field">{{ error }}</p>
        </ValidationProvider>

        <ValidationProvider rules="required" v-slot="{ errors }">
          <GenderField v-model="gender" required />
          <p class="rule-field">{{ errors[0] }}</p>
        </ValidationProvider>

        <div class="actions">
          <Button cancel value="취소" />
          <Button type="submit" value="다음" />
        </div>
        <footer>
          <a class="logo" href="#"></a>
        </footer>
      </Form>
    </ValidationObserver>
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
import { POLICY_SUMMARY } from "./constants"
import ValidationProvider from "@/utils/validation/validator"
import { ValidationObserver } from "vee-validate"

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
    ValidationProvider,
    ValidationObserver,
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
  }),
  methods: {
    ...mapActions(["fetchRegisterAndSetApplicantInfo"]),
    async submit() {
      try {
        await this.fetchRegisterAndSetApplicantInfo({
          name: this.name,
          phoneNumber: this.phoneNumber,
          email: this.email,
          password: this.password,
          gender: this.gender.toUpperCase(),
          birthday: DateUtil.formatLocalDate(this.birth),
        })
        this.$router.push({
          path: `/application-forms/new`,
          query: {
            recruitmentId: this.recruitmentId,
          },
        })
      } catch (e) {
        alert(e.response.data.message)
      }
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
}

.summary {
  padding: 0;
  margin: 0;
  color: #333;
  font-size: 12px;
}

.rule-field {
  margin-left: 8px;
  font-size: 12px;
  color: #ff0000;
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
