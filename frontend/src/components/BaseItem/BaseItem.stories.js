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
  startDateTime: "2021-07-20",
  endDateTime: "2021-07-20",
};
