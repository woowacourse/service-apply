<template>
  <div class="recruitment-register">
    <Form @submit.prevent="submit">
      <h1>지원서 작성</h1>

      <div v-for="(item, index) in recruitmentItems" v-bind:key="item.id">
        <TextField
          v-model="recruitmentItemInputs[index]"
          name="name"
          type="textarea"
          :label="item.title"
          :description="item.description"
          placeholder="내용을 입력해주세요."
          :rules="[v => !!v || '필수 정보입니다.']"
          :maxLength="item.maximumLength"
          required
        />
      </div>

      <div class="actions">
        <Button type="button" cancel value="취소" />
        <Button type="submit" :disabled="!factCheck" value="다음" />
      </div>
      <footer>
        <a class="logo" href="#"></a>
      </footer>
      <SummaryCheckField
        name="fact"
        label="위 지원서에 작성한 내용은 모두 사실입니다."
        v-model="factCheck"
        required
      />
    </Form>
  </div>
</template>

<script>
import {
  Button,
  Form,
  SummaryCheckField,
  TextField,
} from "@/components/form"
import * as RecruitmentApi from "../api/recruitments"

export default {
  components: {
    Form,
    Button,
    TextField,
    SummaryCheckField,
  },
  data: () => ({
    factCheck: false,
    name: "김범준",
    recruitmentItems: [],
    recruitmentItemInputs: [],
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
</style>