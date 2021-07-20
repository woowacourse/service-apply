import React from "react";
import BaseItem from "./BaseItem";

export default {
  title: "BaseItem",
  component: BaseItem,
};

const Template = (args) => <BaseItem {...args} />;

export const Default = Template.bind({});

Default.args = {
  title: "제목",
  start: "2021-07-20",
  end: "2021-07-20",
};
