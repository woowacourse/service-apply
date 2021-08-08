import React from "react";
import Recruits from "./Recruits";

export default {
  title: "page/Recruits",
  component: Recruits,
};

const Template = (args) => <Recruits {...args} />;

export const Default = Template.bind({});
