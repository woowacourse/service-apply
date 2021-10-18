import React from "react";
import Button from "./IconButton";

export default {
  title: "components/IconButton",
  component: Button,
};

const Template = (args) => <Button {...args} />;

export const Default = Template.bind({});

Default.args = {
  src: "https://dummyimage.com/100x100",
};

export const Disabled = Template.bind({});

Disabled.args = {
  src: "https://dummyimage.com/100x100",
  disabled: true,
};
