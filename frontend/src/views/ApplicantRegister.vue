<template>
  <div class="applicant-register">
    <RecruitCard :recruitment="recruitment" />
    <ValidationObserver v-slot="{ handleSubmit, passed }">
      <Form @submit.prevent="handleSubmit(submit)">
        <h1>지원자 정보</h1>
        <ValidationProvider rules="required">
          <SummaryCheckField
            name="policy"
            label="개인정보 수집 및 이용 동의"
            v-model="policyCheck"
            required
          >
            <p class="summary" v-html="policySummary"></p>
          </SummaryCheckField>
        </ValidationProvider>
        <ValidationProvider rules="required|name" v-slot="{ errors }">
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
        <ValidationProvider rules="required|phoneNumber" v-slot="{ errors }">
          <TextField
            v-model="phoneNumber"
            name="phone-number"
            type="text"
            label="전화번호"
            placeholder="연락 가능한 전화번호를 입력해 주세요."
            required
          />
          <p class="rule-field">{{ errors[0] }}</p>
        </ValidationProvider>
        <ValidationProvider rules="required|email" v-slot="{ errors }">
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
        <ValidationProvider name="password" rules="required|password" v-slot="{ errors }">
          <TextField
            v-model="password"
            name="password"
            type="password"
            label="비밀번호"
            placeholder="비밀번호를 입력해 주세요."
            required
          />
          <p class="rule-field">{{ errors[0] }}</p>
        </ValidationProvider>
        <ValidationProvider rules="required|confirmed:@password" v-slot="{ errors }">
          <TextField
            v-model="rePassword"
            name="re-password"
            type="password"
            label="비밀번호 확인"
            placeholder="비밀번호를 다시 한번 입력해 주세요."
            required
          />
          <p class="rule-field">{{ errors[0] }}</p>
        </ValidationProvider>
        <ValidationProvider rules="year|month|day" v-slot="{ errors }">
          <BirthField v-model="birth" required />
          <p class="rule-field">{{ errors[0] }}</p>
        </ValidationProvider>
        <ValidationProvider rules="required" v-slot="{ errors }">
          <GenderField v-model="gender" required />
          <p class="rule-field">{{ errors[0] }}</p>
        </ValidationProvider>
        <template v-slot:actions>
          <Button cancel value="취소" />
          <Button type="submit" :disabled="!passed" value="다음" />
        </template>
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
import RecruitCard from "@/components/RecruitCard"
import * as DateUtil from "@/utils/date"
import { POLICY_SUMMARY } from "./constants"

export default {
  props: {
    recruitmentId: Number,
  },
  components: {
    RecruitCard,
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
  }),
  computed: {
    recruitment() {
      return this.$store.getters["recruitments/findById"](this.recruitmentId)
    },
  },
  methods: {
    ...mapActions("token", ["fetchRegister"]),
    async submit() {
      try {
        await this.fetchRegister({
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
        alert("이미 신청서를 작성했습니다. 로그인 페이지로 이동합니다.")
        this.$router.replace("/login")
      }
    },
  },
}
</script>

<style scoped>
.applicant-register {
  display: flex;
  flex-direction: column;
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
</style>
