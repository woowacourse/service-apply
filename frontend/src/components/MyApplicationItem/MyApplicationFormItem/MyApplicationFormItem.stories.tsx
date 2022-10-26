import { ComponentMeta, ComponentStory } from "@storybook/react";
import { MemoryRouter } from "react-router-dom";
import { RECRUITMENT_STATUS } from "./../../../constants/recruitment";
import MyApplicationFormItem from "./MyApplicationFormItem";

export default {
  title: "components/MyRecruitmentItem",
  component: MyApplicationFormItem,
  decorators: [
    (Story) => (
      <MemoryRouter
        initialEntries={[
          {
            pathname: "/'",
          },
        ]}
      >
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
      </MemoryRouter>
    ),
  ],
} as ComponentMeta<typeof MyApplicationFormItem>;

const Template: ComponentStory<typeof MyApplicationFormItem> = (args) => (
  <MyApplicationFormItem {...args} />
);

export const AfterSubmit = Template.bind({});
AfterSubmit.args = {
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
  submitted: true,
};

export const beforeSubmitRecruitable = Template.bind({});
beforeSubmitRecruitable.args = {
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
    status: RECRUITMENT_STATUS.RECRUITABLE,
  },
  submitted: false,
};

export const beforeSubmitRecruiting = Template.bind({});
beforeSubmitRecruiting.args = {
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
    status: RECRUITMENT_STATUS.RECRUITING,
  },
  submitted: false,
};

export const beforeSubmitUnrecruitable = Template.bind({});
beforeSubmitUnrecruitable.args = {
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
    status: RECRUITMENT_STATUS.UNRECRUITABLE,
  },
  submitted: false,
};

export const beforeSubmitEnded = Template.bind({});
beforeSubmitEnded.args = {
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
    status: RECRUITMENT_STATUS.ENDED,
  },
  submitted: false,
};
