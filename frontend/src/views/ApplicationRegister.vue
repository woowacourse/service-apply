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
        v-model="referenceUrl"
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
        <Button @click="save(false)" value="임시 저장" />
        <Button type="submit" :disabled="!canSubmit" value="제출" />
      </div>
      <footer>
        <a class="logo" href="#"></a>
      </footer>
    </Form>
  </div>
</template>

<script>
import { Button, CheckBox, Field, Form, TextField } from "@/components/form"
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
  },
  data: () => ({
    status: "",
    factCheck: false,
    password: "",
    rePassword: "",
    referenceUrl: "",
    recruitmentItems: [],
    rules: { ...register },
    validPassword: false,
    tempSaveTime: "",
  }),
  computed: {
    isEditing() {
      return this.$route.query.action === "edit"
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
        this.referenceUrl = applicationForm.referenceUrl
        console.log(recruitmentItems)
        console.log(applicationForm)
        this.recruitmentItems = recruitmentItems.map(recruitmentItem => ({
          ...recruitmentItem,
          contents: applicationForm.answers.find(
            ({ recruitmentItemId }) => recruitmentItemId === recruitmentItem.id,
          ).contents,
        }))
      }
    } catch (e) {
      if (e.message.includes("404")) {
        let query = Object.assign({}, this.$route.query)
        delete query.action
        this.$router.replace({ query })
      } else {
        alert("잘못된 요청입니다.")
        this.$router.replace("/recruits")
      }
    }
  },
  methods: {
    async parseApplicationInfo() {
      return {
        recruitmentId: this.recruitmentId,
        referenceUrl: this.referenceUrl,
        answers: await this.recruitmentItems.map(item => ({
          contents: item.contents,
          recruitmentItemId: item.id,
        })),
      }
    },
    reset() {
      this.factCheck = false
      this.password = ""
      this.rePassword = ""
      this.recruitmentItems = this.recruitmentItems.map(recruitmentItem => ({
        ...recruitmentItem,
        contents: "",
      }))
      this.referenceUrl = ""
    },
    save(isSubmitted) {
      this.parseApplicationInfo()
        .then(data => ({
          ...data,
          isSubmitted: isSubmitted,
        }))
        .then(data => {
          if (this.isEditing) {
            data = {
              ...data,
              password: this.password,
            }
            //TODO: 업데이트 API 만들기
          } else {
            //TODO: 작성 API 만들기
          }
          console.log(data)
        })
    },
    submit() {
      if (confirm("제출하신 뒤에는 수정하실 수 없습니다. 정말로 제출하시겠습니까?")) {
        this.save(true)
      }
    },
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
