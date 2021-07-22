import React from "react";
import MainHeader from "./MainHeader";

export default {
  title: "MainHeader",
  component: MainHeader,
};

const Template = (args) => <MainHeader {...args} />;

export const Default = Template.bind({});

Default.args = {};
