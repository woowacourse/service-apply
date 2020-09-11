<template>
  <div class="application-register">
    <Form @submit.prevent="submit">
      <h1>지원서 작성</h1>

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

      <div class="actions">
        <Button type="button" cancel value="취소" />
        <Button type="submit" value="다음" />
      </div>

      <footer>
        <a class="logo" href="#"></a>
      </footer>
    </Form>
  </div>
</template>

<script>
import { Button, Form, TextField } from "@/components/form"
import * as RecruitmentApi from "../api/recruitments"
import { regist } from "@/utils/validation"

export default {
  components: {
    Form,
    Button,
    TextField,
  },
  data: () => ({
    name: "김범준",
    recruitmentItems: [],
    recruitmentItemInputs: [""],
    rules: { ...regist },
  }),
  methods: {
    submit() {},
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
