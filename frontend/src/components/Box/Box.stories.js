import React from "react";
import Box from "./Box";

export default {
  title: "components/Box",
  component: Box,
};

const Template = (args) => <Box {...args} />;

export const Default = Template.bind({});

Default.args = {
  children: "내용물",
};
