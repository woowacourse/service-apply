import React from "react";
import Container, { CONTAINER_SIZE } from "./Container";

export default {
  title: "components/Container",
  component: Container,
};

const Template = (args) => <Container {...args} />;

export const Default = Template.bind({});

Default.args = {
  children: "내용물",
};

export const Narrow = Template.bind({});

Narrow.args = {
  children: "내용물",
  size: CONTAINER_SIZE.NARROW,
};
