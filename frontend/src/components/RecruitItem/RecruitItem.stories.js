import React from "react";
import { RECRUITMENT_STATUS } from "../../constants/recruitment";
import RecruitItem from "./RecruitItem";

export default {
  title: "RecruitItem",
  component: RecruitItem,
};

const Template = (args) => <RecruitItem {...args} />;

export const Default = Template.bind({});

Default.args = {
  recruitment: {
    id: "1",
    title: "title",
    status: RECRUITMENT_STATUS.RECRUITING,
    startDateTime: "2021-07-21",
    endDateTime: "2021-07-21",
  },
};
