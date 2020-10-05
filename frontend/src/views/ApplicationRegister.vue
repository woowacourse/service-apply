<template>
  <div class="application-register">
    <Form @submit.prevent="submit">
      <h1>지원서 작성</h1>
      <TextField
        :value="$store.state.applicantInfo.name"
        name="name"
        type="text"
        label="이름"
        readonly
      />

      <div v-if="isEditing">
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
      </div>

      <TextField
        v-for="(item, index) in recruitmentItems"
        v-bind:key="item.id"
        v-model="item.contents"
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
        <Label>지원서 작성 내용 사실 확인</Label>
        <Description>
          기재한 사실 중 허위사실이 발견되는 즉시, 교육 대상자에서 제외되며 향후 지원도
          불가능합니다.
        </Description>
        <CheckBox v-model="factCheck" label="동의합니다."></CheckBox>
      </Field>
      <div class="actions">
        <Button @click="reset" value="초기화" />
        <Button @click="save" value="임시 저장" />
        <Button type="submit" :disabled="!canSubmit" value="제출" />
      </div>
      <footer>
        <a class="logo" href="#"></a>
      </footer>
    </Form>
  </div>
</template>

<script>
import { Button, CheckBox, Field, Form, TextField, Label, Description } from "@/components/form"
import * as RecruitmentApi from "@/api/recruitments"
import * as ApplicationFormsApi from "@/api/application-forms"
import { register } from "@/utils/validation"

export default {
  props: {
    recruitmentId: Number,
  },
  components: {
    Form,
    Button,
    TextField,
    CheckBox,
    Field,
    Label,
    Description,
  },
  data: () => ({
    factCheck: false,
    password: "",
    rePassword: "",
    url: "",
    recruitmentItems: [],
    rules: { ...register },
    validPassword: false,
  }),
  computed: {
    isEditing() {
      return this.$route.name === "edit"
    },
    canSubmit() {
      return this.factCheck && (this.isEditing ? this.validPassword : true)
    },
  },
  async created() {
    try {
      const { data: recruitmentItems } = await RecruitmentApi.fetchItems(this.recruitmentId)
      this.recruitmentItems = recruitmentItems.map(recruitmentItem => ({
        ...recruitmentItem,
        contents: "",
      }))
      if (this.isEditing) {
        const { data: applicationForm } = await ApplicationFormsApi.fetchForm({
          token: this.$store.getters["token"],
          recruitmentId: this.recruitmentId,
        })
        this.url = applicationForm.referenceUrl
        this.recruitmentItems = recruitmentItems.map(recruitmentItem => ({
          ...recruitmentItem,
          contents: applicationForm.answers.items.find(
            ({ recruitmentItemId }) => recruitmentItemId === recruitmentItem.id,
          ).contents,
        }))
      }
    } catch (e) {
      alert("잘못된 요청입니다.")
      this.$router.replace("/recruits")
    }
  },
  methods: {
    reset() {
      this.factCheck = false
      this.password = ""
      this.rePassword = ""
      this.recruitmentItems = this.recruitmentItems.map(recruitmentItem => ({
        ...recruitmentItem,
        contents: "",
      }))
      this.url = ""
    },
    save() {
      // TODO: 임시 저장 기능 추가 필요
    },
    submit() {
      confirm("정말로 제출하시겠습니까?")
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
