import { ComponentMeta, ComponentStory } from "@storybook/react";
import MyApplicationFormItem from "./MyApplicationFormItem";

export default {
  title: "components/MyRecruitmentItem",
  component: MyApplicationFormItem,
  decorators: [
    (Story) => (
      <div
        style={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          height: "100vh",
          width: "100%",
        }}
      >
        <Story />
      </div>
    ),
  ],
} as ComponentMeta<typeof MyApplicationFormItem>;

const Template: ComponentStory<typeof MyApplicationFormItem> = (args) => (
  <MyApplicationFormItem {...args} />
);

export const Default = Template.bind({});
Default.args = {
  recruitment: {
    id: 1,
    title: "우아한테크코스 1기",
    recruitable: true,
    term: {
      id: 1,
      name: "name",
    },
    hidden: false,
    startDateTime: "2020-10-05T10:00:00",
    endDateTime: "2020-11-05T10:00:00",
    status: "ENDED",
  },
  submitted: false,
};

export const Recruiting = Template.bind({});
Recruiting.args = {
  recruitment: {
    id: 3,
    title: "우아한테크코스 2기",
    recruitable: true,
    term: {
      id: 1,
      name: "name",
    },
    hidden: false,
    startDateTime: "2020-10-25T15:00:00",
    endDateTime: "2021-11-30T10:00:00",
    status: "RECRUITING",
  },
  submitted: false,
};

export const AfterRecruit = Template.bind({});
AfterRecruit.args = {
  recruitment: {
    id: 3,
    title: "우아한테크캠프 Pro 2기",
    recruitable: true,
    term: {
      id: 1,
      name: "name",
    },
    hidden: false,
    startDateTime: "2020-10-25T15:00:00",
    endDateTime: "2021-11-30T10:00:00",
    status: "RECRUITING",
  },
  submitted: true,
};
