import React from "react";
import RecruitCard from "./RecruitCard";

export default {
  title: "RecruitCard",
  component: RecruitCard,
};

const Template = (args) => <RecruitCard {...args} />;

export const Default = Template.bind({});

Default.args = {
  title: "제목",
  startDateTime: "2021-07-20",
  endDateTime: "2021-07-20",
};
