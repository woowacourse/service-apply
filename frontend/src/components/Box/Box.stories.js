import React from "react";
import Box, { BOX_SIZE } from "./Box";

export default {
  title: "components/Box",
  component: Box,
};

const Template = (args) => <Box {...args} />;

export const Default = Template.bind({});

Default.args = {
  children: "내용물",
};

export const Narrow = Template.bind({});

Narrow.args = {
  children: "내용물",
  size: BOX_SIZE.NARROW,
};
