import React from "react";
import FormInput from "./FormInput";

export default {
  title: "form/FormInput",
  component: FormInput,
};

const Template = (args) => <FormInput {...args} />;

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
