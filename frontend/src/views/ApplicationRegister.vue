<template>
  <div class="application-register">
    <RecruitCard class="recruit-card" :recruitment="recruitment" />
    <ValidationObserver v-slot="{ handleSubmit, passed }">
      <Form class="application-form" @submit.prevent="handleSubmit(submit)">
        <h1>지원서 작성</h1>
        <p class="autosave-indicator" v-if="isEditing">
          임시 저장되었습니다. ({{ modifiedDateTime }})
        </p>
        <ValidationProvider
          v-for="(item, index) in recruitmentItems"
          v-bind:key="item.id"
          rules="required"
          v-slot="{ errors }"
        >
          <TextField
            v-model="item.contents"
            name="recruitment-item"
            type="textarea"
            :label="`${index + 1}. ${item.title}`"
            :description="item.description"
            placeholder="내용을 입력해주세요."
            :max-length="item.maximumLength"
            required
          />
          <p class="rule-field">{{ errors[0] }}</p>
        </ValidationProvider>
        <ValidationProvider rules="url" immediate v-slot="{ errors }">
          <TextField
            v-model="referenceUrl"
            name="url"
            type="url"
            :description="
              `자신을 드러낼 수 있는 개인 블로그, GitHub, 포트폴리오 주소 등이 있다면 입력해 주세요.
              <div style='margin-top: 4px; font-size: 14px; color: #555'>여러 개가 있는 경우 Notion, Google 문서 등을 사용하여 하나로 묶어 주세요.</div>`
            "
            label="URL"
            placeholder="ex) https://woowacourse.github.io/javable"
          />
          <p class="rule-field">{{ errors[0] }}</p>
        </ValidationProvider>
        <ValidationProvider rules="required">
          <Field>
            <Label required>지원서 작성 내용 사실 확인</Label>
            <Description>
              기재한 사실 중 허위사실이 발견되는 즉시, 교육 대상자에서 제외되며 향후 지원도
              불가능합니다.
            </Description>
            <CheckBox v-model="factCheck" label="동의합니다." />
          </Field>
        </ValidationProvider>
        <template v-slot:actions>
          <Button @click="reset" value="초기화" />
          <Button @click="saveTemp" value="임시 저장" />
          <Button type="submit" :disabled="!passed" value="제출" />
        </template>
      </Form>
    </ValidationObserver>
  </div>
</template>

<script>
import { Button, CheckBox, Field, Form, TextField, Label, Description } from "@/components/form"
import RecruitCard from "@/components/RecruitCard"
import * as RecruitmentApi from "@/api/recruitments"
import * as ApplicationFormsApi from "@/api/application-forms"
import { parseLocalDateTime } from "@/utils/date"
import { ALREADY_REGISTER } from "@/views/constants"

export default {
  props: {
    recruitmentId: Number,
    status: String,
  },
  components: {
    RecruitCard,
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
    referenceUrl: "",
    recruitmentItems: [],
    modifiedDateTime: null,
  }),
  computed: {
    isEditing() {
      return this.status === "edit"
    },
    token() {
      return this.$store.state.token.value
    },
    recruitment() {
      return this.$store.getters["recruitments/findById"](this.recruitmentId)
    },
  },
  async created() {
    try {
      await this.fetchRecruitmentItems()
      if (this.isEditing) {
        await this.fetchApplicationForm()
      } else {
        await ApplicationFormsApi.createForm({
          token: this.token,
          recruitmentId: this.recruitmentId,
        })
      }
    } catch (e) {
      if (e.response.data.message === ALREADY_REGISTER) {
        alert("이미 신청서를 작성했습니다. 로그인 페이지로 이동합니다.")
        this.$router.replace("/login")
      } else {
        alert(e.response.data.message)
        this.$router.replace("/")
      }
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
        alert(e.response.data.message)
        this.$router.replace("/")
      }
    },
    async fetchApplicationForm() {
      try {
        const { data } = await ApplicationFormsApi.fetchForm({
          token: this.token,
          recruitmentId: this.recruitmentId,
        })
        this.fillForm(data)
      } catch (e) {
        alert(e.response.data.message)
        this.$router.replace("/")
      }
    },
    fillForm(applicationForm) {
      this.modifiedDateTime = parseLocalDateTime(new Date(applicationForm.modifiedDateTime))
      this.referenceUrl = applicationForm.referenceUrl
      this.recruitmentItems = this.recruitmentItems.map(recruitmentItem => ({
        ...recruitmentItem,
        contents:
          (applicationForm.answers.find(
            ({ recruitmentItemId }) => recruitmentItemId === recruitmentItem.id,
          ) || {})["contents"] || "",
      }))
    },
    reset() {
      if (confirm("정말 초기화하시겠습니까?")) {
        this.factCheck = false
        this.recruitmentItems = this.recruitmentItems.map(recruitmentItem => ({
          ...recruitmentItem,
          contents: "",
        }))
        this.referenceUrl = ""
      }
    },
    save(submitted) {
      const applicationForm = {
        recruitmentId: this.recruitmentId,
        referenceUrl: this.referenceUrl,
        answers: this.recruitmentItems.map(item => ({
          contents: item.contents,
          recruitmentItemId: item.id,
        })),
        submitted,
      }
      return ApplicationFormsApi.updateForm({
        token: this.token,
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
        alert(e.response.data.message)
        this.$router.replace("/")
      }
    },
    async submit() {
      if (confirm("제출하신 뒤에는 수정하실 수 없습니다. 정말로 제출하시겠습니까?")) {
        try {
          await this.save(true)
          alert("정상적으로 제출되었습니다.")
        } catch (e) {
          alert(e.response.data.message)
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
  align-items: center;
}

.recruit-card,
.application-form {
  width: 800px;
}

@media screen and (max-width: 800px) {
  .recruit-card,
  .application-form {
    width: 100vw;
  }
}

.rule-field {
  margin-left: 8px;
  font-size: 12px;
  color: #ff0000;
}

.autosave-indicator {
  color: darkred;
}
</style>
