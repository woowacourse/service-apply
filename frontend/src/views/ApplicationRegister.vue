<template>
  <div class="application-register">
    <Box class="information">
      <div class="title">{{ recruitment.title }}</div>
      <div class="period">{{ startDateTime }} ~ {{ endDateTime }}</div>
    </Box>
    <Form @submit.prevent="submit">
      <h1>지원서 작성</h1>
      <p class="autosave-indicator" v-if="isEditing">
        임시 저장되었습니다. ({{ modifiedDateTime }})
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
        <Button @click="saveTemp" :disabled="!canSave" value="임시 저장" />
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
import Box from "@/components/Box"

export default {
  props: {
    recruitmentId: Number,
    status: String,
  },
  components: {
    Box,
    Form,
    Button,
    TextField,
    CheckBox,
    Field,
  },
  data: () => ({
    factCheck: false,
    password: "",
    rePassword: "",
    referenceUrl: "",
    recruitmentItems: [],
    rules: { ...register },
    validPassword: false,
    modifiedDateTime: null,
  }),
  computed: {
    canSubmit() {
      return this.factCheck && (this.isEditing ? this.validPassword : true)
    },
    canSave() {
      return this.isEditing ? this.validPassword : true
    },
    isEditing() {
      return this.status === "edit"
    },
    recruitment() {
      return this.$store.state.recruitments.items.find(v => v.id === this.recruitmentId)
    },
    startDateTime() {
      return parseLocalDateTime(new Date(this.recruitment.startDateTime))
    },
    endDateTime() {
      return parseLocalDateTime(new Date(this.recruitment.endDateTime))
    },
  },
  async created() {
    await this.fetchRecruitmentItems()
    if (this.isEditing) {
      await this.fetchApplicationForm()
    }
  },
  methods: {
    async fetchRecruitmentItems() {
      try {
        const { data } = await RecruitmentApi.fetchItems(this.recruitmentId)
        this.recruitmentItems = data.map(recruitmentItem => ({
          ...recruitmentItem,
          contents: "",
        }))
      } catch (e) {
        alert(e.response.data)
        this.$router.replace("/")
      }
    },
    async fetchApplicationForm() {
      try {
        const { data } = await ApplicationFormsApi.fetchForm({
          token: this.$store.getters["token"],
          recruitmentId: this.recruitmentId,
        })
        this.fillForm(data)
      } catch (e) {
        alert(e.response.data)
        this.$router.replace("/")
      }
    },
    fillForm(applicationForm) {
      this.modifiedDateTime = parseLocalDateTime(new Date(applicationForm.modifiedDateTime))
      this.referenceUrl = applicationForm.referenceUrl
      this.recruitmentItems = this.recruitmentItems.map(recruitmentItem => ({
        ...recruitmentItem,
        contents: applicationForm.answers.find(
          ({ recruitmentItemId }) => recruitmentItemId === recruitmentItem.id,
        ).contents,
      }))
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
      const applicationForm = {
        recruitmentId: this.recruitmentId,
        referenceUrl: this.referenceUrl,
        answers: this.recruitmentItems.map(item => ({
          contents: item.contents,
          recruitmentItemId: item.id,
        })),
        password: this.password,
        isSubmitted,
      }
      const invoke = this.isEditing ? ApplicationFormsApi.updateForm : ApplicationFormsApi.saveForm
      return invoke({
        token: this.$store.getters["token"],
        data: applicationForm,
      })
    },
    async saveTemp() {
      try {
        await this.save(false)
        alert("정상적으로 저장되었습니다.")
        if (!this.isEditing) {
          this.$router.replace("edit?recruitmentId=" + this.recruitmentId)
        }
        this.fetchApplicationForm()
      } catch (e) {
        alert(e.response.data)
        this.$router.replace("/")
      }
    },
    async submit() {
      if (confirm("제출하신 뒤에는 수정하실 수 없습니다. 정말로 제출하시겠습니까?")) {
        try {
          await this.save(true)
          alert("정상적으로 제출되었습니다.")
        } catch (e) {
          alert(e.response.data)
        } finally {
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

.title {
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 10px;
}

.period {
  font-size: 16px;
}

.information {
  max-width: 512px;
  margin-bottom: 0;
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
