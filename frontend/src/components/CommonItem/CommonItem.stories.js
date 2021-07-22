import React from "react";
import CommonItem from "./CommonItem";

export default {
  title: "CommonItem",
  component: CommonItem,
};

const Template = (args) => <CommonItem {...args} />;

export const Default = Template.bind({});

Default.args = {
  buttonLabel: "라벨",
  activeButton: false,
  goPage: () => {},
  recruitment: {
    title: "제목",
    startDateTime: "2021-07-20",
    endDateTime: "2021-07-20",
  },
};
