<template>
  <div class="application-register">
    <Form @submit.prevent="submit">
      <h1>지원서 작성</h1>
      <!-- TODO: 지원자 정보 입력 페이지에서 입력한 이름을 받아 온다. -->
      <TextField v-model="name" name="name" type="text" label="이름" readonly />
      <TextField
        v-model="password"
        name="password"
        type="password"
        label="비밀번호"
        placeholder="비밀번호를 입력해 주세요"
        :rules="rules.password"
        @valid="v => (this.validPassword = v)"
        required
      />
      <TextField
        v-model="rePassword"
        name="re-password"
        type="password"
        label="비밀번호 확인"
        placeholder="비밀번호를 다시 한 번 입력해 주세요"
        :rules="[...rules.rePassword, v => v === password || '비밀번호가 일치하지 않습니다']"
        @valid="v => (this.validPassword = v && this.password === this.rePassword)"
        required
      />

      <TextField
        v-for="(item, index) in recruitmentItems"
        v-bind:key="item.id"
        v-model="recruitmentItemInputs[index]"
        name="recruitment-item"
        type="textarea"
        :label="`${index + 1}. ${item.title}`"
        :description="item.description"
        placeholder="내용을 입력해주세요."
        :rules="rules.recruitmentItem"
        :max-length="item.maximumLength"
        required
      />

      <TextField
        v-model="url"
        name="url"
        type="url"
        :description="'블로그, GitHub, 포트폴리오 주소 등을 입력해 주세요.'"
        label="URL"
        placeholder="ex) https://woowacourse.github.io/javable/"
      />
      <Field>
        <CheckBox
          v-model="factCheck"
          label="위 지원서에 작성한 내용은 모두 사실입니다."
          required
        ></CheckBox>
      </Field>
      <div class="actions">
        <Button @click="reset" value="초기화" />
        <Button @click="save" value="임시 저장" />
        <Button type="submit" :disabled="!factCheck || !validPassword" value="제출" />
      </div>
      <footer>
        <a class="logo" href="#"></a>
      </footer>
    </Form>
  </div>
</template>

<script>
import { Form, Button, TextField, CheckBox, Field } from "@/components/form"
import * as RecruitmentApi from "../api/recruitments"
import { register } from "@/utils/validation"

export default {
  components: {
    Form,
    Button,
    TextField,
    CheckBox,
    Field,
  },
  data: () => ({
    factCheck: false,
    name: "서버에서 받아올 이름",
    password: "",
    rePassword: "",
    url: "",
    recruitmentItems: [],
    recruitmentItemInputs: [""],
    rules: { ...register },
    validPassword: false,
  }),
  methods: {
    reset() {
      this.factCheck = false
      this.password = ""
      this.rePassword = ""
      this.url = ""
      this.recruitmentItemInputs = this.recruitmentItems.map(() => "")
    },
    save() {
      // TODO: 임시 저장 기능 추가 필요
    },
    submit() {
      confirm("정말로 제출하시겠습니까?")
    },
  },
  async mounted() {
    const { data } = await RecruitmentApi.fetchItems(1)
    this.recruitmentItems = data
    this.recruitmentItemInputs = data.map(() => "")
  },
}
</script>

<style scoped>
.application-register {
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-height: 100%;
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
