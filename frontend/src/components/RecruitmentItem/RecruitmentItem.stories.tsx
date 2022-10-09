import { ComponentMeta, ComponentStory } from "@storybook/react";
import RecruitmentItem from "./RecruitmentItem";

export default {
  title: "components/RecruitmentItem",
  component: RecruitmentItem,
} as ComponentMeta<typeof RecruitmentItem>;

const Template: ComponentStory<typeof RecruitmentItem> = (args) => <RecruitmentItem {...args} />;

export const Default = Template.bind({});
Default.args = {
  recruitment: {
    id: 1,
    title: "우아한테크코스 1기",
    term: {
      id: 1,
      name: "이름",
    },
    recruitable: true,
    hidden: false,
    startDateTime: "2020-10-05T10:00:00",
    endDateTime: "2020-11-05T10:00:00",
    status: "RECRUITING",
  },
};

export const WithButton = Template.bind({});
WithButton.args = {
  onClickButton: () => {},
  recruitment: {
    id: 1,
    title: "우아한테크코스 1기",
    term: {
      id: 1,
      name: "이름",
    },
    recruitable: true,
    hidden: false,
    startDateTime: "2020-10-05T10:00:00",
    endDateTime: "2020-11-05T10:00:00",
    status: "ENDED",
  },
};
