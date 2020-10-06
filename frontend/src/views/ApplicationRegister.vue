<template>
  <div class="application-register">
    <Form @submit.prevent="submit">
      <h1>지원서 작성</h1>
      <p class="autosave-indicator" v-if="this.isEditing">
        임시 저장되었습니다. ({{ tempSavedTime }})
      </p>
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
        <Button @click="tempSave()" :disabled="!canSave" value="임시 저장" />
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
import { parseLocalDateTime } from "@/utils/date"

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
    tempSaveTime: undefined,
  }),
  computed: {
    canSubmit() {
      return this.factCheck && (this.isEditing ? this.validPassword : true)
    },
    canSave() {
      return this.isEditing ? this.validPassword : true
    },
    tempSavedTime() {
      if (this.tempSaveTime) {
        return parseLocalDateTime(new Date(this.tempSaveTime))
      }
      return null
    },
    isEditing() {
      return this.$route.params.status === "edit"
    },
  },
  created() {
    this.refreshForm()
  },
  methods: {
    async fetchRecruitmentItems() {
      const { data: recruitmentItems } = await RecruitmentApi.fetchItems(this.recruitmentId)
      this.recruitmentItems = recruitmentItems.map(recruitmentItem => ({
        ...recruitmentItem,
        contents: "",
      }))
      return recruitmentItems
    },
    async fetchApplicationForm() {
      const { data: applicationForm } = await ApplicationFormsApi.fetchForm({
        token: this.$store.getters["token"],
        recruitmentId: this.recruitmentId,
      })
      if (applicationForm.submitted) {
        throw { message: "이미 제출된 지원서입니다. 수정할 수 없습니다." }
      } else if (!this.isEditing) {
        throw { message: "이미 저장된 지원서가 있습니다." }
      }
      return applicationForm
    },
    fillForm(applicationForm, recruitmentItems) {
      this.tempSaveTime = applicationForm.modifiedDateTime
      this.referenceUrl = applicationForm.referenceUrl
      this.recruitmentItems = recruitmentItems.map(recruitmentItem => ({
        ...recruitmentItem,
        contents: applicationForm.answers.find(
          ({ recruitmentItemId }) => recruitmentItemId === recruitmentItem.id,
        ).contents,
      }))
    },
    async refreshForm() {
      try {
        const recruitmentItems = await this.fetchRecruitmentItems()
        const applicationForm = await this.fetchApplicationForm()
        this.fillForm(applicationForm, recruitmentItems)
      } catch (e) {
        if (e.message.includes("404") && this.isEditing) {
          alert("저장된 지원서가 존재하지 않습니다.")
          this.$router.replace("/")
        } else if (!e.message.includes("404")) {
          alert(e.message ? e.message : "잘못된 요청입니다.")
          this.$router.replace("/")
        }
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
    parseApplicationInfo() {
      return {
        recruitmentId: this.recruitmentId,
        referenceUrl: this.referenceUrl,
        answers: this.recruitmentItems.map(item => ({
          contents: item.contents,
          recruitmentItemId: item.id,
        })),
      }
    },
    async save(isSubmitted) {
      const applicationForm = {
        ...this.parseApplicationInfo(),
        isSubmitted: isSubmitted,
      }
      return await this.saveOrUpdate(applicationForm)
    },
    async saveOrUpdate(applicationForm) {
      let saveOrUpdateFunction
      if (this.isEditing) {
        applicationForm = {
          ...applicationForm,
          password: this.password,
        }
        saveOrUpdateFunction = ApplicationFormsApi.updateForm
      } else {
        saveOrUpdateFunction = ApplicationFormsApi.saveForm
      }
      await saveOrUpdateFunction({
        token: this.$store.getters["token"],
        data: applicationForm,
      })
    },
    async tempSave() {
      try {
        await this.save(false).then(() => {
          alert("정상적으로 저장되었습니다.")
          if (!this.isEditing) {
            this.$router.replace("edit?recruitmentId=" + this.recruitmentId)
          }
          this.refreshForm()
        })
      } catch (e) {
        alert(e.response.data)
        this.$router.replace("/")
      }
    },
    async submit() {
      if (confirm("제출하신 뒤에는 수정하실 수 없습니다. 정말로 제출하시겠습니까?")) {
        try {
          await this.save(true).then(() => {
            alert("정상적으로 제출되었습니다.")
            this.$router.replace("/")
          })
        } catch (e) {
          alert(e.response.data)
          this.$router.replace("/")
        }
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
.autosave-indicator {
  color: darkred;
}
</style>
