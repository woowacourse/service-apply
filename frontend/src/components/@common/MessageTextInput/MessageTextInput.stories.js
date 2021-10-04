import React from "react";
import MessageTextInput from "./MessageTextInput";

export default {
  title: "form/MessageTextInput",
  component: MessageTextInput,
};

const Template = (args) => <MessageTextInput {...args} />;

export const Default = Template.bind({});
Default.args = {
  label: "이름",
};

export const Required = Template.bind({});
Required.args = {
  label: "이름",
  required: true,
};

export const WithDescription = Template.bind({});
WithDescription.args = {
  label: "이름",
  description: "이름을 입력하세요.",
};

export const MaxLength = Template.bind({});
MaxLength.args = {
  label: "이름",
  maxLength: 30,
  value: "썬",
};
