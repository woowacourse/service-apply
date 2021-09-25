import React from "react";
import Textarea from "./Textarea";

export default {
  title: "form/Textarea",
  component: Textarea,
};

const Template = (args) => <Textarea {...args} />;

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
