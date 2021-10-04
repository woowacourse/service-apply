import React from "react";
import TextInput from "./TextInput";

export default {
  title: "form/TextInput",
  component: TextInput,
};

const Template = (args) => <TextInput {...args} />;

export const Default = Template.bind({});
Default.args = {
  maxLength: 100,
};

export const ReadOnly = Template.bind({});
ReadOnly.args = {
  maxLength: 100,
  readOnly: true,
  value: "읽기전용 입력창입니다.",
};
