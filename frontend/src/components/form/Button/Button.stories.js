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

export const Cancel = Template.bind({});
Cancel.args = {
  children: "버튼",
  cancel: true,
};
