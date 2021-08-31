import React from "react";
import GenderField from "./GenderField";

export default {
  title: "form/GenderField",
  component: GenderField,
};

const Template = (args) => <GenderField {...args} />;

export const Default = Template.bind({});
Default.args = {};
