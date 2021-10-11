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

export const Outlined = Template.bind({});
Outlined.args = {
  children: "버튼",
  variant: "outlined",
};

export const Cancel = Template.bind({});
Cancel.args = {
  children: "취소",
  cancel: true,
};

export const Disabled = Template.bind({});
Disabled.args = {
  children: "버튼",
  disabled: true,
};
