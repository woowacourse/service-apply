import React from "react";
import RecruitItem from "./RecruitItem";

export default {
  title: "RecruitItem",
  component: RecruitItem,
};

const Template = (args) => <RecruitItem {...args} />;

export const Default = Template.bind({});

Default.args = {
  recruitment: {
    status: "RECRUITING",
    id: 1,
  },
};
