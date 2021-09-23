import React from "react";
import RecruitmentItem from "./RecruitmentItem";

export default {
  title: "RecruitmentItem",
  component: RecruitmentItem,
};

const Template = (args) => <RecruitmentItem {...args} />;

export const Default = Template.bind({});

Default.args = {
  recruitment: {
    title: "제목",
    startDateTime: "2021-07-20 10:00:00",
    endDateTime: "2021-07-20 20:00:00",
  },
};

export const WithButton = Template.bind({});

WithButton.args = {
  buttonLabel: "라벨",
  activeButton: false,
  onClick: () => {},
  recruitment: {
    title: "제목",
    startDateTime: "2021-07-20 10:00:00",
    endDateTime: "2021-07-20 20:00:00",
  },
};
