<template>
  <div class="recruitment-register">
    <Form @submit.prevent="submit">
      <h1>지원서 작성</h1>

      <TextField
        v-for="(item, index) in recruitmentItems" v-bind:key="item.id"
        v-model="recruitmentItemInputs[index]"
        name="recruitment-item"
        type="textarea"
        :label="item.title"
        :description="item.description"
        placeholder="내용을 입력해주세요."
        :rules="rules.recruitmentItem"
        :maxLength="item.maximumLength"
        required
      />

      <div class="actions">
        <Button type="button" cancel value="취소" />
        <Button type="submit" :disabled="!factCheck" value="다음" />
      </div>

      <Field>
        <CheckBox
          label="위 지원서에 작성한 내용은 모두 사실입니다."
          v-model="factCheck"
          required
        />
      </Field>

      <footer>
        <a class="logo" href="#"></a>
      </footer>
    </Form>
  </div>
</template>

<script>
import {
  Button,
  Form,
  TextField,
  CheckBox,
} from "@/components/form"
import * as RecruitmentApi from "../api/recruitments"
import Field from "@/components/form/Field"
import { regist } from "@/utils/validation"

export default {
  components: {
    Field,
    Form,
    Button,
    TextField,
    CheckBox,
  },
  data: () => ({
    factCheck: false,
    name: "김범준",
    recruitmentItems: [],
    recruitmentItemInputs: [],
    rules: { ...regist },
  }),
  methods: {
    submit(e) {
      e.preventDefault()
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
.recruitment-register {
  display: flex;
  flex-direction: column;
  justify-content: center;
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