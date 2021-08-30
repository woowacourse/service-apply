import React from "react";
import BirthField from "./BirthField";

export default {
  title: "form/BirthField",
  component: BirthField,
};

const Template = (args) => <BirthField {...args} />;

export const Default = Template.bind({});

export const Required = Template.bind({});
Required.args = {
  required: true,
};
