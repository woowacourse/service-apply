import React from "react";
import Button from "./Button";

export default {
  title: "form/Button",
  component: Button,
};

const Template = (args) => <Button {...args} />;

export const Default = Template.bind({});
Default.args = {
  children: "버튼",
};

export const Disabled = Template.bind({});
Disabled.args = {
  children: "버튼",
  disabled: true,
};

export const Cancel = Template.bind({});
Cancel.args = {
  children: "취소",
  cancel: true,
};
